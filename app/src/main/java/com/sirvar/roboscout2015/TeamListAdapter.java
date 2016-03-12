package com.sirvar.roboscout2015;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by katyalrikin on 16-03-12.
 */
public class TeamListAdapter extends RecyclerView.Adapter<TeamListAdapter.TeamViewHolder> {

    ArrayList<Team> teams;

    public TeamListAdapter(ArrayList<Team> teams) {
        this.teams = teams;
    }

    @Override
    public TeamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_item, parent, false);
        return new TeamViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TeamViewHolder holder, int position) {
        Team team = teams.get(position);

        holder.teamNumber.setText(team.getTeamNumber());
        holder.region.setText(team.getRegion());
    }

    @Override
    public int getItemCount() {
        return teams.size();
    }

    public class TeamViewHolder extends RecyclerView.ViewHolder {

        TextView teamNumber;
        TextView region;

        public TeamViewHolder(View itemView) {
            super(itemView);

            teamNumber = (TextView) itemView.findViewById(R.id.teamNumber);
            region = (TextView) itemView.findViewById(R.id.region);
        }
    }
}
