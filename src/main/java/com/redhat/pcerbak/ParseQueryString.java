package com.redhat.pcerbak;

import java.util.ArrayList;

public class ParseQueryString {
    static ArrayList<String[]> parseToList(String queryString) {
        String[] tempArray = queryString.split(" ");
        ArrayList<String[]> queryList = new ArrayList<>();

        for(String s : tempArray) {
            if(s.charAt(0) == '{') {
                if(s.charAt(s.length() - 1) != '}') {
                    throw new RuntimeException("Expected closing }.");
                }
                String[] a = s.substring(1, s.length() - 1).split(",");
                queryList.add(a);
            } else {
                String[] a = new String[1];
                a[0] = s;
                queryList.add(a);
            }
        }
        return queryList;
    }

    static boolean checkJobWithQuery(String[] jobArray, ArrayList<String[]> queryList) {
        for(int i = 0; i < queryList.size(); i++) {
            String[] qArr = queryList.get(i);
            if(qArr.length == 1) {
                if(!qArr[0].equals("*") && !qArr[0].equals(jobArray[i])) {
                    return false;
                }
            } else {
                boolean doesContain = false;
                for(String str : qArr) {
                    if (str.equals(jobArray[i])) {
                        doesContain = true;
                        break;
                    }
                }
                if(!doesContain) {
                    return false;
                }
            }
        }
        return true;
    }
}
