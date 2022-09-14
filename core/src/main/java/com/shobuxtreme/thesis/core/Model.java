package com.shobuxtreme.thesis.core;

import java.util.ArrayList;
import java.util.UUID;

public class Model {
    private String modelId;

    private String modelName;

    private String modelImage;

    private int level;

    private String trainCmd;

    private String validateCmd;

    private String predictCmd;

    private ArrayList<ModelParameter> parameters;

    public Model() {
        this.modelId = "";
        this.modelName = "";
        this.modelImage = "";
        this.level = -1;
        this.trainCmd = "";
        this.validateCmd = "";
        this.predictCmd = "";
        this.parameters = null;
    }

    public Model(String modelName, String modelImage, int level,
            String trainCmd, String validateCmd,
            String predictCmd) {
        this.modelId = UUID.randomUUID().toString();
        this.modelName = modelName;
        this.modelImage = modelImage;
        this.level = level;
        this.trainCmd = trainCmd;
        this.validateCmd = validateCmd;
        this.predictCmd = predictCmd;
        this.parameters = new ArrayList<>();
    }

    public Model(final Model baseModel) {
        this.modelId = UUID.randomUUID().toString();
        this.modelName = baseModel.getModelName();
        this.modelImage = baseModel.getModelImage();
        this.level = baseModel.getLevel();
        this.trainCmd = baseModel.getTrainCmd();
        this.validateCmd = baseModel.getValidateCmd();
        this.predictCmd = baseModel.getPredictCmd();
        this.parameters = baseModel.getParameters();
    }

    public Model(String modelId, String modelName, String modelImage, int level,
            String trainCmd, String validateCmd,
            String predictCmd, ArrayList<ModelParameter> parameters) {
        this.modelId = modelId;
        this.modelName = modelName;
        this.modelImage = modelImage;
        this.level = level;
        this.trainCmd = trainCmd;
        this.validateCmd = validateCmd;
        this.predictCmd = predictCmd;
        this.parameters = parameters;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getModelImage() {
        return modelImage;
    }

    public void setModelImage(String modelImage) {
        this.modelImage = modelImage;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getTrainCmd() {
        return trainCmd;
    }

    public void setTrainCmd(String trainCmd) {
        this.trainCmd = trainCmd;
    }

    public String getValidateCmd() {
        return validateCmd;
    }

    public void setValidateCmd(String validateCmd) {
        this.validateCmd = validateCmd;
    }

    public String getPredictCmd() {
        return predictCmd;
    }

    public void setPredictCmd(String predictCmd) {
        this.predictCmd = predictCmd;
    }

    public ArrayList<ModelParameter> getParameters() {
        return parameters;
    }

    public ModelParameter getParameter(int pos) {
        return parameters.get(pos);
    }

    public void addParameter(ModelParameter parameter) {
        parameters.add(parameter);
    }

    public void setParameters(ArrayList<ModelParameter> parameters) {
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return "Model{" +
                "modelId='" + modelId + '\'' +
                ", modelName='" + modelName + '\'' +
                ", modelImage='" + modelImage + '\'' +
                ", level=" + level +
                ", trainCmd='" + trainCmd + '\'' +
                ", validateCmd='" + validateCmd + '\'' +
                ", predictCmd='" + predictCmd + '\'' +
                ", parameters=" + parameters +
                '}';
    }
}
