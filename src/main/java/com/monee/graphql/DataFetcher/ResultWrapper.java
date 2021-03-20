package com.monee.graphql.DataFetcher;

public class ResultWrapper {

    private String str;

    public ResultWrapper (String str){
        this.str = str;
    }

    @Override
    public String toString() {
        return "ResultWrapper{" +
                "str='" + str + '\'' +
                '}';
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }
}
