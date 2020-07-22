package com.togethernet.togethernet.WifiJumper.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.togethernet.togethernet.GlobalApp.GlobalApp;

/**
 * Created by pietr_000 on 02/11/2017.
 */

public class gpsUtilities {
    //Controllo i permessi riguardo la localizzazione
    public void statusCheck( Activity Act) {
        final GlobalApp globalApp = ( GlobalApp ) Act.getApplication();
        final LocationManager manager = (LocationManager) Act.getSystemService(Context.LOCATION_SERVICE);
        //Se ho i permessi ma il gps non è attivo
        //Chiedo all'utente di accendere il gps per me
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps(Act);
        }else{
            globalApp._GPS_REQUESTED = true;
        }
    }

    //Allert -> richiesta di accensione del GPS
    public void buildAlertMessageNoGps(final Activity Act) {
        final GlobalApp globalApp = ( GlobalApp ) Act.getApplication();
        final AlertDialog.Builder builder = new AlertDialog.Builder(Act);
        builder.setMessage("Il tuo GPS sembrerebbe spento, sfortunatamente questo preclude alcune funzionalità, vuoi attivarlo?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        Act.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        globalApp._GPS_REQUESTED = true;
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                        globalApp._GPS_REQUESTED = true;
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

}
