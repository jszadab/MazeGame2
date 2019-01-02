package com.example.jacek.mazegame;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

public class GameActivity extends AppCompatActivity {


    public Chronometer chrono;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        chrono = (Chronometer)findViewById(R.id.chrono);


    }



}
