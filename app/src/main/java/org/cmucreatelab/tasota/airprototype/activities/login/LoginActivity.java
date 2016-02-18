package org.cmucreatelab.tasota.airprototype.activities.login;

import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.activities.SessionExpiredDialog;
import org.cmucreatelab.tasota.airprototype.helpers.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;

import java.util.Date;

public class LoginActivity extends ActionBarActivity
        implements View.OnClickListener {

    private EditText editTextLoginUsername,editTextLoginPassword;
    private TextView textViewLogoutUsername;
    private final LoginRequestListenerLoginActivity loginRequest = new LoginRequestListenerLoginActivity(this);
    protected String username="",password="";
    protected boolean loggedIn=false; // locally determines if you should be viewing the Login or Logout view.


    public void display() {
        if (!loggedIn) {
            setContentView(R.layout.__login__login_activity);
            editTextLoginUsername = (EditText) findViewById(R.id.editTextLoginUsername);
            editTextLoginPassword = (EditText) findViewById(R.id.editTextLoginPassword);
            findViewById(R.id.buttonLogin).setOnClickListener(this);
        } else {
            setContentView(R.layout.__login__logout_activity);
            textViewLogoutUsername = (TextView) findViewById(R.id.textViewLogoutUsername);
            if (username.equals("")) {
                GlobalHandler globalHandler = GlobalHandler.getInstance(getApplicationContext());
                if (globalHandler.esdrLoginHandler.isUserLoggedIn()) {
                    textViewLogoutUsername.setText(globalHandler.esdrAccount.getUsername());
                }
            } else {
                textViewLogoutUsername.setText(username);
            }
            findViewById(R.id.buttonLogout).setOnClickListener(this);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loggedIn = GlobalHandler.getInstance(getApplicationContext()).esdrLoginHandler.isUserLoggedIn();
        display();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.action_bar);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
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
                display();
            }
        }
        super.onResume();
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        display();
    }


    @Override
    public void onClick(View view) {
        GlobalHandler globalHandler;

        if (!loggedIn) {
            globalHandler = GlobalHandler.getInstance(getApplicationContext());
            this.username = editTextLoginUsername.getText().toString();
            this.password = editTextLoginPassword.getText().toString();
            globalHandler.settingsHandler.userFeedsNeedsUpdated = true;
            globalHandler.esdrAuthHandler.requestEsdrToken(username,password,loginRequest,loginRequest);
            this.loggedIn = true;
            display();
        } else {
            globalHandler = GlobalHandler.getInstance(getApplicationContext());
            globalHandler.servicesHandler.stopEsdrRefreshService();
            globalHandler.esdrLoginHandler.setUserLoggedIn(false);
            this.loggedIn = false;
            globalHandler.settingsHandler.userFeedsNeedsUpdated = true;
            // clears specks on logout
            globalHandler.readingsHandler.clearSpecks();
            globalHandler.readingsHandler.populateSpecks();
            display();
        }
    }

}
