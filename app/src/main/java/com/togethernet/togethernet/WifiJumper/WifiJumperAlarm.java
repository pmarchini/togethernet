package com.togethernet.togethernet.WifiJumper;

/**
 * Created by k1008014 on 30/10/2017.
 */

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.widget.Toast;


import com.togethernet.togethernet.WifiJumper.TogetherNetsRemover.NetRemover;

public class WifiJumperAlarm extends BroadcastReceiver
{
    public static int SCAN_DELAY_TIME = 1000 * 10; //in millisecondi (STD == 10)


    @Override
    public void onReceive(Context context, Intent intent)
    {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();

        WifiManager.WifiLock wifiLock = wm.createWifiLock(WifiManager.WIFI_MODE_FULL, "WIFIJUMPER");
        wifiLock.setReferenceCounted(true);

        Intent i = new Intent(context, WifiJumperAlarm.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);

        if (wifiLock != null) { // May be null if wm is null
            wifiLock.acquire();
            //Controllo ed elimino eventuali reti residue
            NetRemover.removeNets(context);
            //Instanzio WifiScan
            WifiScan scanner = new WifiScan();
            //Scan delle reti presenti
            scanner.ScanNets(context);
        }

        //Mostro un toast se l'app è visibile
        Toast.makeText(context, "Scanning!!!!", Toast.LENGTH_LONG).show();

        //Rilascio i locks
        wifiLock.release();
        wl.release();

        //Rischedulo lo scan tra X secondi
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+ SCAN_DELAY_TIME , pi);

    }

    public void setAlarm(Context context)
    {
        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, WifiJumperAlarm.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);

        //am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 10 , pi); // Millisec * Second * Minute
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+ 50, pi);

        Toast.makeText(context, "Allarme settato", Toast.LENGTH_LONG).show(); // For example
    }

    public void cancelAlarm(Context context)
    {
        Intent intent = new Intent(context, WifiJumperAlarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

}