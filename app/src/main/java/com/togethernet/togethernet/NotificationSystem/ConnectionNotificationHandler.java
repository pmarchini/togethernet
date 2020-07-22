package com.togethernet.togethernet.NotificationSystem;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.togethernet.togethernet.GlobalApp.GlobalApp;
import com.togethernet.togethernet.R;

/**
 * Created by pietr_000 on 06/11/2017.
 */

//!IMPORTANTE -> Canale 001 -> Pubblicità connessione attuale
    //        -> Canale 002 -> Notifica di una connessione migliore

public class ConnectionNotificationHandler {

    public static void buildConnectionEventNotification(Context context, String SSID){
        //
        //Get an instance of NotificationManager//
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_notification_logo)
                        .setContentTitle("TogetherNet : \"" + SSID + "\"")
                        .setContentText("Sei connesso ad una rete TogetherNet");

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //Utilizzo un canale solo in modo sovrascrivere le notifiche
        mNotificationManager.notify(001, mBuilder.build());
    }

    public static void buildConnectionEventNotificationProAdv(Context context, String SSID){
        String PubblicityBigText;
        //
        Bitmap bm = BitmapFactory.decodeResource(context.getResources() , R.drawable.background);
        //Get an instance of NotificationManager//
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_notification_logo)
                        .setContentTitle("TogetherNet : \"" + SSID + "\"")
                        .setContentText("Sei connesso ad una rete TogetherNet")
                        .setStyle(new NotificationCompat.BigPictureStyle()
                                .setBigContentTitle("Pubblicità dimostrativa")
                                .setSummaryText("Scopri Ulteriori dettagli!")
                                .bigPicture(bm));

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //Utilizzo un canale solo in modo sovrascrivere le notifiche
        mNotificationManager.notify(001, mBuilder.build());
    }

    //Notifica di avviso connessione disponibile
    public static void buildBestAvConnectionNotification(Context context, String SSID){
        //
        //Get an instance of NotificationManager//
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_notification_logo)
                        .setContentTitle("TogetherNet : " + SSID)
                        .setContentText("Una rete migliore è disponibile");

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //Utilizzo un canale solo in modo sovrascrivere le notifiche
        mNotificationManager.notify(002, mBuilder.build());
    }

    public static void buildAvConnectionNotification(Context context){
        //
        //Get an instance of NotificationManager//
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_notification_logo)
                        .setContentTitle("TogetherNet")
                        .setContentText("Una rete è disponibile, connettiti subito");

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //Utilizzo un canale solo in modo sovrascrivere le notifiche
        mNotificationManager.notify(002, mBuilder.build());
    }

    public static void clearNotification(Context context, Integer IdCanale){
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //Pulisco notifiche
        mNotificationManager.cancel(IdCanale);

    }
}
