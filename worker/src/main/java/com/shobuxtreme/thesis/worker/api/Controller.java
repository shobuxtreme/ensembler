package com.shobuxtreme.thesis.worker.api;

import com.shobuxtreme.thesis.core.ExecuteRequest;
import com.shobuxtreme.thesis.worker.svc.WorkerService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class Controller implements ApplicationContextAware {

    private ApplicationContext context;

    private final WorkerService svc;

    public Controller(WorkerService svc) {
        this.svc = svc;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @PostMapping(value = "/shutdown")
    @ResponseStatus(HttpStatus.OK)
    public void shutDown(){
        ((ConfigurableApplicationContext) context).close();
    }

    // Mappings
    @GetMapping(value = "/", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String getInfo(){
        return "Worker msvc running." ;
    }

    @PostMapping(value = "/executeCmd", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ExecuteRequest test(@RequestBody final ExecuteRequest request){
        return svc.executeRuntime(request);
    }

}
