package com.jamespeccia.notify.ui.installed_applications;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jamespeccia.notify.R;

import java.util.List;

class InstalledApplicationsRecyclerViewAdapter extends RecyclerView.Adapter<InstalledApplicationsRecyclerViewAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<String> installedApplicationsList;
    private SharedPreferences selectedApplications;

    InstalledApplicationsRecyclerViewAdapter(Context context, final List<String> installedApplicationsList, SharedPreferences selectedApplications) {
        this.inflater = LayoutInflater.from(context);
        this.installedApplicationsList = installedApplicationsList;
        this.selectedApplications = selectedApplications;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InstalledApplicationsRecyclerViewAdapter.ViewHolder holder, int position) {
        final String applicationLabel = installedApplicationsList.get(position);
        holder.textView.setText(applicationLabel);
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(selectedApplications.getBoolean(applicationLabel, false));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selectedApplications.edit().putBoolean(applicationLabel, isChecked).apply();
            }
        });
    }

    @Override
    public int getItemCount() {
        return installedApplicationsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        CheckBox checkBox;

        ViewHolder(final View itemView) {
            super(itemView);
            setIsRecyclable(false);
            textView = itemView.findViewById(R.id.application);
            checkBox = itemView.findViewById(R.id.checkBox);
            checkBox.setChecked(false);
        }
    }
}