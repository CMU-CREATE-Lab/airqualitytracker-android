package org.cmucreatelab.tasota.airprototype.helpers;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by mike on 6/26/15.
 */
public class SettingsHandler {

    private static SettingsHandler classInstance;
    private SharedPreferences sharedPreferences;
    // TODO consider timestamps for last updated user info
    private boolean appUsesLocation=true,userLoggedIn=false;
    private String username="",accessToken="",refreshToken="";
    private long userId;
    protected ArrayList<Long> blacklistedDevices;
    protected GlobalHandler globalHandler;
    // run-time only flag to determine if we want to pull info from ESDR
    public boolean userFeedsNeedsUpdated=true;
    public String getRefreshToken() {
        return refreshToken;
    }
    public String getAccessToken() {
        return accessToken;
    }
    public String getUsername() {
        return username;
    }
    public boolean isUserLoggedIn() {
        return userLoggedIn;
    }
    public boolean appUsesLocation() {
        return appUsesLocation;
    }
    public long getUserId() {
        return userId;
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


    // Nobody accesses the constructor
    private SettingsHandler(GlobalHandler globalHandler) {
        this.globalHandler = globalHandler;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(globalHandler.appContext);
        Log.v(Constants.LOG_TAG, "SHAREDPREFERENCES: " + sharedPreferences.getAll().toString());
        this.blacklistedDevices = new ArrayList<>();
        this.updateSettings();
    }


    // Only public way to get instance of class (synchronized means thread-safe)
    // NOT PUBLIC: for public access, use GlobalHandler
    protected static synchronized SettingsHandler getInstance(GlobalHandler globalHandler) {
        if (classInstance == null) {
            classInstance = new SettingsHandler(globalHandler);
        }
        return classInstance;
    }


    protected void updateSettings() {
        appUsesLocation = this.sharedPreferences.getBoolean(Constants.SettingsKeys.appUsesLocation, (Boolean) Constants.DEFAULT_SETTINGS.get(Constants.SettingsKeys.appUsesLocation));
        userLoggedIn = this.sharedPreferences.getBoolean(Constants.SettingsKeys.userLoggedIn, (Boolean) Constants.DEFAULT_SETTINGS.get(Constants.SettingsKeys.userLoggedIn));
        username = this.sharedPreferences.getString(Constants.SettingsKeys.username, (String) Constants.DEFAULT_SETTINGS.get(Constants.SettingsKeys.username));
        accessToken = this.sharedPreferences.getString(Constants.SettingsKeys.accessToken, (String) Constants.DEFAULT_SETTINGS.get(Constants.SettingsKeys.accessToken));
        refreshToken = this.sharedPreferences.getString(Constants.SettingsKeys.refreshToken, (String) Constants.DEFAULT_SETTINGS.get(Constants.SettingsKeys.refreshToken));
        userId = this.sharedPreferences.getLong(Constants.SettingsKeys.userId, (int) Constants.DEFAULT_SETTINGS.get(Constants.SettingsKeys.userId));
        blacklistedDevices.clear();
        String[] devicesList = this.sharedPreferences.getString(Constants.SettingsKeys.blacklistedDevices, (String) Constants.DEFAULT_SETTINGS.get(Constants.SettingsKeys.blacklistedDevices)).split(",");
        for (String deviceId : devicesList) {
            if (deviceId != "")
                blacklistedDevices.add(Long.parseLong(deviceId));
        }
    }


    public void setUserLoggedIn(boolean userLoggedIn) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putBoolean(Constants.SettingsKeys.userLoggedIn,userLoggedIn);
        // TODO commit() vs. apply() ?
        editor.apply();
        this.userLoggedIn = userLoggedIn;
        // repopulates specks on successful login/logout
        globalHandler.headerReadingsHashMap.populateSpecks();
        // also clears the blacklisted devices
        clearBlacklistedDevices();
    }


    public void updateEsdrAccount(String username, long userId, String accessToken, String refreshToken) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString(Constants.SettingsKeys.username,username);
        editor.putString(Constants.SettingsKeys.accessToken,accessToken);
        editor.putString(Constants.SettingsKeys.refreshToken,refreshToken);
        editor.putLong(Constants.SettingsKeys.userId,userId);
        editor.apply();
        this.username = username;
        this.userId = userId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }


    public void updateEsdrTokens(String accessToken, String refreshToken) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString(Constants.SettingsKeys.accessToken, accessToken);
        editor.putString(Constants.SettingsKeys.refreshToken, refreshToken);
        editor.apply();
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }


    public void removeEsdrAccount() {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString(Constants.SettingsKeys.username,(String)Constants.DEFAULT_SETTINGS.get(Constants.SettingsKeys.username));
        editor.putString(Constants.SettingsKeys.accessToken,(String)Constants.DEFAULT_SETTINGS.get(Constants.SettingsKeys.accessToken));
        editor.putString(Constants.SettingsKeys.refreshToken, (String) Constants.DEFAULT_SETTINGS.get(Constants.SettingsKeys.refreshToken));
        editor.apply();
        this.setUserLoggedIn(false);
    }


    public int getAddressLastPosition() {
        int position = this.sharedPreferences.getInt(Constants.SettingsKeys.addressLastPosition, (int) Constants.DEFAULT_SETTINGS.get(Constants.SettingsKeys.addressLastPosition));
        setAddressLastPosition(position + 1);
        return position;
    }


    public int getSpeckLastPosition() {
        int position = this.sharedPreferences.getInt(Constants.SettingsKeys.speckLastPosition, (int) Constants.DEFAULT_SETTINGS.get(Constants.SettingsKeys.speckLastPosition));
        setSpeckLastPosition(position + 1);
        return position;
    }


    public void setAddressLastPosition(int position) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putInt(Constants.SettingsKeys.addressLastPosition, position);
        editor.apply();
    }


    public void setSpeckLastPosition(int position) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putInt(Constants.SettingsKeys.speckLastPosition, position);
        editor.apply();
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
        globalHandler.headerReadingsHashMap.refreshHash();
    }

}
