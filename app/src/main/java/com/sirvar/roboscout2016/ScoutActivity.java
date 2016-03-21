package com.sirvar.roboscout2016;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

public class ScoutActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private SessionManager sessionManager;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private Team team;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        sessionManager = new SessionManager(getApplicationContext());

        try {
            team = getIntent().getExtras().getParcelable("team");
            getSupportActionBar().setTitle("Scouting Team " + team.getTeamNumber());
        } catch (NullPointerException npe) {
            team = new Team();
        }

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), team);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_recruit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Saves the data to Parse and local datastore if unable to save online
     */
    public void save() {
        InfoFragment info = mSectionsPagerAdapter.infoFragment;
        AutoFragment auto = mSectionsPagerAdapter.autoFragment;
        TeleopFragment teleop = mSectionsPagerAdapter.teleopFragment;
        DrivingFragment driving = mSectionsPagerAdapter.drivingFragment;

        ParseObject teamParse;

        if (team.getuID() == null) {
            teamParse = new ParseObject("T" + sessionManager.getUserDetails().get(SessionManager.KEY_TEAM));
        } else {
            teamParse = ParseObject.createWithoutData("T" + sessionManager.getUserDetails().get(SessionManager.KEY_TEAM), team.getuID());
        }

        teamParse.put("team", info.getTeamNumber());
        teamParse.put("region", info.getRegion());
        teamParse.put("school", info.getSchool());
        teamParse.put("teamName", info.getTeamName());
        teamParse.put("strategy", info.getStrategy());

        if (info.getShooting().isChecked()) {
            teamParse.put("robotType", "Shooting");
        } else if (info.getDefensive().isChecked()) {
            teamParse.put("robotType", "Defensive");
        }

        if (isNetworkAvailable()) {
            teamParse.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    finish();
                }
            });
        } else {
            teamParse.saveEventually();
            finish();
        }

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_scout, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public InfoFragment infoFragment;
        public AutoFragment autoFragment;
        public TeleopFragment teleopFragment;
        public DrivingFragment drivingFragment;

        public SectionsPagerAdapter(FragmentManager fm, Team team) {
            super(fm);

            Bundle teamInfo = new Bundle();
            teamInfo.putParcelable("team", team);

            infoFragment = new InfoFragment();
            infoFragment.setArguments(teamInfo);

            autoFragment = new AutoFragment();
            teleopFragment = new TeleopFragment();
            drivingFragment = new DrivingFragment();
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.

            // Return proper fragment instance based on position
            switch (position) {
                case 0:
                    return infoFragment;
                case 1:
                    return autoFragment;
                case 2:
                    return teleopFragment;
                case 3:
                    return drivingFragment;
                default:
                    return PlaceholderFragment.newInstance(position + 1);
            }
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Info";
                case 1:
                    return "Auto";
                case 2:
                    return "Teleop";
                case 3:
                    return "Driving";
            }
            return null;
        }
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
}
