package com.togethernet.togethernet;


import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.togethernet.togethernet.GlobalApp.Preferences.PreferenceManager;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {


    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        final PreferenceManager prefManager = new PreferenceManager(this.getContext().getApplicationContext());
        Switch sw = (Switch) view.findViewById(R.id.automatSwitch);
        sw.setChecked(prefManager.isAutomaticConnectionSetted());
        //Nome connesione
        WifiManager wm = (WifiManager) this.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wm.isWifiEnabled()) {
            if (!wm.getConnectionInfo().getBSSID().isEmpty()) {
                TextView tx = (TextView) view.findViewById(R.id.fragment_current_connection_2);
                tx.setText( wm.getConnectionInfo().getSSID());
            } else {
                TextView tx = (TextView) view.findViewById(R.id.fragment_current_connection_2);
                tx.setText("Nessuna connesione");
            }
        }
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == false){
                    prefManager.setAutomaticConnection(false);
                }else{
                    prefManager.setAutomaticConnection(true);
                }
            }
        });
    }

}
