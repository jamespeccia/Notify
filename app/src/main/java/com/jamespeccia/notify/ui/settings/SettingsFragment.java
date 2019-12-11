package com.jamespeccia.notify.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.jamespeccia.notify.MainActivity;
import com.jamespeccia.notify.R;

public class SettingsFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        final SharedPreferences serverSettings = getActivity().getSharedPreferences("server_information", Context.MODE_PRIVATE);
        final EditText host = root.findViewById(R.id.host);
        final EditText port = root.findViewById(R.id.port);
        final Button submit = root.findViewById(R.id.submit);

        host.setText(serverSettings.getString("host", "localhost"));
        port.setText(serverSettings.getInt("port", 80)+"");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                host.onEditorAction(EditorInfo.IME_ACTION_DONE);
                serverSettings.edit().putString("host", host.getText().toString()).apply();
                try {
                    int portNumber = Integer.parseInt(port.getText().toString());
                    serverSettings.edit().putInt("port", portNumber).apply();
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Not a valid port. Try again.", Toast.LENGTH_LONG).show();
                }
                MainActivity.updateNavHeader();
            }
        });

        return root;
    }
}