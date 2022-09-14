package com.shobuxtreme.thesis.factorizer.api;

import com.shobuxtreme.thesis.core.DataRequest;
import com.shobuxtreme.thesis.factorizer.svc.FactorizerSvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class Controller {

    private final FactorizerSvc svc;

    public Controller(FactorizerSvc svc) {
        this.svc = svc;
    }

    @GetMapping(value = "/", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String getInfo(){
        return "Factorizer msvc running.";
    }

    @PostMapping(value = "/test", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public DataRequest test(@RequestBody final DataRequest request){
        return svc.factorize(request);
    }

    @PostMapping(value = "/factorizer", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public DataRequest factorize(@RequestBody final DataRequest request){
        return svc.factorize(request);
    }
}

