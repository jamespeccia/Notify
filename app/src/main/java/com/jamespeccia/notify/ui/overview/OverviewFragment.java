package com.jamespeccia.notify.ui.overview;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jamespeccia.notify.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class OverviewFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_overview, container, false);
        SharedPreferences selected_applications = getActivity().getSharedPreferences("selected_applications", Context.MODE_PRIVATE);

        List<String> selectedApplicationList = new ArrayList<>();
        //Finds all selected applications from SharedPreferences and adds them to selectedApplicationList
        Map<String, ?> keys = selected_applications.getAll();
        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            if (entry.getValue() instanceof Boolean && entry.getValue() == Boolean.TRUE) {
                selectedApplicationList.add(entry.getKey());
            }
        }
        Collections.sort(selectedApplicationList);

        RecyclerView recyclerView = root.findViewById(R.id.selected_applications);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        OverviewRecyclerViewAdapter adapter = new OverviewRecyclerViewAdapter(getContext(), selectedApplicationList, selected_applications);
        recyclerView.setAdapter(adapter);

        return root;
    }

}