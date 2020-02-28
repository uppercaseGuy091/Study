package com.lab1.study;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class CardsActivity extends AppCompatActivity {

    TextView quesOrAnsTv;
    Button showOrHideAnsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);

        quesOrAnsTv = findViewById(R.id.questOrAns);
        showOrHideAnsBtn = findViewById(R.id.cardsBtn);

        Intent getIntent = getIntent();
        final int deckId = getIntent.getIntExtra("deckId", 0);
        final String username = getIntent.getStringExtra("username");

        //final LinearLayout decksLayout = findViewById(R.id.ListLayout);


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<Card> cards = DbConnection.getInstance().getCards(deckId);

                CardsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                    }
                });
            }
        });

        thread.start();

    }
}
