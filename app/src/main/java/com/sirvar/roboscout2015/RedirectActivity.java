package com.sirvar.roboscout2015;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

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

        if (sessionManager.isLoggedIn()) {
            // Open main screen
            finish();
        } else {
            // Open login screen
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        overridePendingTransition(0, 0);

    }

}
