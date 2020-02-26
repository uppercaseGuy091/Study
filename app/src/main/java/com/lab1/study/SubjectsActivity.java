package com.lab1.study;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class SubjectsActivity extends AppCompatActivity {
        TextView txtView = null;
        LinearLayout r = new LinearLayout(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);

        txtView = findViewById(R.id.txtView);


        String username = this.getIntent().getExtras().getString("username");// To be sent to next activity
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<String> subjects = DbConnection.getInstance().fetchSubjects();

                SubjectsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for(String sub: subjects)
                       txtView.setText(sub);
                        TextView textView = new TextView(SubjectsActivity.this);
                        r.addView(txtView);
                        textView.setH
                    }
                });
            }
        });
        thread.start();



        //ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, Arrays.asList(courses));
        //coursesListView.setAdapter(arrayAdapter);

    }
}
