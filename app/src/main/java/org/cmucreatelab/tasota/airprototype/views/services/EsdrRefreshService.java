package org.cmucreatelab.tasota.airprototype.views.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;
import org.cmucreatelab.tasota.airprototype.helpers.Constants;

public class EsdrRefreshService extends Service {

    private boolean timerStarted=false;
    protected ResultReceiver resultReceiver;
    BroadcastReceiver broadcastReceiver;
    PendingIntent pendingIntent;
    AlarmManager alarmManager;


    private void destroyAlarm() {
        alarmManager.cancel(pendingIntent);
        this.unregisterReceiver(broadcastReceiver);
    }


    public EsdrRefreshService() {
        super();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.hasExtra("startService")) {
            Log.d(Constants.LOG_TAG, "onStartCommand (handling intent)");
            final String username = intent.getStringExtra("username");
            resultReceiver = intent.getParcelableExtra(Constants.EsdrRefreshIntent.RECEIVER);
            if (timerStarted) {
                destroyAlarm();
            }

            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Log.d(Constants.LOG_TAG, "EsdrRefreshService onHandleIntent: username=" + username);
                    Toast.makeText(EsdrRefreshService.this, "username is "+username, Toast.LENGTH_SHORT).show();
                    Bundle bundle = new Bundle();
                    bundle.putString("foo", "bars");
                    resultReceiver.send(0,bundle);
                }
            };
            this.registerReceiver(broadcastReceiver, new IntentFilter(Constants.EsdrRefreshIntent.ALARM_RECEIVER));
            pendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(Constants.EsdrRefreshIntent.ALARM_RECEIVER), 0);
            alarmManager = (AlarmManager) (this.getSystemService(Context.ALARM_SERVICE));
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 4000, pendingIntent);
            timerStarted = true;

            // Restart Behavior: the original Intent is re-delivered to the onStartCommand method.
            return Service.START_REDELIVER_INTENT;
        } else {
            return super.onStartCommand(intent, flags, startId);
        }
    }


    @Override
    public void onDestroy() {
        Log.d(Constants.LOG_TAG, "DESTROYING EsdrRefreshService");
        destroyAlarm();
        super.onDestroy();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
