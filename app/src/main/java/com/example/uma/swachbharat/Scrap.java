package com.example.uma.swachbharat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;

import java.util.Random;

/**
 * Created by uma on 3/22/16.
 */
public class Scrap {
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
    public Scrap(Context context,int screenX,int screenY){
        Random generator1=new Random();
        int rand=generator1.nextInt(5)+1;
        //bitmap = BitmapFactory.decodeResource
          //      (context.getResources(), R.drawable.scrap1);
        System.out.print("Randm value(5) generaderd is "+rand);
        Log.e("output","Randm value(5) generaderd is "+rand);
        if(rand==1)
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.scrap1);
        else if(rand==2)
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.scrap2);
        else if(rand==3)
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.scrap3);
        else if(rand==4)
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.scrap4);
        else
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.scrap5);

        maxX=screenX;
        maxY=screenY;
        minX=0;
        minY=0;
        Random generator=new Random();
        speed=generator.nextInt(6)+1;
        x=screenX;
        y=screenY-150;
        flag=y;
        hitBox = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
        count=100;
        //flag=1;
    }

    public void update(int playerSpeed){
// Move to the left
        x -= playerSpeed;
        x -= speed;

//respawn when off screen
        if(x < minX-bitmap.getWidth()){
            Random generator = new Random();
            speed = generator.nextInt(10)+1;
            x = maxX;
            y = flag;
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
