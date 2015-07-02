package org.cmucreatelab.tasota.airprototype.helpers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.services.EsdrRefreshService;

/**
 * Created by mike on 7/1/15.
 */
public class ServicesHandler {

    private Context appContext;
    private static ServicesHandler classInstance;
    private GlobalHandler globalHandler;


    // Nobody accesses the constructor
    private ServicesHandler(Context ctx, GlobalHandler globalHandler) {
        this.appContext = ctx;
        this.globalHandler = globalHandler;
    }


    // Only way to get instance of class (synchronized means thread-safe)
    // NOT PUBLIC: for public access, use GlobalHandler
    protected static synchronized ServicesHandler getInstance(Context ctx, GlobalHandler globalHandler) {
        if (classInstance == null) {
            classInstance = new ServicesHandler(ctx,globalHandler);
        }
        return classInstance;
    }


    protected void initializeBackgroundServices() {
        // only start EsdrRefreshService if the user was logged in
        if (globalHandler.settingsHandler.userLoggedIn) {
            startEsdrRefreshService();
        } else {
            stopEsdrRefreshService();
        }
    }


    // perform location updates periodically
    protected void startLocationService() {
        if (globalHandler.googleApiClientHandler.isClientConnected()) {
            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setInterval(Constants.Location.LOCATION_REQUEST_INTERVAL);
            locationRequest.setFastestInterval(Constants.Location.LOCATION_REQUEST_FASTEST_INTERVAL);
            locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    globalHandler.googleApiClientHandler.googleApiClient, locationRequest, globalHandler.googleApiClientHandler);
        } else {
            Log.e(Constants.LOG_TAG, "googleApiClientHandler client is not connected.");
        }
    }


    // stop periodic location updates
    protected void stopLocationService() {
        LocationServices.FusedLocationApi.removeLocationUpdates(globalHandler.googleApiClientHandler.googleApiClient, globalHandler.googleApiClientHandler);
    }


    public void startEsdrRefreshService() {
        Intent intent = new Intent(appContext, EsdrRefreshService.class);
        intent.putExtra("startService", true);
        appContext.startService(intent);
    }


    public void stopEsdrRefreshService() {
        Intent intent = new Intent(appContext, EsdrRefreshService.class);
        appContext.stopService(intent);
    }

}
