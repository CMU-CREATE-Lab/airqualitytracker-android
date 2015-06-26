package org.cmucreatelab.tasota.airprototype.helpers;

import android.content.Context;
import android.content.Intent;
import android.location.Geocoder;
import android.location.Location;
import android.os.Handler;
import android.util.Log;
import com.google.android.gms.location.LocationServices;
import org.cmucreatelab.tasota.airprototype.classes.SimpleAddress;
import org.cmucreatelab.tasota.airprototype.views.services.AddressResultReceiver;
import org.cmucreatelab.tasota.airprototype.views.services.FetchAddressIntentService;
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
    public SettingsHandler settingsHandler;
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
        this.settingsHandler = SettingsHandler.getInstance(this);
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
            updateLastLocation();
            addressFeedsHashMap.hashMap.put(
                    addressFeedsHashMap.gpsAddress,
                    addressFeedsHashMap.pullFeedsForAddress(addressFeedsHashMap.gpsAddress)
            );
        }
    }


    public void updateLastLocation() {
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClientHandler.googleApiClient);
        if (lastLocation == null) {
            Log.w(Constants.LOG_TAG, "getLastLocation returned null.");
        } else {
            Log.d(Constants.LOG_TAG, "getLastLocation returned: " + lastLocation.toString());

            this.addressFeedsHashMap.setGpsAddressLocation(lastLocation);
            this.notifyGlobalDataSetChanged();
            // TODO consider this when more than one update can occur.
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
        this.settingsHandler.updateSettings();
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
