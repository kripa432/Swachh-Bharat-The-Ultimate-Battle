package com.example.uma.swachbharat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.io.Console;
import java.util.Random;

/**
 * Created by uma on 3/22/16.
 */
public class Enemy {
    private Bitmap bitmap;
    int x;
    int y;
    private int speed = 1;
    // Detect enemies leaving the screen
    private int maxX;
    private int minX;
    // Spawn enemies within screen bounds
    private int maxY;
    private int minY;
    private int count;
    private Rect hitBox;
    private int flag;
    public Enemy(Context context,int screenX,int screenY,int level){
        Random generator1=new Random();
        int rand=generator1.nextInt(level)+1;
        System.out.print(rand);

        if(level==1){
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy1);
        }else if(level==2){
            if(rand==1)
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy1);
            else
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy2);
        }else if(level==3){
            if(rand==1)
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy1);
            else if(rand==2)
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy2);
            else
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy3);
        }else if(level==4){
            if(rand==1)
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy1);
            else if(rand==2)
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy2);
            else if(rand==3)
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy3);
            else
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy4);
        }else{
            if(rand==1)
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy1);
            else if(rand==2)
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy2);
            else if(rand==3)
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy3);
            else if(rand==4)
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy4);
            else
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy5);
        }
        maxX=screenX;
        maxY=screenY;
        minX=0;
        minY=0;
        Random generator=new Random();
        speed=generator.nextInt(6)+10;
        x=screenX;
        y=generator.nextInt(maxY)-  bitmap.getHeight();
        hitBox = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
        count=100;
        flag=1;
    }

    public void update(int playerSpeed){
// Move to the left
        //x -= playerSpeed;
        x -= speed;

//respawn when off screen
        if(x < minX-bitmap.getWidth()){
            Random generator = new Random();
            speed = generator.nextInt(10)+1;
            x = maxX;
            y = generator.nextInt(maxY) - bitmap.getHeight();
        }
        Random generator = new Random();
        speed = generator.nextInt(10)+1;

        if(flag==1){
            count--;
            y+=speed;
        }else{
            y-=speed;
            count--;
        }
        if(count==0){
            count=100;
            if(flag==1)
                flag=2;
            else
                flag=1;
        }


        hitBox.left = x;
        hitBox.top = y;
        hitBox.right = x + bitmap.getWidth();
        hitBox.bottom = y + bitmap.getHeight();
    }

    //Getters and Setters
    public Bitmap getBitmap(){
        return bitmap;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public Rect getHitbox(){
        return hitBox;
    }
    public void setX(int x) {
        this.x = x;
    }
}
