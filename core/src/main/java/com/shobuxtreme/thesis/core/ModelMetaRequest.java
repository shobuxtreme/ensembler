package com.shobuxtreme.thesis.core;

public class ModelMetaRequest {

    private String jobId;

    private String modelId;

    private int level;

    private byte[] model;

    private byte[] trainInput;

    private byte[] validateInput;

    private byte[] validateResult;

    private byte[] predictInput;

    private byte[] predictResult;

    public ModelMetaRequest() {
        this.jobId = "";
        this.modelId = "";
        this.level = -1;
        this.model = null;
        this.trainInput = null;
        this.validateInput = null;
        this.validateResult = null;
        this.predictInput = null;
        this.predictResult = null;
    }

    public ModelMetaRequest(String jobId, String modelId, int level) {
        this.jobId = jobId;
        this.modelId = modelId;
        this.level = level;
    }

    public ModelMetaRequest(String jobId, String modelId, int level,
                            byte[] model, byte[] trainInput, byte[] validateInput,
                            byte[] validateResult, byte[] predictInput, byte[] predictResult) {
        this.jobId = jobId;
        this.modelId = modelId;
        this.level = level;
        this.model = model;
        this.trainInput = trainInput;
        this.validateInput = validateInput;
        this.validateResult = validateResult;
        this.predictInput = predictInput;
        this.predictResult = predictResult;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public byte[] getModel() {
        return model;
    }

    public void setModel(byte[] model) {
        this.model = model;
    }

    public byte[] getTrainInput() {
        return trainInput;
    }

    public void setTrainInput(byte[] trainInput) {
        this.trainInput = trainInput;
    }

    public byte[] getValidateInput() {
        return validateInput;
    }

    public void setValidateInput(byte[] validateInput) {
        this.validateInput = validateInput;
    }

    public byte[] getValidateResult() {
        return validateResult;
    }

    public void setValidateResult(byte[] validateResult) {
        this.validateResult = validateResult;
    }

    public byte[] getPredictInput() {
        return predictInput;
    }

    public void setPredictInput(byte[] predictInput) {
        this.predictInput = predictInput;
    }

    public byte[] getPredictResult() {
        return predictResult;
    }

    public void setPredictResult(byte[] predictResult) {
        this.predictResult = predictResult;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "ModelMetaRequest{" +
                "jobId='" + jobId + '\'' +
                ", modelId='" + modelId + '\'' +
                ", level='" + level + '\'' +
                '}';
    }
}
