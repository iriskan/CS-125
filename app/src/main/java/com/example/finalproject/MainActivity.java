package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    private double[] path;
    private MapView mapView;
    private CountDownTimer timer;
    private int dy1;
    private int dx;
    private int dy2;
    private MediaPlayer mapMusic;
    private MediaPlayer gymButtonSound;
    private MediaPlayer enterDoorSound;
    private Switch bgMusic;
    private Switch soundFx;
    public static boolean bgSoundOn;
    public static boolean fxSoundOn;
    private AlertDialog.Builder builder;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.drawMap();

        //set up dialog
        builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);
        builder.setView(view);
        bgMusic = view.findViewById(R.id.bgmusic);
        soundFx = view.findViewById(R.id.soundeffects);

        dialog = builder.create();


        //settings button
        ImageButton settings = findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pop up dialog
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        //sound setting saved in a file
        bgSoundOn = StartActivity.bgSoundOn;
        fxSoundOn = StartActivity.fxSoundOn;
        if (bgSoundOn) {
            Log.e("checked????", "checked");
            bgMusic.setChecked(true);
        }
        if (fxSoundOn) {
            Log.e("checked????", "checked");
            soundFx.setChecked(true);
        }
        bgMusic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton cb, boolean on) {
                if (on) {
                    //Do something when Switch button is on/checked
                    mapMusic.seekTo(0);
                    mapMusic.start();
                    mapMusic.setLooping(true);
                    StartActivity.bgSoundOn = true;
                    bgSoundOn = true;
                    BattleActivity.bgSoundOn = true;
                    soundSetting(MainActivity.this, "bgSound", "on");
                } else {
                    //Do something when Switch is off/unchecked
                    mapMusic.stop();
                    try {
                        mapMusic.prepare();
                    } catch (Exception e) {
                    }
                    StartActivity.bgSoundOn = false;
                    bgSoundOn = false;
                    BattleActivity.bgSoundOn = false;
                    soundSetting(MainActivity.this, "bgSound", "off");
                }
            }
        });
        soundFx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton cb, boolean on) {
                if (on) {
                    //Do something when Switch button is on/checked
                    StartActivity.fxSoundOn = true;
                    fxSoundOn = true;
                    BattleActivity.fxSoundOn = true;
                    soundSetting(MainActivity.this, "fxSound", "on");
                } else {
                    //Do something when Switch is off/unchecked
                    gymButtonSound.stop();
                    try {
                        gymButtonSound.prepare();
                    } catch (Exception e) {
                    }
                    gymButtonSound.seekTo(0);
                    StartActivity.fxSoundOn = false;
                    fxSoundOn = false;
                    BattleActivity.fxSoundOn = false;
                    soundSetting(MainActivity.this, "fxSound", "off");
                }
            }
        });

        mapMusic = MediaPlayer.create(MainActivity.this, R.raw.mapmusic);
        path = new double[4];
        timer = null;
        gymButtonSound = MediaPlayer.create(MainActivity.this, R.raw.startbutton);
        enterDoorSound = MediaPlayer.create(MainActivity.this, R.raw.enterdoor);
        gymButton();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mapMusic.isPlaying()) {
            mapMusic.stop();
            try {
                mapMusic.prepare();
            } catch (Exception e) {
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("resume????", "yes");
        mapMusic.seekTo(0);
        if (bgSoundOn) {
            mapMusic.start();
            mapMusic.setLooping(true);
        }
    }

    /**
     * When a gym is clicked.
     */
    public void gymButton() {
        mapView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                Log.e("gym", "ACTION" + String.valueOf(e));
                if (e.getAction() == MotionEvent.ACTION_DOWN && timer == null) {
                    Log.e("passed", "it passed if statement");
                    int touchX = (int) e.getX();
                    int touchY = (int) e.getY();
                    double[] possiblepath = mapView.aGymPath(touchX, touchY);
                    Log.e("possible path", "null????" + possiblepath);
                    if (possiblepath != null) {
                        if (fxSoundOn) {
                            gymButtonSound.start();
                        }
                        Log.e("sighhhhh", "possible path is null");
                        for (int i = 0; i < path.length; i++) {
                            path[i] = possiblepath[i];
                        }
                        double div = (5000.0 / 150) / 3;
                        dy1 = (int) (path[0] / div);
                        dx = (int) (path[1] / div);
                        dy2 = (int) (path[2] / div);
                        Log.e("y1", "y1 distance: " + path[0]);
                        Log.e("x", "x distance: " + path[1]);
                        Log.e("y2", "y2 distance: " + path[2]);
                        timer = new CountDownTimer(5000, 150) {
                            @Override
                            public void onTick(long l) {
                                Log.e("onTick", "path[0]: " + path[0] + ", path[1]: " + path[1] + "path[2]" + path[2]);
                                if (path[0] > 0) {
                                    path[0] -= dy1;
                                    mapView.setPosition(0, dy1, (int) path[3]);
                                } else if (path[1] > 0) {
                                    path[1] -= dx;
                                    mapView.setPosition(dx, 0, (int) path[3]);
                                } else if (path[2] > 0) {
                                    path[2] -= dy2;
                                    mapView.setPosition(0, dy2, (int) path[3]);
                                }
                            }

                            @Override
                            public void onFinish() {
                                //NEED TO ADD BATTLE ACTIVITY INTENT
                                if (fxSoundOn) {
                                    enterDoorSound.start();
                                }
                                Intent battleIntent = new Intent(MainActivity.this, BattleActivity.class);
                                mapView.restoreOriginal();
                                timer = null;
                                startActivity(battleIntent);
                                overridePendingTransition(R.anim.fade_battle_in, R.anim.fade_battle_out);
                            }
                        };
                        timer.start();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * write in Sound file settings preferences
     * @param context which activity context
     * @param fileName name of the file: sound fx or bg music
     * @param sound off or on
     */
    public void soundSetting(Context context, String fileName, String sound) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            outputStreamWriter.write(sound);
            outputStreamWriter.close();
        } catch (IOException e) {
        }
    }

    public void showPokemonDialog() {

    }
}
