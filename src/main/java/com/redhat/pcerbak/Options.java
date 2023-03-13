package com.redhat.pcerbak;

public class Options {

    public Options() {
        this.queryString = "";
        this.skipFailed = true;
    }
    public Operations getOperation() {
        return operation;
    }

    public void setOperation(Operations operation) {
        this.operation = operation;
    }

    public String getJobsPath() {
        return jobsPath;
    }

    public void setJobsPath(String jobsPath) {
        this.jobsPath = jobsPath;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public boolean isSkipFailed() {
        return skipFailed;
    }

    public void setSkipFailed(boolean skipFailed) {
        this.skipFailed = skipFailed;
    }

    public enum Operations {
        List,
        Enumerate,
        Compare
    }

    private String queryString;
    private Operations operation;
    private String jobsPath;
    private boolean skipFailed;
}
