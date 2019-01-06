package com.github.jszadab.Pathfinder;

public class Cell {

    boolean
            topWall = true,
            leftWall = true,
            bottomWall = true,
            rightWall = true,
            visited = true;

    int col, row;

    public Cell(int col, int row){
        this.col = col;
        this.row = row;
    }

}
