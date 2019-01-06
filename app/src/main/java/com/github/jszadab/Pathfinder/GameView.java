package com.github.jszadab.Pathfinder;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

/**
 * Class which is responsible for the game view where is the most functionality of Pathfinder app.
 * There is all of game logic.
 */
public class GameView extends View {

    private enum Direction{
        UP, DOWN, LEFT, RIGHT
    }

    /**
     * Flag to recognize in what state chronometer is.
     * It was needed to extend basic functionality of Chronometer class.
     */
    public boolean isChronometerStarted = false;

    /**
     * Hardcoded number of lamps what user can use to help during the game
     */
    public static final int LAMPS = 2;

    /**
     * Object of game activity what is need to create an "exchange" instance between activity and view
     * in one layout.
     */
    public GameActivity ga;

    private Cell[][] cells;
    private Cell[] lamps;
    private Cell player, exit;
    private static final int COLS = 10, ROWS = 10;
    private static final int WALL_THICKNESS = 4;
    private float cellSize, hMargin, vMargin;
    private Paint wallPaint, playerPaint, exitPaint, lampPaint;
    private Random random;

    /**
     * Constructor which load game by creating maze and initialize start variables.
     * @param context includes current context
     * @param attrs includes current attribute set
     */
    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        wallPaint = new Paint();
        wallPaint.setStrokeWidth(WALL_THICKNESS);
        playerPaint = new Paint();
        exitPaint = new Paint();
        lampPaint = new Paint();

        random = new Random();

        createMaze();

