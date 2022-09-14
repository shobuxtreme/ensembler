package com.shobuxtreme.thesis.core;

import java.util.UUID;

public class ExecuteRequest {

    private String jobId;

    private String id;

    private ExecuteKind kind;

    private ExecuteStatus status;

    private String modelId;

    private Model model;

    private String trainDataSource;

    private String validateDataSource;

    private String predictDataSource;

    private String factorizeCmd;

    private int execResult;

    private String execResultMsg;

    private long execTime;

    public ExecuteRequest() {
        this.id = UUID.randomUUID().toString();
        this.execResult = -1;
        this.execResultMsg = "Initialized";
        this.execTime = -1;
    }

    public ExecuteRequest(final String jobId, final ExecuteKind kind, final Model model,  final String trainDataSource,
                          final String validateDataSource, final String predictDataSource, final String factorizeCmd) {
        this.jobId = jobId;
        this.id = UUID.randomUUID().toString();
        this.kind = kind;
        this.status = ExecuteStatus.SCHEDULED;
        this.modelId = model.getModelId();
        this.model = model;
        this.trainDataSource = trainDataSource;
        this.validateDataSource = validateDataSource;
        this.predictDataSource = predictDataSource;
        this.factorizeCmd =  factorizeCmd;
        this.execResult = -1;
        this.execResultMsg = "Initialized";
        this.execTime = -1;
    }

    public ExecuteRequest(final ExecuteRequest copy, final boolean metaOnly) {
        if(metaOnly){
            this.id = UUID.randomUUID().toString();
            this.execResult = -1;
            this.execResultMsg = "Initialized";
            this.execTime = -1;
        }else{
            this.id = copy.getId();
            this.execResult = copy.getExecResult();
            this.execResultMsg = copy.getExecResultMsg();
            this.execTime = copy.getExecTime();
        }
        this.jobId = copy.getJobId();
        this.kind = copy.getKind();
        this.status = copy.getStatus();
        this.modelId = copy.getModel().getModelId();
        this.model = copy.getModel();
        this.trainDataSource = copy.getTrainDataSource();
        this.validateDataSource = copy.getValidateDataSource();
        this.predictDataSource = copy.getPredictDataSource();
        this.factorizeCmd = copy.getFactorizeCmd();
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ExecuteKind getKind() {
        return kind;
    }

    public void setKind(ExecuteKind kind) {
        this.kind = kind;
    }

    public ExecuteStatus getStatus() {
        return status;
    }

    public void setStatus(ExecuteStatus status) {
        this.status = status;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.modelId = model.getModelId();
        this.model = model;
    }

    public String getTrainDataSource() {
        return trainDataSource;
    }

    public void setTrainDataSource(String trainDataSource) {
        this.trainDataSource = trainDataSource;
    }

    public String getValidateDataSource() {
        return validateDataSource;
    }

    public void setValidateDataSource(String validateDataSource) {
        this.validateDataSource = validateDataSource;
    }

    public String getPredictDataSource() {
        return predictDataSource;
    }

    public void setPredictDataSource(String predictDataSource) {
        this.predictDataSource = predictDataSource;
    }

    public int getExecResult() {
        return execResult;
    }

    public void setExecResult(int execResult) {
        this.execResult = execResult;
    }

    public String getExecResultMsg() {
        return execResultMsg;
    }

    public void setExecResultMsg(String execResultMsg) {
        this.execResultMsg = execResultMsg;
    }

    public long getExecTime() {
        return execTime;
    }

    public void setExecTime(long execTime) {
        this.execTime = execTime;
    }

    public String getFactorizeCmd() {
        return factorizeCmd;
    }

    public void setFactorizeCmd(String factorizeCmd) {
        this.factorizeCmd = factorizeCmd;
    }

    @Override
    public String toString() {
        return "ExecuteRequest{" +
                "jobId='" + jobId + '\'' +
                ", id='" + id + '\'' +
                ", kind=" + kind +
                ", status=" + status +
                ", modelId=" + modelId +
                ", model=" + model.toString() +
                ", trainDataSource='" + trainDataSource + '\'' +
                ", validateDataSource='" + validateDataSource + '\'' +
                ", predictDataSource='" + predictDataSource + '\'' +
                ", factorizeCmd='" + factorizeCmd + '\'' +
                ", execResult=" + execResult +
                ", execResultMsg='" + execResultMsg + '\'' +
                ", execTime='" + execTime + '\'' +
                '}';
    }

    public String getModelId() {
        return modelId;
    }
}
