package org.cmucreatelab.tasota.airprototype.helpers;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.GpsReadingHandler;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by mike on 6/26/15.
 */
public class SettingsHandler {


    // Singleton Implementation


    protected GlobalHandler globalHandler;
    private static SettingsHandler classInstance;

    // Only public way to get instance of class (synchronized means thread-safe)
    public static synchronized SettingsHandler getInstance(GlobalHandler globalHandler) {
        if (classInstance == null) {
            classInstance = new SettingsHandler(globalHandler);
        }
        return classInstance;
    }

    // Nobody accesses the constructor
    private SettingsHandler(GlobalHandler globalHandler) {
        this.globalHandler = globalHandler;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(globalHandler.appContext);
        Log.v(Constants.LOG_TAG, "SHAREDPREFERENCES: " + sharedPreferences.getAll().toString());
        this.blacklistedDevices = new ArrayList<>();
    }


    // Handler attributes and methods


    private SharedPreferences sharedPreferences;
    // TODO consider timestamps for last updated user info
    private boolean appUsesLocation=true;
    protected ArrayList<Long> blacklistedDevices;
    // run-time only flag to determine if we want to pull info from ESDR
    public boolean userFeedsNeedsUpdated=true;
    public boolean appUsesLocation() {
        return appUsesLocation;
    }
    public String getStringifiedBlacklistedDeviceIds() {
        String list = "";
        for (int i=0; i<blacklistedDevices.size(); i++) {
            list += blacklistedDevices.get(i).toString();
            if (i+1 < blacklistedDevices.size())
                list += ",";
        }
        return list;
    }
    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }


    protected void updateSettings() {
        appUsesLocation = this.sharedPreferences.getBoolean(Constants.SettingsKeys.appUsesLocation, (Boolean) Constants.DEFAULT_SETTINGS.get(Constants.SettingsKeys.appUsesLocation));
        globalHandler.esdrLoginHandler.updateEsdrLoginSettings();
        blacklistedDevices.clear();
        String[] devicesList = this.sharedPreferences.getString(Constants.SettingsKeys.blacklistedDevices, (String) Constants.DEFAULT_SETTINGS.get(Constants.SettingsKeys.blacklistedDevices)).split(",");
        for (String deviceId : devicesList) {
            if (deviceId != "")
                blacklistedDevices.add(Long.parseLong(deviceId));
        }
    }


    public boolean deviceIsBlacklisted(long deviceId) {
        for (Long id: blacklistedDevices) {
            if (id == deviceId) {
                return true;
            }
        }
        return false;
    }


    public void addToBlacklistedDevices(Long deviceId) {
        blacklistedDevices.add(deviceId);
        String list = getStringifiedBlacklistedDeviceIds();
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString(Constants.SettingsKeys.blacklistedDevices, list);
        editor.apply();
    }


    public void clearBlacklistedDevices() {
        blacklistedDevices.clear();
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString(Constants.SettingsKeys.blacklistedDevices, "");
        editor.apply();
    }


    public void setAppUsesLocation(boolean appUsesLocation) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putBoolean(Constants.SettingsKeys.appUsesLocation, appUsesLocation);
        editor.apply();
        this.appUsesLocation = appUsesLocation;
        if (this.appUsesLocation) {
            globalHandler.servicesHandler.googleApiClientHandler.connect();
        } else {
            globalHandler.servicesHandler.googleApiClientHandler.disconnect();
        }
        globalHandler.readingsHandler.refreshHash();
    }

}
