package com.sirvar.roboscout;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

    public static final String KEY_TEAM = "team";
    public static final String KEY_EMAIL = "email";
    public static final String IS_LOGGED_IN = "logged-in";
    private final String PREF_NAME = "user";
    private final int PREF_MODE = 0;
    Context context;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    /**
     * Initializes SharedPreferences
     *
     * @param context application context at current state
     */
    public SessionManager(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(PREF_NAME, PREF_MODE);
        editor = preferences.edit();
    }

    /**
     * Creates new login session and saves it in SharedPref
     *
     * @param team  the team number
     * @param email the email logged in with
     */
    public void createLoginSession(String team, String email) {
        editor.putBoolean(IS_LOGGED_IN, true);

        editor.putString(KEY_TEAM, team);
        editor.putString(KEY_EMAIL, email);

        editor.commit();
    }

    /**
     * Gets the details of the logged in user
     *
     * @return HashMap of the user's details
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> details = new HashMap<>();
        details.put(KEY_TEAM, preferences.getString(KEY_TEAM, null));
        details.put(KEY_EMAIL, preferences.getString(KEY_EMAIL, null));

        return details;
    }

    /**
     * Logs out the user
     */
    public void logout() {
        editor.clear();
        editor.commit();
    }

    /**
     * Checks if user is logged in
     *
     * @return boolean if user is logged in or not
     */
    public boolean isLoggedIn() {
        return preferences.getBoolean(IS_LOGGED_IN, false);
    }

}
