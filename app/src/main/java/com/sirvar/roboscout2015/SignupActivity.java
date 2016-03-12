package com.sirvar.roboscout2015;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupActivity extends AppCompatActivity {

    Button signup;
    Button login;

    EditText teamNumber;
    EditText email;
    EditText password;
    EditText passwordAgain;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sessionManager = new SessionManager(getApplicationContext());

        signup = (Button) findViewById(R.id.signup);
        login = (Button) findViewById(R.id.login);

        teamNumber = (EditText) findViewById(R.id.tNumber);
        email = (EditText) findViewById(R.id.tEmail);
        password = (EditText) findViewById(R.id.tPassword);
        passwordAgain = (EditText) findViewById(R.id.tPasswordAgain);

        // Try to create new user on Parse backend
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if passwords match
                if (password.getText().toString().equals(passwordAgain.getText().toString())) {
                    signup(teamNumber.getText().toString(), email.getText().toString(), password.getText().toString());
                } else {
                    Toast.makeText(getApplicationContext(), "Passwords don't match. Try again.", Toast.LENGTH_SHORT).show();
                    password.setText("");
                    passwordAgain.setText("");
                }
            }
        });

        // Starts login activity
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });

    }

    public void signup(final String teamNumber, final String email, String password) {
        // Make sure all fields are filled in
        if (teamNumber.equals("") || email.equals("") || password.equals("")) {
            Toast.makeText(getApplicationContext(), "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return;
        }
        // Create new ParseUser object to signup with field credentials
        ParseUser user = new ParseUser();
        user.setEmail(email);
        user.setUsername(email);
        user.setPassword(password);

        user.put("Team", teamNumber);

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                // Signup was successful, no exception
                if (e == null) {
                    Toast.makeText(getApplicationContext(), "Successfully signed up.", Toast.LENGTH_SHORT).show();
                    sessionManager.createLoginSession(teamNumber, email);
//                    startActivity(new Intent(getApplicationContext(), MAIN));
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Unable signed up. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
