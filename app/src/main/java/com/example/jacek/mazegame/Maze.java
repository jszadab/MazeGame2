package com.example.jacek.mazegame;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Maze {

    Cell[][] cells;
    String date;

    public Maze(Cell[][] cells) {

        this.cells = cells;

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
        date = df.format(c);
    }
}
