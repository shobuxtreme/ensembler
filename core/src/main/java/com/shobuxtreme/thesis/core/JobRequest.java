package com.shobuxtreme.thesis.core;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class JobRequest {

    private String jobId;

    private String jobName;

    private Date dateCreated;

    private String trainDataSource;

    private String validateDataSource;

    private String predictDataSource;


    private String factorizeCmd;

    private HashMap<String, ModelSet> modelSet;

    private ExecuteKind jobKind;

    private ExecuteStatus jobStatus;

    public JobRequest() {
        this.jobId = UUID.randomUUID().toString();
        this.dateCreated = new Date();
        this.modelSet = new HashMap<>();
        this.jobKind = ExecuteKind.INIT;
        this.jobStatus = ExecuteStatus.RUNNING;
    }

    public JobRequest(JobRequest copyObj) {
        this.jobId = copyObj.getJobId();
        this.jobName = copyObj.getJobName();
        this.dateCreated = copyObj.getDateCreated();
        this.trainDataSource = copyObj.getTrainDataSource();
        this.validateDataSource = copyObj.getValidateDataSource();
        this.predictDataSource = copyObj.getPredictDataSource();
        this.factorizeCmd = copyObj.getFactorizeCmd();
        this.modelSet = copyObj.getModelSet();
        this.jobKind = copyObj.getJobKind();
        this.jobStatus = copyObj.getJobStatus();
    }

    public JobRequest(String jobName, String trainDataSource, String validateDataSource, String predictDataSource,
                      String factorizeCmd) {
        this.jobId = UUID.randomUUID().toString();
        this.jobName = jobName;
        this.dateCreated = new Date();
        this.trainDataSource = trainDataSource;
        this.validateDataSource = validateDataSource;
        this.predictDataSource = predictDataSource;
        this.factorizeCmd = factorizeCmd;
        this.modelSet = new HashMap<>();
        this.jobKind = ExecuteKind.INIT;
        this.jobStatus = ExecuteStatus.RUNNING;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getTrainDataSource() {
        return trainDataSource;
    }

    public void setTrainDataSource(String trainDataSource) {
        this.trainDataSource = trainDataSource;
    }

    public String getValidateDataSource() {
        return validateDataSource;
    }

    public void setValidateDataSource(String validateDataSource) {
        this.validateDataSource = validateDataSource;
    }

    public String getPredictDataSource() {
        return predictDataSource;
    }

    public void setPredictDataSource(String predictDataSource) {
        this.predictDataSource = predictDataSource;
    }

    public HashMap<String, ModelSet> getModelSet() {
        return modelSet;
    }

    public void setModelSet(HashMap<String, ModelSet> modelSet) {
        this.modelSet = modelSet;
    }

    public ExecuteKind getJobKind() {
        return jobKind;
    }

    public void setJobKind(ExecuteKind jobKind) {
        this.jobKind = jobKind;
    }

    public ExecuteStatus getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(ExecuteStatus jobStatus) {
        this.jobStatus = jobStatus;
    }

    public ModelSet getModelSet(String name) {
        return this.modelSet.get(name);
    }

    public void addModelSet(String name, ModelSet set) {
        this.modelSet.put(name, set);
    }

    public String getFactorizeCmd() {
        return factorizeCmd;
    }

    public void setFactorizeCmd(String factorizeCmd) {
        this.factorizeCmd = factorizeCmd;
    }

    @Override
    public String toString() {
        return "JobRequest{" +
                "jobId='" + jobId + '\'' +
                ", jobName='" + jobName + '\'' +
                ", dateCreated=" + dateCreated +
                ", trainDataSource='" + trainDataSource + '\'' +
                ", validateDataSource='" + validateDataSource + '\'' +
                ", predictDataSource='" + predictDataSource + '\'' +
                ", factorizeCmd='" + factorizeCmd + '\'' +
                ", modelSet=" + modelSet +
                ", jobKind=" + jobKind +
                ", jobStatus=" + jobStatus +
                '}';
    }
}
