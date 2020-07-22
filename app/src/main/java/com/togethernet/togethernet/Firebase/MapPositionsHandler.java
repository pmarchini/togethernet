package com.togethernet.togethernet.Firebase;

import android.content.Context;
import android.content.Loader;
import android.graphics.Point;
import android.util.Log;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.togethernet.togethernet.LocationUtilites.LocationUtils;
import com.togethernet.togethernet.MapsActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by k1008014 on 19/12/2017.
 */

public class MapPositionsHandler {

    private FirebaseDatabase Database;
    private DatabaseReference mDatabase;
    private DatabaseReference ApsRef;
    private GeoFire geoFire;
    private GeoQuery geoQuery;
    public int counter;


    public MapPositionsHandler() {
        // Prendo istanza del DB
        this.Database = FirebaseDatabase.getInstance();
        // Referenzio il DB
        this.mDatabase = Database.getReferenceFromUrl("https://togethernet-8221b.firebaseio.com/");
        ApsRef = Database.getReference("XY");
        geoFire = new GeoFire(ApsRef);
    }

    //Se GeoQuery non è ancora istanziata istanzio e avvio i listener
    //In caso contrario aggiorno il centro e il range[radius]
    public void addMarkersByPositions(final GoogleMap map, LatLng XY, double range , final ArrayList<Marker> array) {
        if (geoQuery == null) {
            this.geoQuery = geoFire.queryAtLocation(new GeoLocation(XY.latitude, XY.longitude), range);
            double test = geoQuery.getRadius();
            geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                @Override
                public void onKeyEntered(String key, GeoLocation location) {
                    System.out.println(String.format("Key %s entered the search area at [%f,%f]", key, location.latitude, location.longitude));
                    array.add(map.addMarker(new MarkerOptions()
                            .position(new LatLng(location.latitude, location.longitude))
                            .title(key)));
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
                }

                @Override
                public void onGeoQueryError(DatabaseError error) {
                    System.err.println("There was an error with this query: " + error);
                }
            });
        }else{
            geoQuery.setLocation(new GeoLocation(XY.latitude, XY.longitude), range);
        }
    }
    //Aggiungi Location -> "Tabella" XY
    public void addGeoFireLocation(String Title, LatLng XY){
        final DatabaseReference ApsRef = Database.getReference("XY");
        GeoFire geoFire = new GeoFire(ApsRef);
        geoFire.setLocation(Title, new GeoLocation(XY.latitude, XY.longitude));
    }


    //Ricerca a raggi concentrici incrementali
    //seleziona la rete più vicina nel raggio massimo di 1000km attualmente
    //TODO -> Decidere una distanza ragionevole
    public void getGeoFirePositionsIncrementale(final GoogleMap map, final LatLng XY, double range , final ArrayList<Marker> array, final MapsActivity activity) {

        final GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(XY.latitude, XY.longitude), range);
        double test = geoQuery.getRadius();
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                System.out.println(String.format("Key %s entered the search area at [%f,%f]", key, location.latitude, location.longitude));
                array.add(map.addMarker(new MarkerOptions()
                        .position(new LatLng(location.latitude, location.longitude))
                        .title(key)));
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
                if(array.isEmpty()){
                    if (geoQuery.getRadius() < 1000) {
                        geoQuery.setRadius(geoQuery.getRadius() + 2.5);
                    }else{
                        geoQuery.removeAllListeners();
                    }
                }else{
                    //Ho ricevuto delle reti vicine, mi muovo in quella direzione e blocco l'eventListener
                    geoQuery.removeAllListeners();
                    activity.moveCameraToNearest(array, XY.latitude, XY.longitude);
                }
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                System.err.println("There was an error with this query: " + error);
            }
        });
    }
}
