package com.togethernet.togethernet.WifiJumper.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;

/**
 * Created by pietr_000 on 02/11/2017.
 */

public class gpsUtilities {
    //Controllo i permessi riguardo la localizzazione
    public void statusCheck( Activity Act) {
        final LocationManager manager = (LocationManager) Act.getSystemService(Context.LOCATION_SERVICE);
        //Se ho i permessi ma il gps non è attivo
        //Chiedo all'utente di accendere il gps per me
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps(Act);
        }
    }

    //Allert -> richiesta di accensione del GPS
    public void buildAlertMessageNoGps(final Activity Act) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(Act);
        builder.setMessage("Il tuo GPS sembrerebbe spento, sfortunatamente questo preclude alcune funzionalità, vuoi attivarlo?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        Act.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
