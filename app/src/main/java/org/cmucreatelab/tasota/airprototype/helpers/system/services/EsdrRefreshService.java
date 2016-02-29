package org.cmucreatelab.tasota.airprototype.helpers.system.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.application.GlobalHandler;

import java.util.Date;

public class EsdrRefreshService extends Service {

    private boolean timerStarted=false;
    private BroadcastReceiver broadcastReceiver;
    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;


    private void destroyAlarm() {
        if (alarmManager != null && pendingIntent != null && broadcastReceiver != null) {
            Log.i(Constants.LOG_TAG,"destroying AlarmManager in EsdrRefreshService...");
            alarmManager.cancel(pendingIntent);
            this.unregisterReceiver(broadcastReceiver);
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final GlobalHandler globalHandler = GlobalHandler.getInstance(getApplicationContext());
        if (intent.hasExtra("startService") && globalHandler != null && globalHandler.settingsHandler != null && globalHandler.esdrLoginHandler.isUserLoggedIn()) {
            Log.d(Constants.LOG_TAG, "onStartCommand (handling intent)");

            // clear old alarms
            if (timerStarted) {
                destroyAlarm();
            }

            // create alarm manager/handler
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    // Tokens: check & refresh
                    GlobalHandler globalHandler = GlobalHandler.getInstance(getApplicationContext());
                    long timestamp = (long) (new Date().getTime() / 1000.0);
                    long expiresAt = globalHandler.esdrAccount.getExpiresAt();
                    String refreshToken = globalHandler.esdrAccount.getRefreshToken();
                    globalHandler.esdrAuthHandler.checkAndRefreshEsdrTokens(expiresAt, timestamp, refreshToken);
                }
            };
            this.registerReceiver(broadcastReceiver, new IntentFilter(Constants.EsdrRefreshIntent.ALARM_RECEIVER));
            pendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(Constants.EsdrRefreshIntent.ALARM_RECEIVER), 0);
            alarmManager = (AlarmManager) (this.getSystemService(Context.ALARM_SERVICE));
            alarmManager.setInexactRepeating(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(),
                    Constants.EsdrRefreshIntent.ALARM_INTERVAL_MILLISECONDS, pendingIntent
            );
            timerStarted = true;

            // Restart Behavior: the original Intent is re-delivered to the onStartCommand method.
            return Service.START_REDELIVER_INTENT;
        } else {
            destroyAlarm();
            return super.onStartCommand(intent, flags, startId);
        }
    }


    @Override
    public void onDestroy() {
        destroyAlarm();
        Log.i(Constants.LOG_TAG,"destroying instance of EsdrRefreshService");
        super.onDestroy();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
