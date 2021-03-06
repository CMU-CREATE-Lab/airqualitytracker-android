package org.cmucreatelab.tasota.airprototype.helpers.application;

import android.content.Context;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;
import com.google.android.gms.location.LocationServices;
import org.cmucreatelab.tasota.airprototype.activities.options_menu.manage_trackers.ManageTrackersAdapter;
import org.cmucreatelab.tasota.airprototype.activities.readable_list.sticky_grid.StickyGridAdapter;
import org.cmucreatelab.tasota.airprototype.activities.secret_menu.SecretMenuListFeedsAdapter;
import org.cmucreatelab.tasota.airprototype.classes.EsdrAccount;
import org.cmucreatelab.tasota.airprototype.classes.readables.AirNowReadable;
import org.cmucreatelab.tasota.airprototype.classes.readables.SimpleAddress;
import org.cmucreatelab.tasota.airprototype.classes.readables.Speck;
import org.cmucreatelab.tasota.airprototype.helpers.system.ServicesHandler;
import org.cmucreatelab.tasota.airprototype.helpers.http.AirNowRequestHandler;
import org.cmucreatelab.tasota.airprototype.helpers.http.esdr.EsdrAuthHandler;
import org.cmucreatelab.tasota.airprototype.helpers.http.esdr.EsdrFeedsHandler;
import org.cmucreatelab.tasota.airprototype.helpers.http.esdr.EsdrSpecksHandler;
import org.cmucreatelab.tasota.airprototype.helpers.http.HttpRequestHandler;
import org.cmucreatelab.tasota.airprototype.helpers.http.esdr.EsdrTilesHandler;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.system.database.AddressDbHelper;
import org.cmucreatelab.tasota.airprototype.helpers.system.database.SpeckDbHelper;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by mike on 6/2/15.
 */
public class GlobalHandler {


    // Singleton Implementation


    private static GlobalHandler classInstance;
    public Context appContext;


    // Only public way to get instance of class (synchronized means thread-safe)
    public static synchronized GlobalHandler getInstance(Context ctx) {
        if (classInstance == null) {
            classInstance = new GlobalHandler(ctx);
        }
        return classInstance;
    }


    // Nobody accesses the constructor
    private GlobalHandler(Context ctx) {
        // context and handlers
        this.appContext = ctx;
        this.settingsHandler = new SettingsHandler(this);
        this.esdrLoginHandler = new EsdrLoginHandler(this);
        this.positionIdHelper = new PositionIdHelper(this);
        settingsHandler.updateSettings();
        this.servicesHandler = new ServicesHandler(this);
        this.httpRequestHandler = new HttpRequestHandler(this);
        this.airNowRequestHandler = new AirNowRequestHandler(this);
        this.esdrFeedsHandler = new EsdrFeedsHandler(this);
        this.esdrAuthHandler = new EsdrAuthHandler(this);
        this.esdrSpecksHandler = new EsdrSpecksHandler(this);
        this.esdrTilesHandler = new EsdrTilesHandler(this);
        // data structures
        this.readingsHandler = new ReadingsHandler(this);
        this.esdrAccount = new EsdrAccount(settingsHandler.getSharedPreferences());
        // load from database
        ArrayList<SimpleAddress> dbAddresses = AddressDbHelper.fetchAddressesFromDatabase(ctx);
        ArrayList<Speck> dbSpecks = SpeckDbHelper.fetchSpecksFromDatabase(ctx);
        for (SimpleAddress address: dbAddresses) {
            readingsHandler.addReading(address);
        }
        for (Speck speck: dbSpecks) {
            readingsHandler.addReading(speck);
            esdrSpecksHandler.requestChannelsForSpeck(speck);
        }
        if (Constants.USES_BACKGROUND_SERVICES)
            servicesHandler.initializeBackgroundServices();
    }


    // Handler attributes and methods


    // managed global instances
    public AirNowRequestHandler airNowRequestHandler;
    public EsdrAuthHandler esdrAuthHandler;
    public EsdrFeedsHandler esdrFeedsHandler;
    public EsdrLoginHandler esdrLoginHandler;
    public EsdrSpecksHandler esdrSpecksHandler;
    public EsdrTilesHandler esdrTilesHandler;
    public HttpRequestHandler httpRequestHandler;
    public PositionIdHelper positionIdHelper;
    public ServicesHandler servicesHandler;
    public SettingsHandler settingsHandler;
    // data structure
    public ReadingsHandler readingsHandler;
    public EsdrAccount esdrAccount;
    // Keep track of ALL your array adapters for notifyGlobalDataSetChanged()
    public StickyGridAdapter gridAdapter;
    public ManageTrackersAdapter trackersAdapter;
    public SecretMenuListFeedsAdapter secretMenuListFeedsAdapter;
    public boolean displaySessionExpiredDialog = false;
    // use this to store the item's index; useful when navigating "Up" without an Intent
    public int readableShowItemIndex = -1;
    public AirNowReadable readableShowToAirNow;



    public Context getAppContext() {
        return appContext;
    }


    // This function provides a mechanism for notifying all (active) list adapters
    // in the app when the dataset gets updated.
    public void notifyGlobalDataSetChanged() {
        if (this.gridAdapter != null) {
            this.gridAdapter.notifyDataSetChanged();
        }
        if (this.secretMenuListFeedsAdapter != null) {
            this.secretMenuListFeedsAdapter.notifyDataSetChanged();
        }
    }


    public void updateReadings() {
        // Tokens: refresh within threshold
        if (esdrLoginHandler.isUserLoggedIn()) {
            long timestamp = (long) (new Date().getTime() / 1000.0);
            long expiresAt = esdrAccount.getExpiresAt();
            long timeRemaining = expiresAt - timestamp;
            if (timeRemaining <= Constants.ESDR_TOKEN_TIME_TO_UPDATE_ON_REFRESH) {
                String refreshToken = esdrAccount.getRefreshToken();
                esdrAuthHandler.requestEsdrRefresh(refreshToken);
            }
        }

        // Now, perform readings update
        readingsHandler.updateAddresses();
        readingsHandler.updateSpecks();
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
            this.readingsHandler.gpsReadingHandler.setGpsAddressLocation(lastLocation);
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
        readingsHandler.refreshHash();
        this.notifyGlobalDataSetChanged();
    }

}
