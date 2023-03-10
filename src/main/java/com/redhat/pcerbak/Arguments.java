package com.redhat.pcerbak;

public class Arguments {
    public static Options parse(String[] arguments) {
        Options options = new Options();

        if(arguments.length >= 2) {
            for (int i = 0; i < arguments.length; i++) {
                switch (arguments[i]) {
                    case "-l", "--list" -> options.setOperation(Options.Operations.List);
                    case "-e", "--enumerate" -> options.setOperation(Options.Operations.Enumerate);
                    case "-c", "--compare" -> options.setOperation(Options.Operations.Compare);
                    case "-p", "--path" -> {
                        if(i + 1 <= arguments.length) {
                            options.setJobsPath(arguments[i + 1]);
                        } else {
                            throw new RuntimeException("Expected path to jobs after -p.");
                        }
                    }
                    case "-q", "--query" -> {
                        if(i + 1 <= arguments.length) {
                            options.setQueryString(arguments[i + 1]);
                        } else {
                            throw new RuntimeException("Expected query string after -q.");
                        }
                    }
                }
            }
        } else {
            throw new RuntimeException("Expected arguments.");
        }
        return options;
    }
}
