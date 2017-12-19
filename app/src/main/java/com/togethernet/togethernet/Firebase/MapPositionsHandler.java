package com.togethernet.togethernet.Firebase;

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

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by k1008014 on 19/12/2017.
 */

public class MapPositionsHandler {

    private FirebaseDatabase Database;
    private DatabaseReference mDatabase;
    public int counter;


    public MapPositionsHandler() {
        // Prendo istanza del DB
        this.Database = FirebaseDatabase.getInstance();
        // Referenzio il DB
        this.mDatabase = Database.getReferenceFromUrl("https://togethernet-8221b.firebaseio.com/");
    }

    public void addMarkersByPositions(final GoogleMap map, LatLng XY, double range , final ArrayList<Marker> array) {
        final DatabaseReference ApsRef = Database.getReference("XY");
        GeoFire geoFire = new GeoFire(ApsRef);
        /*geoFire.setLocation("La drogheria TogetherNet", new GeoLocation(45.158404, 10.795369));
        geoFire.setLocation("Caffè Modì TogetherNet", new GeoLocation(45.161540, 10.798899));
        geoFire.setLocation("Coda di Cavallo TogetherNet", new GeoLocation(45.157042, 10.791001));
        geoFire.setLocation("Robi Fini TogetherNet", new GeoLocation(45.157117, 10.791301));
        geoFire.setLocation("Malaspina TogetherNet", new GeoLocation(45.159476, 10.789840));
        geoFire.setLocation("Er Cozza TogetherNet", new GeoLocation(44.485909, 11.355065));*/

        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(XY.latitude, XY.longitude), range);
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
    }
}
