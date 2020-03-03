package com.lab1.study;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private EditText usernameTxtField = null;
    private EditText passwordTxtField = null;
    private TextView invalidTxtView = null;
    private TextView forgotPasswordTxtView = null;
    private ScrollView scrollView = null;
    private CheckBox rememberMe = null;
    private Button loginBtn = null;
    private Button signUpBtn = null;
    private final int SIGN_UP_REQ = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        setViewsOnClickListeners();
        writeUserSavedInfo();

    }

    private void findViews() {
        usernameTxtField = findViewById(R.id.usernameTxtField);
        passwordTxtField = findViewById(R.id.passwordTxtField);
        invalidTxtView = findViewById(R.id.invalidTxtView);
        scrollView = findViewById(R.id.scrollView);
        rememberMe = findViewById(R.id.rememberMe);
        forgotPasswordTxtView = findViewById(R.id.forgotPasswordTxtView);
        loginBtn = findViewById(R.id.loginBtn);
        signUpBtn = findViewById(R.id.signUpBtn);

    }

    private void setViewsOnClickListeners() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn();
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignUpActivity();
            }
        });

        forgotPasswordTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    private void showDialog() {

        final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle(MainActivity.this.getResources().getText(R.string.forgot_password));
        alert.setMessage(MainActivity.this.getResources().getText(R.string.password_dialog_message));

        final EditText input = new EditText(alert.getContext());
        input.requestFocus();
        alert.setView(input);
        alert.setPositiveButton(MainActivity.this.getResources().getText(R.string.ok), null);
        alert.setNegativeButton(MainActivity.this.getResources().getText(R.string.cancel), null);

        final AlertDialog dialog = alert.create();
        dialog.show();

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final String email = input.getText().toString();
                        final String password = DbConnection.getInstance().getPassword(email);

                        if (password != null) {
                            try {
                                sendPassword(email, password);

                            } catch (RuntimeException e) {

                            }
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this,
                                            MainActivity.this.getResources().getText(R.string.password_sent)
                                            , Toast.LENGTH_LONG).show();
                                    dialog.cancel();
                                }
                            });

                        } else {
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    dialog.setMessage(MainActivity.this.getResources().getString(R.string.password_dialog_message) + "\n\n"
                                            + MainActivity.this.getResources().getString(R.string.no_email) + email);
                                }
                            });
                        }

                    }
                });
                thread.start();
            }
        });

        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }

    private void sendPassword(String email, String password) {
        Mail mail = new Mail(email,
                "Study Cards",
                "Hi,\n\nYou have requested your account's password.\nPassword: " + password + "\n\nBest regards,\nStudy Cards Team");
        mail.send();

    }


    private void writeUserSavedInfo() {
        ArrayList<String> logInSavedInfo = FileUtils.readFromFile("Remember me.txt", MainActivity.this);
        if (logInSavedInfo.size() == 2) {
            usernameTxtField.setText(logInSavedInfo.get(0));
            passwordTxtField.setText(logInSavedInfo.get(1));
        }

    }

    private void logIn() {
        invalidTxtView.setText("");
        loginBtn.setEnabled(false);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final User verifiedUser = tryTologIn();

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (verifiedUser != null) {
                            if (rememberMe.isChecked()) {
                                FileUtils.writeToFile("Remember me.txt", verifiedUser.getUsername()
                                                + "\r\n" + verifiedUser.getPassword(),
                                        MainActivity.this);

                            } else {
                                FileUtils.writeToFile("Remember me.txt", "", MainActivity.this);
                            }
                            Intent intent = new Intent(MainActivity.this, SubjectsActivity.class);
                            intent.putExtra("username", verifiedUser.getUsername());
                            startActivity(intent);
                        } else {
                            scrollView.scrollTo(0, 0);
                            invalidTxtView.setText(R.string.invalid_login);
                        }
                        loginBtn.setEnabled(true);
                    }
                });
            }
        });
        thread.start();

    }

    private User tryTologIn() {
        String username = usernameTxtField.getText().toString();
        String password = passwordTxtField.getText().toString();
        User user = new User(username, password);
        return user.logIn();

    }

    private void openSignUpActivity() {
        Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
        startActivityForResult(intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_UP_REQ) {
            if ((resultCode == RESULT_OK) && (data != null)) {
                String username = data.getExtras().getString("username");
                String password = data.getExtras().getString("password");
                Toast.makeText(getApplicationContext(),
                        MainActivity.this.getResources().getText(R.string.account_created),
                        Toast.LENGTH_LONG).show();
                usernameTxtField.setText(username);
                passwordTxtField.setText(password);
            }
        }
    }

}
