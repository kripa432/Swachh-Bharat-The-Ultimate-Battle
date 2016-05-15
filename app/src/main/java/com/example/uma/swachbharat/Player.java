package com.example.uma.swachbharat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

/**
 * Created by uma on 3/22/16.
 */
public class Player {
    private Bitmap bitmap;
    private int shieldStrength;
    private int x, y;
    private int maxY,maxX;
    private int minY,minX;
    private int level1;
    private final int GRAVITY = -12-level1;

    private final int MIN_SPEED = 1;
    private final int MAX_SPEED = 20;
    private int speed = 0;
    public int score;
    private boolean boosting;
    private Rect hitBox;
    public Player(Context context,int screenX, int screenY,int level){
        level1=level;
        x = 50;
        score=0;
        speed = 1;
        if(level==1 || level==2) {
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player1);
        }else if(level==3){
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player2);
        }else{
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player3);
        }

        boosting = false;

        maxY = screenY - bitmap.getHeight();
        minY = 0;
        minX=0;
        maxX=screenX-bitmap.getWidth();
        hitBox = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
        shieldStrength = 10;
        y = screenY-bitmap.getWidth()-100;

    }
    public void update(){
        if (boosting) {
// Speed up
            speed += 2;
        } else {
// Slow down
            speed -= 5;
        }
        x += speed + GRAVITY;
        // Constrain top speed
        if (speed > MAX_SPEED) {
            speed = MAX_SPEED;
        }
// Never stop completely
        if (speed < MIN_SPEED) {
            speed = MIN_SPEED;
        }
        if (x < minX) {
            x = minX;
        }
        if (x > maxX) {
            x = maxX;
        }
        hitBox.left = x;
        hitBox.top = y;
        hitBox.right = x + bitmap.getWidth();
        hitBox.bottom = y + bitmap.getHeight();
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public int getSpeed(){
        return speed;
    }
    public Bitmap getBitmap(){
        return bitmap;
    }

    public void setBoosting() {
        boosting = true;
    }
    public void stopBoosting() {
        boosting = false;
    }
    public Rect getHitbox(){
        return hitBox;
    }
    public int getShieldStrength() {
        return shieldStrength;
    }
    public void reduceShieldStrength(){
        shieldStrength --;
    }
}
