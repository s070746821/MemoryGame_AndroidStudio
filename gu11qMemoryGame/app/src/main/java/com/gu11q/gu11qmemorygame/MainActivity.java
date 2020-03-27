package com.gu11q.gu11qmemorygame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void newgameClick(View view){


        Intent i= new Intent(this, MemoryGame.class);
        startActivity(i);
        finish();
        System.exit(0);

    }

    public void exitClick(View view) {

        finish();
        System.exit(0);
    }

}
