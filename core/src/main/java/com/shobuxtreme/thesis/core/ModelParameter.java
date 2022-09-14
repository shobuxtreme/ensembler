package com.shobuxtreme.thesis.core;

public class ModelParameter {

    private String name;

    private String type;

    private String stringValue;

    private String minLimit;

    private String maxLimit;

    public ModelParameter(String name) {
        this.name = name;
    }

    public ModelParameter(String name, String type, String stringValue, String minLimit, String maxLimit) {
        this.name = name;
        this.type = type;
        this.stringValue = stringValue;
        this.minLimit = minLimit;
        this.maxLimit = maxLimit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getMinLimit() {
        return minLimit;
    }

    public void setMinLimit(String minLimit) {
        this.minLimit = minLimit;
    }

    public String getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(String maxLimit) {
        this.maxLimit = maxLimit;
    }

    @Override
    public String toString() {
        return "ModelParameter{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", stringValue='" + stringValue + '\'' +
                ", minLimit='" + minLimit + '\'' +
                ", maxLimit='" + maxLimit + '\'' +
                '}';
    }
}
