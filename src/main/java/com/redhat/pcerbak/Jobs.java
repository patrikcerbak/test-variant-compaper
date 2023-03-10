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

        this.variantsList = new ArrayList<>();
        for(int i = 0; i < jobsInDir.length; i++) {
            ArrayList<String> jArr = new ArrayList<>();
            for(File job : jobsInDir) {
                String varStr = job.getName().split("[.-]")[i];
                if(!jArr.contains(varStr)) {
                    jArr.add(varStr);
                }
            }
            variantsList.add(jArr);
        }
    }

    public void printJobs() {
        for(int i = 0; i < jobsInDir.length; i++) {
            System.out.printf("%d) %s\n", i, jobsInDir[i].getName());
        }
    }

    public void printVariants() {
        for(ArrayList<String> variantList : variantsList) {
            System.out.printf("%d) ", variantsList.indexOf(variantList) + 1);
            for(String variant : variantList) {
                if(variantList.indexOf(variant) != 0) {
                    System.out.print(", ");
                }
                System.out.printf("%s", variant);
            }
            System.out.print("\n");
        }
    }

    public Integer[] getJobIndexes(ArrayList<String[]> queryList) {
        Integer[] listOfJobIndexes = new Integer[listWithJobVariants.size()];
        Arrays.fill(listOfJobIndexes, 1);

        for(String[] job : listWithJobVariants) {
            if(!ParseQueryString.checkJobWithQuery(job, queryList)) {
                listOfJobIndexes[listWithJobVariants.indexOf(job)] = 0;
            }
        }

        return listOfJobIndexes;
    }

    public File[] getJobsInDir() {
        return jobsInDir;
    }

    public ArrayList<String[]> getListWithJobVariants() {
        return listWithJobVariants;
    }

    public ArrayList<ArrayList<String>> getVariantsList() {
        return variantsList;
    }

    private final File[] jobsInDir;
    private final ArrayList<String[]> listWithJobVariants;
    public ArrayList<ArrayList<String>> variantsList;
}
