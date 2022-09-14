package com.shobuxtreme.thesis.core;

public class DataRequest {


    private String jobId;
    private String factorizeCmd;

    private ExecuteKind kind;

    private int level;

    private byte[] data;

    private int result;

    private String resultMsg;

    public DataRequest() {
        this.jobId = "";
        this.factorizeCmd = "";
        this.kind = ExecuteKind.TRAIN;
        this.level = 0;
        result = -1;
        resultMsg = "";
        data = null;
    }

    public DataRequest(String jobId, ExecuteKind kind, int level, String factorizeCmd) {
        this.jobId = jobId;
        this.factorizeCmd = factorizeCmd;
        this.kind = kind;
        this.level = level;
        this.result = -1;
        this.resultMsg = "";
        this.data = null;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public ExecuteKind getKind() {
        return kind;
    }

    public void setKind(ExecuteKind kind) {
        this.kind = kind;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getFactorizeCmd() {
        return factorizeCmd;
    }

    public void setFactorizeCmd(String factorizeCmd) {
        this.factorizeCmd = factorizeCmd;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DataRequest{" +
                "jobId='" + jobId + '\'' +
                ", factorizeCmd=" + factorizeCmd +
                ", kind=" + kind +
                ", level=" + level +
                ", result=" + result +
                ", resultMsg='" + resultMsg + '\'' +
                '}';
    }
}
