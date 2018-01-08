package com.togethernet.togethernet.WifiJumper.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.togethernet.togethernet.WifiJumper.AvNetConnect;

import java.util.HashMap;

/**
 * Created by pietr_000 on 03/11/2017.
 */

public class wifiUtilities {

    //Message alert -> richiesta di accensione del modulo wifi
    public static void buildAlertMessageNoWifi(final Activity Act) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(Act);
        builder.setMessage("Per eseguire queste funzionalità togethernet necessità del tuo wifi, attivare in questo momento?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        WifiManager wifi = (WifiManager) Act.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                        wifi.setWifiEnabled(true);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    //Controllo stato Wifi, se disattivato chiedo consenso per attivare
    public static void WifiStateControl(Activity Act){
        WifiManager wifi = (WifiManager) Act.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if(!wifi.isWifiEnabled()){
            Log.i("TogetherNet ", "Wifi is not enabled, asking for permission to activate");
            buildAlertMessageNoWifi(Act);
        }else{
            Log.i("TogetherNet ", "Wifi Control completed -> Wifi is enabled");
        }
    }

    public static void builAlertMessageGoWifi(final Activity Act,final HashMap<String,String> net){
        final AlertDialog.Builder builder = new AlertDialog.Builder(Act);
        builder.setMessage("Stai per conneterti ad una rete TogetherNet, sei sicuro?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        //TODO -> confirm connection
                        AvNetConnect.ConnectToWifi(net , Act.getApplicationContext());
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

}
