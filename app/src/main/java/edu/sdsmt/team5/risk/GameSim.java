package edu.sdsmt.team5.risk;

import java.io.Serializable;

public class GameSim {

    public static class GameSimState implements Serializable {
        private float captureItemX = 0;
        private float captureItemY = 0;
        private CaptureType captureType;
        private int playerOneScore = 0;
        private int playerTwoScore = 0;
        private int currentTurnPlayerNumber = 1;
        private String playerOneName = "";
        private String playerTwoName = "";
        private int targetRoundNumber = Integer.MAX_VALUE;
        private int numRounds = 1;
        private GameGrid gameGrid;
        private boolean touchesDisabled;
    }

    private GameSimState state;

    public GameSim() {
        state = new GameSimState();
        state.gameGrid = new GameGrid();
    }

    /**
     * Used for reconstructing the GameSim after screen rotation
     *
     * @param state The stored state, likely retrieved from a bundle
     * @author Mican Vollan
     */
    public GameSim(Serializable state) {
        this.state = (GameSimState) state;
    }

    /**
     * A simple getter for the gameGrid.
     *
     * @return gameGrid
     */
    public GameGrid getGameGrid() {
        return state.gameGrid;
    }

    public CaptureType getCaptureType() {
        return state.captureType;
    }

    public float getCaptureItemX() {
        return state.captureItemX;
    }

    public float getCaptureItemY() {
        return state.captureItemY;
    }

    public void setCaptureItemX(float newX) {
        state.captureItemX = newX;
    }

    public void setCaptureItemY(float newY) {
        state.captureItemY = newY;
    }

    public int getCurrentTurnPlayerNumber() {
        return state.currentTurnPlayerNumber;
    }

    public void setCurrentTurnPlayerNumber(int playerNumber) {
        state.currentTurnPlayerNumber = playerNumber;
    }

    public int getPlayerOneScore() {
        return state.playerOneScore;
    }

    public void setPlayerOneScore(int newScore) {
        state.playerOneScore = newScore;
    }

    public int getPlayerTwoScore() {
        return state.playerTwoScore;
    }

    public void setPlayerTwoScore(int newScore) {
        state.playerTwoScore = newScore;
    }

    public String getPlayerOneName() {
        return state.playerOneName;
    }

    public void setPlayerOneName(String name) {
        state.playerOneName = name;
    }

    public String getPlayerTwoName() {
        return state.playerTwoName;
    }

    public void setPlayerTwoName(String name) {
        state.playerTwoName = name;
    }

    public void setCaptureType(CaptureType captureType) {
        state.captureType = captureType;
    }

    public void setNumRounds(int numRounds) {
        state.numRounds = numRounds;
    }

    public int getNumRounds() {
        return state.numRounds;
    }

    public void setTouchesDisabled(boolean touchesDisabled) {
        state.touchesDisabled = touchesDisabled;
    }

    public boolean getTouchesDisabled() {
        return state.touchesDisabled;
    }

    public Serializable getState() {
        return state;
    }

    public void setState(GameSimState state) {
        this.state = state;
    }

    public void setTargetRoundCount(int numRounds) {
        state.targetRoundNumber = numRounds;
    }

    //We expect to be given the winner's name if they have won and null if not.
    public String getWinningPlayer() {
        if (state.targetRoundNumber < state.numRounds) {
            if (state.playerOneScore > state.playerTwoScore) {
                return state.playerOneName;
            } else {
                return state.playerTwoName;
            }
        }
        return null;
    }
}
