package com.togethernet.togethernet.LocationDB;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.togethernet.togethernet.WifiJumper.AvNetsList.AvNetsList;

/**
 * Created by k1008014 on 12/01/2018.
 */

public class netsLocationGlobalList {

        public netsLocationList GlobalNetsLocation;

        public netsLocationGlobalList(){
            //Istanzio la rete Gl
            this.GlobalNetsLocation = new netsLocationList();
        }
}

