package main.java.com.redhat.pcerbak;

import java.util.ArrayList;

public class Options {

    public Options() {
        this.listOfCompares = new ArrayList<>();
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

    public ArrayList<Integer> getListOfCompares() {
        return listOfCompares;
    }

    public void setListOfCompares(ArrayList<Integer> listOfCompares) {
        this.listOfCompares = listOfCompares;
    }

    public void addToListOfCompares(Integer compareNumber) {
        this.listOfCompares.add(compareNumber);
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public enum Operations {
        List,
        Compare
    }

    private String queryString;
    private Operations operation;
    private String jobsPath;
    private ArrayList<Integer> listOfCompares;
}
