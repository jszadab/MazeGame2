package com.example.jacek.mazegame;

import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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

    public void serializeMazes(){

        String filePath = getAppContext().getFilesDir().getPath() + "/fileName.txt";
        File f = new File(filePath);

        Gson gson = new Gson();

        try {
            FileInputStream fileIn = new FileInputStream(f);
            ObjectInputStream in = new ObjectInputStream(fileIn);

            mazes = gson.fromJson((String)in.readObject(), Maze[].class);

            in.close();
            fileIn.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void deserializeMazes(){

        String filePath = getAppContext().getFilesDir().getPath() + "/fileName.txt";
        File f = new File(filePath);

        Gson gson = new Gson();
        String json = gson.toJson(mazes);

        try {
            FileOutputStream fileOut = new FileOutputStream(f);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(json);
            out.close();
            fileOut.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
