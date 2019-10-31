package com.dreamlin.gankvm.retrofit;

public class UniteException extends Exception {

    int code;
    String message;

    public UniteException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
