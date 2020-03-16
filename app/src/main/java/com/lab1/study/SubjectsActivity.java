package com.lab1.study;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SubjectsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);

        start();
    }

    private void start() {

        final String username = this.getIntent().getExtras().getString("username"); // To be sent to next activity
        final String deckName = this.getIntent().getExtras().getString("deckName");

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
                                    intent.putExtra("deckName", deckName);
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


        ImageView addIcon = (ImageView) findViewById(R.id.addIcon);
        addIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSubject();
            }
        });

    }


    public void addSubject() {

        final LinearLayout subjectLayout = findViewById(R.id.ListLayout);
        //show a dialog to ask for subject name
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Add Subject");
        alert.setMessage("Enter the name of the Subject");

        // Create EditText for entry
        final EditText input = new EditText(this);
        alert.setView(input);

        // Make an "OK" button to save the name
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {

                final String inputName = input.getText().toString();

                if ((input.getText().toString().isEmpty())) {
                    Toast.makeText(getApplicationContext(), "No name was entered" + inputName, Toast.LENGTH_LONG).show();


                }else {

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            DbConnection.getInstance().addSubjectToDB(inputName);
                            subjectLayout.removeViewsInLayout(1, subjectLayout.getChildCount() - 1);
                            start();

                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }
                });
                thread.start();

                Toast.makeText(getApplicationContext(), "You added the subject, " + inputName, Toast.LENGTH_LONG).show();
            }
            }
        });


        // Make a "Cancel" button that simply dismisses the alert
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert.show();
    }

}
