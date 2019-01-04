package com.example.jacek.mazegame;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

    private static Context context;

    private static Maze[] mazes;

    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
        MyApplication.mazes = new Maze[10];
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }

    public void setMaze(Maze maze){
        mazes[0] = maze;
    }



}
