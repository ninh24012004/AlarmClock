package com.example.alarmclock;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;
import java.util.List;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isRunning = false;
        String string = intent.getExtras().getString("extra");

        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (AlarmNotificationService.class.getName().equals(service.service.getClassName())) {
                isRunning = true;
            }
        }

        Intent mIntent = new Intent(context, AlarmNotificationService.class);
        if (string.equals("on") && !isRunning) {
            context.startService(mIntent);
            MainActivity.activeAlarm = intent.getExtras().getString("active");
        } else if (string.equals("off")) {
            context.stopService(mIntent);
            MainActivity.activeAlarm = "";
        }
    }
}
