package com.togethernet.togethernet.WifiJumper.TogetherNetsRemover;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.List;

/**
 * Created by pietr_000 on 19/11/2017.
 */

public class NetRemover {
    public static void removeNets(Context context) {
        String tmp;
        String currentSSID;
        //Istanzio WifiManager
        WifiManager wm = ( WifiManager ) context.getSystemService(Context.WIFI_SERVICE);
        //SSID connesso se esiste
        if (wm.isWifiEnabled()){
            currentSSID = wm.getConnectionInfo().getSSID();
        }else{
            currentSSID = " ";
        }
        //Prendo lista delle connessioni disponibili
        List<WifiConfiguration> list = wm.getConfiguredNetworks();
        //Looppo su tutte le reti
        for (WifiConfiguration i : list) {
            tmp = i.toString().substring(i.toString().lastIndexOf("cname="),i.toString().indexOf(" ", i.toString().lastIndexOf("cname=")));
            if ( tmp.contentEquals("cname=com.togethernet.togethernet") && !currentSSID.contentEquals(i.SSID)){
                wm.removeNetwork(i.networkId);
            }
        }
    }
    public static void removeNetsOnBoot(Context context) {
        String tmp;
        String currentSSID;
        //Istanzio WifiManager
        WifiManager wm = ( WifiManager ) context.getSystemService(Context.WIFI_SERVICE);
        // accendo il wifi
        wm.setWifiEnabled(true);
        //Prendo lista delle connessioni disponibili
        List<WifiConfiguration> list = wm.getConfiguredNetworks();
        //Looppo su tutte le reti
        for (WifiConfiguration i : list) {
            tmp = i.toString().substring(i.toString().lastIndexOf("cname="),i.toString().indexOf(" ", i.toString().lastIndexOf("cname=")));
            if ( tmp.contentEquals("cname=com.togethernet.togethernet")){
                wm.removeNetwork(i.networkId);
            }
        }
        wm.setWifiEnabled(false);
    }
}
