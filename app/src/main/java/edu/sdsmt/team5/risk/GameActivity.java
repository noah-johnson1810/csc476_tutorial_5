package edu.sdsmt.team5.risk;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {
    private GameSim gameSim;
    private TextView playerOneNameTextView;
    private TextView playerTwoNameTextView;
    private TextView currentPlayerTextView;
    private TextView playerOneScoreTextView;
    private TextView playerTwoScoreTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            gameSim = new GameSim(intent.getSerializableExtra("Sim", GameSim.GameSimState.class));
        } else {
            gameSim = new GameSim(intent.getSerializableExtra("Sim"));
        }

        GameArea gameArea = findViewById(R.id.gameArea);
        gameArea.setGameActivity(this);

        playerOneNameTextView = findViewById(R.id.player1NameGameView);
        playerTwoNameTextView = findViewById(R.id.player2NameGameView);
        currentPlayerTextView = findViewById(R.id.currentPlayerText);
        playerOneScoreTextView = findViewById(R.id.player1ScoreText);
        playerTwoScoreTextView = findViewById(R.id.player2ScoreText);

        updateUI();
    }

    public void updateUI() {
        playerOneNameTextView.setText(gameSim.getPlayerOneName());
        playerTwoNameTextView.setText(gameSim.getPlayerTwoName());
        currentPlayerTextView.setText(String.format("%s's turn", gameSim.getCurrentTurnPlayerNumber() == 1 ? gameSim.getPlayerOneName() : gameSim.getPlayerTwoName()));
        playerOneScoreTextView.setText(String.valueOf(gameSim.getPlayerOneScore()));
        playerTwoScoreTextView.setText(String.valueOf(gameSim.getPlayerTwoScore()));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putSerializable("Sim", gameSim.getState());
        //gameArea.saveToBundle(bundle);
    }

    public GameSim getGameSim() {
        return this.gameSim;
    }
}
