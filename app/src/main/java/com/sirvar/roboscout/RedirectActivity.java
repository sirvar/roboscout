package com.sirvar.roboscout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.parse.Parse;

public class RedirectActivity extends AppCompatActivity {

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redirect);

        // Initialize Parse
        try {
            Parse.enableLocalDatastore(this);
            Parse.initialize(this, "hL3tRVlLb2ru2fc3CEVkzWKNyXnR2eBuprD1eTTC", "Bp10SKAGT7Q2CK60qeHiLeH5dGqqTQobezET46GF");
        } catch (RuntimeException re) {
            Log.v("Parse", "Unable to initialize");
        }

        sessionManager = new SessionManager(getApplicationContext());

        Class activity;

        if (sessionManager.isLoggedIn()) {
            // Open main screen
            activity = TeamListActivity.class;
        } else {
            // Open login screen
            activity = LoginActivity.class;
        }

        // Start new activity and finish this
        startActivity(new Intent(this, activity));
        finish();
        overridePendingTransition(0, 0);

    }

}
