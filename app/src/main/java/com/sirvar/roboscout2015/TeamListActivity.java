package com.sirvar.roboscout2015;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class TeamListActivity extends AppCompatActivity implements TeamListAdapter.ClickListener {

    SessionManager sessionManager;

    String teamNumber;
    String email;

    ArrayList<Team> teams;

    RecyclerView recyclerView;
    TeamListAdapter adapter;

    SwipeRefreshLayout refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ScoutActivity.class));
            }
        });

        teams = new ArrayList<>();

        sessionManager = new SessionManager(getApplicationContext());

        // Get team number and email from logged in user
        teamNumber = sessionManager.getUserDetails().get(SessionManager.KEY_TEAM);
        email = sessionManager.getUserDetails().get(SessionManager.KEY_EMAIL);

        // Set title to team number
        getSupportActionBar().setTitle("RoboScout: Team " + teamNumber);

        updateTeamList();

        refresh = (SwipeRefreshLayout) findViewById(R.id.teamListRefresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateTeamList();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.teamList);
        adapter = new TeamListAdapter(teams);
        adapter.setClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);

    }

    /**
     * Updates the team list by querying the Parse database.
     */
    public void updateTeamList() {
        // Save copy in case fetching doesn't work
        final ArrayList<Team> oldTeams = new ArrayList<>(teams);

        if (isNetworkAvailable()) {
            // Query backend
            ParseQuery<ParseObject> query = ParseQuery.getQuery("T" + teamNumber);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(final List<ParseObject> list, ParseException e) {
                    // No exception, query successful
                    ParseObject.unpinAllInBackground(new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {
                            ParseObject.pinAllInBackground(list);
                        }
                    });
                    teams.clear();
                    if (e == null) {
                        for (ParseObject object : list) {
                            if (object.getString("Team").equals("")) continue;
                            Team team = new Team(object.getString("Team"), object.getString("Region"), object.getString("School"), object.getString("TeamName"), object.getObjectId());
                            teams.add(team);
                        }
                    } else {
                        // Unable to fetch team list, reset list to previous teams
                        Toast.makeText(getApplicationContext(), "Unable to fetch data.", Toast.LENGTH_SHORT).show();
                        teams = oldTeams;
                    }
                    adapter.notifyDataSetChanged();
                    refresh.setRefreshing(false);
                }
            });
        } else {
            // Query backend
            ParseQuery<ParseObject> query = ParseQuery.getQuery("T" + teamNumber);
            query.fromLocalDatastore().findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    // No exception, query successful
                    teams.clear();
                    if (e == null) {
                        Collections.reverse(list);
                        for (ParseObject object : list) {
                            if (object.getString("Team").equals("")) continue;
                            Team team = new Team(object.getString("Team"), object.getString("Region"), object.getString("School"), object.getString("TeamName"), object.getObjectId());
                            teams.add(team);
                        }
                    } else {
                        // Unable to fetch team list, reset list to previous teams
                        Toast.makeText(getApplicationContext(), "Unable to fetch data.", Toast.LENGTH_SHORT).show();
                        teams = oldTeams;
                    }
                    adapter.notifyDataSetChanged();
                    refresh.setRefreshing(false);
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateTeamList();
    }

    @Override
    public void teamClicked(View v, int position) {
        // Attach Parcelable to edit team
        Bundle teamInfo = new Bundle();
        teamInfo.putParcelable("team", teams.get(position));
        startActivity(new Intent(getApplicationContext(), ScoutActivity.class).putExtras(teamInfo));
    }

    /**
     * Checks if the device is connected to the Internet
     * @return connection state
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
