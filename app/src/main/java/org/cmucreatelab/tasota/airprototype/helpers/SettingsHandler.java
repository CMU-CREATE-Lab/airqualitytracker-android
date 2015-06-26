package org.cmucreatelab.tasota.airprototype.helpers;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by mike on 6/26/15.
 */
public class SettingsHandler {

    private static SettingsHandler classInstance;
    protected GlobalHandler globalHandler;
    private SharedPreferences sharedPreferences;
    public boolean appUsesLocation=true,colorblindMode=false;
    public String username="",accessToken="",refreshToken="";


    // Nobody accesses the constructor
    private SettingsHandler(GlobalHandler globalHandler) {
        this.globalHandler = globalHandler;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(globalHandler.appContext);
        Log.d(Constants.LOG_TAG,"SHAREDPREFERENCES: "+sharedPreferences.getAll().toString());
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
        username = this.sharedPreferences.getString("username", "");
        accessToken = this.sharedPreferences.getString("access_token", "");
        refreshToken = this.sharedPreferences.getString("refresh_token", "");
    }


    // TODO may also want to use timestamp somewhere?
    public void setEsdrAccount(String username, String accessToken, String refreshToken) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString("username",username);
        editor.putString("access_token",accessToken);
        editor.putString("refresh_token",refreshToken);
        editor.apply();
        this.username = username;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}
