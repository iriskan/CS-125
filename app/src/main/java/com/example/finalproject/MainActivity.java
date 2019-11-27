package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.drawMap();
        mapMusic = MediaPlayer.create(MainActivity.this, R.raw.mapmusic);
        path = new double[4];
        timer = null;
        gymButtonSound = MediaPlayer.create(MainActivity.this, R.raw.startbutton);
        enterDoorSound = MediaPlayer.create(MainActivity.this,R.raw.enterdoor);
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
        mapMusic.stop();
        try {
            mapMusic.prepare();
        } catch(Exception e) {
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.e("resume????", "yes");
        mapMusic.seekTo(0);
        mapMusic.start();
        mapMusic.setLooping(true);
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
                       gymButtonSound.start();
                       Log.e("sighhhhh", "possible path is null");
                       for (int i = 0; i < path.length; i++) {
                           path[i] = possiblepath[i];
                       }
                       double div = (5000 / 150) / 3;
                       dy1 = (int) (path[0] / div);
                       dx = (int) (path[1] / div);
                       dy2 = (int) (path[2] / div);
                       Log.e("y1", "y1 distance: " + path[0]);
                       Log.e("x", "x distance: " + path[1]);
                       Log.e("y2", "y2 distance: " + path[2]);
                       timer = new CountDownTimer(5000,150) {
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
                               enterDoorSound.start();
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
}
