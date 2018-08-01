package com.example.linh.thefirsttest.usecase;

import java.io.IOException;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import rx.internal.operators.SingleOnSubscribeUsing;

public abstract class BaseUseCase<T> {
    private Scheduler mainThread;
    private Scheduler ioThread;

    public BaseUseCase(Scheduler mainThread, Scheduler ioThread) {
        this.mainThread = mainThread;
        this.ioThread = ioThread;
    }

    public void execute(UseCaseCallback callback) {

        SingleOnSubscribe<T> emitter = e -> {
            e.onSuccess(background());
        };

        Single.create(emitter)
                .subscribeOn(ioThread)
                .observeOn(mainThread)
                .subscribe(
                        callback::onSuccess,
                        callback::onError);
    }

    public abstract T background() throws IOException;

    public interface UseCaseCallback<T> {
        void onSuccess(T success);

        void onError(Throwable t);
    }
}
