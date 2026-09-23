package com.example.jojos_cup_of_jo.ui.team;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.jojos_cup_of_jo.data.SampleDataProvider;
import com.example.jojos_cup_of_jo.databinding.FragmentTeamBinding;

public class TeamFragment extends Fragment {

    private FragmentTeamBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        binding = FragmentTeamBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.teamRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.teamRecyclerView.setAdapter(new TeamAdapter(SampleDataProvider.getTeamMembers()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
