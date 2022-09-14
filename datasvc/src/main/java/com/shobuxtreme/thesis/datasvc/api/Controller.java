package com.shobuxtreme.thesis.datasvc.api;

import com.shobuxtreme.thesis.core.ExecuteRequest;
import com.shobuxtreme.thesis.core.JobRequest;
import com.shobuxtreme.thesis.core.ModelMetaRequest;
import com.shobuxtreme.thesis.core.LevelDataStore;
import com.shobuxtreme.thesis.datasvc.mongo.model.ModelStore;
import com.shobuxtreme.thesis.datasvc.mongo.repository.JobRepository;
import com.shobuxtreme.thesis.datasvc.svc.ExecuteSvc;
import com.shobuxtreme.thesis.datasvc.svc.LevelDataSvc;
import com.shobuxtreme.thesis.datasvc.svc.ModelSvc;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class Controller {

    private final JobRepository jobRepository;

    private final ModelSvc modelSvc;

    private final ExecuteSvc executeSvc;

    private final LevelDataSvc levelDataSvc;

    public Controller(JobRepository jobRepository, ModelSvc modelSvc, ExecuteSvc executeSvc, LevelDataSvc levelDataSvc) {
        this.jobRepository = jobRepository;
        this.modelSvc = modelSvc;
        this.executeSvc = executeSvc;
        this.levelDataSvc = levelDataSvc;
    }

    @PostMapping(value = "/jobs", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public JobRequest addJob(@RequestBody JobRequest jobRequest, HttpServletResponse response) {
        if (jobRepository.findJobByJobName(jobRequest.getJobName())!=null){
            return jobRepository.save(jobRequest);
        }
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return null;
    }

    @GetMapping(value = "/jobs", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<JobRequest> getAllJobs(){
        return jobRepository.findAll();
    }

    @GetMapping(value = "/jobs/{name}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public JobRequest getJob(@PathVariable String name){
        return jobRepository.findJobByJobName(name);
    }


    @PostMapping(value = "/models", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ModelStore addModel(@RequestBody ModelMetaRequest request, HttpServletResponse response) {
        return modelSvc.storeModel(request);
    }

    @GetMapping(value = "/models/job/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<ModelMetaRequest> getJobModels(@PathVariable String id){
        return modelSvc.findByJobId(id);
    }

    @GetMapping(value = "/models/job/{id}/level/{level}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<ModelMetaRequest> getJobModelsForLevel(@PathVariable String id, @PathVariable int level){
        return modelSvc.findModelsInLevelByJobId(id, level);
    }

    @GetMapping(value = "/models/job/{id}/level/{level}/validateResults", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LevelDataStore getValidateResultsForLevel(@PathVariable String id, @PathVariable int level){
        return levelDataSvc.getValidateResultsInLevelByJobId(id, level);
    }

    @GetMapping(value = "/models/job/{id}/level/{level}/predictResults", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LevelDataStore getPredictResultsForLevel(@PathVariable String id, @PathVariable int level){
        return levelDataSvc.getPredictResultsInLevelByJobId(id, level);
    }

    @GetMapping(value = "/models/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ModelMetaRequest getJobModel(@PathVariable String id){
        return modelSvc.findByModelId(id);
    }

    @PostMapping(value = "/responses", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ExecuteRequest addResponse(@RequestBody ExecuteRequest response) {
        return executeSvc.storeResponse(response);
    }

    @GetMapping(value = "/responses/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ExecuteRequest getResponse(@PathVariable String id){
        return executeSvc.getResponse(id);
    }

    @CrossOrigin()
    @GetMapping(value = "/responses/job/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<ExecuteRequest> getResponsesForJob(@PathVariable String id){
        return executeSvc.getAllResponsesForJobId(id);
    }

    @GetMapping(value = "/responses/model/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<ExecuteRequest> getResponsesForModel(@PathVariable String id){
        return executeSvc.getAllResponsesForModelId(id);
    }

}
