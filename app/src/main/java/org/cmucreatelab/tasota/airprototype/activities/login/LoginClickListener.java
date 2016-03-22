package org.cmucreatelab.tasota.airprototype.activities.login;

import android.view.View;
import org.cmucreatelab.tasota.airprototype.helpers.application.GlobalHandler;

/**
 * Created by mike on 3/22/16.
 */
public class LoginClickListener implements View.OnClickListener {

    private LoginActivity activity;


    public LoginClickListener(LoginActivity activity) {
        this.activity = activity;
    }


    @Override
    public void onClick(View view) {
        GlobalHandler globalHandler;

        if (!activity.loggedIn) {
            globalHandler = GlobalHandler.getInstance(activity.getApplicationContext());
            activity.username = activity.uiElements.editTextLoginUsername.getText().toString();
            activity.password = activity.uiElements.editTextLoginPassword.getText().toString();
            globalHandler.settingsHandler.userFeedsNeedsUpdated = true;
            globalHandler.esdrAuthHandler.requestEsdrToken(activity.username,activity.password,activity.loginRequest,activity.loginRequest);
            activity.loggedIn = true;
            activity.uiElements.display();
        } else {
            globalHandler = GlobalHandler.getInstance(activity.getApplicationContext());
            globalHandler.servicesHandler.stopEsdrRefreshService();
            globalHandler.esdrLoginHandler.setUserLoggedIn(false);
            activity.loggedIn = false;
            globalHandler.settingsHandler.userFeedsNeedsUpdated = true;
            // clears specks on logout
            globalHandler.readingsHandler.clearSpecks();
            globalHandler.readingsHandler.populateSpecks();
            activity.uiElements.display();
        }
    }

}
