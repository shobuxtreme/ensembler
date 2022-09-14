package com.shobuxtreme.thesis.worker.svc;

import com.shobuxtreme.thesis.core.*;
import com.shobuxtreme.thesis.worker.WorkerConfig;
import com.shobuxtreme.thesis.worker.amqp.AMQPConfig;
import org.apache.commons.io.FileUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;

@Component
public class WorkerService {
    private final WorkerConfig config;

    private final DataFetchService dataFetchSvc;

    private final ModelMetaStoreSvc modelStoreSvc;

    private final ResponseStoreSvc responseStoreSvc;

    @Qualifier("rabbitTemplate")
    private final RabbitTemplate template;

    public WorkerService(WorkerConfig config, DataFetchService dataFetchSvc, ModelMetaStoreSvc modelStoreSvc, ResponseStoreSvc responseStoreSvc, RabbitTemplate template) {
        this.config = config;
        this.dataFetchSvc = dataFetchSvc;
        this.modelStoreSvc = modelStoreSvc;
        this.responseStoreSvc = responseStoreSvc;
        this.template = template;
    }

    @RabbitListener(queues = "jobs."+"${worker.model.name}")
    public void listener(final ExecuteRequest request) throws Exception {
        System.out.println("----- REQUEST -----");
        System.out.println(request.getId());
        System.out.println(request.getKind());
        ExecuteRequest response = new ExecuteRequest(request, false);

        final ExecuteKind kind = request.getKind();
        switch (kind){
            case TRAIN -> {
                response = timedCommand(request);
                saveResponse(response);

                if(response.getExecResult()!=0){
                    throw new Exception("Training Failed, retry");
                }

                ExecuteRequest validateRequest = new ExecuteRequest(request, true);
                validateRequest.setKind(ExecuteKind.VALIDATE);

                ExecuteRequest validateResponse = timedCommand(validateRequest);
                saveResponse(validateResponse);
            }

            case VALIDATE, PREDICT -> {
                response = timedCommand(request);
                saveResponse(response);
            }


            case INIT -> {
                response.setExecResult(0);
                response.setExecResultMsg("Worker ID:"+ config.id+" IMG:"+config.img+" msvc running.");
                saveResponse(response);
            }
        }

        if(response.getExecResult()!=0){
            throw new Exception("Execution Failed, retry");
        }

        template.convertAndSend(AMQPConfig.EXCHANGE_JOBS,
                AMQPConfig.ROUTING_KEY_ALL_RESULTS, response);
    }

    private ExecuteRequest timedCommand(final ExecuteRequest request) throws IOException {
        ExecuteRequest response = new ExecuteRequest(request, false);
        final Instant start = java.time.Instant.now();

        switch (request.getKind()){
            case TRAIN -> response = train(request);
            case VALIDATE -> response = validate(request);
            case PREDICT -> response = predict(request);
        }

        final Instant end = java.time.Instant.now();
        final long execTime = Duration.between(start,end).toMillis();

        if(response.getExecResult()==0)
            response.setStatus(ExecuteStatus.SUCCESS);
        else
            response.setStatus(ExecuteStatus.FAILED);
        response.setExecTime(execTime);

        System.out.println("----- RESPONSE -----");
        System.out.println(response);
        System.out.println("----- X -----");
        return response;
    }

    public ExecuteRequest executeRuntime(final ExecuteRequest request){
        final ExecuteKind kind = request.getKind();
        final String execCmd;
        switch (kind){
            case VALIDATE -> execCmd = request.getModel().getValidateCmd();
            case PREDICT -> execCmd = request.getModel().getPredictCmd();
            default -> execCmd = request.getModel().getTrainCmd();
        }

        return executeModel(request, execCmd);
    }

