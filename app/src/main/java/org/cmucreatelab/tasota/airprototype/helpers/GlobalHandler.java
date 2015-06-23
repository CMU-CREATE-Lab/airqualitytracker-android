package org.cmucreatelab.tasota.airprototype.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import org.cmucreatelab.tasota.airprototype.classes.SimpleAddress;
import org.cmucreatelab.tasota.airprototype.views.uielements.ArrayAdapterAddressList;
import java.util.ArrayList;

/**
 * Created by mike on 6/2/15.
 */
public class GlobalHandler {

    private static GlobalHandler classInstance;
    protected Context appContext;
    public AddressFeedsHashMap addressFeedsHashMap;
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
        this.googleApiClientHandler = GoogleApiClientHandler.getInstance(this);
        this.locationUpdateHandler = LocationUpdateHandler.getInstance(this.googleApiClientHandler);
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


    public void updateAddresses() {
        addressFeedsHashMap.updateAddresses();
        if (appUsesLocation) {
            googleApiClientHandler.updateLastLocation();
            addressFeedsHashMap.hashMap.put(
                    addressFeedsHashMap.gpsAddress,
                    addressFeedsHashMap.pullFeedsForAddress(addressFeedsHashMap.gpsAddress)
            );
        }
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
