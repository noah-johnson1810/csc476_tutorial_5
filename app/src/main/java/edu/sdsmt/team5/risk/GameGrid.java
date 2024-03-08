package edu.sdsmt.team5.risk;

import java.io.Serializable;

public class GameGrid implements Serializable {
    private static final int NUM_ROWS = 10;
    private static final int NUM_COLUMNS = 10;
    public final GridTile[][] grid = new GridTile[NUM_ROWS][NUM_COLUMNS];

    public GameGrid() {
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLUMNS; j++) {
                grid[i][j] = new GridTile();
            }
        }
    }
}
