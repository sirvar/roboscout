package com.sirvar.roboscout2015;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
                login(email.getText().toString(), password.getText().toString(), v);
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
    public void login(final String email, final String pass, final View view) {
        if (!isNetworkAvailable()) {
            Snackbar.make(view, "Unable to connect to the Internet. Try again.", Snackbar.LENGTH_SHORT).show();
            return;
        }

        // Make sure all fields are filled in
        if (email.equals("") || pass.equals("")) {
            Snackbar.make(view, "Please fill in all fields.", Snackbar.LENGTH_SHORT).show();
            return;
        }

        // Login to Parse backend
        ParseUser.logInInBackground(email, pass, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (parseUser != null) {
                    Snackbar.make(view, "Successfully logged in.", Snackbar.LENGTH_SHORT).show();
                    sessionManager.createLoginSession(parseUser.getString("Team"), email);
                    startActivity(new Intent(getApplicationContext(), TeamListActivity.class));
                    finish();
                } else {
                    Snackbar.make(view, "Unable to login.", Snackbar.LENGTH_SHORT).show();
                    password.setText("");
                }
            }
        });

    }

    /**
     * Checks if the device is connected to the Internet
     *
     * @return connection state
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
