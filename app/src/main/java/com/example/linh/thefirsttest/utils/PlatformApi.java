package com.example.linh.thefirsttest.utils;

import com.example.linh.thefirsttest.model.ApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface PlatformApi {
    @GET("country/get/all")
    Call<ApiResponse> getRestResponse();
}
