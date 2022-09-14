package com.shobuxtreme.thesis.core;

import java.util.ArrayList;

public class ModelSet {

    private int level;

    private Model model;

    private int replicas;

    private ArrayList<ModelParameter> parameters;

    private boolean tuneParameters;

    public ModelSet() {
        this.level = -1;
        this.model = null;
        this.replicas = -1;
        this.parameters = null;
        this.tuneParameters = false;
    }

    public ModelSet(int level, Model model, int replicas,
            boolean tuneParameters) {
        this.level = level;
        this.model = model;
        this.replicas = replicas;
        this.parameters = new ArrayList<>();
        this.tuneParameters = tuneParameters;
    }

    public ModelSet(int level, Model model, int replicas,
            ArrayList<ModelParameter> parameters,
            boolean tuneParameters) {
        this.level = level;
        this.model = model;
        this.replicas = replicas;
        this.parameters = parameters;
        this.tuneParameters = tuneParameters;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public int getReplicas() {
        return replicas;
    }

    public void setReplicas(int replicas) {
        this.replicas = replicas;
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

    public boolean isTuneParameters() {
        return tuneParameters;
    }

    public void setTuneParameters(boolean tuneParameters) {
        this.tuneParameters = tuneParameters;
    }

    @Override
    public String toString() {
        return "ModelSet{" +
                "level=" + level +
                ", model=" + model +
                ", replicas=" + replicas +
                ", parameters=" + parameters +
                ", tuneParameters=" + tuneParameters +
                '}';
    }
}
