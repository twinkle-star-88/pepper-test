package com.example.linh.thefirsttest.utils;

import com.example.linh.thefirsttest.model.ApiResponse;

import java.io.IOException;

public class Repository implements RepositoryInterface {
    private PlatformApi platformApi;

    public Repository(PlatformApi platformApi) {
        this.platformApi = platformApi;
    }

    @Override
    public ApiResponse getRestResponse() throws IOException {
        return platformApi.getRestResponse().execute().body();
    }
}
