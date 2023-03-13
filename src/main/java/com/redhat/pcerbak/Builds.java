package com.redhat.pcerbak;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import hudson.plugins.report.jck.main.cmdline.JobsRecognition;
import org.w3c.dom.Document;
import java.io.File;
import java.util.*;

public class Builds {
    private static boolean checkIfSuccessful(File build) {
        try {
            File buildXml = new File(build.getAbsolutePath() + "/build.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(buildXml);
            doc.getDocumentElement().normalize();

            String result = doc
                    .getElementsByTagName("result").item(0)
                    .getChildNodes().item(0)
                    .getNodeValue();

            // TODO should there also be UNSTABLE builds?
            if(result.equals("SUCCESS")) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean checkForNvr(File build, String nvrQuery) {
        if(nvrQuery.equals("") || nvrQuery.equals("*")) {
            return true;
        } else if(nvrQuery.charAt(0) == '{') {
            if (nvrQuery.charAt(nvrQuery.length() - 1) != '}') {
                throw new RuntimeException("Expected closing }.");
            }
            String[] nvrs = nvrQuery.substring(1, nvrQuery.length() - 1).split(",");
            String buildNvr = JobsRecognition.getChangelogsNvr(build);
            return Arrays.stream(nvrs).toList().contains(buildNvr);
        } else {
            return nvrQuery.equals(JobsRecognition.getChangelogsNvr(build));
        }
    }

    public static ArrayList<File> getBuilds(File job, boolean skipFailed, String nvrQuery) {
        ArrayList<File> listOfBuilds = new ArrayList<>();

        File buildDir = new File(job.getAbsolutePath() + "/builds/");
        File[] buildsInDir = Arrays
                .stream(Objects.requireNonNull(buildDir.listFiles()))
                .filter(File::isDirectory)
                .toArray(File[]::new);

        // sorting and then reversing to be sure it goes from the newest build to the oldest
        // sorting by parsing the build name to an integer first (to sort correctly)
        Arrays.sort(buildsInDir, Comparator.comparingInt(a -> Integer.parseInt(a.getName())));
        Collections.reverse(Arrays.asList(buildsInDir));

        boolean moreResults = !nvrQuery.equals("") && nvrQuery.charAt(0) == '{';

        if(skipFailed) {
            for(File build : buildsInDir) {
                if(checkIfSuccessful(build) && checkForNvr(build, nvrQuery)) {
                    listOfBuilds.add(build);
                    if(!moreResults) {
                        break;
                    }
                }
            }
        } else {
            for(File build : buildsInDir) {
                if(checkForNvr(build, nvrQuery)) {
                    listOfBuilds.add(build);
                    if(!moreResults) {
                        break;
                    }
                }
            }
        }
        return listOfBuilds;
    }

    public static ArrayList<String> getJobNvrs(File job) {
        ArrayList<String> nvrs = new ArrayList<>();
        File buildDir = new File(job.getAbsolutePath() + "/builds/");
        File[] buildsInDir = Arrays
                .stream(Objects.requireNonNull(buildDir.listFiles()))
                .filter(File::isDirectory)
                .toArray(File[]::new);

        for(File build : buildsInDir) {
            String nvr = JobsRecognition.getChangelogsNvr(build);
            if(nvr != null && !nvrs.contains(nvr)) {
                nvrs.add(nvr);
            }
        }
        return nvrs;
    }
}