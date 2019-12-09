package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
    private Animation hurtAnimation;
    private Context context;
    private Animation fadeAnimation;
    private ImageView enemy;
    private ImageView yourPokemon;
    private ProgressBar yourHealth;
    private String enemyName;
    private MediaPlayer hurtSound;

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

        //change the Pokemon
        final ImageView nordle = findViewById(R.id.nordle);
        final ImageView challenmander = findViewById(R.id.challenmander);
        final ProgressBar nordleHealth = findViewById(R.id.pokemonHealth2);
        final ProgressBar challenHealth = findViewById(R.id.pokemonHealth);
        if (MainActivity.getActivePokemon().equals("NORDLE")) {
            nordle.setVisibility(View.VISIBLE);
            challenmander.setVisibility(View.GONE);
            yourPokemon = nordle;
            challenHealth.setVisibility(View.GONE);
            nordleHealth.setVisibility(View.VISIBLE);
            yourHealth = nordleHealth;
        } else {
            challenmander.setVisibility(View.VISIBLE);
            nordle.setVisibility(View.GONE);
            yourPokemon = challenmander;
            nordleHealth.setVisibility(View.GONE);
            challenHealth.setVisibility(View.VISIBLE);
            yourHealth = challenHealth;
        }

        //TextView stuff?
        TextView text1 = findViewById(R.id.battleText);
        text1.setText("WHAT WILL " + MainActivity.getYourName().toUpperCase() + " DO?");

        //initialize opponent's progress bar
        final ProgressBar opponentHealth = findViewById(R.id.opponentHealth);
        opponentHealth.setProgress(100);
        //initialize your pokemon progress bar
        yourHealth.setProgress(100);

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
            enemy = aabass;
            enemyName = "AABASS";
        } else if (gym == 1.0) {
            jishking.setVisibility(View.VISIBLE);
            enemy = jishking;
            enemyName = "JISHKING";
        } else if (gym == 2.0) {
            nercoal.setVisibility(View.VISIBLE);
            enemy = nercoal;
            enemyName = "NERCOAL";
        } else if (gym == 3.0) {
            healotic.setVisibility(View.VISIBLE);
            enemy = healotic;
            enemyName = "HEALOTIC";
        } else if (gym == 4.0) {
            vinithyama.setVisibility(View.VISIBLE);
            enemy = vinithyama;
            enemyName = "VINITHYAMA";
        } else if (gym == 5.0) {
            silasria.setVisibility(View.VISIBLE);
            enemy = silasria;
            enemyName = "SILASRIA";
        } else if (gym == 6.0) {
            mingnectric.setVisibility(View.VISIBLE);
            enemy = mingnectric;
            enemyName = "MINGNECTRIC";
        } else if (gym == 7.0) {
            mohammaunt.setVisibility(View.VISIBLE);
            enemy = mohammaunt;
            enemyName = "MOHAMMAUNT";
        }

        // run button goes back to prev activity w/ fade in/out transition
        Button runButton = findViewById(R.id.run);
        runButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(final View v) {
                findViewById(R.id.no).performClick();
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
        context = getApplicationContext();
        hurtAnimation = AnimationUtils.loadAnimation(context,R.anim.hurt);
        fadeAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_out);
        hurtSound = MediaPlayer.create(BattleActivity.this, R.raw.attack);

        Button fightButton = findViewById(R.id.fight);
        fightButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v) {
                findViewById(R.id.no).performClick();
                int oppHealth = opponentHealth.getProgress();
                int minus = oppHealth - 25;
                if (fxSoundOn) {
                    hurtSound.start();
                    try {
                        hurtSound.prepare();
                    } catch (Exception e) {
                        System.out.println("What!? An error!");
                    }
                    hurtSound.seekTo(0);
                }
                enemy.startAnimation(hurtAnimation);
                while (oppHealth >= minus) {
                    final int oh = oppHealth;
                    if (oh > 30 && oh < 50) {
                        opponentHealth.setProgressTintList(ColorStateList.valueOf(Color.YELLOW));
                    } else if (oh <= 30) {
                        opponentHealth.setProgressTintList(ColorStateList.valueOf(Color.RED));
                    }
                    new CountDownTimer(6450, 100000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            opponentHealth.setProgress(oh);
                        }

                        @Override
                        public void onFinish() {
                        }
                    }.start();
                    oppHealth--;
                }

                final TextView reText = findViewById(R.id.battleText);
                reText.setText(MainActivity.getActivePokemon() + " ATTACKS...");

                final int oh = oppHealth;
                //opponent attacks your pokemon if opponent is still alive
                if (oh > 0) {
                    new CountDownTimer(600, 10) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                        }
                        @Override
                        public void onFinish() {
                            findViewById(R.id.no).performClick();
                            int uHealth = yourHealth.getProgress();
                            int yourMinus = uHealth - 20;
                            if (fxSoundOn) {
                                hurtSound.start();
                                try {
                                    hurtSound.prepare();
                                } catch (Exception e) {
                                    System.out.println("What!? An error!");
                                }
                                hurtSound.seekTo(0);
                            }
                            yourPokemon.startAnimation(hurtAnimation);
                            while (uHealth >= yourMinus) {
                                final int o = uHealth;
                                if (o > 30 && o < 50) {
                                    yourHealth.setProgressTintList(ColorStateList.valueOf(Color.YELLOW));
                                } else if (o <= 30) {
                                    yourHealth.setProgressTintList(ColorStateList.valueOf(Color.RED));
                                }
                                new CountDownTimer(6450, 100000) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        yourHealth.setProgress(o);
                                    }

                                    @Override
                                    public void onFinish() {
                                    }
                                }.start();
                                uHealth--;
                            }
                            reText.setText(enemyName + " ATTACKS...");
                        }
                    }.start();
                }
                if (oh <= 0) {
                    enemy.setAnimation(fadeAnimation);
                    opponentHealth.setAnimation(fadeAnimation);
                    enemy.setVisibility(View.INVISIBLE);
                    opponentHealth.setVisibility(View.INVISIBLE);
                }
                new CountDownTimer(1500, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    @Override
                    public void onFinish() {
                        if (oh <= 0) {
                            battleMusic.pause();
                            reText.setText("THE OTHER POKEMON FAINTED. YOU WON!");
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
                            new CountDownTimer(4450, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                }

                                @Override
                                public void onFinish() {
                                    finish();//go back to the previous Activity
                                    overridePendingTransition(R.anim.fade_battle_in, R.anim.fade_battle_out);
                                }
                            }.start();
                        } else {
                            reText.setText("WHAT WILL " + MainActivity.getYourName().toUpperCase() + " DO?");
                        }
                    }
                }.start();
            }
        });

        Button bagButton = findViewById(R.id.bag);
        bagButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v) {
                findViewById(R.id.no).performClick();
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

        Button pokemon = findViewById(R.id.pokemon);
        pokemon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v) {
                final TextView reText = findViewById(R.id.battleText);
                if (MainActivity.gotNordle == true) {
                    if (MainActivity.getActivePokemon().equals("NORDLE")) {
                        reText.setText("USE CHALLEN MANDER?");
                    } else {
                        reText.setText("USE NORDLE?");
                    }
                    Button no = findViewById(R.id.no);
                    no.setVisibility(View.VISIBLE);
                    no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View V) {
                            findViewById(R.id.yes).setVisibility(View.GONE);
                            findViewById(R.id.no).setVisibility(View.GONE);
                            reText.setText("WHAT WILL " + MainActivity.getYourName().toUpperCase() + " DO?");
                        }
                    });
                    Button yes = findViewById(R.id.yes);
                    yes.setVisibility(View.VISIBLE);
                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View V) {
                            if (MainActivity.getActivePokemon().equals("NORDLE")) {
                                MainActivity.setActivePokemon("CHALLENDMANDER");
                                findViewById(R.id.challenmander).setVisibility(View.VISIBLE);
                                findViewById(R.id.nordle).setVisibility(View.GONE);
                                findViewById(R.id.pokemonHealth2).setVisibility(View.GONE);
                                findViewById(R.id.pokemonHealth).setVisibility(View.VISIBLE);
                                yourPokemon = challenmander;
                                yourHealth = challenHealth;
                            } else {
                                MainActivity.setActivePokemon("NORDLE");
                                findViewById(R.id.nordle).setVisibility(View.VISIBLE);
                                findViewById(R.id.challenmander).setVisibility(View.GONE);
                                findViewById(R.id.pokemonHealth).setVisibility(View.GONE);
                                findViewById(R.id.pokemonHealth2).setVisibility(View.VISIBLE);
                                yourPokemon = nordle;
                                yourHealth = nordleHealth;
                            }
                            findViewById(R.id.no).performClick();
                        }
                    });

                } else {
                    reText.setText("YOU HAVE NO OTHER POKEMON.");
                }
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
