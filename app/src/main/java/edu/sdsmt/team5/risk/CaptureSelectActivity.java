package edu.sdsmt.team5.risk;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

public class CaptureSelectActivity extends AppCompatActivity {
    private GameSim gameSim;
    private final static String P1_NAME = "P1Name";
    private final static String P2_NAME = "P2Name";
    private final static String NUM_ROUNDS = "NumRounds";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gameSim = new GameSim();

        // get saved state data if it exists
        Intent intent = getIntent();

        if (intent != null) {
            String playerOneName = intent.getStringExtra(P1_NAME);
            String playerTwoName = intent.getStringExtra(P2_NAME);
            String numRounds = intent.getStringExtra(NUM_ROUNDS);

            if (playerOneName == null || playerTwoName == null || numRounds == null) {
                // we came from GameActivity
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    gameSim.setState(intent.getSerializableExtra("Sim", GameSim.GameSimState.class));
                } else {
                    gameSim.setState((GameSim.GameSimState) intent.getSerializableExtra("Sim"));
                }
            } else {
                // we came from WelcomeActivity
                gameSim.setPlayerOneName(playerOneName);
                gameSim.setPlayerTwoName(playerTwoName);
                gameSim.setTargetRoundCount(Integer.parseInt(numRounds));
            }
        }

        setContentView(R.layout.activity_capture_select);

        final Button lineButton = findViewById(R.id.lineCaptureButton);
        final Button rectangleButton = findViewById(R.id.rectangleCaptureButton);
        final Button dotButton = findViewById(R.id.dotCaptureButton);

        final TextView playerText = findViewById(R.id.currentPlayerText);

        rectangleButton.setOnClickListener(view -> selectCaptureType(CaptureType.RECTANGLE));
        lineButton.setOnClickListener(view -> selectCaptureType(CaptureType.LINE));
        dotButton.setOnClickListener(view -> selectCaptureType(CaptureType.DOT));

        if(gameSim.getCurrentTurnPlayerNumber() == 1){
            playerText.setText(String.format("%s %s", playerText.getText(), gameSim.getPlayerOneName()));
        }
        else{
            playerText.setText(String.format("%s %s", playerText.getText(), gameSim.getPlayerTwoName()));
        }

        // GRADING: BACK
        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Do not let the player go back from the
                // capture selector activity, because
                // that would go back into the previous
                // player's turn.
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putSerializable("Sim", gameSim.getState());
    }

    private void selectCaptureType(CaptureType captureType) {
        Intent intent = new Intent(this, GameActivity.class);
        gameSim.setCaptureType(captureType);
        //Increment the round counter so we properly leave to the final screen
        if(gameSim.getCurrentTurnPlayerNumber() == 2) {
            gameSim.setNumRounds(gameSim.getNumRounds() + 1);
        }
        intent.putExtra("Sim", gameSim.getState());
        startActivity(intent);
    }
}
