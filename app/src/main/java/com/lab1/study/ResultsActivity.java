package com.lab1.study;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

public class ResultsActivity extends AppCompatActivity {

    TextView resultsTv;
    TextView rightTv;
    TextView wrongTv;
    TextView skipped;
    Button startOverBtn;
    Button fbShare;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Intent getIntent = getIntent();
        final String username = getIntent.getStringExtra("username");
        final String subject = getIntent.getStringExtra("subject");
        final int wrongAns = getIntent.getIntExtra("wrongAns", 0);
        final int rightAns = getIntent.getIntExtra("correctAns", 0);
        final String deckName = getIntent.getStringExtra("deckName");
        final int skippedCount = getIntent.getIntExtra("skippedCount",0);


        resultsTv = findViewById(R.id.results);
        rightTv = findViewById(R.id.right);
        wrongTv = findViewById(R.id.wrong);
        startOverBtn = findViewById(R.id.startAgain);
        fbShare = findViewById(R.id.fb);
        skipped = findViewById(R.id.skipped);

        resultsTv.setText("Here are the results for user " + username + "\nfor the subject of " + subject + "\nand deck " + deckName);
        rightTv.setText("You knew " + rightAns + " of the questions, good job!");
        wrongTv.setText("You need to work on " + wrongAns + " of the questions.");
        skipped.setText("You skipped " + skippedCount + " of the questions.");


        startOverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultsActivity.this, SubjectsActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        fbShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fbShare.getBackground().setAlpha(128);
                startOverBtn.getBackground().setAlpha(128);
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        SharePhoto photo = new SharePhoto.Builder()
                                .setBitmap(takeScreenshot(findViewById(R.id.root)))
                                .build();
                        SharePhotoContent content = new SharePhotoContent.Builder()
                                .addPhoto(photo)
                                .build();
                        final ShareDialog shareDialog = new ShareDialog(ResultsActivity.this);
                        shareDialog.show(content);

                        ResultsActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                fbShare.getBackground().setAlpha(255);
                                startOverBtn.getBackground().setAlpha(255);

                            }
                        });
                    }
                });
                thread.start();
            }

        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ResultsActivity.this, CardsActivity.class);

        Intent getIntent = getIntent();
        int deckId = getIntent.getIntExtra("deckId", 0);
        final String username = getIntent.getStringExtra("username");
        final String subject = getIntent.getStringExtra("subject");

        intent.putExtra("username", username);
        intent.putExtra("deckId", deckId);
        intent.putExtra("subject", subject);
        startActivity(intent);
    }

    private Bitmap takeScreenshot(View v) {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(),
                v.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            canvas.drawColor(ResultsActivity.this.getResources().getColor(R.color.lightGray , getTheme()));
        }else {
            canvas.drawColor(ResultsActivity.this.getResources().getColor(R.color.lightGray ));
        }
        v.draw(canvas);

        return bitmap;

    }

}
