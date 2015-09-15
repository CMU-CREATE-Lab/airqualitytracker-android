package org.cmucreatelab.tasota.airprototype.helpers;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;

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


    // Nobody accesses the constructor
    private SettingsHandler(GlobalHandler globalHandler) {
        this.globalHandler = globalHandler;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(globalHandler.appContext);
        Log.v(Constants.LOG_TAG, "SHAREDPREFERENCES: " + sharedPreferences.getAll().toString());
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
        appUsesLocation = this.sharedPreferences.getBoolean(Constants.SettingsKeys.appUsesLocation,(Boolean)Constants.DEFAULT_SETTINGS.get(Constants.SettingsKeys.appUsesLocation));
        userLoggedIn = this.sharedPreferences.getBoolean(Constants.SettingsKeys.userLoggedIn,(Boolean)Constants.DEFAULT_SETTINGS.get(Constants.SettingsKeys.userLoggedIn));
        username = this.sharedPreferences.getString(Constants.SettingsKeys.username, (String) Constants.DEFAULT_SETTINGS.get(Constants.SettingsKeys.username));
        accessToken = this.sharedPreferences.getString(Constants.SettingsKeys.accessToken,(String)Constants.DEFAULT_SETTINGS.get(Constants.SettingsKeys.accessToken));
        refreshToken = this.sharedPreferences.getString(Constants.SettingsKeys.refreshToken,(String)Constants.DEFAULT_SETTINGS.get(Constants.SettingsKeys.refreshToken));
        userId = this.sharedPreferences.getLong(Constants.SettingsKeys.userId, (int)Constants.DEFAULT_SETTINGS.get(Constants.SettingsKeys.userId));
    }


    public void setUserLoggedIn(boolean userLoggedIn) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putBoolean(Constants.SettingsKeys.userLoggedIn,userLoggedIn);
        // TODO commit() vs. apply() ?
        editor.apply();
        this.userLoggedIn = userLoggedIn;
        // repopulates specks on successful login/logout
        globalHandler.headerReadingsHashMap.populateSpecks();
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
        editor.putString(Constants.SettingsKeys.accessToken,accessToken);
        editor.putString(Constants.SettingsKeys.refreshToken, refreshToken);
        editor.apply();
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }


    public void removeEsdrAccount() {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString(Constants.SettingsKeys.username,(String)Constants.DEFAULT_SETTINGS.get(Constants.SettingsKeys.username));
        editor.putString(Constants.SettingsKeys.accessToken,(String)Constants.DEFAULT_SETTINGS.get(Constants.SettingsKeys.accessToken));
        editor.putString(Constants.SettingsKeys.refreshToken,(String)Constants.DEFAULT_SETTINGS.get(Constants.SettingsKeys.refreshToken));
        editor.apply();
        this.setUserLoggedIn(false);
    }

}
