package org.cmucreatelab.tasota.airprototype.helpers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.location.Location;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import org.cmucreatelab.tasota.airprototype.classes.SimpleAddress;
import org.cmucreatelab.tasota.airprototype.classes.Feed;
import org.cmucreatelab.tasota.airprototype.views.services.AddressResultReceiver;
import org.cmucreatelab.tasota.airprototype.views.services.FetchAddressIntentService;
import org.cmucreatelab.tasota.airprototype.views.uielements.ArrayAdapterAddressList;
import java.util.ArrayList;

/**
 * Created by mike on 6/2/15.
 */
public class GlobalHandler {

    private static GlobalHandler classInstance;
    private AddressFeedsHashMap addressFeedsHashMap;
    protected Context appContext;
    public HttpRequestHandler httpRequestHandler;
    public GoogleApiClientHandler googleApiClientHandler;
    public LocationUpdateHandler locationUpdateHandler;
    public boolean appUsesLocation=true,colorblindMode=false;
    // this is the instance used by AddressListActivity and should only be instantiated once.
    public final ArrayList<SimpleAddress> addressList = new ArrayList<>();

    // Keep track of ALL your array adapters for notifyGlobalDataSetChanged()
    public ArrayAdapterAddressList listAdapter;


    // Nobody accesses the constructor
    private GlobalHandler(Context ctx) {
        // context and handlers
        this.appContext = ctx;
        this.httpRequestHandler = HttpRequestHandler.getInstance(ctx);
        this.googleApiClientHandler = GoogleApiClientHandler.getInstance(ctx,this);
        this.locationUpdateHandler = LocationUpdateHandler.getInstance(ctx, this.googleApiClientHandler);
        // data structures
        this.addressFeedsHashMap = new AddressFeedsHashMap(this);
        updateSettings();
    }


    protected void notifyGlobalDataSetChanged() {
        // TODO this function provides a mechanism for notifying all (active) list adapters in the app when the dataset gets updated.
        if (this.listAdapter != null) {
            this.listAdapter.notifyDataSetChanged();
        }
    }


    protected void updateCurrentLocation(Location lastLocation) {
        addressFeedsHashMap.setGpsAddressLocation(lastLocation);
        notifyGlobalDataSetChanged();
        // TODO consider this when more than one update can occur.
        startFetchAddressIntentService(lastLocation);
    }


    protected void startFetchAddressIntentService(Location lastLocation) {
        if (Geocoder.isPresent()) {
            Intent intent = new Intent(this.appContext, FetchAddressIntentService.class);
            AddressResultReceiver resultReceiver = new AddressResultReceiver(new Handler(),this);

            intent.putExtra(Constants.AddressIntent.RECEIVER, resultReceiver);
            intent.putExtra("latitude",lastLocation.getLatitude());
            intent.putExtra("longitude",lastLocation.getLongitude());
            this.appContext.startService(intent);
        } else {
            Log.e(Constants.LOG_TAG, "Tried starting FetchAddressIntentService but Geocoder is not present.");
        }
    }


    public void removeAddress(SimpleAddress simpleAddress) {
        addressFeedsHashMap.removeAddress(simpleAddress);
        requestAddressesForDisplay();
    }


    public void addAddress(SimpleAddress simpleAddress) {
        addressFeedsHashMap.addAddress(simpleAddress);
        requestAddressesForDisplay();
    }


    public void updateAddresses() {
        addressFeedsHashMap.updateAddresses();
    }


    public SimpleAddress getGpsAddress() {
        return addressFeedsHashMap.gpsAddress;
    }


    public ArrayList<SimpleAddress> getAddresses() {
        return addressFeedsHashMap.addresses;
    }


    public ArrayList<Feed> getFeedsFromAddressInHashMap(SimpleAddress simpleAddress) {
        return addressFeedsHashMap.hashMap.get(simpleAddress);
    }


    public ArrayList<SimpleAddress> requestAddressesForDisplay() {
        addressList.clear();

        if (appUsesLocation) {
            addressList.add(addressFeedsHashMap.gpsAddress);
        }
        addressList.addAll(addressFeedsHashMap.addresses);

        return addressList;
    }


    public void updateSettings() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(appContext);
        appUsesLocation = prefs.getBoolean("checkbox_location",true);
        colorblindMode = prefs.getBoolean("checkbox_colorblind", false);
        requestAddressesForDisplay();
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
