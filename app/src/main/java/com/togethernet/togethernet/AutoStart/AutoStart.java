package com.togethernet.togethernet.AutoStart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

import com.togethernet.togethernet.WifiJumper.TogetherNetsRemover.NetRemover;

import static com.togethernet.togethernet.NotificationSystem.ConnectionNotificationHandler.buildConnectionEventNotificationProAdv;

/**
 * Created by pietr_000 on 19/11/2017.
 */

public class AutoStart extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
        {
            NetRemoverReceiver.SetAlarm(context);
        }
    }
}