    private ExecuteRequest train(final ExecuteRequest request) throws IOException {
        final String jobId = request.getJobId();
        final String modelId = request.getModel().getModelId();
        final int level = request.getModel().getLevel();

        //1.Fetch Data
        if(level == 0) {
            final DataRequest dataRequest = new DataRequest(jobId, ExecuteKind.TRAIN,
                    request.getModel().getLevel(), request.getFactorizeCmd());
            fetchData(request, request.getTrainDataSource(), dataRequest, "train.csv");

        } else {
            // Ensembler: Get validate as TRAIN
            final DataRequest dataRequest = new DataRequest(jobId, ExecuteKind.VALIDATE,
                    request.getModel().getLevel(), request.getFactorizeCmd());
            fetchData(request, request.getValidateDataSource(), dataRequest,
                    "train.csv");

            saveLevelDatasets(jobId, ExecuteKind.VALIDATE, level-1, "train");
        }

        //2.Load Train model
        final String execCmd = request.getModel().getTrainCmd();
        ExecuteRequest response = executeModel(request, execCmd);

        //3.Save Model & Dataset
        saveTrainMeta(jobId, modelId, level);
        return response;
    }

    private ExecuteRequest validate(final ExecuteRequest request) throws IOException {
        final String jobId = request.getJobId();
        final String modelId = request.getModel().getModelId();
        final int level = request.getModel().getLevel();

        //1.Fetch Data
        if(level == 0) {
            final DataRequest dataRequest = new DataRequest(jobId, ExecuteKind.VALIDATE,
                    request.getModel().getLevel(), request.getFactorizeCmd());
            fetchData(request, request.getValidateDataSource(), dataRequest,
                    "validate.csv");

        } else {
            // Ensembler
            final DataRequest dataRequest = new DataRequest(jobId, ExecuteKind.VALIDATE,
                    request.getModel().getLevel(), request.getFactorizeCmd());
            fetchData(request, request.getValidateDataSource(), dataRequest,
                    "validate.csv");
            saveLevelDatasets(jobId, ExecuteKind.VALIDATE,level-1, "validate");
        }

        //2.Load model
        if(!Files.exists(Path.of("latest.model.joblib"))){
            loadTrainedModel(modelId);
        }

        //3.Run command
        final String execCmd = request.getModel().getValidateCmd();
        ExecuteRequest response = executeModel(request, execCmd);

        //3.Save Dataset
        saveValidateMeta(jobId, modelId, level);
        return response;
    }

    private ExecuteRequest predict(final ExecuteRequest request) throws IOException {
        final String jobId = request.getJobId();
        final String modelId = request.getModel().getModelId();
        final int level = request.getModel().getLevel();

        //1.Fetch Data
        if(request.getModel().getLevel() == 0) {
            final DataRequest dataRequest = new DataRequest(jobId, ExecuteKind.PREDICT,
                    request.getModel().getLevel(), request.getFactorizeCmd());
            fetchData(request, request.getPredictDataSource(), dataRequest,
                    "predict.csv");

        } else {
            // Ensembler
            final DataRequest dataRequest = new DataRequest(jobId, ExecuteKind.PREDICT,
                    request.getModel().getLevel(), request.getFactorizeCmd());
            fetchData(request, request.getPredictDataSource(), dataRequest,
                    "predict.csv");

            saveLevelDatasets(jobId, ExecuteKind.PREDICT,level-1, "predict");
        }

        //2.Load model
        if(!Files.exists(Path.of("latest.model.joblib"))){
            loadTrainedModel(modelId);
        }

        //3.Run command
        final String execCmd = request.getModel().getPredictCmd();
        ExecuteRequest response = executeModel(request, execCmd);

        //3.Save Dataset
        savePredictMeta(jobId, modelId, level);
        return response;
    }

