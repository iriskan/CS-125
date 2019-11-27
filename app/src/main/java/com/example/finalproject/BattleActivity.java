package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class BattleActivity extends AppCompatActivity {
    private MediaPlayer battleMusic;
    private MediaPlayer runSound;
    // I used this for my graphics: https://github.com/hydrozoa-yt/pokemon/tree/master/res
    // I included in the drawable folder all the pokemon for each gym
    // the last element in path array in main activity contains gym number
    /**
     * a gym is clicked, specific gym battle generated
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);

        // create battle music
        battleMusic = MediaPlayer.create(BattleActivity.this, R.raw.battlemusic);

        //create run sound when exit battle
        runSound = MediaPlayer.create(BattleActivity.this, R.raw.runbutton);

        // run button goes back to prev activity w/ fade in/out transition
        Button runButton = findViewById(R.id.run);
        runButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(final View v) {
                runSound.start();
                finish();//go back to the previous Activity
                overridePendingTransition(R.anim.fade_battle_in, R.anim.fade_battle_out);
            }
        });
    }

    /**
     * When default back button pressed, uses fade in/out transition animation
     * instead of default transition b/w activities
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        runSound.start();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
    @Override
    protected void onPause() {
        super.onPause();
        battleMusic.stop();
        try {
            battleMusic.prepare();
        } catch(Exception e) {
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.e("resume????", "yes");
        battleMusic.seekTo(0);
        battleMusic.start();
        battleMusic.setLooping(true);
    }
}
