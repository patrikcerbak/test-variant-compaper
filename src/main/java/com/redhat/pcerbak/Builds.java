package com.redhat.pcerbak;

import java.io.File;
import java.util.Arrays;
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
}
