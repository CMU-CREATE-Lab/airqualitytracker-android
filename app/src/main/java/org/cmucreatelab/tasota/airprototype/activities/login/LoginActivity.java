package org.cmucreatelab.tasota.airprototype.activities.login;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import org.cmucreatelab.tasota.airprototype.activities.SessionExpiredDialog;
import org.cmucreatelab.tasota.airprototype.helpers.application.GlobalHandler;
import java.util.Date;

public class LoginActivity extends ActionBarActivity {

    protected LoginUIElements uiElements;
    protected final LoginClickListener clickListener = new LoginClickListener(this);
    protected final LoginRequestListener loginRequest = new LoginRequestListener(this);
    protected String username="",password="";
    protected boolean loggedIn=false; // locally determines if you should be viewing the Login or Logout view.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiElements = new LoginUIElements(this);
        uiElements.populate();
    }


    @Override
    protected void onResume() {
        if (loggedIn) {
            // Tokens: check & refresh
            GlobalHandler globalHandler = GlobalHandler.getInstance(getApplicationContext());
            long timestamp = (long) (new Date().getTime() / 1000.0);
            long expiresAt = globalHandler.esdrAccount.getExpiresAt();
            String refreshToken = globalHandler.esdrAccount.getRefreshToken();
            boolean updatingTokens = globalHandler.esdrAuthHandler.checkAndRefreshEsdrTokens(expiresAt, timestamp, refreshToken);
            if (!updatingTokens) {
                // Alert
                SessionExpiredDialog dialog = new SessionExpiredDialog(this);
                dialog.getAlertDialog().show();
                loggedIn = false;
                uiElements.display();
            }
        }
        super.onResume();
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        uiElements.display();
    }

}
