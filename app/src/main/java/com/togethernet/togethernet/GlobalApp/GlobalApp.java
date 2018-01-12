package com.togethernet.togethernet.GlobalApp;

import android.app.Application;
import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.togethernet.togethernet.LocationDB.netsLocationGlobalList;
import com.togethernet.togethernet.WifiJumper.AvNetsList.AppAvNetsListGlobalizer.AvNetsGlobalList;
import com.togethernet.togethernet.WifiJumper.BlackList.AppBlackListGlobalizer.GlobalBlackList;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by pietr_000 on 05/11/2017.
 */

//App Global
    //In questo punto globalizzo le istanze delle classi legati al salvataggio di dati
    //Queste classi istanziate sono accessibile da qualsiasi punto dell'applicazione

public class GlobalApp extends Application {


    //BlackList globale #BEG
    GlobalBlackList BlackList;

    /*public GlobalApp(){
        BlackList = new GlobalBlackList();
    }*/

    //NetList Globale
    public netsLocationGlobalList NetGlobalList;


    public void  initGlobalBlackList(Context context){
        this.BlackList = new GlobalBlackList(context);
    }


    public GlobalBlackList getBlackList(){
        return  BlackList;
    }

    public boolean GlobalBlackListVerify(String BSSID){
        return BlackList.GlobalBlackList.blackListVerifyBSSID(BSSID);
    }
    public void GlobalBlackListAddNet(String BSSID, String SSID){
        BlackList.GlobalBlackList.addNetToBlackList(BSSID, SSID);
    }
    //BlackList globale #END

    //AvNetsList globale #BEG
    AvNetsGlobalList AvNetsList = new AvNetsGlobalList();

    public ArrayList<HashMap<String, String>> GetAvNetsList(){
        return this.AvNetsList.GlobalAvNets.getAvNetsList();
    }
    public void SetAvNetsList(ArrayList<HashMap<String, String>> NetsToSet){
        this.AvNetsList.GlobalAvNets.ClearAndSetAvNetsList(NetsToSet);
    }
    //AvNetsList globale #END

    //Lista posizioni reti Vicine #BEG
    public void setGlobalNetsLocationList(){
        this.NetGlobalList = new netsLocationGlobalList();
    }

    public netsLocationGlobalList getLocationNetsList(){
        return this.NetGlobalList;
    }
    //Lista posizioni reti Vicine #END

}
