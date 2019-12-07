package com.example.finalproject;

import android.content.Context;
import android.graphics.Canvas;
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

    private Drawable challenNorth0;
    private Drawable challenNorth1;
    private Drawable challenNorth2;
    private Drawable challenWest0;
    private Drawable challenWest1;
    private Drawable challenWest2;
    private Drawable challenEast0;
    private Drawable challenEast1;
    private Drawable challenEast2;

    private Drawable nordleNorth0;
    private Drawable nordleNorth1;
    private Drawable nordleNorth2;
    private Drawable nordleWest0;
    private Drawable nordleWest1;
    private Drawable nordleWest2;
    private Drawable nordleEast0;
    private Drawable nordleEast1;
    private Drawable nordleEast2;

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

        //challenmander walking north
        challenNorth0 = ResourcesCompat.getDrawable(context.getResources(), R.drawable.challen_north_0, null);
        challenNorth1 = ResourcesCompat.getDrawable(context.getResources(), R.drawable.challen_north_1, null);
        challenNorth2 = ResourcesCompat.getDrawable(context.getResources(), R.drawable.challen_north_2, null);
        //challenmander walking west
        challenWest0 = ResourcesCompat.getDrawable(context.getResources(), R.drawable.challen_west_0, null);
        challenWest1 = ResourcesCompat.getDrawable(context.getResources(), R.drawable.challen_west_1, null);
        challenWest2 = ResourcesCompat.getDrawable(context.getResources(), R.drawable.challen_west_2, null);
        //challenmander walking east
        challenEast0 = ResourcesCompat.getDrawable(context.getResources(), R.drawable.challen_east_0, null);
        challenEast1 = ResourcesCompat.getDrawable(context.getResources(), R.drawable.challen_east_1, null);
        challenEast2 = ResourcesCompat.getDrawable(context.getResources(), R.drawable.challen_east_2, null);

        //nordle walking north
        nordleNorth0 = ResourcesCompat.getDrawable(context.getResources(), R.drawable.nord_north_0, null);
        nordleNorth1 = ResourcesCompat.getDrawable(context.getResources(), R.drawable.nord_north_1, null);
        nordleNorth2 = ResourcesCompat.getDrawable(context.getResources(), R.drawable.nord_north_2, null);
        //nordle walking west
        nordleWest0 = ResourcesCompat.getDrawable(context.getResources(), R.drawable.nord_west_0, null);
        nordleWest1 = ResourcesCompat.getDrawable(context.getResources(), R.drawable.nord_west_1, null);
        nordleWest2 = ResourcesCompat.getDrawable(context.getResources(), R.drawable.nord_west_2, null);
        //nordle walking east
        nordleEast0 = ResourcesCompat.getDrawable(context.getResources(), R.drawable.nord_east_0, null);
        nordleEast1 = ResourcesCompat.getDrawable(context.getResources(), R.drawable.nord_east_1, null);
        nordleEast2 = ResourcesCompat.getDrawable(context.getResources(), R.drawable.nord_east_2, null);
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

        //set bounds for BRENDAN
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

        //set bounds for CHALLENMANDER
        Rect challenBounds = new Rect(currentX - 25, currentY - 25 + 50,
                currentX + 25, currentY + 25 + 50);
        //set bounds for north
        challenNorth0.setBounds(challenBounds);
        challenNorth1.setBounds(challenBounds);
        challenNorth2.setBounds(challenBounds);
        //set bounds for west
        challenWest0.setBounds(challenBounds);
        challenWest1.setBounds(challenBounds);
        challenWest2.setBounds(challenBounds);
        //set bounds for east
        challenEast0.setBounds(challenBounds);
        challenEast1.setBounds(challenBounds);
        challenEast2.setBounds(challenBounds);

        //set bounds for NORDLE
        Rect nordleBounds = new Rect(currentX - 100, currentY + 25,
                currentX - 50, currentY + 25 + 50);
        //set bounds for north
        nordleNorth0.setBounds(nordleBounds);
        nordleNorth1.setBounds(nordleBounds);
        nordleNorth2.setBounds(nordleBounds);
        //set bounds for west
        nordleWest0.setBounds(nordleBounds);
        nordleWest1.setBounds(nordleBounds);
        nordleWest2.setBounds(nordleBounds);
        //set bounds for east
        nordleEast0.setBounds(nordleBounds);
        nordleEast1.setBounds(nordleBounds);
        nordleEast2.setBounds(nordleBounds);

        if (standing == 0) {
            brendanNorth1.draw(c);
            challenNorth1.draw(c);
            if (MainActivity.gotNordle) {
                nordleNorth1.draw(c);
            }
            standing++;
        } else if (vertical) {
            if (gait == 0) {
                brendanNorth0.draw(c);
                challenNorth0.draw(c);
                if (MainActivity.gotNordle) {
                    nordleNorth0.draw(c);
                }
            } else if (gait == 1) {
                brendanNorth1.draw(c);
                challenNorth1.draw(c);
                if (MainActivity.gotNordle) {
                    nordleNorth1.draw(c);
                }
            } else {
                brendanNorth2.draw(c);
                challenNorth2.draw(c);
                if (MainActivity.gotNordle) {
                    nordleNorth2.draw(c);
                }
            }
        } else if (west) {
            if (gait == 0) {
                brendanWest0.draw(c);
                challenWest0.draw(c);
                if (MainActivity.gotNordle) {
                    nordleWest0.draw(c);
                }
            } else if (gait == 1) {
                brendanWest1.draw(c);
                challenWest1.draw(c);
                if (MainActivity.gotNordle) {
                    nordleWest1.draw(c);
                }
            } else {
                brendanWest2.draw(c);
                challenWest2.draw(c);
                if (MainActivity.gotNordle) {
                    nordleWest2.draw(c);
                }
            }
        } else {
            if (gait == 0) {
                brendanEast0.draw(c);
                challenEast0.draw(c);
                if (MainActivity.gotNordle) {
                    nordleEast0.draw(c);
                }
            } else if (gait == 1) {
                brendanEast1.draw(c);
                challenEast1.draw(c);
                if (MainActivity.gotNordle) {
                    nordleEast1.draw(c);
                }
            } else {
                brendanEast2.draw(c);
                challenEast2.draw(c);
                if (MainActivity.gotNordle) {
                    nordleEast2.draw(c);
                }
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
