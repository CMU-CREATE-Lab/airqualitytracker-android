package org.cmucreatelab.tasota.airprototype.helpers;

import android.content.Intent;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import org.cmucreatelab.tasota.airprototype.views.services.AddressResultReceiver;
import org.cmucreatelab.tasota.airprototype.views.services.FetchAddressIntentService;

/**
 * Created by mike on 6/10/15.
 */
public class GoogleApiClientHandler implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

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


    public void updateLastLocation() {
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (lastLocation == null) {
            Log.w(Constants.LOG_TAG, "getLastLocation returned null.");
        } else {
            Log.d(Constants.LOG_TAG, "getLastLocation returned: " + lastLocation.toString());

            this.globalHandler.addressFeedsHashMap.setGpsAddressLocation(lastLocation);
            this.globalHandler.notifyGlobalDataSetChanged();
            // TODO consider this when more than one update can occur.
            if (Geocoder.isPresent()) {
                Intent intent = new Intent(globalHandler.appContext, FetchAddressIntentService.class);
                AddressResultReceiver resultReceiver = new AddressResultReceiver(new Handler(),globalHandler);

                intent.putExtra(Constants.AddressIntent.RECEIVER, resultReceiver);
                intent.putExtra("latitude",lastLocation.getLatitude());
                intent.putExtra("longitude",lastLocation.getLongitude());
                globalHandler.appContext.startService(intent);
            } else {
                Log.e(Constants.LOG_TAG, "Tried starting FetchAddressIntentService but Geocoder is not present.");
            }
        }
    }


    @Override
    public void onConnected(Bundle bundle) {
        Log.i(Constants.LOG_TAG, "...googleApiClient connected.");
        GoogleApiClientHandler.this.clientConnected = true;
        updateLastLocation();
        globalHandler.locationUpdateHandler.startLocationUpdates();
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

}
