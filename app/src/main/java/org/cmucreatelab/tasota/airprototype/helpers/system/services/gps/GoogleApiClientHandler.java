package org.cmucreatelab.tasota.airprototype.helpers.system.services.gps;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import org.cmucreatelab.tasota.airprototype.helpers.application.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;

/**
 * Created by mike on 6/10/15.
 */
public class GoogleApiClientHandler
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {


    // Singleton Implementation


    private static GoogleApiClientHandler classInstance;
    protected GlobalHandler globalHandler;


    // Nobody accesses the constructor
    private GoogleApiClientHandler(GlobalHandler globalHandler) {
        this.globalHandler = globalHandler;
        this.googleApiClient = new GoogleApiClient.Builder(globalHandler.appContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    // Only public way to get instance of class (synchronized means thread-safe)
    public static synchronized GoogleApiClientHandler getInstance(GlobalHandler globalHandler) {
        if (classInstance == null) {
            classInstance = new GoogleApiClientHandler(globalHandler);
        }
        return classInstance;
    }


    // Handler attributes and methods


    private boolean clientConnected;
    public GoogleApiClient googleApiClient;


    public boolean isClientConnected() {
        return this.clientConnected;
    }


    public void connect() {
        Log.i(Constants.LOG_TAG, "connecting googleApiClient...");
        googleApiClient.connect();
    }


    public void disconnect() {
        Log.i(Constants.LOG_TAG, "discconnecting googleApiClient");
        googleApiClient.disconnect();
    }


    // Override Methods


    @Override
    public void onConnected(Bundle bundle) {
        Log.i(Constants.LOG_TAG, "...googleApiClient connected.");
        GoogleApiClientHandler.this.clientConnected = true;
        globalHandler.updateLastLocation();
        globalHandler.servicesHandler.startLocationService();
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
