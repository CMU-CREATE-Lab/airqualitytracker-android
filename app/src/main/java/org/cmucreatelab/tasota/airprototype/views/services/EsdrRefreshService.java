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
import org.cmucreatelab.tasota.airprototype.helpers.GlobalHandler;

public class EsdrRefreshService extends Service {

    private boolean timerStarted=false;
    private ResultReceiver resultReceiver;
    private BroadcastReceiver broadcastReceiver;
    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;


    private void destroyAlarm() {
        if (alarmManager != null && pendingIntent != null && broadcastReceiver != null) {
            alarmManager.cancel(pendingIntent);
            this.unregisterReceiver(broadcastReceiver);
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.hasExtra("startService")) {
            Log.d(Constants.LOG_TAG, "onStartCommand (handling intent)");

            // clear old alarms
            if (timerStarted) {
                destroyAlarm();
            }

            // handle intent
            resultReceiver = intent.getParcelableExtra(Constants.EsdrRefreshIntent.RECEIVER);

            // create alarm manager/handler
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    // TODO just testing
                    String refreshToken = String.valueOf(Math.random());
                    GlobalHandler.getInstance(getApplicationContext()).settingsHandler.refreshToken = refreshToken;
                    Log.d(Constants.LOG_TAG, "EsdrRefreshService onHandleIntent: refreshToken=" + refreshToken);
                    Toast.makeText(EsdrRefreshService.this, "refreshToken is "+refreshToken, Toast.LENGTH_SHORT).show();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("foo", "bars");
//                    resultReceiver.send(0,bundle);
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
            return super.onStartCommand(intent, flags, startId);
        }
    }


    @Override
    public void onDestroy() {
        destroyAlarm();
        super.onDestroy();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
