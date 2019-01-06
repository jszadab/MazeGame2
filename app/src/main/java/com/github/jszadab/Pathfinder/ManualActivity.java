package com.github.jszadab.Pathfinder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Class which is responsible for manual screen of Pathfinder app
 */
public class ManualActivity extends AppCompatActivity {

    private TextView manualView;

    /**
     * Class constructor which "find" all current layout elements
     * @param savedInstanceState as name of parameter suggests
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);

        manualView = findViewById(R.id.textView);
        manualView.setText(setManualText());
    }

    private String setManualText() {

        String overallInfo = "Pathfinder is a logic game base on finding pathways to solve mazes.\n\n" +
                "Take a look at generated maze, place lamps, switch off the light, set up record " +
                "and then try to beat it! \n\n" +
                "Below you have some tips according to game layout.\n\n";

        String againBtnInfo = "If you are not satisfied your time you can just hit AGAIN button to start " +
                "whenever you want. \n\n";

        String nextBtnInfo = "If you are not satisfied generated maze or reach as good time as you could " +
                "then just hit NEXT button to continue game. \n\n";

        String lightSwInfo = "If light is switch ON you can analyze the maze and use lamps which helps you " +
                "during finding pathways. \n" +
                "If light is switch OFF you can move the player and verify your analysis. \n\n";

        String modeSwInfo = "Day mode of layout is set by default, but if you prefer dark theme fell free " +
                "to change it. \n\n";

        String lampsNumberInfo = "If you are wondering how much lamps you have just take above switches \n\n";

        String bestTimeAreaInfo = "Chronometer is counting up from your first move so take your time " +
                "at the beginning to plan moves carefully. Best time will be shown immediately when you set one.\n\n";

        String goodLuckWishes = "Good luck and have fun! \n\n";

        String manualTextValue = overallInfo + againBtnInfo + nextBtnInfo + lightSwInfo + modeSwInfo + lampsNumberInfo
        + bestTimeAreaInfo + goodLuckWishes;

        return manualTextValue;


    }
}
