package com.redhat.pcerbak;

public class Arguments {
    public static Options parse(String[] arguments) {
        Options options = new Options();

        if(arguments.length >= 2) {
            if(arguments[0].equals("-l") || arguments[0].equals("--list")) {
                options.setOperation(Options.Operations.List);
                options.setJobsPath(arguments[1]);
            } else if(arguments[0].equals("-e") || arguments[0].equals("--enumerate")) {
                options.setOperation(Options.Operations.Enumerate);
                options.setJobsPath(arguments[1]);
            } else if(arguments[0].equals("-c") || arguments[0].equals("--compare")) {
                options.setOperation(Options.Operations.Compare);
                options.setJobsPath(arguments[1]);
                options.setQueryString(arguments[2]);
            }
        } else {
            throw new RuntimeException("Wrong number of arguments.");
        }

        return options;
    }
}
