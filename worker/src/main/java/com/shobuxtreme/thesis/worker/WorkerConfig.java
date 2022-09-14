package com.shobuxtreme.thesis.worker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WorkerConfig {
    @Value("${worker.id}")
    public int id;

    @Value("${worker.img}")
    public String img;

    @Value("${worker.model.name}")
    public String name;

    @Value("${ensembler.datasvc.url}")
    public String dataSvcUrl;

}
