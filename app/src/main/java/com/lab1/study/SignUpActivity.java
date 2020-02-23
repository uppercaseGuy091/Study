package com.lab1.study;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {
    private ScrollView scrollView = null;
    private TextView usernameTxtView = null;
    private EditText usernameTxtField = null;
    private TextView passwordTxtView = null;
    private EditText passwordTxtField = null;
    private TextView confirmPasswordTxtView = null;
    private EditText confirmPasswordTxtField = null;
    private TextView emailTxtView = null;
    private EditText emailTxtField = null;
    private Button signUpBtn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        findViews();

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideTxtView();
                usernameTxtView.setText(SignUpActivity.this.getText(R.string.invalid_username));

                if (usernameTxtField.getText().toString().isEmpty()){
                    usernameTxtView.setText(SignUpActivity.this.getText(R.string.username));
                    usernameTxtView.setVisibility(View.VISIBLE);
                    scrollView.scrollTo(0,0);
                } else if (passwordTxtField.getText().toString().isEmpty()) {
                    passwordTxtView.setVisibility(View.VISIBLE);
                    scrollView.scrollTo(0, passwordTxtView.getTop());
                } else if (!passwordTxtField.getText().toString().equals(confirmPasswordTxtField.getText().toString())) {
                    confirmPasswordTxtView.setVisibility(View.VISIBLE);
                    scrollView.scrollTo(0, confirmPasswordTxtView.getTop());
                } else {
                    signUp();
                }
            }
        });
    }

    private void findViews() {
        scrollView = findViewById(R.id.scrollView);
        usernameTxtView = findViewById(R.id.usernameTxtView);
        usernameTxtField = findViewById(R.id.usernameTxtField);
        passwordTxtView = findViewById(R.id.passwordTxtView);
        passwordTxtField = findViewById(R.id.passwordTxtField);
        confirmPasswordTxtView = findViewById(R.id.confirmPasswordTxtView);
        confirmPasswordTxtField = findViewById(R.id.confirmPasswordTxtField);
        emailTxtView = findViewById(R.id.emailTxtView);
        emailTxtField = findViewById(R.id.emailTxtField);
        signUpBtn = findViewById(R.id.signUpBtn);

    }

    private void signUp() {
        signUpBtn.setEnabled(false);
        final String username = usernameTxtField.getText().toString();
        final String password = passwordTxtField.getText().toString();
        final String email = emailTxtField.getText().toString();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DbConnection.getInstance().addUser(username, password, email);
                    Intent intent = SignUpActivity.this.getIntent();
                    intent.putExtra("username", username);
                    intent.putExtra("password", password);
                    setResult(RESULT_OK, intent);
                    finish();
                } catch (Exception e) {
                    final String exceptionMessage = e.getMessage();
                    SignUpActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (exceptionMessage.contains("username")) {
                                usernameTxtView.setVisibility(View.VISIBLE);
                                scrollView.scrollTo(0, 0);

                            } else if (exceptionMessage.contains("email")) {
                                emailTxtView.setVisibility(View.VISIBLE);
                                scrollView.scrollTo(0, emailTxtView.getTop());
                            }

                        }
                    });
                } finally {
                    SignUpActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            signUpBtn.setEnabled(true);
                        }
                    });
                }
            }
        });
        thread.start();

    }

    private void hideTxtView() {
        usernameTxtView.setVisibility(View.INVISIBLE);
        passwordTxtView.setVisibility(View.INVISIBLE);
        confirmPasswordTxtView.setVisibility(View.INVISIBLE);
        emailTxtView.setVisibility(View.INVISIBLE);
    }

}
