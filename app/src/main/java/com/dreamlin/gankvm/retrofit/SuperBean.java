package com.dreamlin.gankvm.retrofit;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SuperBean<T> implements Serializable {

    public @SerializedName("error")
    boolean error;
    public @SerializedName("results")
    T results;

}
