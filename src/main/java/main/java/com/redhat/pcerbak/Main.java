package main.java.com.redhat.pcerbak;

import hudson.plugins.report.jck.main.CompareBuilds;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws Exception {

        Options options = Arguments.parse(args);

        Jobs jobs = new Jobs(options.getJobsPath());

        if(options.getOperation() == Options.Operations.List) {
            jobs.printJobs();
        } else if(options.getOperation() == Options.Operations.Compare) {
            Integer[] jobsToCompare = jobs.getJobIndexes(ParseQueryString.parseToList(options.getQueryString()));

            ArrayList<String> commandArguments = new ArrayList<>();
            for(int i = 0; i < jobs.getJobsInDir().length; i++) {
                if(jobsToCompare[i] == 1) {
                    File buildDir = new File(options.getJobsPath() + "/"
                            + jobs.getJobsInDir()[i].getName() + "/builds/");
                    File[] buildsInDir = Arrays
                            .stream(Objects.requireNonNull(buildDir.listFiles()))
                            .filter(File::isDirectory)
                            .toArray(File[]::new);
                    commandArguments.add(options.getJobsPath() + "/" + jobs.getJobsInDir()[i].getName()
                            + "/builds/" + buildsInDir[buildsInDir.length-1].getName());
                }
            }
            commandArguments.add("--view=diff-list");
            commandArguments.add("--view=info");
            commandArguments.add("--view=info-hide-details");

            CompareBuilds.main(commandArguments.toArray(String[]::new));
        }
    }
}

