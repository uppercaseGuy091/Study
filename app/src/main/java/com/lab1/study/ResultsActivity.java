package com.lab1.study;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ResultsActivity extends AppCompatActivity {

    TextView resultsTv;
    TextView rightTv;
    TextView wrongTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Intent getIntent = getIntent();
        final int deckId = getIntent.getIntExtra("deckId", 0);
        final String username = getIntent.getStringExtra("username");
        final String subject = getIntent.getStringExtra("subject");
        final int wrongAns = getIntent.getIntExtra("wrongAns", 0);
        final int rightAns = getIntent.getIntExtra("correctAns", 0);





        //THIS CLASS IS NOT COMPLETE, I JUST UPLOADED IT FOR ANYONE TO SEE/WORK ON






        resultsTv = findViewById(R.id.results);
        rightTv = findViewById(R.id.right);
        wrongTv = findViewById(R.id.wrong);

        resultsTv.setText("Here are the results for user " + username + "for the subject of " + subject + "and deck " + deckId);
        rightTv.setText("You knew " + rightAns + " of the questions, good job!");
        wrongTv.setText("You need to work on " + wrongAns + " of the questions.");
    }
}
