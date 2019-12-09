package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class BattleActivity extends AppCompatActivity {
    private MediaPlayer battleMusic;
    private MediaPlayer runSound;
    private MediaPlayer victoryMusic;
    public static boolean fxSoundOn;
    public static boolean bgSoundOn;
    private double gym;
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

        //create win sound when finishing a fight
        victoryMusic = MediaPlayer.create(BattleActivity.this, R.raw.victorymusic);

        //get volume settings
        bgSoundOn = StartActivity.bgSoundOn;
        fxSoundOn = StartActivity.fxSoundOn;

        //TextView stuff?
        TextView text1 = findViewById(R.id.battleText);
        text1.setText("WHAT WILL " + MainActivity.getYourName().toUpperCase() + " DO?");

        gym = getIntent().getDoubleExtra("gymNum", -1);
        ImageView aabass = findViewById(R.id.aabass);
        ImageView vinithyama = findViewById(R.id.vinithyama);
        ImageView jishking = findViewById(R.id.jishking);
        ImageView nercoal = findViewById(R.id.nercoal);
        ImageView healotic = findViewById(R.id.healotic);
        ImageView mingnectric = findViewById(R.id.mingnectric);
        ImageView silasria = findViewById(R.id.silasria);
        ImageView mohammaunt = findViewById(R.id.mohammaunt);
        vinithyama.setVisibility(View.INVISIBLE);
        aabass.setVisibility(View.INVISIBLE);
        jishking.setVisibility(View.INVISIBLE);
        nercoal.setVisibility(View.INVISIBLE);
        healotic.setVisibility(View.INVISIBLE);
        mingnectric.setVisibility(View.INVISIBLE);
        silasria.setVisibility(View.INVISIBLE);
        mohammaunt.setVisibility(View.INVISIBLE);

        if (gym == 0.0) {
            aabass.setVisibility(View.VISIBLE);
        } else if (gym == 1.0) {
            jishking.setVisibility(View.VISIBLE);
        } else if (gym == 2.0) {
            nercoal.setVisibility(View.VISIBLE);
        } else if (gym == 3.0) {
            healotic.setVisibility(View.VISIBLE);
        } else if (gym == 4.0) {
            vinithyama.setVisibility(View.VISIBLE);
        } else if (gym == 5.0) {
            silasria.setVisibility(View.VISIBLE);
        } else if (gym == 6.0) {
            mingnectric.setVisibility(View.VISIBLE);
        } else if (gym == 7.0) {
            mohammaunt.setVisibility(View.VISIBLE);
        }

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
                if (gym == 0.0) {
                    trainerSetting(BattleActivity.this, "badge1", "yes");
                    MainActivity.gotBadge1 = true;
                } else if (gym == 1.0) {
                    trainerSetting(BattleActivity.this, "badge2", "yes");
                    MainActivity.gotBadge2 = true;
                } else if (gym == 2.0) {
                    trainerSetting(BattleActivity.this, "badge3", "yes");
                    MainActivity.gotBadge3 = true;
                } else if (gym == 3.0) {
                    trainerSetting(BattleActivity.this, "badge4", "yes");
                    MainActivity.gotBadge4 = true;
                } else if (gym == 4.0) {
                    trainerSetting(BattleActivity.this, "badge5", "yes");
                    MainActivity.gotBadge5 = true;
                } else if (gym == 5.0) {
                    trainerSetting(BattleActivity.this, "badge6", "yes");
                    MainActivity.gotBadge6 = true;
                } else if (gym == 6.0) {
                    trainerSetting(BattleActivity.this, "badge7", "yes");
                    MainActivity.gotBadge7 = true;
                } else if (gym == 7.0) {
                    trainerSetting(BattleActivity.this, "badge8", "yes");
                    MainActivity.gotBadge8 = true;
                }
                if (MainActivity.gotBadge1 && MainActivity.gotBadge2 && MainActivity.gotBadge3
                        && MainActivity.gotBadge4 && MainActivity.gotBadge5 && MainActivity.gotBadge6
                        && MainActivity.gotBadge7 && MainActivity.gotBadge8) {
                    trainerSetting(BattleActivity.this, "nordle", "yes");
                    MainActivity.gotNordle = true;
                }
                if (fxSoundOn) {
                    victoryMusic.start();
                }
                final TextView reText = findViewById(R.id.battleText);
                reText.setText("YOUR POKEMON ATTACKS...");
                CountDownTimer timer = new CountDownTimer(2000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    @Override
                    public void onFinish() {
                        battleMusic.pause();
                        reText.setText("THE OTHER POKEMON FAINTED. YOU WON!");
                    }
                }.start();
                CountDownTimer time = new CountDownTimer(4450, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    @Override
                    public void onFinish() {
                        finish();//go back to the previous Activity
                        overridePendingTransition(R.anim.fade_battle_in, R.anim.fade_battle_out);
                    }
                }.start();
            }
        });

        Button bagButton = findViewById(R.id.bag);
        bagButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v) {
                final TextView reText = findViewById(R.id.battleText);
                reText.setText("THERE'S NO USE FOR THAT RIGHT NOW...");
                CountDownTimer timer = new CountDownTimer(3000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    @Override
                    public void onFinish() {
                        reText.setText("WHAT WILL " + MainActivity.getYourName().toUpperCase() + " DO?");
                    }
                }.start();
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
