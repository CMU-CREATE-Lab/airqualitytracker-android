package org.cmucreatelab.tasota.airprototype.helpers;

import android.content.Context;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;
import com.google.android.gms.location.LocationServices;
import org.cmucreatelab.tasota.airprototype.classes.Feed;
import org.cmucreatelab.tasota.airprototype.classes.SimpleAddress;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.activities.address_list.ArrayAdapterAddressList;
import java.util.ArrayList;

/**
 * Created by mike on 6/2/15.
 */
public class GlobalHandler {

    private static GlobalHandler classInstance;
    protected Context appContext;
    // managed global instances
    public HttpRequestHandler httpRequestHandler;
    public GoogleApiClientHandler googleApiClientHandler;
    public SettingsHandler settingsHandler;
    public ServicesHandler servicesHandler;
    // data structure
    public AddressFeedsHashMap addressFeedsHashMap;
    // lists used for ListViews and their adapters
    public final ArrayList<SimpleAddress> addressList = new ArrayList<>(); // used by AddressListActivity and should only be instantiated once.
    public final ArrayList<Feed> listFeedsUser = new ArrayList<>();

    // Keep track of ALL your array adapters for notifyGlobalDataSetChanged()
    public ArrayAdapterAddressList listAdapter;


    // Nobody accesses the constructor
    private GlobalHandler(Context ctx) {
        // context and handlers
        this.appContext = ctx;
        this.settingsHandler = SettingsHandler.getInstance(this);
        this.servicesHandler = ServicesHandler.getInstance(ctx,this);
        this.httpRequestHandler = HttpRequestHandler.getInstance(ctx);
        this.googleApiClientHandler = GoogleApiClientHandler.getInstance(this);
        // data structures
        this.addressFeedsHashMap = new AddressFeedsHashMap(this);
        if (Constants.USES_BACKGROUND_SERVICES)
            servicesHandler.initializeBackgroundServices();
    }


    protected void notifyGlobalDataSetChanged() {
        // TODO this function provides a mechanism for notifying all (active) list adapters in the app when the dataset gets updated.
        if (this.listAdapter != null) {
            this.listAdapter.notifyDataSetChanged();
        }
    }


    public void updateAddresses() {
        addressFeedsHashMap.updateAddresses();
        if (settingsHandler.appUsesLocation()) {
            if (googleApiClientHandler.googleApiClient.isConnected()) {
                updateLastLocation();
            } else {
                googleApiClientHandler.connect();
            }
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
            if (Geocoder.isPresent()) {
                servicesHandler.startFetchAddressIntentService(lastLocation);
            } else {
                Log.e(Constants.LOG_TAG, "Tried starting FetchAddressIntentService but Geocoder is not present.");
            }
        }
    }


    public ArrayList<SimpleAddress> requestAddressesForDisplay() {
        addressList.clear();

        if (settingsHandler.appUsesLocation()) {
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
