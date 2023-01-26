package com.alarmmanager.diff;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(this.getClass().getName(), "Alert received successfully " + intent.getStringExtra("DATE"));
    }
}