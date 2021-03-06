package com.sirvar.roboscout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
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
        teams.clear();
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
                            if (object.getString("team").equals("")) continue;
                            Team team = new Team(object.getString("team"), object.getString("region"), object.getString("school"), object.getString("teamName"), object.getObjectId());
                            teams.add(team);
                        }
                    } else {
                        // Unable to fetch team list, reset list local store
                        localTeamList();
                    }
                    adapter.notifyDataSetChanged();
                    refresh.setRefreshing(false);
                }
            });
        } else {
            localTeamList();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateTeamList();
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateTeamList();
    }

    @Override
    public void teamClicked(View v, int position) {
        // Attach Parcelable to edit team
        Bundle teamInfo = new Bundle();
        teamInfo.putParcelable("team", teams.get(position));
        startActivity(new Intent(getApplicationContext(), ScoutActivity.class).putExtras(teamInfo));
    }

    @Override
    public void teamLongClicked(View v, final int position) {
        new AlertDialog.Builder(TeamListActivity.this, R.style.AppCompatAlertDialogStyle)
                .setMessage("Are you sure you want to delete this team?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("T" + teamNumber);
                        query.fromLocalDatastore().whereEqualTo("objectId", teams.get(position).getuID()).findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> list, ParseException e) {
                                // No exception, query successful
                                for (ParseObject po : list) {
                                    po.deleteEventually();
                                }
                            }
                        });
                        teams.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .show();
    }

    public void localTeamList() {
        // Query backend
        ParseQuery<ParseObject> query = ParseQuery.getQuery("T" + teamNumber);
        query.fromLocalDatastore().findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                // No exception, query successful
                if (e == null) {
                    teams.clear();
                    Collections.reverse(list);
                    for (ParseObject object : list) {
                        if (object.getString("team").equals("")) continue;
                        Team team = new Team(object.getString("team"), object.getString("region"), object.getString("school"), object.getString("teamName"), object.getObjectId());
                        teams.add(team);
                    }
                }
                adapter.notifyDataSetChanged();
                refresh.setRefreshing(false);
            }
        });
    }

    /**
     * Checks if the device is connected to the Internet
     *
     * @return connection state
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_team_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            sessionManager.logout();
            ParseUser.logOut();
            startActivity(new Intent(getApplicationContext(), RedirectActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
