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
    public TextView timeText;
    public LinearLayout mainLayout;

    GameView gv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        chrono = findViewById(R.id.chrono);
        lightSwitch = findViewById(R.id.lightSw);
        colorSwitch = findViewById(R.id.colorSw);
        timeText = findViewById(R.id.textView);
        timeText.setVisibility(View.INVISIBLE);
        mainLayout = findViewById(R.id.mainLinearLayout);
        gv = findViewById(R.id.gameview);
    }

    public void Again(View view) {

        resetChrono();

        gv.setBegin();
        gv.invalidate();

    }

    public void Next(View view) {

        resetChrono();

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

    public void colorStateChanged(View view) {

        if (colorSwitch.isChecked())
        mainLayout.setBackgroundColor(Color.BLACK);
        else
            mainLayout.setBackgroundColor(Color.rgb(250,	250,	250));
        gv.invalidate();
    }
}
