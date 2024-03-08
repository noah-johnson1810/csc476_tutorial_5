package edu.sdsmt.team5.risk;

/*
Project 1 Grading

        Group:
        __X__ 6pt No redundant activities
        __X__ 6pt How to play dialog
        __X__ 6pt Icons
        __X__ 6pt End activity
        __X__ 6pt Back button handled
        How to open the "how to play dialog": Click the "How To Play" button on the title screen

        Individual:

        Play activity and custom view

        __X__ 15pt Activity appearence\layout
        __X__ 12pt Static Custom View
        __X__ 23pt Dynamic part of the Custom View
        __X__ 10pt Rotation

        Welcome activity and Game Class

        __X__ 8pt Welcome activity appearence\layout
        __X__ 25pt Applying capture rules
        __X__ 12pt Game state
        __X__ 15pt Rotation
        What is the probaility of the reactangle capture: From 50% if the rectangle is < 50% of the game area. Scales down to 10% as it gets larger than that.

        Capture activity and activity sequencing

        __X__ 8pt Capture activity apearence\layout
        __X__ 20pt Player round sequencing
        __X__ 27pt Move to next activity
        __X__ 5pt Rotation

        Timer

        __X__ 16pt Timer activity\appeaerence\layout
        __X__ 20pt Graphic
        __X__ 14pt Player turn end
        __X__ 10pt Rotation


        Please list any additional rules that may be needed to properly grade your project: */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class WelcomeActivity extends AppCompatActivity {

    /**
     * Some constant key names for saving data on rotation.
     */
    private final static String P1_NAME = "P1Name";
    private final static String P2_NAME = "P2Name";
    private final static String NUM_ROUNDS = "NumRounds";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome);

        // If we rotated the device, we'll have something in
        // the bundle.
        if (savedInstanceState != null) {
            // Restore the text and spinner selection.
            EditText P1 = findViewById(R.id.player1Name);
            EditText P2 = findViewById(R.id.player2Name);
            Spinner rounds = findViewById(R.id.roundSelector);

            String P1Name = savedInstanceState.getString(P1_NAME);
            String P2Name = savedInstanceState.getString(P2_NAME);
            String NumRounds = savedInstanceState.getString(NUM_ROUNDS);

            // The players may not have entered text yet.
            // Do not set the Text if the string is null.
            if (P1Name != null) {
                P1.setText(P1Name);
            }

            if (P2Name != null) {
                P2.setText(P1Name);
            }

            rounds.setSelection(((ArrayAdapter) rounds.getAdapter()).getPosition(NumRounds));
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save player entered names and current rounds selection
        // on rotate.
        EditText P1 = findViewById(R.id.player1Name);
        EditText P2 = findViewById(R.id.player2Name);
        Spinner rounds = findViewById(R.id.roundSelector);

        savedInstanceState.putString(P1_NAME, P1.getText().toString());
        savedInstanceState.putString(P2_NAME, P2.getText().toString());
        savedInstanceState.putString(NUM_ROUNDS, rounds.getSelectedItem().toString());
    }

    public void onStartCaptureSelectActivity(View view) {
        Intent intent = new Intent(this, CaptureSelectActivity.class);
        // Prepare the extras.
        EditText P1 = findViewById(R.id.player1Name);
        EditText P2 = findViewById(R.id.player2Name);
        Spinner rounds = findViewById(R.id.roundSelector);
        intent.putExtra(P1_NAME, P1.getText().toString());
        intent.putExtra(P2_NAME, P2.getText().toString());
        intent.putExtra(NUM_ROUNDS, rounds.getSelectedItem().toString());
        startActivity(intent);
    }

    public void onGameHelp(View view) {
        // Create the object of AlertDialog Builder class
        AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeActivity.this);
        // Set the Message
        builder.setMessage(R.string.gameRules);
        // Set Title
        builder.setTitle("How to Play Prospect");
        // Set the neutral button as a way to close the alert.
        builder.setNeutralButton("Got it!", (not, used) -> {
        });
        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();
        // Show the Alert Dialog box
        alertDialog.show();
    }
}