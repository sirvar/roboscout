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

    EditText teamNumber;
    EditText region;

    public InfoFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        teamNumber = (EditText) view.findViewById(R.id.teamNumber);

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

}
