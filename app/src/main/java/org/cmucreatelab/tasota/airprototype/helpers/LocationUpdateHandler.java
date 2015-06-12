package org.cmucreatelab.tasota.airprototype.helpers;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by mike on 6/10/15.
 */
public class LocationUpdateHandler implements com.google.android.gms.location.LocationListener {

    private GoogleApiClientHandler googleApiClientHandler;
    private Context appContext;
    private static LocationUpdateHandler classInstance;


    private LocationUpdateHandler(Context ctx, GoogleApiClientHandler googleApiClientHandler) {
        this.appContext = ctx;
        this.googleApiClientHandler = googleApiClientHandler;
    }


    // perform location updates periodically
    protected void startLocationUpdates() {
        if (googleApiClientHandler.isClientConnected()) {
            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setInterval(10000);
            locationRequest.setFastestInterval(5000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    googleApiClientHandler.googleApiClient, locationRequest, this);
        } else {
            Log.e(Constants.LOG_TAG, "googleApiClientHandler client is not connected.");
        }
    }


    // stop periodic location updates
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClientHandler.googleApiClient, this);
    }


    // Only public way to get instance of class (synchronized means thread-safe)
    // NOT PUBLIC: for public access, use GlobalHandler
    protected static synchronized LocationUpdateHandler getInstance(Context ctx, GoogleApiClientHandler googleApiClientHandler) {
        if (classInstance == null) {
            classInstance = new LocationUpdateHandler(ctx,googleApiClientHandler);
        }
        return classInstance;
    }


    @Override
    public void onLocationChanged(Location location) {
        if (location == null) {
            Log.e(Constants.LOG_TAG, "onLocationChanged received null location.");
        } else {
            Log.d(Constants.LOG_TAG, "onLocationChanged received: " + location.toString());
            googleApiClientHandler.globalHandler.updateCurrentLocation(location);
        }
    }

}
