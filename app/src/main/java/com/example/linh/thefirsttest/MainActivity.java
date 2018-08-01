package com.example.linh.thefirsttest;

import android.content.res.AssetFileDescriptor;
import android.databinding.DataBindingUtil;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;

import com.aldebaran.qi.Consumer;
import com.aldebaran.qi.Future;
import com.aldebaran.qi.sdk.builder.AnimationBuilder;
import com.aldebaran.qi.sdk.object.actuation.Animate;
import com.aldebaran.qi.sdk.object.actuation.Animation;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.builder.AnimateBuilder;
import com.aldebaran.qi.sdk.builder.SayBuilder;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.aldebaran.qi.sdk.object.conversation.Say;
import com.example.linh.thefirsttest.databinding.ActivityMainBinding;
import com.example.linh.thefirsttest.model.ApiResponse;
import com.example.linh.thefirsttest.usecase.BaseUseCase;
import com.example.linh.thefirsttest.usecase.GetRestResponseUseCase;
import com.example.linh.thefirsttest.utils.PlatformApi;
import com.example.linh.thefirsttest.utils.Repository;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends RobotActivity implements RobotLifecycleCallbacks {
    private Animate animate;

    private MediaPlayer mediaPlayer;
    ActivityMainBinding binding;
    private String word;
    String audioUrl = "https://media.ucan.vn/upload/userfiles/organizations/1/1/audio/toeic/toeic9x.mp3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // Register the RobotLifecycleCallbacks to this Activity.
        QiSDK.register(this, this);

        PlatformApi infoAPI = ApiClient.getClient().create(PlatformApi.class);

        GetRestResponseUseCase useCase = new GetRestResponseUseCase(new Repository(infoAPI));

        useCase.execute(new BaseUseCase.UseCaseCallback<ApiResponse>() {
            @Override
            public void onSuccess(ApiResponse success) {
                Log.e("Response", "==> " + new Gson().toJson(success.response.result));
                word = success.response.messages.get(0);
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mediaPlayer = MediaPlayer.create(this, R.raw.gorila_sound);
    }

    @Override
    protected void onStop() {
        mediaPlayer.release();
        mediaPlayer = null;
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // Unregister the RobotLifecycleCallbacks for this Activity.
        QiSDK.unregister(this, this);
        super.onDestroy();
    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        // The robot focus is gained.
        // Create a new say action.
        Say say = SayBuilder.with(qiContext) // Create the builder with the context.
                .withText(word) // Set the text to say.
                .build(); // Build the say action.

        // Execute the action.
        say.run();

        // Create an animation.
        Animation animation = AnimationBuilder.with(qiContext) // Create the builder with the context.
                .withResources(R.raw.dance_b002) // Set the animation resource.
                .build(); // Build the animation.

//        Animation animation1 = AnimationBuilder.with(qiContext).withResources(R.raw)

        // Create an animate action.
        animate = AnimateBuilder.with(qiContext) // Create the builder with the context.
                .withAnimation(animation) // Set the animation.
                .build(); // Build the animate action.

        // Add an on started listener to the animate action.
        animate.addOnStartedListener(new Animate.OnStartedListener() {
            @Override
            public void onStarted() {
                String message = "Animation started.";
                Log.i("TAG", message);
                playMusic();

//                mediaPlayer.start();
            }
        });

        // Run the animate action asynchronously.
        Future<Void> animateFuture = animate.async().run();

        // Add a consumer to the action execution.
        animateFuture.thenConsume(new Consumer<Future<Void>>() {
            @Override
            public void consume(Future<Void> future) throws Throwable {
                if (future.isSuccess()) {
                    String message = "Animation finished with success.";
                    Log.i("TAG", message);
                    mediaPlayer.stop();
                } else if (future.hasError()) {
                    String message = "Animation finished with error.";
                    Log.e("TAG", message, future.getError());
                    mediaPlayer.stop();
                }
            }
        });
    }

    @Override
    public void onRobotFocusLost() {
        // The robot focus is lost.

        // Remove on started listeners from the animate action.
        if (animate != null) {
            animate.removeAllOnStartedListeners();
        }

    }

    @Override
    public void onRobotFocusRefused(String reason) {
        // The robot focus is refused.

    }

    private void playMusic() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}
