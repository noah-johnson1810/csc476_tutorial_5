package edu.sdsmt.team5.risk;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class PostGameActivity extends AppCompatActivity {

    private final static String WINNER_NAME_ID = "postgame_winner_name";
    private String winnerName = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_game);
        if(getIntent() != null){
            Intent intent = getIntent();
            winnerName = intent.getStringExtra(WINNER_NAME_ID);
            ((TextView)findViewById(R.id.winnerName)).setText(winnerName);
        }
        else{
            Log.e("PostGameActivity", "Error upon creating view, winner name not provided");
        }
        if(savedInstanceState != null){
            winnerName = savedInstanceState.getString(WINNER_NAME_ID);
        }

        Button playAgainButton = findViewById(R.id.playAgainButton);
        playAgainButton.setOnClickListener(view -> redirectToStart());

        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // It also doesn't make sense to be able
                // to back out of the end of the game,
                // so we do not allow it.
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString(WINNER_NAME_ID, winnerName);
    }

    private void redirectToStart(){
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
    }
}