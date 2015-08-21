package org.cmucreatelab.tasota.airprototype.activities.login;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.helpers.GlobalHandler;

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
            GlobalHandler globalHandler = GlobalHandler.getInstance(getApplicationContext());
            if (globalHandler.settingsHandler.isUserLoggedIn()) {
                textViewLogoutUsername.setText(globalHandler.settingsHandler.getUsername());
            }
            findViewById(R.id.buttonLogout).setOnClickListener(this);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loggedIn = GlobalHandler.getInstance(getApplicationContext()).settingsHandler.isUserLoggedIn();
        display();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.action_bar);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
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
            globalHandler.httpRequestHandler.requestEsdrToken(username,password,loginRequest,loginRequest);
            this.loggedIn = true;
            display();
        } else {
            globalHandler = GlobalHandler.getInstance(getApplicationContext());
            globalHandler.servicesHandler.stopEsdrRefreshService();
            globalHandler.settingsHandler.setUserLoggedIn(false);
            this.loggedIn = false;
            globalHandler.settingsHandler.userFeedsNeedsUpdated = true;
            // clears specks on logout
            globalHandler.headerReadingsHashMap.populateSpecks();
            display();
        }
    }

}
