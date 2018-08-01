package com.example.linh.thefirsttest.utils;

import com.example.linh.thefirsttest.model.ApiResponse;

import java.io.IOException;

public interface RepositoryInterface {
    ApiResponse getRestResponse() throws IOException;
}
