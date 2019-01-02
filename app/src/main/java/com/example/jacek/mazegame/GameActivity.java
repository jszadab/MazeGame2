package com.example.jacek.mazegame;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Chronometer;


public class GameActivity extends AppCompatActivity {

    public Chronometer chrono;
    GameView gv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        chrono = findViewById(R.id.chrono);


    }

    public void saveAndAgain(View view) {



    }

    public void continueWithoutSave(View view) {

        gv = findViewById(R.id.gameview);
        gv.createMaze();
        gv.invalidate();
    }
}
