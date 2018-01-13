package com.togethernet.togethernet;

import android.*;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.togethernet.togethernet.GlobalApp.GlobalApp;
import com.togethernet.togethernet.WifiJumper.WifiJumperAlarm;
import com.togethernet.togethernet.WifiJumper.utilities.gpsUtilities;
import com.togethernet.togethernet.WifiJumper.utilities.wifiUtilities;


public class LoadingActivity extends AppCompatActivity {

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(45.159476, 10.78984);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;
    private static final String KEY_LOCATION = "location";
    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private GlobalApp globalApp;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    private static final int PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION = 1001;

    private final Handler mHideHandler = new Handler();
    private View mContentView;

    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            ChangeActivs();
        }
    };
    private final Runnable mLoader = new Runnable() {
        @Override
        public void run() {
          delayer(2000);
        }
    };
    private final Runnable mGetLocations = new Runnable() {
        @Override
        public void run() {
            getLocations();
            goToMain(2000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
        }
        //Rimuovo Titolo
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Setto FullScreen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_loading);

        mVisible = true;

        mContentView = findViewById(R.id.fullscreen_content);


        //Carico Le reti Vicine e faccio il primo scan
        //Init GlobalBlackList
        globalApp = ( GlobalApp ) this.getApplication();
        globalApp.resetRequestedFunctions();
        globalApp.initGlobalBlackList(this);
        //GetBlackList
        globalApp.getBlackList();
        //globalApp.GlobalBlackListAddNet("TEST", "TEST");
        //Inserisco firebase
        FirebaseApp.initializeApp(this);
        //Chiedo permessi per localizzazione -> se no wifi ciao
        ActivityCompat.requestPermissions(this , new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION} , PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION);
        //Controllo che il gps sia acceso se no lo chiedo all'utente
        gpsUtilities gpsUtilities = new gpsUtilities();
        gpsUtilities.statusCheck(this);
        //Controllo che sia acceso il wifi se no lo accendo
        wifiUtilities.WifiStateControl(this);
        //Avvio il jobscheduler
        WifiJumperAlarm jumper = new WifiJumperAlarm();
        jumper.setAlarm(this);
        //Setto la lista globale di reti vicine
        globalApp.setGlobalNetsLocationList();
        getLocationPermission();
        getDeviceLocation();

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //getValues(2000);
        //Delayer -> aspetto che siano caricate le impostazioni poi faccio partire i runnable
        delayer(2000);
        //goToMain(3000);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */

    private  void goToMain(int delayMillis){
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    private  void getValues(int delayMillis){
        mHideHandler.removeCallbacks(mGetLocations);
        mHideHandler.postDelayed(mGetLocations, delayMillis);
    }

    private void delayer(int delayMillis){
        if (!this.globalApp._GPS_REQUESTED || !this.globalApp._WIFI_REQUESTED){
            mHideHandler.removeCallbacks(mLoader);
            mHideHandler.postDelayed(mLoader, delayMillis);
        }else{
            getValues(2000);
        }
    }

    private void getLocations(){
        globalApp.getLocationNetsList().GlobalNetsLocation.dataHandler.SearchLocations(new LatLng(this.mLastKnownLocation.getLatitude(), this.mLastKnownLocation.getLongitude()),15000,this);
    }

    private void ChangeActivs(){
            startActivity(new Intent(LoadingActivity.this, MainActivity.class));
            finish();
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        mFusedLocationProviderClient = new FusedLocationProviderClient(this);
        try {
            if (mLocationPermissionGranted) {
                final Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null ) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = new Location(task.getResult());
                        } else {
                            Location tmpLocation = new Location("Default");
                            tmpLocation.setLatitude(mDefaultLocation.latitude);
                            tmpLocation.setLongitude(mDefaultLocation.longitude);
                            mLastKnownLocation = tmpLocation;

                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }


}
