package org.cmucreatelab.tasota.airprototype.helpers;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by mike on 6/10/15.
 */
public class GoogleApiClientHandler
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {

    private boolean clientConnected;
    private static GoogleApiClientHandler classInstance;
    protected GoogleApiClient googleApiClient;
    protected GlobalHandler globalHandler;
    protected boolean isClientConnected() {
        return this.clientConnected;
    }


    // Nobody accesses the constructor
    private GoogleApiClientHandler(GlobalHandler globalHandler) {
        this.globalHandler = globalHandler;
        this.googleApiClient = new GoogleApiClient.Builder(globalHandler.appContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        // make sure you actually CONNECT the api client for it to do anything (so much hatred)
        Log.i(Constants.LOG_TAG, "connecting googleApiClient...");
        googleApiClient.connect();
    }


    // Only public way to get instance of class (synchronized means thread-safe)
    // NOT PUBLIC: for public access, use GlobalHandler
    protected static synchronized GoogleApiClientHandler getInstance(GlobalHandler globalHandler) {
        if (classInstance == null) {
            classInstance = new GoogleApiClientHandler(globalHandler);
        }
        return classInstance;
    }


    // perform location updates periodically
    protected void startLocationUpdates() {
        if (this.isClientConnected()) {
            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setInterval(Constants.Location.LOCATION_REQUEST_INTERVAL);
            locationRequest.setFastestInterval(Constants.Location.LOCATION_REQUEST_FASTEST_INTERVAL);
            locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    this.googleApiClient, locationRequest, this);
        } else {
            Log.e(Constants.LOG_TAG, "googleApiClientHandler client is not connected.");
        }
    }


    // stop periodic location updates
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(this.googleApiClient, this);
    }


    @Override
    public void onConnected(Bundle bundle) {
        Log.i(Constants.LOG_TAG, "...googleApiClient connected.");
        GoogleApiClientHandler.this.clientConnected = true;
        globalHandler.updateLastLocation();
        this.startLocationUpdates();
    }


    @Override
    public void onConnectionSuspended(int i) {
        Log.w(Constants.LOG_TAG, "googleApiClient onConnectionSuspended");
        GoogleApiClientHandler.this.clientConnected = false;
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.w(Constants.LOG_TAG, "googleApiClient onConnectionFailed");
        GoogleApiClientHandler.this.clientConnected = false;
    }


    @Override
    public void onLocationChanged(Location location) {
        // We don't want to update the current location here. This method simply confirms
        // that the location was recently changed (because a new one was requested).
        if (location == null) {
            Log.e(Constants.LOG_TAG, "onLocationChanged received null location.");
        } else {
            Log.d(Constants.LOG_TAG, "onLocationChanged received: " + location.toString());
        }
    }

}
