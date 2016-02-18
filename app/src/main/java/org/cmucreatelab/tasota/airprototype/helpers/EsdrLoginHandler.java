package org.cmucreatelab.tasota.airprototype.helpers;

import android.content.SharedPreferences;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;

/**
 * Created by mike on 12/21/15.
 */
public class EsdrLoginHandler {

    protected GlobalHandler globalHandler;
    private SharedPreferences sharedPreferences;
    private boolean userLoggedIn=false;

    public boolean isUserLoggedIn() {
        return userLoggedIn;
    }


    // ASSERT: only called by SettingsHandler to update settings-related attributes
    protected void updateEsdrLoginSettings() {
        userLoggedIn = this.sharedPreferences.getBoolean(Constants.SettingsKeys.userLoggedIn, (Boolean) Constants.DEFAULT_SETTINGS.get(Constants.SettingsKeys.userLoggedIn));
    }


    // GlobalHandler accesses the constructor
    protected EsdrLoginHandler(GlobalHandler globalHandler) {
        this.globalHandler = globalHandler;
        this.sharedPreferences = globalHandler.settingsHandler.getSharedPreferences();
    }


    public void setUserLoggedIn(boolean userLoggedIn) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putBoolean(Constants.SettingsKeys.userLoggedIn,userLoggedIn);
        // TODO commit() vs. apply() ?
        editor.apply();
        this.userLoggedIn = userLoggedIn;
        // repopulates specks on successful login/logout
        globalHandler.readingsHandler.populateSpecks();
        // also clears the blacklisted devices
        globalHandler.settingsHandler.clearBlacklistedDevices();
    }


    public void updateEsdrAccount(String username, long userId, String accessToken, String refreshToken, long expiresAt) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString(Constants.SettingsKeys.username,username);
        editor.putString(Constants.SettingsKeys.accessToken,accessToken);
        editor.putString(Constants.SettingsKeys.refreshToken, refreshToken);
        editor.putLong(Constants.SettingsKeys.userId, userId);
        editor.putLong(Constants.SettingsKeys.expiresAt, expiresAt);
        editor.apply();
        globalHandler.esdrAccount.loadFromUserDefaults(sharedPreferences);
    }


    public void updateEsdrTokens(String accessToken, String refreshToken, long expiresAt) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString(Constants.SettingsKeys.accessToken, accessToken);
        editor.putString(Constants.SettingsKeys.refreshToken, refreshToken);
        editor.putLong(Constants.SettingsKeys.expiresAt, expiresAt);
        editor.apply();
        globalHandler.esdrAccount.loadFromUserDefaults(sharedPreferences);
    }


    public void removeEsdrAccount() {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString(Constants.SettingsKeys.username, (String) Constants.DEFAULT_SETTINGS.get(Constants.SettingsKeys.username));
        editor.putString(Constants.SettingsKeys.accessToken,(String)Constants.DEFAULT_SETTINGS.get(Constants.SettingsKeys.accessToken));
        editor.putString(Constants.SettingsKeys.refreshToken, (String) Constants.DEFAULT_SETTINGS.get(Constants.SettingsKeys.refreshToken));
        editor.putLong(Constants.SettingsKeys.expiresAt, (long) Constants.DEFAULT_SETTINGS.get(Constants.SettingsKeys.expiresAt));
        editor.apply();
        this.setUserLoggedIn(false);
    }

}
