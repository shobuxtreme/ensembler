package com.shobuxtreme.thesis.datasvc.mongo.model;

import com.shobuxtreme.thesis.core.ModelMetaRequest;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;

public class ModelStore {

    private String jobId;

    @Id
    private String modelId;

    private int level;

    private Binary modelBin;

    private Binary trainBin;

    private Binary validateBin;

    private Binary validateResultBin;

    private Binary predictBin;

    private Binary predictResultBin;

    public ModelStore() {
        this.jobId = "";
        this.modelId = "";
        this.level = -1;
        this.modelBin = null;
        this.trainBin = null;
        this.validateBin = null;
        this.validateResultBin = null;
        this.predictBin = null;
        this.predictResultBin = null;
    }

    public ModelStore(final ModelMetaRequest copy) {
        this.jobId = copy.getJobId();
        this.modelId = copy.getModelId();
        this.level = copy.getLevel();

        if (copy.getModel()!=null){
            this.modelBin = new Binary(copy.getModel());
        }

        if (copy.getTrainInput()!=null){
            this.trainBin = new Binary(copy.getTrainInput());
        }

        if (copy.getValidateInput()!=null){
            this.validateBin = new Binary(copy.getValidateInput());
        }

        if (copy.getValidateResult()!=null){
            this.validateResultBin = new Binary(copy.getValidateResult());
        }

        if (copy.getPredictInput()!=null){
            this.predictBin = new Binary(copy.getPredictInput());
        }

        if (copy.getPredictResult()!=null){
            this.predictResultBin = new Binary(copy.getPredictResult());
        }
    }

    public ModelStore(final ModelStore copy){
        this.jobId = copy.jobId;
        this.modelId = copy.modelId;
        this.level = copy.level;
        if (copy.modelBin!=null){
            this.modelBin = copy.getModelBin();
        }

        if (copy.trainBin!=null){
            this.trainBin = copy.trainBin;
        }

        if (copy.validateBin!=null){
            this.validateBin = copy.validateBin;
        }

        if (copy.validateResultBin!=null){
            this.validateResultBin = copy.validateResultBin;
        }

        if (copy.predictBin!=null){
            this.predictBin = copy.predictBin;
        }

        if (copy.predictResultBin!=null){
            this.predictResultBin = copy.predictResultBin;
        }
    }

    public void update(final ModelStore copy){
        if (copy.modelBin!=null){
            this.modelBin = copy.getModelBin();
        }

        if (copy.trainBin!=null){
            this.trainBin = copy.trainBin;
        }

        if (copy.validateBin!=null){
            this.validateBin = copy.validateBin;
        }

        if (copy.validateResultBin!=null){
            this.validateResultBin = copy.validateResultBin;
        }

        if (copy.predictBin!=null){
            this.predictBin = copy.predictBin;
        }

        if (copy.predictResultBin!=null){
            this.predictResultBin = copy.predictResultBin;
        }
    }

    public ModelStore(String jobId, String modelId) {
        this.jobId = jobId;
        this.modelId = modelId;
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

    public Binary getModelBin() {
        return modelBin;
    }

    public void setModelBin(Binary modelBin) {
        this.modelBin = modelBin;
    }

    public Binary getTrainBin() {
        return trainBin;
    }

    public void setTrainBin(Binary trainBin) {
        this.trainBin = trainBin;
    }

    public Binary getValidateBin() {
        return validateBin;
    }

    public void setValidateBin(Binary validateBin) {
        this.validateBin = validateBin;
    }

    public Binary getValidateResultBin() {
        return validateResultBin;
    }

    public void setValidateResultBin(Binary validateResultBin) {
        this.validateResultBin = validateResultBin;
    }

    public Binary getPredictBin() {
        return predictBin;
    }

    public void setPredictBin(Binary predictBin) {
        this.predictBin = predictBin;
    }

    public Binary getPredictResultBin() {
        return predictResultBin;
    }

    public void setPredictResultBin(Binary predictResultBin) {
        this.predictResultBin = predictResultBin;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
