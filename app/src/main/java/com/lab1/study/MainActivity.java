package com.lab1.study;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText usernameTxtField = findViewById(R.id.usernameTxtField);
        final EditText passwordTxtField = findViewById(R.id.passwordTxtField);
        final TextView invalidTxtView = findViewById(R.id.invalidTxtView);

        Button loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String username = usernameTxtField.getText().toString();
                        String password = passwordTxtField.getText().toString();
                        User user = new User(username, password);
                        user = user.logIn();

                        if (user != null) {
                            usernameTxtField.setText(user.getEmail());
                        } else {
                            invalidTxtView.setText(R.string.invalid_login);
                        }
                    }
                });
                thread.start();

            }
        });

    }
}
