package com.example.jacek.mazegame;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Chronometer;
import android.widget.Switch;


public class GameActivity extends AppCompatActivity {

    public Chronometer chrono;
    public Switch lightSwitch;
    GameView gv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        chrono = findViewById(R.id.chrono);
        lightSwitch = findViewById(R.id.switch1);
    }

    public void Again(View view) {
        gv.setBegin();
        gv.invalidate();
    }

    public void continueWithoutSave(View view) {

        gv = findViewById(R.id.gameview);
        gv.createMaze();
        gv.invalidate();
    }


    public void stateChanged(View view) {

        gv = findViewById(R.id.gameview);
        gv.invalidate();

    }
}
