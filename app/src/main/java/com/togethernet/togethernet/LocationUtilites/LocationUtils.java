package com.togethernet.togethernet.LocationUtilites;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by pietr_000 on 09/01/2018.
 */

public class LocationUtils {
    //Return distance in KMs
    public static float getDistance(LatLng CurrentPosition, LatLng TargetPosition){
        Location loc1 = new Location("");
        loc1.setLatitude(CurrentPosition.latitude);
        loc1.setLongitude(CurrentPosition.longitude);

        Location loc2 = new Location("");
        loc2.setLatitude(TargetPosition.latitude);
        loc2.setLongitude(TargetPosition.longitude);

        return loc1.distanceTo(loc2);
    }
}
