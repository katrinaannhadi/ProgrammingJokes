package com.katrinaann.programmingjokes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.katrinaann.programmingjokes.Entities.JokeLoreResponse;
import com.katrinaann.programmingjokes.Entities.JokeService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {
    private String TAG = "MainActivity";
    private ImageView iv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.joke);
        Button button = findViewById(R.id.button);
        final ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.layout);
        iv1 = (ImageView) findViewById(R.id.iv1);

        //gets array of colors from colors class with a getColor method that generates a random color
        Colors mColor = new Colors();

        //starts the call to play a joke in the TextView onCreate
        getNewJoke();

        //Set on click listener to start a short animation, start new joke method and change the color of the layout and background on animation end
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Animation anim1 = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake);
                final Animation.AnimationListener animationListener = new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        getNewJoke();
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        int color = mColor.getColor();
                        layout.setBackgroundColor(color);
                        button.setTextColor(color);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                };

                anim1.setAnimationListener(animationListener);
                iv1.startAnimation(anim1);

            }
        });
    }

    
    private void getNewJoke() {
        TextView textView = findViewById(R.id.joke);
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.chucknorris.io/").addConverterFactory(GsonConverterFactory.create()).build();

        JokeService service = retrofit.create(JokeService.class);
        Call<JokeLoreResponse> jokeCall = service.getJoke();

        jokeCall.enqueue(new Callback<JokeLoreResponse>() {
            @Override
            public void onResponse(Call<JokeLoreResponse> call, Response<JokeLoreResponse> response) {
                Log.d(TAG, "onResponse: SUCCESS");

                textView.setText(response.body().getValue());
            }
            @Override
            public void onFailure(Call<JokeLoreResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: FAILURE");
            }
        });
    }
}
