package com.github.jszadab.Pathfinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

/**
 * Main activity of game. Includes all features like time measure, switches changing layout etc.
 * and what is important - the game view where is the most functionality of Pathfinder app.
 */
public class GameActivity extends AppCompatActivity {

    /**
     * Chronometer object which is use to show current time resolving of maze.
     */
    public Chronometer chronometer;

    /**
     * Switches used to change light mode and color theme.
     */
    public Switch lightSwitch, colorSwitch;

    /**
     * Text fields to get user feedback about current game.
     */
    public TextView timeValue, timeLabel, lampValue, lampLabel;

    /**
     * Object of layout what is need to create an "exchange" instance between view and activity.
     */
    public LinearLayout mainLayout;

    /**
     * Object of game view what is need to create an "exchange" instance between view and activity
     * in one layout.
     */
    private GameView gv;

    /**
     * Class constructor which "find" all current layout elements and set unnecessary
     * of them (at the begging of game) to invisible.
     * @param savedInstanceState as name of parameter suggests
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        chronometer = findViewById(R.id.chrono);
        lightSwitch = findViewById(R.id.swLight);
        colorSwitch = findViewById(R.id.swColorMode);

        timeValue = findViewById(R.id.txtBestTimeValue);
        timeValue.setVisibility(View.INVISIBLE);
        timeLabel = findViewById(R.id.txtBestTimeLabel);
        timeLabel.setVisibility(View.INVISIBLE);

        lampValue = findViewById(R.id.txtLampValue);
        lampLabel = findViewById(R.id.txtLampLabel);

        mainLayout = findViewById(R.id.mainLinearLayout);

        gv = findViewById(R.id.gameview); //"exchange" instance
        lampValue.setText(String.valueOf(gv.LAMPS));
    }

    /**
     * Method which reset chronometer, set player position to beginning and
     * invalidate (reload) whole layout.
     * @param view actually view object
     */
    public void onClickBtnAgain(View view) {
        resetChronometer();

        gv.setBegin();
        gv.invalidate();
    }

    /**
     * Method which reset chronometer, hide best time section and create new maze
     * to invalidate (reload) whole layout.
     * @param view actually view object
     */
    public void onClickBtnNext(View view) {
        resetChronometer();

        timeValue.setVisibility(View.INVISIBLE);
        timeLabel.setVisibility(View.INVISIBLE);

        gv.createMaze();
        gv.invalidate();
    }

    /**
     * Method which allows to reload layout immediately when light was switched off/on.
     * @param view actually view object
     */
    public void lightStateChange(View view) {
        gv.invalidate();
    }

    /**
     * Method which stop chronometer with additional flag to recognize
     * in what state chronometer actually is.
     */
    public void resetChronometer() {
        chronometer.stop();
        gv.isChronometerStarted = false;
    }

    /**
     * Method which allows to reload layout immediately when day mode was switched to night mode.
     * @param view actually view object
     */
    public void colorsStateChange(View view) {
        gv.invalidate();
    }
}
