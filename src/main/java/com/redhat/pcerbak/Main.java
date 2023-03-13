package com.redhat.pcerbak;

import hudson.plugins.report.jck.main.CompareBuilds;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws Exception {

        Options options = Arguments.parse(args);

        Jobs jobs = new Jobs(options.getJobsPath());

        if(options.getOperation() == Options.Operations.List) {
            jobs.printJobs();
        } else if(options.getOperation() == Options.Operations.Enumerate) {
            jobs.printVariants(options.getQueryString());
        } else if(options.getOperation() == Options.Operations.Compare) {
            Integer[] jobsToCompare = jobs.getJobIndexes(options.getQueryString());

            ArrayList<String> commandArguments = new ArrayList<>();
            for(int i = 0; i < jobs.getJobsInDir().length; i++) {
                if(jobsToCompare[i] == 1) {
                    if(options.isSkipFailed()) {
                        commandArguments.add(Builds.getLastSuccessfulBuild(jobs.getJobsInDir()[i]).getAbsolutePath());
                    } else {
                        commandArguments.add(Builds.getLastBuild(jobs.getJobsInDir()[i]).getAbsolutePath());
                    }
                }
            }
            commandArguments.add("--view=diff-list");
            commandArguments.add("--view=info");
            commandArguments.add("--view=info-hide-details");

            CompareBuilds.main(commandArguments.toArray(String[]::new));
        }
    }
}

