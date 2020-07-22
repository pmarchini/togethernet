package com.togethernet.togethernet.LocationDB;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.togethernet.togethernet.Firebase.FirebaseDataHandler;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by k1008014 on 12/01/2018.
 */

public class netsLocationList {

    public com.togethernet.togethernet.LocationDB.firebaseHandler dataHandler;
    public ArrayList<HashMap<String,String>> List;

    public  netsLocationList(){
        dataHandler = new firebaseHandler();
    }

    public  netsLocationList(LatLng XY, Context context){
        dataHandler = new firebaseHandler();
        dataHandler.SearchLocations(XY, 15, context);
    }

    public ArrayList<HashMap<String,String>> getLocations(){
        return this.dataHandler.getLocationList();
    }

    public void UpdateLocations(LatLng XY, Context context){
        dataHandler.SearchLocations(XY, 15, context);
    }
    public void SetLocations(ArrayList<HashMap<String,String>> List){
        this.List = List;
    }
}
