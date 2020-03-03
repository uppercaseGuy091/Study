package com.lab1.study;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class CardsActivity extends AppCompatActivity {

    TextView quesOrAnsTv;
    Button changeTextViewBtn;
    Button skipQuestionBtn;
    Button yesBtn;
    Button noBtn;
    int count = 0;
    int rightAns = 0;
    int wrongAns = 0;
    int skippedCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);

        quesOrAnsTv = findViewById(R.id.questOrAns);
        changeTextViewBtn = findViewById(R.id.cardsBtn);
        skipQuestionBtn = findViewById(R.id.skipQues);
        yesBtn = findViewById(R.id.yes);
        noBtn = findViewById(R.id.no);

        Intent getIntent = getIntent();
        final int deckId = getIntent.getIntExtra("deckId", 0);
        final String username = getIntent.getStringExtra("username");
        final String subject = getIntent.getStringExtra("subject");
        final String deckName = getIntent.getStringExtra("deckName");

        changeTextViewBtn.setText("Show answer");

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<Card> cards = DbConnection.getInstance().getCards(deckId);

                CardsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        quesOrAnsTv.setText(cards.get(count).getQuestion());

                        changeTextViewBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (quesOrAnsTv.getText().equals(cards.get(count).getQuestion()))
                                    quesOrAnsTv.setText(cards.get(count).getAnswer());
                                else
                                    quesOrAnsTv.setText(cards.get(count).getQuestion());

                                if (changeTextViewBtn.getText().equals("Show answer"))
                                    changeTextViewBtn.setText("Hide answer");
                                else
                                    changeTextViewBtn.setText("Show answer");
                            }
                        });

                        skipQuestionBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                skippedCount++;

                                count++;
                                //sets the question to the textview
                                if (count != cards.size()) {
                                    quesOrAnsTv.setText(cards.get(count).getQuestion());
                                    changeTextViewBtn.setText("Show answer");
                                }


                                //opens results activity after all the questions have passed
                                if (count == cards.size()) {
                                    Intent passValues = new Intent(CardsActivity.this, ResultsActivity.class);
                                    passValues.putExtra("username", username);
                                    passValues.putExtra("subject", subject);
                                    passValues.putExtra("deckId", deckId);
                                    passValues.putExtra("correctAns", rightAns);
                                    passValues.putExtra("wrongAns", wrongAns);
                                    passValues.putExtra("deckName", deckName);
                                    passValues.putExtra("skippedCount", skippedCount);
                                    startActivity(passValues);
                                }
                            }
                        });

                        yesBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                count++;
                                rightAns++;
                                if (count != cards.size()) {
                                    quesOrAnsTv.setText(cards.get(count).getQuestion());
                                }
                                if (count == cards.size()) {
                                    Intent passValues = new Intent(CardsActivity.this, ResultsActivity.class);
                                    passValues.putExtra("username", username);
                                    passValues.putExtra("subject", subject);
                                    passValues.putExtra("deckId", deckId);
                                    passValues.putExtra("correctAns", rightAns);
                                    passValues.putExtra("wrongAns", wrongAns);
                                    passValues.putExtra("deckName", deckName);
                                    passValues.putExtra("skippedCount", skippedCount);
                                    startActivity(passValues);
                                }
                            }
                        });

                        noBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                count++;
                                wrongAns++;
                                if (count != cards.size()) {
                                    quesOrAnsTv.setText(cards.get(count).getQuestion());
                                }

                                if (count == cards.size()) {
                                    Intent passValues = new Intent(CardsActivity.this, ResultsActivity.class);
                                    passValues.putExtra("username", username);
                                    passValues.putExtra("subject", subject);
                                    passValues.putExtra("deckId", deckId);
                                    passValues.putExtra("correctAns", rightAns);
                                    passValues.putExtra("wrongAns", wrongAns);
                                    passValues.putExtra("deckName", deckName);
                                    passValues.putExtra("skippedCount", skippedCount);
                                    startActivity(passValues);
                                    finish();
                                }
                            }
                        });

                    }

                });


            }

        });

        thread.start();

    }

}

