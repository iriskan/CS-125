package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StartActivity extends AppCompatActivity {
    private Context mContext;
    private Activity sActivity;

    private ConstraintLayout startLayout;
    private Button startButton;
    private Animation blinkAnimation;
    private MediaPlayer mediaStartPlayer;
    private MediaPlayer startButtonSound;
    public static boolean bgSoundOn;
    public static boolean fxSoundOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        mContext = getApplicationContext();
        sActivity = StartActivity.this;

        //bg music
        mediaStartPlayer= MediaPlayer.create(StartActivity.this, R.raw.startmusic);
        //start button sound
        startButtonSound = MediaPlayer.create(StartActivity.this,R.raw.startbutton);

        bgSoundOn = readSoundSetting(StartActivity.this, "bgSound");
        fxSoundOn = readSoundSetting(StartActivity.this, "fxSound");
        Log.e("STARTACTIVITY bgsound", "sound: " + bgSoundOn);
        Log.e("STARTACTIVITY fxSound", "sound: " + fxSoundOn);

        // Get the widget reference from XML layout
        startLayout = findViewById(R.id.start);
        startButton = findViewById(R.id.startbutton);

        // Get the animation from resource file
        blinkAnimation = AnimationUtils.loadAnimation(mContext,R.anim.blink);

        // Start the blink animation (fade in and fade out animation)
        // https://android--examples.blogspot.com/2016/10/android-button-blink-fade-in-fade-out.html
        startButton.startAnimation(blinkAnimation);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the blink animation (fade in and fade out animation)
                if (fxSoundOn) {
                    startButtonSound.start();
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent mainIntent = new Intent(sActivity, MainActivity.class);
                        startActivity(mainIntent);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                }, 0);
            }
        });
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (mediaStartPlayer.isPlaying()) {
            mediaStartPlayer.stop();
            try {
                mediaStartPlayer.prepare();
            } catch(Exception e) {
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.e("STARTACTIVITY bgsound", "sound: " + bgSoundOn);
        Log.e("STARTACTIVITY fxSound", "sound: " + fxSoundOn);
        mediaStartPlayer.seekTo(0);
        if (bgSoundOn) {
            mediaStartPlayer.start();
            mediaStartPlayer.setLooping(true);
        }
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.pikachuu)
                .setTitle("Exit Game?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("CANCEL", null)
                .show();
    }

    /**
     * read sound settings file
     * @param context
     * @param fileName
     * @return
     */
    public boolean readSoundSetting(Context context, String fileName) {
        boolean sound = true;
        try {
            InputStream inputStream = context.openFileInput(fileName);
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receivedString;

                while ((receivedString = bufferedReader.readLine()) != null) {
                    if (receivedString.equals("on")) {
                        sound = true;
                    } else if (receivedString.equals("off")) {
                        sound = false;
                    }
                }
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e){
        }
        return sound;
    }
}
