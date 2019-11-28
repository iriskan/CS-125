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
    private int yOffSet;
    private Drawable brendanNorth0;
    private Drawable brendanNorth1;
    private Drawable brendanNorth2;
    private Drawable brendanWest0;
    private Drawable brendanWest1;
    private Drawable brendanWest2;
    private Drawable brendanEast0;
    private Drawable brendanEast1;
    private Drawable brendanEast2;
    private Drawable image;
    private Rect[] original;
    private Rect[] gyms;
    private int[][] originalPaths;
    private double[][] gymPaths;
    private double scaler;
    private boolean vertical;
    private boolean west;
    private int gait;
    private int standing;
    public MapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        image = ResourcesCompat.getDrawable(context.getResources(), R.drawable.map, null);
        height = image.getIntrinsicHeight();
        width = image.getIntrinsicWidth();
        Log.e("HW", "height: " + height + ", width: " + width);
        scaler = getResources().getDisplayMetrics().density;
        Log.e("density","density: " + scaler);
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
            originalPaths[i][3] = i; //gym number
        }
        //brown gym
        int startpt = 460;
        originalPaths[0][0] = startpt - 418; //dy1
        originalPaths[0][2] = 418 - 393; //dy2
        //green gym
        originalPaths[1][0] = startpt - 418; //dy1
        originalPaths[1][2] = 418 - 393; //dy2
        //red gym
        originalPaths[2][0] = startpt - 320; //dy1
        originalPaths[2][2] = 318 - 299; //dy2
        //light blue gym
        originalPaths[3][0] = startpt - 320; //dy1
        originalPaths[3][2] = 318 - 299; //dy2
        //dark blue gym
        originalPaths[4][0] = startpt - 231; //dy1
        originalPaths[4][2] = 220 - 201; //dy2
        //white gym
        originalPaths[5][0] = startpt - 231; //dy1
        originalPaths[5][2] = 220 - 201; //dy2
        //magenta gym
        originalPaths[6][0] = startpt - 144; //dy1
        originalPaths[6][2] = 130 - 109; //dy2
        //purple gym
        originalPaths[7][0] = startpt - 144; //dy1
        originalPaths[7][2] = 130 - 109; //dy2

        //brendan walking north
        brendanNorth0 = ResourcesCompat.getDrawable(context.getResources(), R.drawable.brendan_walk_north_0, null);
        brendanNorth1 = ResourcesCompat.getDrawable(context.getResources(), R.drawable.brendan_walk_north_1, null);
        brendanNorth2 = ResourcesCompat.getDrawable(context.getResources(), R.drawable.brendan_walk_north_2, null);
        //brendan walking west
        brendanWest0 = ResourcesCompat.getDrawable(context.getResources(), R.drawable.brendan_walk_west_0,null);
        brendanWest1 = ResourcesCompat.getDrawable(context.getResources(), R.drawable.brendan_walk_west_1,null);
        brendanWest2 = ResourcesCompat.getDrawable(context.getResources(), R.drawable.brendan_walk_west_2,null);
        //brendan walking east
        brendanEast0 = ResourcesCompat.getDrawable(context.getResources(), R.drawable.brendan_walk_east_0, null);
        brendanEast1 = ResourcesCompat.getDrawable(context.getResources(), R.drawable.brendan_walk_east_1, null);
        brendanEast2 = ResourcesCompat.getDrawable(context.getResources(), R.drawable.brendan_walk_east_2, null);
        x0 = 0;
        y0 = 0;
        currentX = x0;
        currentY = y0;
        vertical = true;
        west = true;
        gait = 0;
        standing = 0;
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        float factor = (float) w / width;
        this.newWidth = w;
        this.newHeight = (int) (factor * height);
        yOffSet = (h - this.newHeight) / 2;
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e("new width", "width: " + newWidth);
        Log.e("factor", "factor: " + factor);
        x0 = newWidth / 2 ;
        y0 = (int) (460 * factor * scaler + yOffSet);
        currentX = x0;
        currentY = y0;

        //new gym coords
        Log.e("nulllllll", "original[0]: " + original[0].toString());
        Log.e("whaaaaat", "left: " + original[0].left);
        gyms = new Rect[8];
        for (int i = 0; i < original.length; i++) {
            int leftBound = (int) (factor * original[i].left * scaler);
            int rightBound = (int) (factor * original[i].right * scaler);
            int topBound = (int) ((factor * original[i].top * scaler) + yOffSet);
            int bottomBound = (int) ((factor * original[i].bottom * scaler) + yOffSet);
            gyms[i] = new Rect(leftBound, topBound,rightBound, bottomBound);
        }

        //new paths to gyms
        gymPaths = new double[8][4];
        for (int i = 0; i < originalPaths.length; i++) {
            gymPaths[i][3] = originalPaths[i][3];
            for (int j = 0; j < originalPaths[0].length - 1; j++) {
                gymPaths[i][j] = factor * originalPaths[i][j] * scaler;
            }
        }
    }
    @Override
    protected void onDraw(Canvas c) {
        Rect newBounds = new Rect(0, yOffSet, newWidth, yOffSet + newHeight);
        Log.e("newBounds", "bounds: " + newBounds.toString());
        image.setBounds(newBounds);
        image.draw(c);
        Rect brenBounds = new Rect(currentX - 50, currentY - 50,
                currentX + 50,currentY + 50);
        //set bounds for north
        brendanNorth0.setBounds(brenBounds);
        brendanNorth1.setBounds(brenBounds);
        brendanNorth2.setBounds(brenBounds);
        //set bounds for west
        brendanWest0.setBounds(brenBounds);
        brendanWest1.setBounds(brenBounds);
        brendanWest2.setBounds(brenBounds);
        //set bounds for east
        brendanEast0.setBounds(brenBounds);
        brendanEast1.setBounds(brenBounds);
        brendanEast2.setBounds(brenBounds);
        if (standing == 0) {
            brendanNorth1.draw(c);
            standing++;
        } else if (vertical) {
            if (gait == 0) {
                brendanNorth0.draw(c);
            } else if (gait == 1) {
                brendanNorth1.draw(c);
            } else {
                brendanNorth2.draw(c);
            }
        } else if (west) {
            if (gait == 0) {
                brendanWest0.draw(c);
            } else if (gait == 1) {
                brendanWest1.draw(c);
            } else {
                brendanWest2.draw(c);
            }
        } else {
            if (gait == 0) {
                brendanEast0.draw(c);
            } else if (gait == 1) {
                brendanEast1.draw(c);
            } else {
                brendanEast2.draw(c);
            }
        }
    }
    public void setPosition(int dx, int dy, int index) {
        if (index % 2 == 0) {
            currentX -= dx;
            west = true;
        } else {
            currentX += dx;
            west = false;
        }
        currentY -= dy;
        if (dy == 0) {
            vertical = false;
        } else {
            vertical = true;
        }
        if (gait < 2) {
            gait++;
        } else {
            gait = 0;
        }
        Log.e("GAITTTTT", "gait: " + gait);
        invalidate();
    }
    public double[] aGymPath (int x, int y) {
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
        standing = 0;
    }
}
