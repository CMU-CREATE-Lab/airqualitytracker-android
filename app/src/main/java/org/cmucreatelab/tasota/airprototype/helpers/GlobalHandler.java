package org.cmucreatelab.tasota.airprototype.helpers;

import android.content.Context;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;
import com.google.android.gms.location.LocationServices;

import org.cmucreatelab.tasota.airprototype.activities.manage_trackers.TrackersAdapter;
import org.cmucreatelab.tasota.airprototype.activities.readable_list.StickyGridAdapter;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;

/**
 * Created by mike on 6/2/15.
 */
public class GlobalHandler {

    private static GlobalHandler classInstance;
    protected Context appContext;
    // managed global instances
    public HttpRequestHandler httpRequestHandler;
    public SettingsHandler settingsHandler;
    public ServicesHandler servicesHandler;
    // data structure
    public HeaderReadingsHashMap headerReadingsHashMap;
    // Keep track of ALL your array adapters for notifyGlobalDataSetChanged()
    public StickyGridAdapter gridAdapter;
    public TrackersAdapter trackersAdapter;


    // Nobody accesses the constructor
    private GlobalHandler(Context ctx) {
        // context and handlers
        this.appContext = ctx;
        this.settingsHandler = SettingsHandler.getInstance(this);
        this.servicesHandler = ServicesHandler.getInstance(this);
        this.httpRequestHandler = HttpRequestHandler.getInstance(this);
        // data structures
        this.headerReadingsHashMap = new HeaderReadingsHashMap(this);
        if (Constants.USES_BACKGROUND_SERVICES)
            servicesHandler.initializeBackgroundServices();
    }


    // This function provides a mechanism for notifying all (active) list adapters
    // in the app when the dataset gets updated.
    public void notifyGlobalDataSetChanged() {
        if (this.gridAdapter != null) {
            this.gridAdapter.notifyDataSetChanged();
        }
    }


    public void updateReadings() {
        headerReadingsHashMap.updateAddresses();
        headerReadingsHashMap.updateSpecks();
        if (settingsHandler.appUsesLocation()) {
            if (servicesHandler.googleApiClientHandler.googleApiClient.isConnected()) {
                updateLastLocation();
            } else {
                servicesHandler.googleApiClientHandler.connect();
            }
        }
    }


    public void updateLastLocation() {
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(servicesHandler.googleApiClientHandler.googleApiClient);
        if (lastLocation == null) {
            Log.w(Constants.LOG_TAG, "getLastLocation returned null.");
        } else {
            Log.d(Constants.LOG_TAG, "getLastLocation returned: " + lastLocation.toString());
            this.headerReadingsHashMap.setGpsAddressLocation(lastLocation);
            this.notifyGlobalDataSetChanged();
            if (Geocoder.isPresent()) {
                servicesHandler.startFetchAddressIntentService(lastLocation);
            } else {
                Log.e(Constants.LOG_TAG, "Tried starting FetchAddressIntentService but Geocoder is not present.");
            }
        }
    }


    public void updateSettings() {
        this.settingsHandler.updateSettings();
        headerReadingsHashMap.refreshHash();
        this.notifyGlobalDataSetChanged();
    }


    // Only public way to get instance of class (synchronized means thread-safe)
    public static synchronized GlobalHandler getInstance(Context ctx) {
        if (classInstance == null) {
            classInstance = new GlobalHandler(ctx);
        }
        return classInstance;
    }

}
