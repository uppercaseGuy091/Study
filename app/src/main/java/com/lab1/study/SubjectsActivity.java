package com.lab1.study;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class SubjectsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);

        String username = this.getIntent().getExtras().getString("username");// To be sent to next activity
        ArrayList<String> courses = DbConnection.getInstance().fetchSubjects();

        ListView coursesListView = findViewById(R.id.SubjectListXmlListView);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, Arrays.asList(courses));
        coursesListView.setAdapter(arrayAdapter);

        System.out.println(courses);
    }
}
