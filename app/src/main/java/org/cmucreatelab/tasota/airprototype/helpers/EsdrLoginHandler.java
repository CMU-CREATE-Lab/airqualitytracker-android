package org.cmucreatelab.tasota.airprototype.helpers;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;

import java.util.ArrayList;

/**
 * Created by mike on 12/21/15.
 */
public class EsdrLoginHandler {

    private static EsdrLoginHandler classInstance;
    protected GlobalHandler globalHandler;
    private SharedPreferences sharedPreferences;

    private String username="",accessToken="",refreshToken="";
    private long userId;
    private boolean userLoggedIn=false;
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
    public long getUserId() {
        return userId;
    }
    // ASSERT: only called by SettingsHandler to update settings-related attributes
    protected void updateEsdrLoginSettings() {
        userLoggedIn = this.sharedPreferences.getBoolean(Constants.SettingsKeys.userLoggedIn, (Boolean) Constants.DEFAULT_SETTINGS.get(Constants.SettingsKeys.userLoggedIn));
        username = this.sharedPreferences.getString(Constants.SettingsKeys.username, (String) Constants.DEFAULT_SETTINGS.get(Constants.SettingsKeys.username));
        accessToken = this.sharedPreferences.getString(Constants.SettingsKeys.accessToken, (String) Constants.DEFAULT_SETTINGS.get(Constants.SettingsKeys.accessToken));
        refreshToken = this.sharedPreferences.getString(Constants.SettingsKeys.refreshToken, (String) Constants.DEFAULT_SETTINGS.get(Constants.SettingsKeys.refreshToken));
        userId = this.sharedPreferences.getLong(Constants.SettingsKeys.userId, (int) Constants.DEFAULT_SETTINGS.get(Constants.SettingsKeys.userId));
    }

    // Nobody accesses the constructor
    private EsdrLoginHandler(GlobalHandler globalHandler) {
        this.globalHandler = globalHandler;
        this.sharedPreferences = globalHandler.settingsHandler.getSharedPreferences();
    }


    // Only public way to get instance of class (synchronized means thread-safe)
    // NOT PUBLIC: for public access, use GlobalHandler
    protected static synchronized EsdrLoginHandler getInstance(GlobalHandler globalHandler) {
        if (classInstance == null) {
            classInstance = new EsdrLoginHandler(globalHandler);
        }
        return classInstance;
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
        globalHandler.settingsHandler.clearBlacklistedDevices();
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
        editor.putString(Constants.SettingsKeys.username, (String) Constants.DEFAULT_SETTINGS.get(Constants.SettingsKeys.username));
        editor.putString(Constants.SettingsKeys.accessToken,(String)Constants.DEFAULT_SETTINGS.get(Constants.SettingsKeys.accessToken));
        editor.putString(Constants.SettingsKeys.refreshToken, (String) Constants.DEFAULT_SETTINGS.get(Constants.SettingsKeys.refreshToken));
        editor.apply();
        this.setUserLoggedIn(false);
    }

}
