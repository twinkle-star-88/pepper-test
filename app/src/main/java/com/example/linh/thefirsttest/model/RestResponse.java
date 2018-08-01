package com.example.linh.thefirsttest.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RestResponse {
    @SerializedName("messages")
    public List<String> messages;

    @SerializedName("result")
    public List<ResultModel> result;
}
