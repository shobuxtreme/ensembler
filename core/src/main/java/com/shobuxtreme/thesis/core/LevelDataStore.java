package com.shobuxtreme.thesis.core;

import java.util.ArrayList;
import java.util.List;

public class LevelDataStore {

    private String jobId;

    private int level;

    private List<byte[]> data;

    public LevelDataStore() {
        this.jobId = "";
        this.level = -1;
        this.data = new ArrayList<>();
    }

    public LevelDataStore(String jobId, int level, List<byte[]> data) {
        this.jobId = jobId;
        this.level = level;
        this.data = data;
    }

    public List<byte[]> getData() {
        return data;
    }
}
