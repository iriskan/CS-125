package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {
    private Context mContext;
    private Activity sActivity;

    private ConstraintLayout startLayout;
    private Button startButton;
    private Animation blinkAnimation;
    private Animation fadeInAnimation;
    private Animation fadeOutAnimation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        mContext = getApplicationContext();
        sActivity = StartActivity.this;

        // Get the widget reference from XML layout
        startLayout = findViewById(R.id.start);
        startButton = findViewById(R.id.startbutton);

        // Get the animation from resource file
        blinkAnimation = AnimationUtils.loadAnimation(mContext,R.anim.blink);
        fadeInAnimation = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
        // Start the blink animation (fade in and fade out animation)
        // https://android--examples.blogspot.com/2016/10/android-button-blink-fade-in-fade-out.html
        startButton.startAnimation(blinkAnimation);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the blink animation (fade in and fade out animation)
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent mainIntent = new Intent(sActivity, MainActivity.class);
                        startActivity(mainIntent);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        finish();
                    }
                }, 0);
            }
        });
    }
}
