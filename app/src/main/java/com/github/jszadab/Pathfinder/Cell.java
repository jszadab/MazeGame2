package com.github.jszadab.Pathfinder;


/**
 * Key class  on which the creation of mazes is based.
 */
public class Cell {

    /**
     * Fields which defined border of each cell
     */
     public boolean
            topWall = true,
            leftWall = true,
            bottomWall = true,
            rightWall = true;

    /**
     * Filed which is use to maze-generating algorithm.
     */
    public boolean visited = true;

    /**
     * Field which is use to locate each cell
     */
     public int col, row;

    /**
     * Class constructor which use "x/y coordinates" to put each cell to 2D array
     * @param col   x-coordinate in maze
     * @param row   y-coordinate in maze
     */
     public Cell(int col, int row){
        this.col = col;
        this.row = row;
     }
}