    private ExecuteRequest executeModel(final ExecuteRequest request, final String cmd) {
        try {
            Runtime runTime = Runtime.getRuntime();
            Process process = runTime.exec(cmd);
            InputStream inputStream = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(inputStream);
            int n1;
            char[] c1 = new char[1024];
            StringBuilder standardOutput = new StringBuilder();
            while ((n1 = isr.read(c1)) > 0) {
                standardOutput.append(c1, 0, n1);
            }
            process.waitFor();
            request.setExecResult(process.exitValue());
            request.setExecResultMsg(standardOutput.toString());
            process.destroy();
        }catch (Exception e){
            e.printStackTrace();
            request.setExecResult(-1);
            request.setExecResultMsg(e.toString());
        }

        return  request;
    }

    private void fetchData(final ExecuteRequest eRequest, final String url, final DataRequest request,
                           final String saveAsName) throws IOException {
        ExecuteRequest response = new ExecuteRequest(eRequest, false);
        final DataRequest data = dataFetchSvc.fetchDataset(url, request, saveAsName);
        if (data == null || data.getResult() != 0) {
            System.out.println("Error during data fetch");
            response.setStatus(ExecuteStatus.FAILED);
            response.setExecResult(-1);
            response.setExecResultMsg("Error during data fetch");
        }
    }

    private void saveLevelDatasets(final String jobId, final ExecuteKind kind, final int level, final String saveAsBase) {
        int index = 1;

        final LevelDataStore store;

        switch (kind) {
            case VALIDATE -> store  = modelStoreSvc.getLevelValidateResultsByJob(config.dataSvcUrl, jobId, level);
            case PREDICT -> store = modelStoreSvc.getLevelPredictResultsByJob(config.dataSvcUrl, jobId, level);
            default -> {
                return;
            }
        }

        for ( final byte[] data : store.getData() ) {
            try {
                String fileName = saveAsBase + "." + index + ".csv";
                FileUtils.writeByteArrayToFile(new File(fileName), data);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ++index;
        }
    }

    private void saveTrainMeta(
            final String jobId, final String modelId, final int level
    ) throws IOException {
        final String url = config.dataSvcUrl+"models";
        final ModelMetaRequest data = new ModelMetaRequest(jobId, modelId, level);
        data.setModel(readFileBytes("latest.model.joblib"));

        byte []trainFileBytes = readFileBytes("train.csv");
        if (trainFileBytes.length<16000000){
            data.setTrainInput(trainFileBytes);
        }
        modelStoreSvc.saveModelMeta(url, data);
    }

    private void saveValidateMeta(
            final String jobId, final String modelId, final int level
    ) throws IOException {
        final String url = config.dataSvcUrl+"models";
        final ModelMetaRequest data = new ModelMetaRequest(jobId, modelId, level);
        data.setValidateInput(readFileBytes("validate.csv"));
        data.setValidateResult(readFileBytes("validate.results.csv"));

        modelStoreSvc.saveModelMeta(url, data);
    }

    private void savePredictMeta(
            final String jobId, final String modelId, final int level
    ) throws IOException {
        final String url = config.dataSvcUrl+"models";
        final ModelMetaRequest data = new ModelMetaRequest(jobId, modelId, level);
        data.setPredictInput(readFileBytes("predict.csv"));
        data.setPredictResult(readFileBytes("predict.results.csv"));

        modelStoreSvc.saveModelMeta(url, data);
    }

    private void loadTrainedModel(final String modelId) throws IOException {
        final ModelMetaRequest model = modelStoreSvc.getModelMeta(config.dataSvcUrl,modelId);
        String fileName = "latest.model.joblib";
        FileUtils.writeByteArrayToFile(new File(fileName), model.getModel());
    }

    private void saveResponse(final ExecuteRequest response) throws IOException {
        responseStoreSvc.saveResponse(config.dataSvcUrl, response);
    }

    private byte[] readFileBytes(final String filePath) throws IOException {
        byte[] fileBytes = new byte[0];
        try{
            fileBytes = Files.readAllBytes(Paths.get(filePath));
        }catch (OutOfMemoryError e){
            // In case file too large
            System.out.println(e.getMessage());
        }
        return fileBytes;
    }

}
