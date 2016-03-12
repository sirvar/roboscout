package com.sirvar.roboscout2015;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class TeamListActivity extends AppCompatActivity {

    SessionManager sessionManager;

    String teamNumber;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sessionManager = new SessionManager(getApplicationContext());

        // Get team number and email from logged in user
        teamNumber = sessionManager.getUserDetails().get(SessionManager.KEY_TEAM);
        email = sessionManager.getUserDetails().get(SessionManager.KEY_EMAIL);

        // Set title to team number
        getSupportActionBar().setTitle("Team " + teamNumber);

    }

}
