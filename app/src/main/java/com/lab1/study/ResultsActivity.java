package com.lab1.study;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultsActivity extends AppCompatActivity {

    TextView resultsTv;
    TextView rightTv;
    TextView wrongTv;
    Button startOverBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Intent getIntent = getIntent();
        int deckId = getIntent.getIntExtra("deckId", 0);
        final String username = getIntent.getStringExtra("username");
        final String subject = getIntent.getStringExtra("subject");
        final int wrongAns = getIntent.getIntExtra("wrongAns", 0);
        final int rightAns = getIntent.getIntExtra("correctAns", 0);

        resultsTv = findViewById(R.id.results);
        rightTv = findViewById(R.id.right);
        wrongTv = findViewById(R.id.wrong);
        startOverBtn = findViewById(R.id.startAgain);

        resultsTv.setText("Here are the results for user " + username + "\nfor the subject of " + subject + "\ndeck " + deckId);
        rightTv.setText("You knew " + rightAns + " of the questions, good job!");
        wrongTv.setText("You need to work on " + wrongAns + " of the questions.");


        startOverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultsActivity.this, SubjectsActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ResultsActivity.this, CardsActivity.class);

        Intent getIntent = getIntent();
        int deckId = getIntent.getIntExtra("deckId", 0);
        final String username = getIntent.getStringExtra("username");
        final String subject = getIntent.getStringExtra("subject");

        intent.putExtra("username", username);
        intent.putExtra("deckId", deckId);
        intent.putExtra("subject", subject);
        startActivity(intent);
    }

}
