package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class BattleActivity extends AppCompatActivity {
    private MediaPlayer battleMusic;
    private MediaPlayer runSound;
    public static boolean fxSoundOn;
    public static boolean bgSoundOn;
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

        //get volume settings
        bgSoundOn = StartActivity.bgSoundOn;
        fxSoundOn = StartActivity.fxSoundOn;

        //TextView stuff?
        TextView text1 = findViewById(R.id.challenmanderDo);
        text1.setText("WHAT WILL " + MainActivity.getYourName().toUpperCase() + " DO?");

        // run button goes back to prev activity w/ fade in/out transition
        Button runButton = findViewById(R.id.run);
        runButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(final View v) {
                if (fxSoundOn) {
                    runSound.start();
                }
                finish();//go back to the previous Activity
                overridePendingTransition(R.anim.fade_battle_in, R.anim.fade_battle_out);
            }
        });

        // for now, the fight button also goes back to the previous activity with transition fade and music
        // i'm hoping to put whatever gym number it is' badge into your trainer card when you fight
        // so fight would be an immediate win. Also maybe to set green health bars?
        Button fightButton = findViewById(R.id.fight);
        fightButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v) {
                if (fxSoundOn) {
                    runSound.start();
                }
                //LinearLayout card = findViewById(R.id.trainerdialog);
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
        if (fxSoundOn) {
            runSound.start();
        }
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (battleMusic.isPlaying()) {
            battleMusic.stop();
            try {
                battleMusic.prepare();
            } catch(Exception e) {
                System.out.println("You done messed up!");
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        battleMusic.seekTo(0);
        if (bgSoundOn) {
            battleMusic.start();
            battleMusic.setLooping(true);
        }
    }

    /**
     * write in nordle or badges file if trainer has badges or nordle
     * Ex: trainerSetting(BattleActivity.this, "badge1", "yes");
     * @param context which activity context (should be BattleActivity.this)
     * @param fileName name of the file: "nordle" or "badge1" or "badge2" ... or "badge8"
     * @param gotItem put "yes" or "no"
     */
    public void trainerSetting(Context context, String fileName, String gotItem) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            outputStreamWriter.write(gotItem);
            outputStreamWriter.close();
        } catch (IOException e) {
            System.out.println("You done bad");
        }
    }
}