        ga = (GameActivity) context; //"exchange" instance

    }

    /**
     * Method which is responsible for key feature of Pathfinder game - creating a maze with many
     * pathway to one end. There is included all algorithms.
     */
    public void createMaze(){

        Stack<Cell> stack = new Stack<>();
        Cell current, next;

        cells = new Cell[COLS][ROWS];

        for (int x=0; x<COLS; x++){
            for(int y=0; y<ROWS; y++){
                cells[x][y] = new Cell(x, y);
            }
        }

        setBegin();

        for (int i=0; i<2; i++){
            for (int j=0; j<2; j++){
                for (int x=i+i*(COLS/2-1); x<COLS/2+i*COLS/2; x++){
                    for (int y=j+j*(ROWS/2-1); y<ROWS/2+j*ROWS/2; y++){

                        cells[x][y].visited=false;

                    }
                }

                current = cells[i+i*4][j+j*4];
                current.visited = true;

                do {
                    next = getNeighbour(current);
                    if (next != null) {
                        removeWall(current, next);
                        stack.push(current);
                        current = next;
                        current.visited = true;
                    } else {
                        current = stack.pop();
                    }
                } while (!stack.empty());
            }
        }

        connectMazes();
    }

    /**
     * Method which reset game what means: reset chronometer, set player position at the beginning and
     * exit position at the end, create a new set of lamps to use by player.
     */
    public void setBegin(){

        if (ga != null){
            ga.chronometer.setBase(SystemClock.elapsedRealtime());
            ga.lampValue.setText(String.valueOf(LAMPS));
        }

        player = cells[0][0];
        exit = cells[COLS -1][ROWS -1];

        lamps = new Cell[LAMPS];
    }

    /**
     * Method which create array list of adjacent cells to use it in maze-generating algorithm.
     * @param cell  cell what finding neighbor is based on
     * @return  nothing if neighbor wasn't found
     */
    private Cell getNeighbour(Cell cell){

        ArrayList<Cell> neighbours = new ArrayList<>();

        //left neighbour
        if (cell.col > 0)
            if(!cells[cell.col-1][cell.row].visited)
                neighbours.add(cells[cell.col-1][cell.row]);

        //right neighbour
        if (cell.col < COLS-1)
            if(!cells[cell.col+1][cell.row].visited)
                neighbours.add(cells[cell.col+1][cell.row]);

        //top neighbour
        if (cell.row > 0)
            if(!cells[cell.col][cell.row-1].visited)
                neighbours.add(cells[cell.col][cell.row-1]);

        //bottom neighbour
        if (cell.row < ROWS-1)
            if(!cells[cell.col][cell.row+1].visited)
                neighbours.add(cells[cell.col][cell.row+1]);

        if(neighbours.size() > 0) {
            int index = random.nextInt(neighbours.size());
            return neighbours.get(index);
        }
        return null;
    }

    /**
     * Method which remove border between neighboring cells
     * @param current   current cell
     * @param next  cell next to the current
     */
    private void removeWall(Cell current, Cell next){

        if(current.col == next.col && current.row == next.row+1){
            current.topWall = false;
            next.bottomWall = false;
        }
        if(current.col == next.col && current.row == next.row-1){
            current.bottomWall = false;
            next.topWall = false;
        }

        if(current.col == next.col+1 && current.row == next.row){
            current.leftWall = false;
            next.rightWall = false;
        }

        if(current.col == next.col-1 && current.row == next.row){
            current.rightWall = false;
            next.leftWall = false;
        }
    }

    /**
     * Method which is needed to connect independently created mazes in one witch a few pathways.
     */
    private void connectMazes(){

        removeWall(cells[COLS/2-1][ROWS/4],cells[COLS/2][ROWS/4]); //1/2
        removeWall(cells[COLS/2-1][ROWS/4+ROWS/2],cells[COLS/2][ROWS/4+ROWS/2]); //3/4

        removeWall(cells[COLS/4][ROWS/2-1],cells[COLS/4][ROWS/2]); //1/3
        removeWall(cells[COLS/4+COLS/2][ROWS/2-1],cells[COLS/4+COLS/2][ROWS/2]); //2/4

    }

    /**
     * Method which is called during creating and reloading the view.
     * As name suggest is responsible for drawing whole view.
     * @param canvas drawing object
     */
    @Override
    protected void onDraw(Canvas canvas) {

        if (!ga.colorSwitch.isChecked())
            canvas.drawColor(Color.DKGRAY);

        setColorMode();

        int width = getWidth();
        int height = getHeight();

        if (width/height < COLS/ROWS)
            cellSize = width/(COLS+1);
        else
            cellSize = height/(ROWS+1);

        hMargin = (width-COLS*cellSize)/2;
        vMargin = (height-ROWS*cellSize)/2;

        canvas.translate(hMargin, vMargin);

        if (ga.lightSwitch.isChecked())
            drawWhole(canvas);
        else
            drawPart(canvas);

        float margin = cellSize/10;

        drawExit(canvas, margin);
        drawLamps(canvas, margin);
        drawPlayer(canvas, margin);
    }

    /**
     * Method which is responsible for switching between day mode and night mode of game layout
     */
    public void setColorMode(){

        if (ga.colorSwitch.isChecked()){
            //day mode (by default)
            wallPaint.setColor(Color.BLACK);
            playerPaint.setColor(Color.rgb(255, 0, 0)); //red
            exitPaint.setColor(Color.rgb(0,0,255)); //blue
            lampPaint.setColor(Color.rgb(255,165,0)); //orange

            ga.mainLayout.setBackgroundColor(Color.rgb(250,	250,	250));

            ga.timeLabel.setTextColor(Color.BLACK);
            ga.timeValue.setTextColor(Color.BLACK);

            ga.timeLabel.setTextColor(Color.BLACK);
            ga.timeValue.setTextColor(Color.BLACK);

            ga.lampLabel.setTextColor(Color.BLACK);
            ga.lampValue.setTextColor(Color.BLACK);

            ga.lightSwitch.setTextColor(Color.BLACK);
            ga.colorSwitch.setTextColor(Color.BLACK);

            ga.chronometer.setTextColor(Color.BLACK);
        }
        else {
            //night mode
            wallPaint.setColor(Color.WHITE);
            playerPaint.setColor(Color.rgb(178,34,34)); //firebrick
            exitPaint.setColor(Color.rgb(65,105,255)); //royalblue
            lampPaint.setColor(Color.rgb(255,140,0)); //darkorange

            ga.mainLayout.setBackgroundColor(Color.DKGRAY);

            ga.timeLabel.setTextColor(Color.WHITE);
            ga.timeValue.setTextColor(Color.WHITE);

            ga.timeLabel.setTextColor(Color.WHITE);
            ga.timeValue.setTextColor(Color.WHITE);

            ga.lampLabel.setTextColor(Color.WHITE);
            ga.lampValue.setTextColor(Color.WHITE);

            ga.colorSwitch.setTextColor(Color.WHITE);
            ga.lightSwitch.setTextColor(Color.WHITE);

            ga.chronometer.setTextColor(Color.WHITE);
        }
    }

    /**
     * Method which draw all of maze when lamps are switch on.
     * @param canvas drawing object
     */
    private void drawWhole(Canvas canvas){
        for (int x=0; x<COLS; x++){
            for(int y=0; y<ROWS; y++){
                if (cells[x][y].topWall)
                    canvas.drawLine(
                            x*cellSize,
                            y*cellSize,
                            (x+1)*cellSize,
                            y*cellSize,
                            wallPaint);

                if (cells[x][y].leftWall)
                    canvas.drawLine(
                            x*cellSize,
                            y*cellSize,
                            x*cellSize,
                            (y+1)*cellSize,
                            wallPaint);

                if (cells[x][y].bottomWall)
                    canvas.drawLine(
                            x*cellSize,
                            (y+1)*cellSize,
                            (x+1)*cellSize,
                            (y+1)*cellSize,
                            wallPaint);
                if (cells[x][y].rightWall)
                    canvas.drawLine(
                            (x+1)*cellSize,
                            y*cellSize,
                            (x+1)*cellSize,
                            (y+1)*cellSize,
                            wallPaint);
            }
        }
    }

    /**
     * Method which draw only part of maze based on player and lamps position
     * when lamps are switch off.
     * @param canvas drawing object
     */
    private void drawPart(Canvas canvas){

        drawFromCell(canvas, player);

        for (int x=0; x<LAMPS; x++){
            if (lamps[x] != null){
                drawFromCell(canvas, lamps[x]);
            }
        }
        drawFromCell(canvas, exit);
    }

    /**
     * Method which draw only a partial view except whole maze.
     * @param canvas drawing object
     * @param cell cell what drawing partial view is based on
     */
    private void drawFromCell(Canvas canvas, Cell cell){

        for (int x=cell.col-1; x<=cell.col+1; x++){
            for(int y=cell.row-1; y<=cell.row+1; y++){

                if (x<0 || x>=COLS || y<0 || y>=ROWS ) continue; //out of range

                if (cells[x][y].topWall)
                    canvas.drawLine(
                            x*cellSize,
                            y*cellSize,
                            (x+1)*cellSize,
                            y*cellSize,
                            wallPaint);

                if (cells[x][y].leftWall)
                    canvas.drawLine(
                            x*cellSize,
                            y*cellSize,
                            x*cellSize,
                            (y+1)*cellSize,
                            wallPaint);

                if (cells[x][y].bottomWall)
                    canvas.drawLine(
                            x*cellSize,
                            (y+1)*cellSize,
                            (x+1)*cellSize,
                            (y+1)*cellSize,
                            wallPaint);
                if (cells[x][y].rightWall)
                    canvas.drawLine(
                            (x+1)*cellSize,
                            y*cellSize,
                            (x+1)*cellSize,
                            (y+1)*cellSize,
                            wallPaint);
            }
        }
    }

    /**
     * Method which draw exit.
     * @param canvas drawing object
     * @param margin padding in cell
     */
    private void drawExit(Canvas canvas, float margin){

        canvas.drawRect(
                exit.col*cellSize+margin,
                exit.row*cellSize+margin,
                (exit.col+1)*cellSize-margin,
                (exit.row+1)*cellSize-margin,
                exitPaint);
    }

    /**
     * Method which draw lamps.
     * @param canvas drawing object
     * @param margin padding in cell
     */
    private void drawLamps(Canvas canvas, float margin){

        for (int x=0; x<LAMPS; x++) {
            if (lamps[x] != null) {
                canvas.drawRect(
                        lamps[x].col * cellSize + margin,
                        lamps[x].row * cellSize + margin,
                        (lamps[x].col + 1) * cellSize - margin,
                        (lamps[x].row + 1) * cellSize - margin,
                        lampPaint);
            }
        }

    }

    /**
     * Method which draw player character.
     * @param canvas drawing object
     * @param margin padding in cell
     */
    private void drawPlayer(Canvas canvas, float margin){

        margin = (float)(1.3*margin); //to see the difference when player is on lamp or reach exit

        canvas.drawRect(
                player.col*cellSize+margin,
                player.row*cellSize+margin,
                (player.col+1)*cellSize-margin,
                (player.row+1)*cellSize-margin,
                playerPaint);
    }

    /**
     * Method which allows interaction and is responsible for recognize user intentions.
     * Here is implemented player's character moving and lamps setting;
     * @param event touch event
     * @return return true in each action
     */
    @Override
    public boolean onTouchEvent (MotionEvent event){

        if (event.getAction() == MotionEvent.ACTION_DOWN){

            if (ga.lightSwitch.isChecked()){
                float x = event.getX();
                float y = event.getY();

                x -= hMargin;
                y -= vMargin;

                int col = (int)(x/cellSize);
                int row = (int)(y/cellSize);

                int currLampsValue = Integer.parseInt(ga.lampValue.getText().toString());

                if (col >= 0 && col < COLS && row >= 0  && row < ROWS ) {

                    if (lamps[0] == null) {
                        lamps[0] = new Cell(col, row);
                        ga.lampValue.setText(String.valueOf(--currLampsValue));
                        invalidate();
                    } else if (lamps[1] == null && ( lamps[0].col != col || lamps[0].row != row)) {

                        lamps[1] = new Cell((int) (x / cellSize), (int) (y / cellSize));
                        ga.lampValue.setText(String.valueOf(--currLampsValue));
                        invalidate();
                    }
                }
            }
            return true;
        }

        if(event.getAction() == MotionEvent.ACTION_MOVE){

            if (!ga.lightSwitch.isChecked() && player != exit){
                float x = event.getX();
                float y = event.getY();

                float playerCenterX = hMargin + (player.col+0.5f)*cellSize;
                float playerCenterY = vMargin + (player.row+0.5f)*cellSize;

                float dx = x-playerCenterX;
                float dy = y-playerCenterY;

                float absDx = Math.abs(dx);
                float absDy = Math.abs(dy);

                if (absDx > cellSize || absDy > cellSize){

                    if (absDx > absDy){
                        //move in x-dir
                        if (dx > 0)
                            movePlayer(Direction.RIGHT);
                        else
                            movePlayer(Direction.LEFT);
                    }
                    else{
                        //move in y-dir
                        if (dy > 0)
                            movePlayer(Direction.DOWN);
                        else
                            movePlayer(Direction.UP);
                    }
                }
            }
            return true;
        }
        return super.onTouchEvent(event);
    }

    /**
     * Method which changing position of player character.
     * @param direction variable which is determined by touch of user
     */
    private void movePlayer(Direction direction){
        switch (direction){
            case UP:
                if (!player.topWall)
                    player = cells[player.col][player.row-1];
                break;
            case DOWN:
                if (!player.bottomWall)
                    player = cells[player.col][player.row+1];
                break;
            case LEFT:
                if (!player.leftWall)
                    player = cells[player.col-1][player.row];
                break;
            case RIGHT:
                if (!player.rightWall)
                    player = cells[player.col+1][player.row];
                break;
        }

        if (!isChronometerStarted) {
            startChronometer();
        }

        checkExit();
        invalidate();
    }

    /**
     * Method which start chronometer from 00:00 with additional flag to recognize
     * in what state chronometer actually is.
     */
    private void startChronometer(){

        ga.chronometer.setBase(SystemClock.elapsedRealtime());
        ga.chronometer.start();
        isChronometerStarted = true;
    }

    /**
     * Method which check if player is reached exit.
     */
    private void checkExit(){

        if (player == exit) {
            setBestTime();
            ga.resetChronometer();
        }
    }

    /**
     * Method which make best time area visible and updates times on each try to beat record to show
     * the shortest one;
     */
    private void setBestTime(){

        int chronometerValue = (int) (SystemClock.elapsedRealtime() - ga.chronometer.getBase()) / 1000;

        if (ga.timeValue.getVisibility() == View.INVISIBLE){
            ga.timeValue.setVisibility(View.VISIBLE);
            ga.timeLabel.setVisibility(View.VISIBLE);
            ga.timeValue.setText(String.valueOf(chronometerValue));
        } else if (chronometerValue < Integer.parseInt(ga.timeValue.getText().toString()) ){
            ga.timeValue.setText(String.valueOf(chronometerValue));
        }
    }
}
