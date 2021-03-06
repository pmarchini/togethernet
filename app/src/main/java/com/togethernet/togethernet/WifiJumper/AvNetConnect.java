package com.togethernet.togethernet.WifiJumper;

import android.content.Context;
import android.net.Network;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.togethernet.togethernet.GlobalApp.Preferences.PreferenceManager;
import com.togethernet.togethernet.NotificationSystem.ConnectionNotificationHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by pietr_000 on 04/11/2017.
 */

public class AvNetConnect {
    //Delta precisione Livello AP
    //Indica il Delta entro cui valuto una connessione migliore
    protected static final int DELTA_LEVEL_AP = 0;

    public static void WifiSeekAndConnect(ArrayList<HashMap<String, String>> AvNets, Context context){
        if (AvNets.size() > 0){
            //TODO -> Aggiungere setting per connessione automatica, se non connetto butto fuori notifica di rete disponibile -> To be tested
            if(!AvNetselection(AvNets).isEmpty()) {
                PreferenceManager prefManager = new PreferenceManager(context);
                //Se è settata la connessione automatica connetto direttamente
                if (prefManager.isAutomaticConnectionSetted()) {
                    ConnectToWifi(AvNetselection(AvNets), context);
                //In caso contrario mostro una notifica
                } else {
                    selectNotification(AvNetselection(AvNets), context);
                }
            }
            //Nessuna connessione disponibile, pulisco eventuali notifiche
        }else {
            ConnectionNotificationHandler.clearNotification(context, 002); //002 -> canale notifiche connessioni disponibili
        }
    }

    public static HashMap<String, String> AvNetselection(ArrayList<HashMap<String, String>> AvNets){

        double bestLevel = 200;
        int counter = 0;
        int best = -1;

        for (HashMap<String, String > net : AvNets){
            //Aggiungo un delta per evitare continui sbalzi tra reti simili
            if(Double.parseDouble(net.get("level")) < ( bestLevel + DELTA_LEVEL_AP) ){
                best = counter;
            }
            counter += 1;
        }
        if(best != -1) {
            return  AvNets.get(best);
        }else{
            return  null;
        }
    }

    //Metodo di connessione
    //Genero le strutture necessarie a connettere
    //Controllo se esiste già una rete con lo stesso nome e la sovrascrivo
    //Infine connetto
    public static void ConnectToWifi(HashMap<String, String> net, Context context){
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if(!net.get("wifi_bssid").equals(wm.getConnectionInfo().getBSSID())){
            //Non sono già connesso al migliore AP
            WifiConfiguration wifiConf = new WifiConfiguration();
            wifiConf.BSSID = "\"" + net.get("wifi_bssid") + "\"";
            wifiConf.SSID = "\"" + net.get("wifi_ssid") + "\"";
            if(net.get("security").contains("WEP")){
                wifiConf.wepKeys[0] = "\"" + net.get("wifi_pwd") + "\"";
                wifiConf.wepTxKeyIndex = 0;
                wifiConf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                wifiConf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            }else if (net.get("security").contains("WPA")){
                wifiConf.preSharedKey = "\"" + net.get("wifi_pwd") + "\"";
            }else{
                wifiConf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            }
            //Controllo che non esista già questa rete tramite SSID
            List<WifiConfiguration> list = wm.getConfiguredNetworks();
            boolean exist = false;
            for (WifiConfiguration config : list ){
                if(config.SSID != null && config.SSID.equals("\"" + net.get("wifi_ssid") + "\"")){
                    //Se esiste già controllo che il BSSID coincida
                    if(config.BSSID != null && config.BSSID.equals("\"" + net.get("wifi_bssid") + "\"")){
                        exist = true;
                    //Se non coincide BSSID la rete non esiste
                    }else{
                        //Non posso sapere se il BSSID è congruente per tanto elimino direttamente
                        wm.removeNetwork(config.networkId);
                        exist = false;
                    }
                }
            }
            if(!exist){
                wm.addNetwork(wifiConf);
            }
            //Loop on nets
            for( WifiConfiguration i : list ) {
                if(i.SSID != null && i.SSID.equals("\"" + net.get("wifi_ssid") + "\"")) {
                    //wm.disconnect();
                    //wm.enableNetwork(i.networkId, true);
                    //wm.reconnect();
                    wm.disableNetwork(i.networkId);
                    wm.enableNetwork(i.networkId, true);
                    SupplicantState supState;
                    WifiInfo wifiInfo = wm.getConnectionInfo();
                    supState = wifiInfo.getSupplicantState();
                    if (supState.equals(SupplicantState.DISCONNECTED)){
                        Log.e("TogetherNet", "Wrong wifi configuration or low signal, if the problem persists erase this net");
                    }
                    ConnectionNotificationHandler.buildConnectionEventNotificationProAdv(context, i.SSID);
                    break;
                }
            }
        }else{
            ConnectionNotificationHandler.buildConnectionEventNotificationProAdv(context, net.get("wifi_ssid")); //net.get("pubblicity"));
            Log.i("TogetherNet", "Nothing to do");
        }
    }

    //Pubblica notifica in base alla situazione
    public static void selectNotification(HashMap<String, String> net, Context context){
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wm.getConnectionInfo().getBSSID() != " " || wm.getConnectionInfo().getBSSID() != ""){
            if(!net.get("wifi_bssid").equals(wm.getConnectionInfo().getBSSID())){
                ConnectionNotificationHandler.buildBestAvConnectionNotification(context, net.get("wifi_ssid"));
            }
        }else{
            ConnectionNotificationHandler.buildAvConnectionNotification(context);
        }
    }
}
