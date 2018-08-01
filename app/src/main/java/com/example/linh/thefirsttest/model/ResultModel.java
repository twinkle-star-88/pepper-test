package com.example.linh.thefirsttest.model;

import com.google.gson.annotations.SerializedName;

public class ResultModel {
    @SerializedName("name")
    public String name;

    @SerializedName("alpha2_code")
    public String alpha2;

    @SerializedName("alpha3_code")
    public String alpha3;
}
