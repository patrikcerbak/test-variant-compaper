package com.redhat.pcerbak;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import hudson.plugins.report.jck.main.cmdline.JobsRecognition;
import org.w3c.dom.Document;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

public class Builds {
    public static File getLastBuild(File job) {
        File buildDir = new File(job.getAbsolutePath() + "/builds/");
        File[] buildsInDir = Arrays
                .stream(Objects.requireNonNull(buildDir.listFiles()))
                .filter(File::isDirectory)
                .toArray(File[]::new);
        return new File(buildDir.getAbsolutePath() + "/" + buildsInDir[buildsInDir.length - 1].getName());
    }

    public static File getLastSuccessfulBuild(File job) {
        File buildDir = new File(job.getAbsolutePath() + "/builds/");
        File[] buildsInDir = Arrays
                .stream(Objects.requireNonNull(buildDir.listFiles()))
                .filter(File::isDirectory)
                .toArray(File[]::new);

        // sorting and then reversing to be sure it goes from the last build to the oldest
        Arrays.sort(buildsInDir);
        Collections.reverse(Arrays.asList(buildsInDir));

        for(File build : buildsInDir) {
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
                    return build;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        throw new RuntimeException("No successful build found.");
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