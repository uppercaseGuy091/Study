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
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);

        quesOrAnsTv = findViewById(R.id.questOrAns);
        changeTextViewBtn = findViewById(R.id.cardsBtn);

        Intent getIntent = getIntent();
        final int deckId = getIntent.getIntExtra("deckId", 0);
        final String username = getIntent.getStringExtra("username");

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
                    }
                });
            }
        });

        thread.start();

    }
}
