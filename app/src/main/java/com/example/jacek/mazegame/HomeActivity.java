package com.example.jacek.mazegame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void newGame(View view) {
        Intent intent = new Intent(HomeActivity.this, GameActivity.class);
        startActivity(intent);
    }

    public void showScores(View view) {
        Intent intent = new Intent(HomeActivity.this, ScoresActivity.class);
        startActivity(intent);
    }
}
