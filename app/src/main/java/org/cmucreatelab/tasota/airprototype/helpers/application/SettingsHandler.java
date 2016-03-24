package org.cmucreatelab.tasota.airprototype.helpers.application;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import java.util.ArrayList;

/**
 * Created by mike on 6/26/15.
 */
public class SettingsHandler {

    protected GlobalHandler globalHandler;
    private SharedPreferences sharedPreferences;
    // TODO consider timestamps for last updated user info
    private boolean appUsesLocation=true;
    public ArrayList<Long> blacklistedDevices;
    // public getters
    public boolean appUsesLocation() { return appUsesLocation; }
    public SharedPreferences getSharedPreferences() { return sharedPreferences; }


    public SettingsHandler(GlobalHandler globalHandler) {
        this.globalHandler = globalHandler;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(globalHandler.appContext);
        Log.v(Constants.LOG_TAG, "SHAREDPREFERENCES: " + sharedPreferences.getAll().toString());
        this.blacklistedDevices = new ArrayList<>();
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


    public void updateSettings() {
        appUsesLocation = this.sharedPreferences.getBoolean(Constants.SettingsKeys.appUsesLocation, (Boolean) Constants.DEFAULT_SETTINGS.get(Constants.SettingsKeys.appUsesLocation));
        globalHandler.esdrLoginHandler.updateEsdrLoginSettings();
        blacklistedDevices.clear();
        String[] devicesList = this.sharedPreferences.getString(Constants.SettingsKeys.blacklistedDevices, (String) Constants.DEFAULT_SETTINGS.get(Constants.SettingsKeys.blacklistedDevices)).split(",");
        for (String deviceId : devicesList) {
            if (deviceId != "")
                blacklistedDevices.add(Long.parseLong(deviceId));
        }
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
