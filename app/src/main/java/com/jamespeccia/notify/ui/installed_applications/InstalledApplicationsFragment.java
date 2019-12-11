package com.jamespeccia.notify.ui.installed_applications;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jamespeccia.notify.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InstalledApplicationsFragment extends Fragment {

    private List<String> installedApplicationsList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final PackageManager packageManager = getActivity().getPackageManager();
        List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER), 0);
        installedApplicationsList = new ArrayList<>(resolveInfos.size());
        for (int i = 0; i < resolveInfos.size(); i++) {
            installedApplicationsList.add(resolveInfos.get(i).loadLabel(packageManager).toString());
        }
        Collections.sort(installedApplicationsList, String.CASE_INSENSITIVE_ORDER);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_installed_applications, container, false);
        SharedPreferences selectedApplications = getActivity().getSharedPreferences("selected_applications", Context.MODE_PRIVATE);

        RecyclerView recyclerView = root.findViewById(R.id.selected_applications);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        InstalledApplicationsRecyclerViewAdapter adapter = new InstalledApplicationsRecyclerViewAdapter(getContext(), installedApplicationsList, selectedApplications);
        recyclerView.setAdapter(adapter);

        return root;
    }

}