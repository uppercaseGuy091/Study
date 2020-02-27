package com.lab1.study;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class SubjectsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);




        final String username = this.getIntent().getExtras().getString("username");// To be sent to next activity

        final LinearLayout coursesLayout = findViewById(R.id.ListLayout);


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<String> courses = DbConnection.getInstance().fetchSubjects();

                SubjectsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (String sub : courses) {
                            final TextView txtView = new TextView(SubjectsActivity.this);
                            txtView.setTextSize(22);
                            txtView.setHeight(150);
                            txtView.setText(sub);

                            txtView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(SubjectsActivity.this, DecksActivity.class);
                                    intent.putExtra("username", username);
                                    intent.putExtra("subject", txtView.getText());
                                    startActivity(intent);
                                }
                            });
                            coursesLayout.addView(txtView);
                        }
                    }
                });
            }
        });

        thread.start();



    }
}
