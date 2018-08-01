package com.example.linh.thefirsttest.usecase;

import com.example.linh.thefirsttest.model.ApiResponse;
import com.example.linh.thefirsttest.utils.Repository;

import java.io.IOException;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class GetRestResponseUseCase extends BaseUseCase<ApiResponse> {

    private Repository repository;

    public GetRestResponseUseCase(Repository repository) {
        super(AndroidSchedulers.mainThread(), Schedulers.io());
        this.repository = repository;
    }

    @Override
    public ApiResponse background() throws IOException {
        return repository.getRestResponse();
    }
}
