package com.togethernet.togethernet.AutoStart;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

/**
 * Created by pietr_000 on 19/11/2017.
 */
//Eseguito al boot
@SuppressLint("Wakelock")
public class NetRemoverReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();

        //Cancello le reti togetherNet direttamente in accesione
        com.togethernet.togethernet.WifiJumper.TogetherNetsRemover.NetRemover.removeNetsOnBoot(context);

        wl.release();
    }

    public static void SetAlarm(Context context)
    {
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, NetRemoverReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+ 500 , pi);

    }

    public void CancelAlarm(Context context)
    {
        Intent intent = new Intent(context, NetRemoverReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}