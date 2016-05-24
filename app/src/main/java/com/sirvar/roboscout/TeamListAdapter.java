package com.sirvar.roboscout;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class TeamListAdapter extends RecyclerView.Adapter<TeamListAdapter.TeamViewHolder> {

    ArrayList<Team> teams;

    ClickListener clickListener;

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

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void teamClicked(View v, int position);

        void teamLongClicked(View v, int position);
    }

    public class TeamViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView teamNumber;
        TextView region;

        public TeamViewHolder(View itemView) {
            super(itemView);

            teamNumber = (TextView) itemView.findViewById(R.id.teamNumber);
            region = (TextView) itemView.findViewById(R.id.region);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.teamClicked(v, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.teamLongClicked(v, getAdapterPosition());
            return true;
        }
    }
}
