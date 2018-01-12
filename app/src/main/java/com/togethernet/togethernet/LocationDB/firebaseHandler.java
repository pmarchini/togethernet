package com.togethernet.togethernet.LocationDB;

import android.app.Application;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.togethernet.togethernet.GlobalApp.GlobalApp;
import com.togethernet.togethernet.LocationUtilites.LocationUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by k1008014 on 12/01/2018.
 */

public class firebaseHandler {

    private FirebaseDatabase Database;
    private DatabaseReference mDatabase;
    private DatabaseReference ApsRef;
    private GeoFire geoFire;
    private GeoQuery geoQuery;
    public int counter;
    private ArrayList<HashMap<String,String>> LocationList;
    private HashMap<String, String> Location;


    public firebaseHandler() {

        //Istanzio Lista posizioni
        this.Location = new HashMap<>();
        this.LocationList = new ArrayList<>();
        // Prendo istanza del DB
        this.Database = FirebaseDatabase.getInstance();
        // Referenzio il DB
        this.mDatabase = Database.getReferenceFromUrl("https://togethernet-8221b.firebaseio.com/");
        ApsRef = Database.getReference("XY");
        geoFire = new GeoFire(ApsRef);
    }

    public void SearchLocations(final LatLng XY, double range, Context context) {
        FirebaseApp.initializeApp(context);
        final ProgressDialog Dialog = new ProgressDialog(context);
        Dialog.setMessage("Caricamento Dati");
        Dialog.show();
        final GlobalApp globalApp = ( GlobalApp ) context.getApplicationContext();


        if (geoQuery == null) {
            geoQuery = geoFire.queryAtLocation(new GeoLocation(XY.latitude, XY.longitude), range);
            double test = geoQuery.getRadius();
            geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                @Override
                public void onKeyEntered(String key, GeoLocation location) {
                    System.out.println(String.format("Key %s entered the search area at [%f,%f]", key, location.latitude, location.longitude));
                    Location = new HashMap<>();
                    Location.put("Name", key);
                    Location.put("Latitude", Double.toString(location.latitude));
                    Location.put("Longitude", Double.toString(location.longitude));
                    Location.put("Distance", Float.toString(LocationUtils.getDistance(XY,new LatLng(location.latitude,location.longitude))));
                    LocationList.add(Location);
                }

                @Override
                public void onKeyExited(String key) {
                    System.out.println(String.format("Key %s is no longer in the search area", key));
                }

                @Override
                public void onKeyMoved(String key, GeoLocation location) {
                    System.out.println(String.format("Key %s moved within the search area to [%f,%f]", key, location.latitude, location.longitude));
                }

                @Override
                public void onGeoQueryReady() {
                    System.out.println("All initial data has been loaded and events have been fired!");
                    globalApp.getLocationNetsList().GlobalNetsLocation.SetLocations(LocationList);
                    Dialog.hide();
                }

                @Override
                public void onGeoQueryError(DatabaseError error) {
                    System.err.println("There was an error with this query: " + error);
                    Dialog.hide();
                }
            });
        }else{
            geoQuery.setLocation(new GeoLocation(XY.latitude, XY.longitude), range);
        }
    }

    public ArrayList<HashMap<String,String>> getLocationList(){
        return this.LocationList;
    }

}
