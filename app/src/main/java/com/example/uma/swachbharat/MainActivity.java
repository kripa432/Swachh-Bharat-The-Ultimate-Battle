package com.example.uma.swachbharat;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity implements View.OnClickListener {
    Button area,player,ins;
    String[] area1={"village","city"};
    String [] player1={"Mohit","Mohini"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences prefs;
        SharedPreferences.Editor editor;
        prefs = getSharedPreferences("sb", MODE_PRIVATE);
        long topScore = prefs.getLong("topScore", 0);
        TextView t=(TextView)findViewById(R.id.topScore);
        t.setText("Top Score: "+topScore );
        final Button  play=(Button) findViewById(R.id.play);
        play.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return false;
    }

    @Override
    public void onClick( View v){
        Intent i=new Intent(this,GamePlay.class);
        startActivity(i);
        finish();
    }
    public void inst(View v){
        Intent i=new Intent(this,Instructions.class);
        startActivity(i);
        finish();

    }

}
