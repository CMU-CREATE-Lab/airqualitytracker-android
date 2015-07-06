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
    private boolean appUsesLocation=true,colorblindMode=false,userLoggedIn=false;
    private String username="",accessToken="",refreshToken="";
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
    public boolean isColorblindMode() {
        return colorblindMode;
    }


    // Nobody accesses the constructor
    private SettingsHandler(GlobalHandler globalHandler) {
        this.globalHandler = globalHandler;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(globalHandler.appContext);
        Log.v(Constants.LOG_TAG,"SHAREDPREFERENCES: "+sharedPreferences.getAll().toString());
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
        appUsesLocation = this.sharedPreferences.getBoolean("checkbox_location",true);
        colorblindMode = this.sharedPreferences.getBoolean("checkbox_colorblind", false);
        userLoggedIn = this.sharedPreferences.getBoolean("user_logged_in", false);
        username = this.sharedPreferences.getString("username", "");
        accessToken = this.sharedPreferences.getString("access_token", "");
        refreshToken = this.sharedPreferences.getString("refresh_token", "");
    }


    public void setUserLoggedIn(boolean userLoggedIn) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putBoolean("user_logged_in",userLoggedIn);
        editor.apply();
        this.userLoggedIn = userLoggedIn;
    }


    public void updateEsdrAccount(String username, String accessToken, String refreshToken) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString("username",username);
        editor.putString("access_token",accessToken);
        editor.putString("refresh_token",refreshToken);
        editor.apply();
        this.username = username;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }


    public void updateEsdrTokens(String accessToken, String refreshToken) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString("access_token",accessToken);
        editor.putString("refresh_token", refreshToken);
        editor.apply();
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }


    public void removeEsdrAccount() {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString("username","");
        editor.putString("access_token","");
        editor.putString("refresh_token","");
        editor.putBoolean("user_logged_in", false);
        editor.apply();
    }

}