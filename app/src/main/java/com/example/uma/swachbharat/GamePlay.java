package com.example.uma.swachbharat;

import android.graphics.Point;
import android.os.Bundle;
import android.app.Activity;
import android.view.Display;
import android.view.KeyEvent;

public class GamePlay extends Activity {
    private GameView gameView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Display display = getWindowManager().getDefaultDisplay();
        // Load the resolution into a Point object
        Point size = new Point();
        display.getSize(size);
        gameView=new GameView(this,size.x,size.y);
        setContentView(gameView);
    }
    @Override
    protected void onPause(){
        super.onPause();
        gameView.pause();

    }
    @Override
    protected void onResume(){
        super.onResume();
        gameView.resume();

    }
    // If the player hits the back button, quit the app
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return false;
    }
    

}
