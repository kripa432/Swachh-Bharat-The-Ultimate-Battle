package com.example.uma.swachbharat;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.nio.channels.FileLock;
import java.util.Random;

/**
 * Created by uma on 3/21/16.
 */
public class GameView extends SurfaceView implements Runnable {
    private boolean gameEnded;
    private int text1;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private int topScore;
    public Player p1;
    public Enemy e1,e2,e3;
    public Scrap s1;
    // For drawing
    private Paint paint;
    private int level=1;
    private Canvas canvas;
    private SurfaceHolder ourHolder;
    Bitmap bg,bg1;
    private int screenX;
    private int screenY;
    Thread gameThread = null;
    volatile boolean playing;
    private float distanceRemaining;
    private long timeTaken;
    private long timeStarted;
    private long fastestTime;
    private Context context;
    private int count;
    SoundPool soundPool=null;
    int flag1=0;
    public int flag=0;
    int start=-1,win=-1,bump=-1,destroyed=-1,background=-1;
    public GameView(Context context,int x,int y) {
        super(context);
        this.context=context;
        playing = false;
        ourHolder = getHolder();
        paint = new Paint();
        prefs = context.getSharedPreferences("sb",
                context.MODE_PRIVATE);
        editor = prefs.edit();
        fastestTime = prefs.getLong("topScore", 0);
        topScore=Math.round(fastestTime);
        screenX = x;
        screenY = y;
        startGame();
        //bg= BitmapFactory.decodeResource(context.getResources(), R.drawable.bg1);
        //bg1=Bitmap.createScaledBitmap(bg,bg.getWidth(),screenY ,true);
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC,0);
        try{
            //Create objects of the 2 required classes
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;

            //create our three fx in memory ready for use
            descriptor = assetManager.openFd("f.mp3");
            start = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("win.ogg");
            win = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("bump.ogg");
            bump = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("crash.ogg");
            destroyed = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("plane.ogg");
            background = soundPool.load(descriptor, 0);



        }catch(IOException e){
            //  Print an error message to the console
            Log.e("error", "failed to load sound files");
        }



    }


    // SurfaceView allows us to handle the onTouchEvent
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_UP:p1.stopBoosting();break;

            case MotionEvent.ACTION_DOWN:p1.setBoosting();if(gameEnded) {startGame();} break;
        }
        return true;
    }


    @Override
    public void run() {
        while (playing) {
            update();
            draw();
            control();
        }
    }

    public void pause() {
        playing = false;
        soundPool.pause(start);
        try {
            gameThread.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
        soundPool.resume(start);
    }

    public void update() {
        count--;
        p1.update();
        s1.update(p1.getSpeed());
        e1.update(p1.getSpeed());
        e2.update(p1.getSpeed());
        e2.update(p1.getSpeed());

        boolean hitDetected = false;
        if(Rect.intersects(p1.getHitbox(), e1.getHitbox())){ e1.setX(screenX+10); hitDetected = true;}
        if(Rect.intersects(p1.getHitbox(), e2.getHitbox())){e2.setX(screenX+100);hitDetected = true;}
        if(Rect.intersects(p1.getHitbox(), e3.getHitbox())){e3.setX(screenX+150);hitDetected = true;}

                        // Collision detection on new positions
                        // Before move because we are testing last frames
                        // position which has just been drawn


        if(Rect.intersects(p1.getHitbox(), s1.getHitbox())){
            //hitDetected = true;
            p1.score+=level;
            Random ran=new Random();
            s1.setX(screenX +ran.nextInt(400));
        }
        if(hitDetected) {
            soundPool.play(bump, 1, 1, 0, 0, 1);
            p1.reduceShieldStrength();
            if (p1.getShieldStrength() < 0) {
                gameEnded = true;
                soundPool.play(destroyed, 1, 1, 0, 0, 1);
            }
        }
        if(!gameEnded) {    //subtract distance to home planet based on current speed

            distanceRemaining -= p1.getSpeed(); //How long has the player been flying

            timeTaken = System.currentTimeMillis() - timeStarted;
        }

        if(distanceRemaining < 0){          //check for new fastest time     //Completed the game!

            if(p1.score>topScore) {
                topScore=p1.score;
                editor.putLong("topScore", p1.score);
                editor.commit();
            }

            distanceRemaining = 0;

            gameEnded = true;// Now end the game
        }
        if(flag1==0){
            flag1=soundPool.play(start,1,1,0,-1,1);
        }
    }

    public void draw() {

        if (ourHolder.getSurface().isValid()) {         //First we lock the area of memory we will be drawing
            canvas = ourHolder.lockCanvas();            // Rub out the last frame
                if(count<-(bg.getWidth()- screenX))
                    count=0;
                canvas.drawBitmap(bg,count,0,paint);
                canvas.drawBitmap(p1.getBitmap(),p1.getX(),p1.getY(),paint);
            if(!gameEnded) {

                canvas.drawBitmap(s1.getBitmap(),s1.getX(),s1.getY(),paint);
                canvas.drawBitmap(e1.getBitmap(),e1.getX(),e1.getY(), paint);
                canvas.drawBitmap(e2.getBitmap(),e2.getX(),e2.getY(), paint);
                canvas.drawBitmap(e3.getBitmap(), e3.getX(), e3.getY(), paint);

                paint.setTextAlign(Paint.Align.LEFT);
                paint.setColor(Color.argb(255, 255, 255, 255));
                paint.setTextSize(25);

                canvas.drawText("Time:" + timeTaken / 1000 + "s", 10, 20, paint);
                canvas.drawText("Level:" + level, screenX /2 , 20,paint);
                canvas.drawText("TopScore:" + prefs.getLong("topScore", 0), screenX -200 , 20,paint);

                canvas.drawText("Distance:" +distanceRemaining / 1000 +" KM", screenX / 3, screenY - 20, paint);
                canvas.drawText("Shield:" +p1.getShieldStrength(), 10, screenY - 20, paint);
                paint.setColor(Color.argb(255, 255, 0, 0));

                canvas.drawText("Score:" + p1.score +" points", (screenX / 3) * 2, screenY - 20, paint); // ourHolder.unlockCanvasAndPost(canvas);

            }else{

                paint.setTextSize(80);
                paint.setTextAlign(Paint.Align.CENTER);
                if(distanceRemaining>0) {
                    paint.setColor(Color.argb(255, 255, 0, 0));
                    canvas.drawText("Game Over", screenX / 2, 100, paint);
                    paint.setTextSize(25);

                    canvas.drawText("Time:" + timeTaken/1000 + "s", screenX / 2, 200, paint);
                    canvas.drawText("Distance remaining:" + distanceRemaining/1000 + " KM",screenX/2, 240, paint);
                    paint.setTextSize(80);
                    canvas.drawText("Tap to replay!", screenX / 2, 350, paint);
                }else{
                    paint.setColor(Color.argb(255,0 , 255, 0));
                    canvas.drawText("You Win", screenX / 2, 100, paint);
                    paint.setColor(Color.argb(255,0, 0, 255));
                    canvas.drawText("Next Level:"+level, screenX / 2, 150, paint);
                    if(flag==1){
                        level++;
                        flag=0;
                    }

                    paint.setTextSize(25);
                    canvas.drawText("Time:" + timeTaken/1000 + "s", screenX / 2, 200, paint);

                    paint.setColor(Color.argb(255, 255, 0, 0));
                    paint.setTextSize(50);
                    canvas.drawText("Score: " + p1.score + " points", screenX / 2, 240, paint);
                    paint.setTextSize(80);
                    paint.setColor(Color.argb(255, 0, 255, 0));
                    canvas.drawText("Tap to replay!", screenX / 2, 350, paint);

                }



            }


            ourHolder.unlockCanvasAndPost(canvas);  // Unlock and draw the scene
        }

    }

    public void control() {
        try {
            gameThread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void startGame(){

        p1 = new Player(context,screenX,screenY,level);
        e1= new Enemy(context,screenX,screenY,level);
        e2= new Enemy(context,screenX,screenY,level);
        e3= new Enemy(context,screenX,screenY,level);
        s1=new Scrap(context,screenX,screenY);

        distanceRemaining = 10000;// 10 km
        timeTaken = 0;
        if(level==1){
            bg= BitmapFactory.decodeResource(context.getResources(), R.drawable.bg1);
            bg=Bitmap.createScaledBitmap(bg,bg.getWidth(),screenY ,true);
        }else if(level==2){
            bg= BitmapFactory.decodeResource(context.getResources(), R.drawable.bg2);
            bg=Bitmap.createScaledBitmap(bg,bg.getWidth(),screenY ,true);
        }else if (level==3){
            bg= BitmapFactory.decodeResource(context.getResources(), R.drawable.bg3);
            bg=Bitmap.createScaledBitmap(bg,bg.getWidth(),screenY ,true);
        }else if(level==4){
            bg= BitmapFactory.decodeResource(context.getResources(), R.drawable.bg4);
            bg=Bitmap.createScaledBitmap(bg,bg.getWidth(),screenY ,true);
        }else if(level==5){
            bg= BitmapFactory.decodeResource(context.getResources(), R.drawable.bg4);
            bg=Bitmap.createScaledBitmap(bg,bg.getWidth(),screenY ,true);
        }
        flag=1;
        timeStarted = System.currentTimeMillis();   // Get start time
        gameEnded = false;
        flag1=0;

    }


}
