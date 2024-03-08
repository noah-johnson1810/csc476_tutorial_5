package edu.sdsmt.team5.risk;

import java.io.Serializable;

public class GridTile implements Serializable {
    private TileStatus status;
    private float x;
    private float y;

    public GridTile() {
        status = TileStatus.EMPTY;
    }

    public void setX(float newX) {
        x = newX;
    }

    public float getX() {
        return x;
    }

    public void setY(float newY) {
        y = newY;
    }

    public float getY() {
        return y;
    }

    public TileStatus getStatus() {
        return status;
    }

    public void setStatus(TileStatus status) {
        this.status = status;
    }
}
