package com.sirvar.roboscout2015;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends Fragment {

    private EditText teamNumber;
    private EditText region;
    private EditText school;
    private EditText teamName;

    private Team team;

    public InfoFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        team = getArguments().getParcelable("team");

        teamNumber = (EditText) view.findViewById(R.id.teamNumber);
        region = (EditText) view.findViewById(R.id.region);
        school = (EditText) view.findViewById(R.id.school);
        teamName = (EditText) view.findViewById(R.id.teamName);

        teamNumber.setText(team.getTeamNumber());
        region.setText(team.getRegion());
        school.setText(team.getSchool());
        teamName.setText(team.getTeamName());

        // Add team number to toolbar
        teamNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (teamNumber.getText().toString().equals("")) {
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Scout");
                } else {
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Scouting Team " + teamNumber.getText().toString());
                }
            }
        });

        return view;
    }

    // Getter methods
    public String getTeamNumber() {
        return teamNumber.getText().toString();
    }

    public String getRegion() {
        return region.getText().toString();
    }

    public String getSchool() {
        return school.getText().toString();
    }

    public String getTeamName() {
        return teamName.getText().toString();
    }
}
