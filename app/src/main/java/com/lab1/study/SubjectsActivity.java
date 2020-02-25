package com.lab1.study;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SubjectsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);

        String username = this.getIntent().getExtras().getString("username");// To be sent to next activity
        ArrayList<String> courses;
    }
}
