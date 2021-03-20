package com.monee.utils;

public class ResponseEntity<T> {

    int status;
    boolean success;
    T resultApi;

    public ResponseEntity(){

    }
    public ResponseEntity(int status, boolean success, T obj){
        this.status = status;
        this.success = success;
        this.resultApi = obj;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getResultApi() {
        return resultApi;
    }

    public void setResultApi(T resultApi) {
        this.resultApi = resultApi;
    }

    @Override
    public String toString() {
        return "ResponseEntity{" +
                "status=" + status +
                ", success=" + success +
                ", resultApi=" + resultApi +
                '}';
    }
}
