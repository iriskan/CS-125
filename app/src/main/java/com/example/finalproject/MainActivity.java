package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Constraints;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    private Dialog dialogTrainer;
    public static boolean gotNordle;
    public static boolean gotBadge1;
    public static boolean gotBadge2;
    public static boolean gotBadge3;
    public static boolean gotBadge4;
    public static boolean gotBadge5;
    public static boolean gotBadge6;
    public static boolean gotBadge7;
    public static boolean gotBadge8;
    private ImageView nordle;
    private ImageView badge1;
    private ImageView badge2;
    private ImageView badge3;
    private ImageView badge4;
    private ImageView badge5;
    private ImageView badge6;
    private ImageView badge7;
    private ImageView badge8;

    private TextView name;
    private EditText editName;
    private AlertDialog nameDialog;
    private static String yourName;
    private static String activePokemon;
    private Drawable brendan;
    private Drawable may;

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
        activePokemon = "CHALLENMANDER";

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
                        System.out.println("Oh no!!");
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
                        System.out.println("What!? An error!");
                    }
                    gymButtonSound.seekTo(0);
                    StartActivity.fxSoundOn = false;
                    fxSoundOn = false;
                    BattleActivity.fxSoundOn = false;
                    soundSetting(MainActivity.this, "fxSound", "off");
                }
            }
        });

        Button restartButton = view.findViewById(R.id.restart);
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(R.drawable.pikachuu)
                        .setTitle("Restart Game?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                trainerSetting(MainActivity.this, "nordle", "no");
                                trainerSetting(MainActivity.this, "badge1", "no");
                                trainerSetting(MainActivity.this, "badge2", "no");
                                trainerSetting(MainActivity.this, "badge3", "no");
                                trainerSetting(MainActivity.this, "badge4", "no");
                                trainerSetting(MainActivity.this, "badge5", "no");
                                trainerSetting(MainActivity.this, "badge6", "no");
                                trainerSetting(MainActivity.this, "badge7", "no");
                                trainerSetting(MainActivity.this, "badge8", "no");
                                finish();
                            }

                        })
                        .setNegativeButton("CANCEL", null)
                        .show();
            }
        });

        mapMusic = MediaPlayer.create(MainActivity.this, R.raw.mapmusic);
        path = new double[4];
        timer = null;
        gymButtonSound = MediaPlayer.create(MainActivity.this, R.raw.startbutton);
        enterDoorSound = MediaPlayer.create(MainActivity.this, R.raw.enterdoor);
        gymButton();



        //trainer card
        //set up trainer card dialog
        builder = new AlertDialog.Builder(MainActivity.this);
        final View viewTrainer = inflater.inflate(R.layout.trainer_card_dialog, null);
        builder.setView(viewTrainer);

        dialogTrainer = builder.create();

        //image views of nordle and badges
        nordle = viewTrainer.findViewById(R.id.nordle);
        Log.e("nordle null?", "null: " + nordle);
        badge1 = viewTrainer.findViewById(R.id.badge1);
        badge2 = viewTrainer.findViewById(R.id.badge2);
        badge3 = viewTrainer.findViewById(R.id.badge3);
        badge4 = viewTrainer.findViewById(R.id.badge4);
        badge5 = viewTrainer.findViewById(R.id.badge5);
        badge6 = viewTrainer.findViewById(R.id.badge6);
        badge7 = viewTrainer.findViewById(R.id.badge7);
        badge8 = viewTrainer.findViewById(R.id.badge8);
        //booleans if trainer has badges or nordle
        gotNordle = readTrainerSetting(MainActivity.this, "nordle");
        gotBadge1 = readTrainerSetting(MainActivity.this, "badge1");
        gotBadge2 = readTrainerSetting(MainActivity.this, "badge2");
        gotBadge3 = readTrainerSetting(MainActivity.this, "badge3");
        gotBadge4 = readTrainerSetting(MainActivity.this, "badge4");
        gotBadge5 = readTrainerSetting(MainActivity.this, "badge5");
        gotBadge6 = readTrainerSetting(MainActivity.this, "badge6");
        gotBadge7 = readTrainerSetting(MainActivity.this, "badge7");
        gotBadge8 = readTrainerSetting(MainActivity.this, "badge8");

        //text view and edit text for trainer's name
        yourName = readNameSetting(MainActivity.this, "name");
        name = viewTrainer.findViewById(R.id.name);
        if (yourName != null) {
            name.setText(yourName);
        }
        nameDialog = new AlertDialog.Builder(this).create();
        editName = new EditText(this);
        nameDialog.setView(editName);
        nameDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                InputMethodManager imm = (InputMethodManager)getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editName.getWindowToken(), 0);
                editName.clearComposingText();
                name.setText(editName.getText());
                yourName = name.getText().toString();
                nameSetting(MainActivity.this, "name", yourName);
            }
        });
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editName.setText(name.getText());
                nameDialog.show();
            }
        });

        //set on clicker for pokeball button
        ImageButton pokeball = findViewById(R.id.pokeball);
        pokeball.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pop up dialog
                Window trainerWindow = dialogTrainer.getWindow();
                trainerWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                trainerWindow.setGravity(Gravity.CENTER);
                trainerWindow.setGravity(Gravity.CENTER_HORIZONTAL);
                trainerWindow.setGravity(Gravity.CENTER_VERTICAL);

                Button changeTrainer = findViewById(R.id.trainer);
                changeTrainer.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(final View v) {
                        trainerWindow.setBackgroundDrawable();
                    }
                });

                //set visibility of nordle or badges
                Log.e("gotNordle", "true?: " + gotNordle);
                if (gotNordle) {
                    nordle.setVisibility(View.VISIBLE);
                }
                if (gotBadge1) {
                    badge1.setVisibility(View.VISIBLE);
                }
                if (gotBadge2) {
                    badge2.setVisibility(View.VISIBLE);
                }
                if (gotBadge3) {
                    badge3.setVisibility(View.VISIBLE);
                }
                if (gotBadge4) {
                    badge4.setVisibility(View.VISIBLE);
                }
                if (gotBadge5) {
                    badge5.setVisibility(View.VISIBLE);
                }
                if (gotBadge6) {
                    badge6.setVisibility(View.VISIBLE);
                }
                if (gotBadge7) {
                    badge7.setVisibility(View.VISIBLE);
                }
                if (gotBadge8) {
                    badge8.setVisibility(View.VISIBLE);
                }
                dialogTrainer.show();
            }
        });
    }

    public static void setActivePokemon(String newPokemon) {
        activePokemon = newPokemon;
    }

    public static String getActivePokemon() {
        return activePokemon;
    }

    public boolean readTrainerSetting(Context context, String fileName) {
        boolean gotItem = false;
        try {
            InputStream inputStream = context.openFileInput(fileName);
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receivedString;

                while ((receivedString = bufferedReader.readLine()) != null) {
                    if (receivedString.equals("yes")) {
                        gotItem = true;
                    } else if (receivedString.equals("no")) {
                        gotItem = false;
                    }
                }
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e){
        }
        return gotItem;
    }

    public String readNameSetting(Context context, String fileName) {
        String n = null;
        try {
            InputStream inputStream = context.openFileInput(fileName);
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receivedString;

                while ((receivedString = bufferedReader.readLine()) != null) {
                    n = receivedString;
                }
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e){
        }
        return n;
    }

    public void nameSetting(Context context, String fileName, String aName) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            outputStreamWriter.write(aName);
            outputStreamWriter.close();
        } catch (IOException e) {
        }
    }

    public static String getYourName() {
        if (yourName == null) {
            return "you";
        }
        return yourName;
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
                                battleIntent.putExtra("gymNum", path[3]);
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
