package com.example.jacek.mazegame;

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

public class GameView extends View {

    private enum Direction{
        UP, DOWN, LEFT, RIGHT
    }

    private Cell[][] cells;
    private Cell[] lamps;
    private Cell player, exit;
    private static final int COLS = 10, ROWS = 10, LAMPS = 2;
    private static final int WALL_THICKNESS = 4;

    private float cellSize, hMargin, vMargin;
    private Paint wallPaint, playerPaint, exitPaint, lampPaint;
    private Random random;

    public boolean isChronoStarted = false;
    private int chronoTime;

    GameActivity ga;

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        wallPaint = new Paint();
        wallPaint.setStrokeWidth(WALL_THICKNESS);
        playerPaint = new Paint();
        exitPaint = new Paint();
        lampPaint = new Paint();

        random = new Random();

        createMaze();

        ((MyApplication) MyApplication.getAppContext()).setMaze(new Maze(cells));

        ga = (GameActivity) context; //class instance with chrono etc.

    }

    public void colorMode(){

        if (ga.colorSwitch.isChecked()){
            wallPaint.setColor(Color.WHITE);
            playerPaint.setColor(Color.RED);
            exitPaint.setColor(Color.BLUE);
            lampPaint.setColor(Color.YELLOW); //night mode
        } else {
            wallPaint.setColor(Color.GRAY);
            playerPaint.setColor(Color.RED);
            exitPaint.setColor(Color.BLUE);
            lampPaint.setColor(Color.YELLOW); //day mode default
        }


        //TO DO: main layout


    }


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

    public void setBegin(){


        if (ga != null){
            ga.chrono.setBase(SystemClock.elapsedRealtime());
        }
        player = cells[0][0];
        lamps = new Cell[LAMPS];
        exit = cells[COLS -1][ROWS -1];

    }

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

    private void connectMazes(){

        removeWall(cells[COLS/2-1][ROWS/4],cells[COLS/2][ROWS/4]); //1/2
        removeWall(cells[COLS/2-1][ROWS/4+ROWS/2],cells[COLS/2][ROWS/4+ROWS/2]); //3/4

        removeWall(cells[COLS/4][ROWS/2-1],cells[COLS/4][ROWS/2]); //1/3
        removeWall(cells[COLS/4+COLS/2][ROWS/2-1],cells[COLS/4+COLS/2][ROWS/2]); //2/4

    }

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

    private void drawPart(Canvas canvas){

        drawFromCell(canvas, player);

        for (int x=0; x<LAMPS; x++){
            if (lamps[x] != null){
                drawFromCell(canvas, lamps[x]);
            }
        }

        drawFromCell(canvas, exit);


    }

    private void drawFromCell(Canvas canvas, Cell cell){
        for (int x=cell.col-1; x<=cell.col+1; x++){
            for(int y=cell.row-1; y<=cell.row+1; y++){
                if (x<0 || x>=COLS || y<0 || y>=ROWS ) continue;
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


    @Override
    protected void onDraw(Canvas canvas) {

        if (ga.colorSwitch.isChecked())
            canvas.drawColor(Color.BLACK);

        colorMode();

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

    private void drawPlayer(Canvas canvas, float margin){

        canvas.drawRect(
                player.col*cellSize+2*margin,
                player.row*cellSize+2*margin,
                (player.col+1)*cellSize-2*margin,
                (player.row+1)*cellSize-2*margin,
                playerPaint);
    }

    private void drawExit(Canvas canvas, float margin){

        canvas.drawRect(
                exit.col*cellSize+margin,
                exit.row*cellSize+margin,
                (exit.col+1)*cellSize-margin,
                (exit.row+1)*cellSize-margin,
                exitPaint);
    }

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

        if (!isChronoStarted){
            ga.chrono.setBase(SystemClock.elapsedRealtime());
            ga.chrono.start();
            isChronoStarted = true;
        }

        checkExit();
        invalidate();
    }

    private void checkExit(){

            if (player == exit){

            setBestTime();
            ga.resetChrono();

            }
    }

    private void setBestTime(){


        chronoTime = (int)(SystemClock.elapsedRealtime() - ga.chrono.getBase())/1000;

        if (ga.timeValue.getVisibility() == View.INVISIBLE){
            ga.timeValue.setVisibility(View.VISIBLE);
            ga.timeLabel.setVisibility(View.VISIBLE);
            ga.timeValue.setText(String.valueOf(chronoTime));
        } else if (chronoTime < Integer.parseInt(ga.timeValue.getText().toString()) ){
            ga.timeValue.setText(String.valueOf(chronoTime));
        }


    }


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

                if (col >= 0 && col < COLS && row >= 0  && row < ROWS ) {

                    if (lamps[0] == null) {
                        lamps[0] = new Cell(col, row);
                        invalidate();
                    } else if (lamps[1] == null && ( lamps[0].col != col || lamps[0].row != row)) {

                        lamps[1] = new Cell((int) (x / cellSize), (int) (y / cellSize));
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
}
