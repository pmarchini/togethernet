package com.togethernet.togethernet.WifiJumper;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;
import java.util.StringTokenizer;
import java.util.jar.Manifest;

import static android.Manifest.*;
import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;

/**
 * Created by k1008014 on 30/10/2017.
 */

import  com.togethernet.togethernet.Firebase.FirebaseDataHandler;
import com.togethernet.togethernet.GlobalApp.GlobalApp;

public class WifiScan {

    WifiManager wifi;
    int size = 0;
    public List<ScanResult> results;

    public void ScanNets(Context context) {
        wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        LocationManager position = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_status = position.isProviderEnabled(LocationManager.GPS_PROVIDER);
        //Se localizzazione o Wifi sono disabilitati avverto e svuoto le reti disponibili
        if (!wifi.isWifiEnabled() || !gps_status){
            //svuoto le reti disponibile
            GlobalApp globalApp = ( GlobalApp ) context.getApplicationContext();
            globalApp.GetAvNetsList().clear();
            //Avverto
            //Toast.makeText(context, "wifi or location is disabled ", Toast.LENGTH_LONG).show();
            //TODO -> Valutare se accendere dinamicamente o meno il wifi , per ora lo accendo solo se il gps è acceso
            if (gps_status){
                wifi.setWifiEnabled(true);
            }
        }else{
            results = wifi.getScanResults();
            size = results.size();
            try{
                this.SearchTogetherNets(context);
            }catch (InterruptedException exc){
                Log.w("Interrupted exception", exc);
            }
        }
    }
    //Cerco le reti togheternet all'interno delle reti esistenti
    public void SearchTogetherNets(Context context) throws InterruptedException {
        if(size == 0) {
            Log.e("TogetherNet Scan", "controlling empty ScanResult");
            return;
        }

        FirebaseDataHandler fireHandler = new FirebaseDataHandler();
        //Inizio Async
        fireHandler.getAVTogetherNets(results, context);
        //controllo le reti
        for(int i = 0; i < this.results.size(); i++){
            Log.i("TogetherNet Scan", "controlling :" + this.results.get(i).SSID + " With intensity level : " + this.results.get(i).level + " And BSSID : " + this.results.get(i).BSSID);
        }
    }

}
