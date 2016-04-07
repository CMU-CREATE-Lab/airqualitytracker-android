package org.cmucreatelab.tasota.airprototype.activities.options_menu.login;

import android.os.Bundle;
import org.cmucreatelab.tasota.airprototype.activities.BaseActivity;
import org.cmucreatelab.tasota.airprototype.helpers.application.GlobalHandler;
import java.util.Date;

public class LoginActivity extends BaseActivity<LoginUIElements> {

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
            if (globalHandler.esdrAuthHandler.alertLogout()) {
                // Alert
                LoginSessionExpiredDialog dialog = new LoginSessionExpiredDialog(this);
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
