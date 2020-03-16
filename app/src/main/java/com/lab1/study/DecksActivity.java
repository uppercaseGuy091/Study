package com.lab1.study;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DecksActivity extends AppCompatActivity {

    ArrayList<String> questionArray = new ArrayList<>();
    ArrayList<String> answerArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decks);

        start();
    }


    private void start() {

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

                            final LinearLayout linearLayout = new LinearLayout(DecksActivity.this);
                            linearLayout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

                            final TextView txtView = new TextView(DecksActivity.this);
                            TextView addSign = new TextView(DecksActivity.this);

                            LinearLayout.LayoutParams lnp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

                            linearLayout.setLayoutParams(lnp);

                            txtView.setTextSize(22);
                            txtView.setHeight(150);
                            txtView.setText(deck.getName());
                            txtView.setMinWidth(10000);

                            addSign.setText("+");
                            addSign.setTextSize(22);
                            addSign.setHeight(150);
                            addSign.setMinWidth(200);
                            addSign.setGravity(Gravity.CENTER);

                            linearLayout.addView(addSign);
                            linearLayout.addView(txtView);


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


                            addSign.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    addCard(deck.getId());
                                }
                            });
                            decksLayout.addView(linearLayout);
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


    public void addCard(final int deckId) {

        //show a dialog to ask for subject name
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        //Add a TextView here for the "Title" label, as noted in the comments
        alert.setTitle("Add Card");
        final EditText question = new EditText(this);
        question.setHint("Question");
        layout.addView(question); // Notice this is an add method

        //Add another TextView here for the "Description" label
        final EditText answer = new EditText(this);
        answer.setHint("Answer");
        layout.addView(answer); // Another add method

        alert.setMessage("Enter answer and question");

        alert.setPositiveButton("Add Card", null);
        alert.setNegativeButton("Cancel", null);
        alert.setNeutralButton("Done", null);

        alert.setView(layout); // Again this is a set method, not add

        final AlertDialog dialog = alert.create();
        dialog.show();

        //Make an "OK" button to save the name
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((answer.getText().toString().isEmpty()) || (question.getText().toString().isEmpty())) {
                    dialog.setMessage("No question and answer entered");
                    dialog.setMessage(Html.fromHtml("<font color='#FF0000'>No question and answer entered</font>"));
                } else {
                    questionArray.add(question.getText().toString());
                    answerArray.add(answer.getText().toString());
                    question.setText("");
                    answer.setText("");
                }

            }

        });

        //Make a "Cancel" button that simply dismisses the alert
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();

            }


        });


        dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((answer.getText().toString().isEmpty()) || (question.getText().toString().isEmpty())) {

                } else {
                    //questionArray.add(question.getText().toString());
                    //answerArray.add(answer.getText().toString());
                }
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            for (int i = 0; i < questionArray.size(); i++) {
                                DbConnection.getInstance().addCardToDB(questionArray.get(i), answerArray.get(i), deckId);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();


                        }
                    }
                });
                thread.start();

                dialog.cancel();

                Toast.makeText(getApplicationContext(), "Your questions are added", Toast.LENGTH_LONG).show();

            }


        });


    }


    public void addDeck(final String subjectName) {
        final LinearLayout decksLayout = findViewById(R.id.ListLayout);
        //show a dialog to ask for subject name
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Add Deck");
        alert.setMessage("Enter the name of the Deck");

        //Create EditText for entry
        final EditText input = new EditText(this);
        alert.setView(input);

        //Make an "OK" button to save the name
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
                                DbConnection.getInstance().addDeckToDB(inputName, subjectName);
                                decksLayout.removeViewsInLayout(1, decksLayout.getChildCount() - 1);
                                start();
                            } catch (Exception e) {
                                e.printStackTrace();


                            }
                        }
                    });
                    thread.start();

                    Toast.makeText(getApplicationContext(), "You added the deck, " + inputName, Toast.LENGTH_LONG).show();
                }
            }
        });

        //Make a "Cancel" button that simply dismisses the alert
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert.show();
    }
}