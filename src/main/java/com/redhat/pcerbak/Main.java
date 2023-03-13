package com.redhat.pcerbak;

import hudson.plugins.report.jck.main.CompareBuilds;

import java.io.File;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        Options options = Arguments.parse(args);

        Jobs jobs = new Jobs(options.getJobsPath());

        if(options.getOperation() == Options.Operations.List) {
            jobs.printJobs(options);
        } else if(options.getOperation() == Options.Operations.Enumerate) {
            jobs.printVariants(options.getQueryString());
        } else if(options.getOperation() == Options.Operations.Compare) {
            Integer[] jobsToCompare = jobs.getJobIndexes(options.getQueryString());

            ArrayList<String> commandArguments = new ArrayList<>();
            for(int i = 0; i < jobs.getJobsInDir().length; i++) {
                if(jobsToCompare[i] == 1) {
                    ArrayList<File> builds = Builds.getBuilds(jobs.getJobsInDir()[i], options.isSkipFailed(), options.getNvrQuery());
                    for(File build : builds) {
                        commandArguments.add(build.getAbsolutePath());
                    }
                }
            }
            commandArguments.add("--view=diff-list");
            commandArguments.add("--view=info");
            commandArguments.add("--view=info-hide-details");

            try {
                CompareBuilds.main(commandArguments.toArray(String[]::new));
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}

