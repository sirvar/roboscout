package com.sirvar.roboscout2015;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.parse.Parse;

public class RedirectActivity extends AppCompatActivity {

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redirect);

        try {
            Parse.enableLocalDatastore(this);
            Parse.initialize(this, "ALp8bFYR7LdRfH4Wba6Uz5klQte4dOcsMqGBugH5", "PV2POubfxe04VaRgLRF1S0WKQB2Se6LSdPMce2cL");
        } catch (RuntimeException re) {
            Log.v("Parse", "Unable to initialize");
        }

        sessionManager = new SessionManager(getApplicationContext());

        Class activity;

        if (sessionManager.isLoggedIn()) {
            // Open main screen
            activity = TeamListActivity.class;
            Toast.makeText(getApplicationContext(), "Welcome back Team " + sessionManager.getUserDetails().get(SessionManager.KEY_TEAM), Toast.LENGTH_SHORT).show();
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
