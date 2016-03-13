package com.sirvar.roboscout2015;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class TeamListActivity extends AppCompatActivity {

    SessionManager sessionManager;

    String teamNumber;
    String email;

    ArrayList<Team> teams = new ArrayList<>();

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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        sessionManager = new SessionManager(getApplicationContext());

        // Get team number and email from logged in user
        teamNumber = sessionManager.getUserDetails().get(SessionManager.KEY_TEAM);
        email = sessionManager.getUserDetails().get(SessionManager.KEY_EMAIL);

        // Set title to team number
        getSupportActionBar().setTitle("Team " + teamNumber);

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
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);

    }

    /**
     * Updates the team list by querying the Parse database.
     */
    public void updateTeamList() {
        // Clear teams and save copy in case fetching doesn't work
        final ArrayList<Team> oldTeams = new ArrayList<>(teams);
        teams.clear();

        // Query backend
        ParseQuery<ParseObject> query = ParseQuery.getQuery("T" + teamNumber);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                // No exception, query successful
                if (e == null) {
                    for (ParseObject object : list) {
                        if (object.getString("Team").equals("")) continue;
                        Team team = new Team(object.getString("Team"), object.getString("Region"), object.getString("School"), object.getString("TeamName"), object.getObjectId());
                        teams.add(team);
                    }
                    Toast.makeText(getApplicationContext(), "Successfully updated team list", Toast.LENGTH_SHORT).show();
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
