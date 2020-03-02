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
        final int deckId = getIntent.getIntExtra("deckId", 0);
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


        /*
        //this crashes for some reason
        startOverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultsActivity.this, SubjectsActivity.class);
                startActivity(intent);
            }
        });*/
    }

    //this is supposed to restart the previous activity entirely
    //so it doesn't give indexOutOfBounds anc crash
    //but it doesn't work as it should
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ResultsActivity.this, CardsActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
