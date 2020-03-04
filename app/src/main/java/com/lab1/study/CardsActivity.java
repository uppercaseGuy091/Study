package com.lab1.study;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CardsActivity extends AppCompatActivity {

    TextView questionTv;
    TextView answerTv;
    private CardView answerLayout1;
    private CardView questionLayout1;
    Button animateBtn;
    Button skipQuestionBtn;
    Button yesBtn;
    Button noBtn;
    int count = 0;
    int rightAns = 0;
    int wrongAns = 0;
    int skippedCount = 0;
    private int height; //To get height of the card so it can flip over it
    int open = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);

        answerLayout1 = findViewById(R.id.AnswerCard);
        questionLayout1 = findViewById(R.id.QuestionCard);
        questionTv = findViewById(R.id.questionTextViw);
        answerTv = findViewById(R.id.answerTextView);
        animateBtn = findViewById(R.id.cardsBtn);
        skipQuestionBtn = findViewById(R.id.skipQues);
        yesBtn = findViewById(R.id.yes);
        noBtn = findViewById(R.id.no);

        Intent getIntent = getIntent();
        final int deckId = getIntent.getIntExtra("deckId", 0);
        final String username = getIntent.getStringExtra("username");
        final String subject = getIntent.getStringExtra("subject");
        final String deckName = getIntent.getStringExtra("deckName");

        animateBtn.setText("Show answer");

        questionLayout1.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                height = ((questionLayout1.getHeight()) / 2) + 10; //divide it by two so it wont go too much on both sides (up and down)

            }
        });

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<Card> cards = DbConnection.getInstance().getCards(deckId);

                CardsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        questionTv.setText(cards.get(count).getQuestion());

                        answerTv.setText(cards.get(count).getAnswer());

                        animateBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                //Toast.makeText(CardsActivity.this, "Flip!", Toast.LENGTH_SHORT).show();

                                openClose(); //Type one cards transition
                                //openClose2();  //Type two cards transition

                                if (animateBtn.getText().equals("Show answer"))
                                    animateBtn.setText("Hide answer");
                                else
                                    animateBtn.setText("Show answer");
                            }
                        });

                        skipQuestionBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ifOpen();

                                skippedCount++;
                                count++;

                                if (count != cards.size()) {
                                    questionTv.setText(cards.get(count).getQuestion());
                                    answerTv.setText(cards.get(count).getAnswer());
                                    animateBtn.setText("Show answer");
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

                                ifOpen();

                                if (count != cards.size()) {
                                    questionTv.setText(cards.get(count).getQuestion());
                                    answerTv.setText(cards.get(count).getAnswer());
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

                                ifOpen();

                                if (count != cards.size()) {
                                    questionTv.setText(cards.get(count).getQuestion());
                                    answerTv.setText(cards.get(count).getAnswer());
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


    public void openClose() {

        if (open == 1) {
            //Toast.makeText(CardsActivity.this, "OPEN!", Toast.LENGTH_SHORT).show();
            questionLayout1.animate().translationY(height).start();
            answerLayout1.animate().translationY(-1 * height).start();
            open = 0;
        } else {
            //Toast.makeText(CardsActivity.this, "CLOSE!", Toast.LENGTH_SHORT).show();
            questionLayout1.animate().translationY(0).start();
            answerLayout1.animate().translationY(0).start();
            open = 1;
        }
    }

    public void ifOpen() {
        if (open == 0) { //if open.. close
            questionLayout1.animate().translationY(0).start();
            answerLayout1.animate().translationY(0).start();
            open = 1;
        }
    }


    //For the developers ;)
    public void openClose2() {
        //flipping cards (one visible at a time)
        questionLayout1.animate().translationY(height).start();
        answerLayout1.animate().translationY(-1 * height).withEndAction(new Runnable() {  //withEndAction gets called once the animation is completed
            @Override
            public void run() {

                if (open == 1) {
                    //Toast.makeText(MainActivity.this, "answer", Toast.LENGTH_SHORT).show();
                    questionLayout1.animate().translationY(0).start();
                    answerLayout1.animate().translationY(0);
                    questionLayout1.setVisibility(View.INVISIBLE);
                    open = 0;
                } else if (open == 0) {
                    questionLayout1.animate().translationY(0).start();
                    answerLayout1.animate().translationY(0);
                    questionLayout1.setVisibility(View.VISIBLE);
                    open = 1;
                }
            }
        }).start();

    }


//End
}
