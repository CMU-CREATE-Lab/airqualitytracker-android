package org.cmucreatelab.tasota.airprototype.helpers;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * Created by mike on 6/10/15.
 */
public class GoogleApiClientHandler implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private Context appContext;
    private boolean clientConnected;
    private static GoogleApiClientHandler classInstance;
    protected GoogleApiClient googleApiClient;
    protected GlobalHandler globalHandler;
    protected boolean isClientConnected() {
        return this.clientConnected;
    }


    // Nobody accesses the constructor
    private GoogleApiClientHandler(Context ctx, GlobalHandler globalHandler) {
        this.appContext = ctx;
        this.globalHandler = globalHandler;
        this.googleApiClient = new GoogleApiClient.Builder(ctx)
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
    protected static synchronized GoogleApiClientHandler getInstance(Context ctx, GlobalHandler globalHandler) {
        if (classInstance == null) {
            classInstance = new GoogleApiClientHandler(ctx,globalHandler);
        }
        return classInstance;
    }


    @Override
    public void onConnected(Bundle bundle) {
        Log.i(Constants.LOG_TAG, "...googleApiClient connected.");
        GoogleApiClientHandler.this.clientConnected = true;
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (lastLocation == null) {
            Log.w(Constants.LOG_TAG, "getLastLocation returned null.");
        } else {
            Log.d(Constants.LOG_TAG, "getLastLocation returned: " + lastLocation.toString());
            GoogleApiClientHandler.this.globalHandler.updateCurrentLocation(lastLocation);
        }
    }


    @Override
    public void onConnectionSuspended(int i) {
        Log.w(Constants.LOG_TAG, "googleApiClient onConnectionSuspended");
        // TODO handle suspended connection
        GoogleApiClientHandler.this.clientConnected = false;
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.w(Constants.LOG_TAG, "googleApiClient onConnectionFailed");
        // TODO handle failed connection
        GoogleApiClientHandler.this.clientConnected = false;
    }

}
