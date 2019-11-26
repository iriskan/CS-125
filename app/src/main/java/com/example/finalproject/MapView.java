package com.example.finalproject;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;

public class MapView extends View {
    private int x0;
    private int y0;
    private int currentX;
    private int currentY;
    private int height;
    private int width;
    private int newHeight;
    private int newWidth;
    private int yScreen;
    private Drawable brendan;
    private Drawable image;
    private Rect[] original;
    private Rect[] gyms;
    private int[][] originalPaths;
    private int[][] gymPaths;
    private double scaler;
    public MapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        image = ResourcesCompat.getDrawable(context.getResources(), R.drawable.map, null);
        height = image.getIntrinsicHeight();
        width = image.getIntrinsicWidth();
        Log.e("HW", "height: " + height + ", width: " + width);
        scaler = 2.75;
        //original location of gyms
        original = new Rect[8];
        //brown gym
        original[0] = new Rect(20, 344, 110, 380);
        //green gym
        original[1] = new Rect(183, 342, 272, 381);
        //red gym
        original[2] = new Rect(20, 247, 110, 283);
        //light blue gym
        original[3] = new Rect(183, 247, 272, 283);
        //dark blue gym
        original[4] = new Rect(20, 152, 110, 190);
        //white gym
        original[5] = new Rect(183, 152, 272, 190);
        //magenta gym
        original[6] = new Rect(20, 55, 110, 94);
        //purple gym
        original[7] = new Rect(183, 55, 272, 94);
        Log.e("nul???", "original[0]: " + original[0].toString());
        //original paths to gyms
        originalPaths = new int[8][4];
        for (int i = 0; i < 8; i++) {
            originalPaths[i][1] = 80; //dx
            originalPaths[i][2] = 20; //dy2
            originalPaths[i][3] = i;
        }
        //brown gym
        originalPaths[0][0] = 483 - 418; //dy1
        //green gym
        originalPaths[1][0] = 483 - 418; //dy1
        //red gym
        originalPaths[2][0] = 483 - 318; //dy1
        //light blue gym
        originalPaths[3][0] = 483 - 318; //dy1
        //dark blue gym
        originalPaths[4][0] = 483 - 222; //dy1
        //white gym
        originalPaths[5][0] = 483 - 222; //dy1
        //magenta gym
        originalPaths[6][0] = 483 - 127; //dy1
        //purple gym
        originalPaths[7][0] = 483 - 127; //dy1

        brendan = ResourcesCompat.getDrawable(context.getResources(), R.drawable.brendan_walk_north_1, null);
        x0 = 0;
        y0 = 0;
        currentX = x0;
        currentY = y0;
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        float factor = (float) w / width;
        this.newWidth = w;
        this.newHeight = (int) (factor * height);
        yScreen = (h - this.newHeight) / 2;
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e("new width", "width: " + newWidth);
        Log.e("factor", "factor: " + factor);
        x0 = newWidth / 2;
        y0 = newHeight;
        currentX = x0;
        currentY = y0;

        //new gym coords
        Log.e("nulllllll", "original[0]: " + original[0].toString());
        Log.e("whaaaaat", "left: " + original[0].left);
        gyms = new Rect[8];
        for (int i = 0; i < original.length; i++) {
            int leftBound = (int) (factor * original[i].left * scaler);
            int rightBound = (int) (factor * original[i].right * scaler);
            int topBound = (int) ((factor * original[i].top * scaler) + yScreen);
            int bottomBound = (int) ((factor * original[i].bottom * scaler) + yScreen);
            gyms[i] = new Rect(leftBound, topBound,rightBound, bottomBound);
        }

        //new paths to gyms
        gymPaths = new int[8][4];
        for (int i = 0; i < originalPaths.length; i++) {
            gymPaths[i][3] = originalPaths[i][3];
            for (int j = 1; j < originalPaths[0].length - 1; j++) {
                gymPaths[i][j] = (int) (factor * originalPaths[i][j] * scaler);
            }
        }
        gymPaths[0][0] = (int) (factor * originalPaths[1][0] * 1.8);
        gymPaths[1][0] = (int) (factor * originalPaths[1][0] * 1.8);
        gymPaths[2][0] = (int) (factor * originalPaths[2][0] * 2.2);
        gymPaths[3][0] = (int) (factor * originalPaths[3][0] * 2.2);
        gymPaths[4][0] = (int) (factor * originalPaths[4][0] * 2.3);
        gymPaths[5][0] = (int) (factor * originalPaths[5][0] * 2.3);
        gymPaths[6][0] = (int) (factor * originalPaths[6][0] * 2.42);
        gymPaths[7][0] = (int) (factor * originalPaths[7][0] * 2.42);
    }
    @Override
    protected void onDraw(Canvas c) {
        Rect newBounds = new Rect(0, yScreen, newWidth, yScreen + newHeight);
        Log.e("newBounds", "bounds: " + newBounds.toString());
        image.setBounds(newBounds);
        image.draw(c);
        Rect brenBounds = new Rect(currentX - 50, currentY - 50,
                currentX + 50,currentY + 50);
        brendan.setBounds(brenBounds);
        brendan.draw(c);
    }
    public void setPosition(int dx, int dy, int index) {
        if (index % 2 == 0) {
            currentX -= dx;
        } else {
            currentX += dx;
        }
        currentY -= dy;
        invalidate();
    }
    public int[] aGymPath (int x, int y) {
        for (int i = 0; i < gyms.length; i++) {
            Log.e("gym stuff", "coords: " + gyms[i].toString());
            //inside gym
            if (x >= gyms[i].left && x <= gyms[i].right
                    && y <= gyms[i].bottom && y >= gyms[i].top) {
                return gymPaths[i];
            }
        }
        return null;
    }
    public void drawMap() {
        invalidate();
    }
    public void restoreOriginal() {
        currentX = x0;
        currentY = y0;
    }
}
