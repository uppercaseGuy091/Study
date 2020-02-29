package com.lab1.study;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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


        ImageView addIcon = (ImageView) findViewById(R.id.addIcon);
        addIcon.setOnClickListener(new View.OnClickListener() {
           @Override
             public void onClick(View v) {
                 addSubject();
          }
         });

    }




        public void addSubject(){
            Toast.makeText(getApplicationContext(),
                    "Button clicked",
                    Toast.LENGTH_SHORT).show();

            // otherwise, show a dialog to ask for their name
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Add Subject!");
            alert.setMessage("Enter the name of the Subject?");

            // Create EditText for entry
            final EditText input = new EditText(this);
            alert.setView(input);


            // Make an "OK" button to save the name
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int whichButton) {

                    // Grab the EditText's input
                    final String inputName = input.getText().toString();

                    // Put it into memory (don't forget to commit!)
                    //SharedPreferences.Editor e = mSharedPreferences.edit();
                    //e.putString(PREF_NAME, inputName);
                    //e.commit();

                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                DbConnection.getInstance().addSubjectToDB(inputName);
                                finish();
                            } catch (Exception e) {
                                e.printStackTrace();
                           /* final String exceptionMessage = e.getMessage();
                            SubjectsActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (exceptionMessage.contains("subject")) {
                                        usernameTxtView.setVisibility(View.VISIBLE);
                                        scrollView.scrollTo(0, 0);

                                    } else if (exceptionMessage.contains("email")) {
                                        emailTxtView.setVisibility(View.VISIBLE);
                                        scrollView.scrollTo(0, emailTxtView.getTop());
                                    }

                                }
                            });*/

                            }
                        }
                    });
                    thread.start();







                    // Welcome the new user
                    Toast.makeText(getApplicationContext(), "You added the subject, " + inputName + "!", Toast.LENGTH_LONG).show();
                }
            });

            // Make a "Cancel" button
            // that simply dismisses the alert
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int whichButton) {}
            });

            alert.show();
        }






    }

