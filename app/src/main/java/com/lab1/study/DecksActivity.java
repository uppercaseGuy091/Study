package com.lab1.study;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DecksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decks);

        Intent getIntent = getIntent();
        final String subjectName = getIntent.getStringExtra("subject");
        final String username = getIntent.getStringExtra("username");

        TextView title = (TextView) (findViewById(R.id.title));
        title.setText(subjectName + " Decks");


        final LinearLayout decksLayout = findViewById(R.id.ListLayout);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<Deck> decks = DbConnection.getInstance().getDecks(subjectName);

                DecksActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (final Deck deck : decks) {
                            final TextView txtView = new TextView(DecksActivity.this);
                            txtView.setTextSize(22);
                            txtView.setHeight(150);
                            txtView.setText(deck.getName());

                            txtView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(DecksActivity.this, CardsActivity.class);
                                    intent.putExtra("username", username);
                                    intent.putExtra("deckId", deck.getId());
                                    intent.putExtra("subject", subjectName);
                                    intent.putExtra("deckName", deck.getName());
                                    startActivity(intent);
                                }
                            });
                            decksLayout.addView(txtView);
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
                addDeck(subjectName);
            }
        });

    }


    public void addDeck(final String subjectName){
        //show a dialog to ask for subject name
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Add Deck");
        alert.setMessage("Enter the name of the Deck");

        // Create EditText for entry
        final EditText input = new EditText(this);
        alert.setView(input);

        // Make an "OK" button to save the name
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {

                final String inputName = input.getText().toString();

                //SharedPreferences.Editor e = mSharedPreferences.edit();
                //e.putString(PREF_NAME, inputName);
                //e.commit();

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            DbConnection.getInstance().addDeckToDB(inputName, subjectName);
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

                Toast.makeText(getApplicationContext(), "You added the deck, " + inputName, Toast.LENGTH_LONG).show();
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
