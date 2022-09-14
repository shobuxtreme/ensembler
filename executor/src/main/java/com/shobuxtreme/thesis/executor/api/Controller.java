package com.shobuxtreme.thesis.executor.api;

import com.shobuxtreme.thesis.core.ExecuteStatus;
import com.shobuxtreme.thesis.core.JobRequest;
import com.shobuxtreme.thesis.executor.svc.ExecuteService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class Controller {

    @Qualifier("executeService")
    private final ExecuteService service;

    Controller(ExecuteService service) {
        this.service = service;
    }

    @CrossOrigin()
    @PostMapping(value = "/execute", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public JobRequest executeJob(@RequestBody JobRequest jobRequest) {

        switch (jobRequest.getJobKind()) {
            case INIT -> System.out.println("INIT");
            case TRAIN -> System.out.println("TRAIN");
            case PREDICT -> System.out.println("PREDICT");
            case VALIDATE -> System.out.println("VALIDATE");
            default -> System.out.println("NOT VALID");
        }

        jobRequest.setJobStatus(ExecuteStatus.SCHEDULED);
        service.schedule(jobRequest);

        return jobRequest;
    }

    @PostMapping(value = "/clear")
    @ResponseStatus(HttpStatus.OK)
    public void clear() {
        service.clear();
    }
}
