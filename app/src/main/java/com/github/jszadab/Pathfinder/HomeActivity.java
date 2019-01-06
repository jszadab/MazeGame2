package com.github.jszadab.Pathfinder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * Class which is responsible for main screen of Pathfinder app after start
 */
public class HomeActivity extends AppCompatActivity {

    /**
     * Class constructor which "find" all current layout elements
     * @param savedInstanceState as name of parameter suggests
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    /**
     * Method which change activity from home to game
     * @param view current view
     */
    public void onClickNewGame(View view) {
        Intent intent = new Intent(HomeActivity.this, GameActivity.class);
        startActivity(intent);
    }

    /**
     * Method which change activity from home to manual
     * @param view current view
     */
    public void onClickManual(View view) {
        Intent intent = new Intent(HomeActivity.this, ManualActivity.class);
        startActivity(intent);
    }
}
