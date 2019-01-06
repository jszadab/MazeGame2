package com.github.jszadab.Pathfinder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Class which is responsible for manual screen of Pathfinder app
 */
public class ManualActivity extends AppCompatActivity {

    /**
     * Class constructor which "find" all current layout elements
     * @param savedInstanceState as name of parameter suggests
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);
    }
}
