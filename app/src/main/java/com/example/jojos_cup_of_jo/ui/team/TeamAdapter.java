package com.example.jojos_cup_of_jo.ui.team;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jojos_cup_of_jo.databinding.ItemTeamMemberBinding;
import com.example.jojos_cup_of_jo.model.TeamMember;
import com.example.jojos_cup_of_jo.ui.util.PlaceholderStyle;

import java.util.List;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.TeamViewHolder> {

    private final List<TeamMember> members;

    public TeamAdapter(List<TeamMember> members) {
        this.members = members;
    }

    @NonNull
    @Override
    public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTeamMemberBinding binding = ItemTeamMemberBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new TeamViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamViewHolder holder, int position) {
        holder.bind(members.get(position));
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    static class TeamViewHolder extends RecyclerView.ViewHolder {

        private final ItemTeamMemberBinding binding;

        TeamViewHolder(ItemTeamMemberBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(TeamMember member) {
            binding.memberName.setText(member.getName());
            binding.memberRole.setText(member.getRole());
            binding.memberQuote.setText(member.getQuote());
            binding.memberInitials.setText(PlaceholderStyle.initialsFor(member.getName()));
            PlaceholderStyle.applySwatchTint(binding.memberAvatar, member.getSwatchIndex());
        }
    }
}
