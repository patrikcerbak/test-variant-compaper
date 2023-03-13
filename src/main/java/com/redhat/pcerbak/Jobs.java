package com.redhat.pcerbak;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Jobs {
    public Jobs(String jobsDirectory) {
        File directory = new File(jobsDirectory);
        this.jobsInDir = Arrays
                .stream(Objects.requireNonNull(directory.listFiles()))
                .filter(File::isDirectory)
                .toArray(File[]::new);

        this.listWithJobVariants = new ArrayList<>();
        for(File job : jobsInDir) {
            String dirName = job.getName();
            listWithJobVariants.add(dirName.split("[.-]"));
        }
    }

    public void printJobs() {
        for(int i = 0; i < jobsInDir.length; i++) {
            System.out.printf("%d) %s\n", i, jobsInDir[i].getName());
        }
    }

    private ArrayList<ArrayList<String>> getVariantsList(String queryString) {
        ArrayList<ArrayList<String>> variantsList = new ArrayList<>();
        for(int i = 0; i < jobsInDir[0].getName().split("[.-]").length; i++) {
            ArrayList<String> jArr = new ArrayList<>();
            for(File job : jobsInDir) {
                String[] jobArr = job.getName().split("[.-]");
                if(ParseQueryString.checkJobWithQuery(jobArr, queryString) && !jArr.contains(jobArr[i])) {
                    jArr.add(jobArr[i]);
                }
            }
            variantsList.add(jArr);
        }
        return variantsList;
    }

    public void printVariants(String queryString) {
        ArrayList<ArrayList<String>> variantsList = getVariantsList(queryString);
        for(int i = 0; i < variantsList.size(); i++) {
            System.out.printf("%d) ", i + 1);
            for(String variant : variantsList.get(i)) {
                if(variantsList.get(i).indexOf(variant) != 0) {
                    System.out.print(", ");
                }
                System.out.printf("%s", variant);
            }
            System.out.print("\n");
        }
    }

    public Integer[] getJobIndexes(String queryString) {
        Integer[] listOfJobIndexes = new Integer[listWithJobVariants.size()];
        Arrays.fill(listOfJobIndexes, 1);

        for(String[] job : listWithJobVariants) {
            if(!ParseQueryString.checkJobWithQuery(job, queryString)) {
                listOfJobIndexes[listWithJobVariants.indexOf(job)] = 0;
            }
        }

        return listOfJobIndexes;
    }

    public File[] getJobsInDir() {
        return jobsInDir;
    }
    private final File[] jobsInDir;
    private final ArrayList<String[]> listWithJobVariants;
}
