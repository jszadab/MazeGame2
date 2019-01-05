package com.example.jacek.mazegame;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;


public class GameActivity extends AppCompatActivity {

    public Chronometer chrono;
    public Switch lightSwitch, colorSwitch;
    public TextView timeValue, timeLabel;
    public LinearLayout mainLayout;

    GameView gv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        chrono = findViewById(R.id.chrono);
        lightSwitch = findViewById(R.id.swLight);
        colorSwitch = findViewById(R.id.swColorMode);

        timeValue = findViewById(R.id.bestTimeValue);
        timeValue.setVisibility(View.INVISIBLE);
        timeLabel = findViewById(R.id.bestTimeLabel);
        timeLabel.setVisibility(View.INVISIBLE);


        mainLayout = findViewById(R.id.mainLinearLayout);

        gv = findViewById(R.id.gameview); //"exchange" instance
    }

    public void Again(View view) {

        resetChrono();

        gv.setBegin();
        gv.invalidate();

    }

    public void Next(View view) {

        resetChrono();

        timeValue.setVisibility(View.INVISIBLE);
        timeLabel.setVisibility(View.INVISIBLE);

        gv.createMaze();
        gv.invalidate();
    }


    public void lightStateChanged(View view) {

        gv.invalidate();

    }



    public void resetChrono()
    {
        chrono.stop();
        gv.isChronoStarted = false;
    }

    public void colorsStateChanged(View view) {

        gv.invalidate();
    }
}
