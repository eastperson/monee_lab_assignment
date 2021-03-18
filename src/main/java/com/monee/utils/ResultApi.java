package com.monee.utils;

import com.monee.model.Post;

import java.util.Objects;

public class ResultApi<t> {

    private Boolean success;
    private int status;
    private t data;

    public static class statusCode{
        public final static int OK = 200;
        public final static int CREATED = 201;
        public final static int ACCEPTED = 202;
        public final static int NO_CONTENT = 204;

        public final static int BAD_REQUEST = 400;
        public final static int UNAUTHORIZED = 401;
        public final static int FORBIDDEN = 403;
        public final static int NOT_FOUND = 404;
        public final static int METHOD_NOT_ALLOWED = 405;
        public final static int CONFLICT = 409;
        public final static int TOO_MANY_REQUEST = 429;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResultApi resultApi = (ResultApi) o;
        return status == resultApi.status && Objects.equals(success, resultApi.success);
    }

    @Override
    public int hashCode() {
        return Objects.hash(success, status);
    }

    @Override
    public String toString() {
        return "ResultApi{" +
                "success=" + success +
                ", status=" + status +
                '}';
    }

    public void setData(t obj) {
        this.data = obj;
    }
}
