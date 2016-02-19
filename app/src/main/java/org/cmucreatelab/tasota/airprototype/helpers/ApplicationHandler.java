package org.cmucreatelab.tasota.airprototype.helpers;

import android.app.Application;
import android.util.Log;

import org.cmucreatelab.tasota.airprototype.activities.SessionExpiredDialog;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;

import java.util.Date;

/**
 * Created by mike on 2/18/16.
 */
public class ApplicationHandler extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(Constants.LOG_TAG, "==== Application Launches! ====");
        GlobalHandler globalHandler = GlobalHandler.getInstance(getApplicationContext());
        if (globalHandler.esdrLoginHandler.isUserLoggedIn()) {
            // Tokens: check & refresh
            long timestamp = (long) (new Date().getTime() / 1000.0);
            long expiresAt = globalHandler.esdrAccount.getExpiresAt();
            String refreshToken = globalHandler.esdrAccount.getRefreshToken();
            boolean updatingTokens = globalHandler.esdrAuthHandler.checkAndRefreshEsdrTokens(expiresAt, timestamp, refreshToken);
            if (!updatingTokens) {
                globalHandler.displaySessionExpiredDialog = true;
            }
        }
    }

}
