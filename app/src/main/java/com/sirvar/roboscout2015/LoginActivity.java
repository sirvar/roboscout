package com.sirvar.roboscout2015;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    Button login;
    Button signup;

    EditText email;
    EditText password;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sessionManager = new SessionManager(getApplicationContext());

        login = (Button) findViewById(R.id.login);
        signup = (Button) findViewById(R.id.signup);

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);

        // Try to login into Parse backend
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(email.getText().toString(), password.getText().toString());
            }
        });

        // Opens signup activity
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignupActivity.class));
                finish();
            }
        });

    }

    /**
     * Attempts to login to Parse backend
     *
     * @param email email of user
     * @param pass  password of user
     */
    public void login(final String email, final String pass) {
        // Make sure all fields are filled in
        if (email.equals("") || pass.equals("")) {
            Toast.makeText(getApplicationContext(), "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Login to Parse backend
        ParseUser.logInInBackground(email, pass, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (parseUser != null) {
                    Toast.makeText(getApplicationContext(), "Successfully logged in.", Toast.LENGTH_SHORT).show();
                    sessionManager.createLoginSession(parseUser.getString("Team"), email);
                    startActivity(new Intent(getApplicationContext(), TeamListActivity.class));
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Unable to login.", Toast.LENGTH_SHORT).show();
                    password.setText("");
                }
            }
        });

    }

}
