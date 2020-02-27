package com.lab1.study;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class DecksActivity extends AppCompatActivity {

    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decks);

        Intent intent = getIntent();
        final String subjectName = intent.getStringExtra("username");

        TextView title = (TextView)(findViewById(R.id.title));
        title.setText( subjectName + " Decks");



        final LinearLayout decksLayout = findViewById(R.id.ListLayout);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<String> decks = DbConnection.getInstance().getDecks(subjectName);

                DecksActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (String deck : decks) {
                            final TextView txtView = new TextView(DecksActivity.this);
                            txtView.setTextSize(22);
                            txtView.setHeight(150);
                            txtView.setText(deck);

                            txtView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                            decksLayout.addView(txtView);
                        }
                    }
                });
            }
        });

        thread.start();
















    }
}
