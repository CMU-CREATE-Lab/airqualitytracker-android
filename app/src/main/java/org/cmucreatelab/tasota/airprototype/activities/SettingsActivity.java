package org.cmucreatelab.tasota.airprototype.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.helpers.GlobalHandler;

public class SettingsActivity extends PreferenceActivity {


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setupSimplePreferencesScreen();
    }


    @Override
    protected void onDestroy() {
        // update the settings before destroying the activity
        GlobalHandler globalHandler = GlobalHandler.getInstance(this.getApplicationContext());
        globalHandler.updateSettings();
        if (globalHandler.settingsHandler.appUsesLocation()) {
            globalHandler.servicesHandler.googleApiClientHandler.connect();
        } else {
            globalHandler.servicesHandler.googleApiClientHandler.disconnect();
        }
        super.onDestroy();
    }


    /**
     * Shows the simplified settings UI if the device configuration if the
     * device configuration dictates that a simplified, single-pane UI should be
     * shown.
     */
    private void setupSimplePreferencesScreen() {
        // In the simplified UI, fragments are not used at all and we instead
        // use the older PreferenceActivity APIs.
        addPreferencesFromResource(R.xml.pref_basic);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return false;
    }

}
