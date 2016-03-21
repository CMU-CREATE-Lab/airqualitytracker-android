package org.cmucreatelab.tasota.airprototype.classes;

import android.content.SharedPreferences;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;

/**
 * Created by mike on 2/18/16.
 */
public class EsdrAccount {

    // class attributes
    private String username;
    private long userId;
    private long expiresAt;
    private String accessToken;
    private String refreshToken;
    // getters/setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }
    public long getExpiresAt() { return expiresAt; }
    public void setExpiresAt(long expiresAt) { this.expiresAt = expiresAt; }
    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }


    // class constructor
    public EsdrAccount(SharedPreferences sharedPreferences) {
        loadFromUserDefaults(sharedPreferences);
    }


    public void loadFromUserDefaults(SharedPreferences sharedPreferences) {
        username = sharedPreferences.getString(Constants.SettingsKeys.username, (String) Constants.DEFAULT_SETTINGS.get(Constants.SettingsKeys.username));
        userId = sharedPreferences.getLong(Constants.SettingsKeys.userId, (long) Constants.DEFAULT_SETTINGS.get(Constants.SettingsKeys.userId));
        expiresAt = sharedPreferences.getLong(Constants.SettingsKeys.expiresAt, (long) Constants.DEFAULT_SETTINGS.get(Constants.SettingsKeys.expiresAt));
        accessToken = sharedPreferences.getString(Constants.SettingsKeys.accessToken, (String) Constants.DEFAULT_SETTINGS.get(Constants.SettingsKeys.accessToken));
        refreshToken = sharedPreferences.getString(Constants.SettingsKeys.refreshToken, (String) Constants.DEFAULT_SETTINGS.get(Constants.SettingsKeys.refreshToken));
    }


    public void clear() {
        username = null;
        userId = 0;
        expiresAt = 0;
        accessToken = null;
        refreshToken = null;
    }

}
