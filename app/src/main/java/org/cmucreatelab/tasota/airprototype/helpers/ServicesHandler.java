package org.cmucreatelab.tasota.airprototype.helpers;

import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.util.Log;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.services.AddressResultReceiver;
import org.cmucreatelab.tasota.airprototype.services.EsdrRefreshService;
import org.cmucreatelab.tasota.airprototype.services.FetchAddressIntentService;

/**
 * Created by mike on 7/1/15.
 */
public class ServicesHandler {

    private static ServicesHandler classInstance;
    private GlobalHandler globalHandler;


    // Nobody accesses the constructor
    private ServicesHandler(GlobalHandler globalHandler) {
        this.globalHandler = globalHandler;
    }


    // Only way to get instance of class (synchronized means thread-safe)
    // NOT PUBLIC: for public access, use GlobalHandler
    protected static synchronized ServicesHandler getInstance(GlobalHandler globalHandler) {
        if (classInstance == null) {
            classInstance = new ServicesHandler(globalHandler);
        }
        return classInstance;
    }


    protected void initializeBackgroundServices() {
        // only start EsdrRefreshService if the user was logged in
        if (globalHandler.settingsHandler.isUserLoggedIn()) {
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


    protected void startFetchAddressIntentService(Location location) {
        Intent intent = new Intent(globalHandler.appContext, FetchAddressIntentService.class);
        AddressResultReceiver resultReceiver = new AddressResultReceiver(new Handler(),globalHandler);

        intent.putExtra(Constants.AddressIntent.RECEIVER, resultReceiver);
        intent.putExtra("latitude",location.getLatitude());
        intent.putExtra("longitude",location.getLongitude());
        globalHandler.appContext.startService(intent);
    }


    public void startEsdrRefreshService() {
        Intent intent = new Intent(globalHandler.appContext, EsdrRefreshService.class);
        intent.putExtra("startService", true);
        globalHandler.appContext.startService(intent);
    }


    public void stopEsdrRefreshService() {
        Intent intent = new Intent(globalHandler.appContext, EsdrRefreshService.class);
        globalHandler.appContext.stopService(intent);
    }

}
