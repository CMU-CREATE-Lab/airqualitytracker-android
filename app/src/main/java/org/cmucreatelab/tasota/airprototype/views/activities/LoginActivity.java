package org.cmucreatelab.tasota.airprototype.views.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.helpers.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.GlobalHandler;

public class LoginActivity extends ActionBarActivity
        implements View.OnClickListener {

//    private boolean loggedIn=false; // controls the views that you see
    private EditText editTextLoginUsername,editTextLoginPassword;


    private boolean loggedIn() {
        return GlobalHandler.getInstance(getApplicationContext()).settingsHandler.userLoggedIn;
    }


    protected void display() {
        if (!loggedIn()) {
            setContentView(R.layout.activity_login);

            editTextLoginUsername = (EditText) findViewById(R.id.editTextLoginUsername);
            editTextLoginPassword = (EditText) findViewById(R.id.editTextLoginPassword);
            findViewById(R.id.buttonLogin).setOnClickListener(this);
        } else {
            setContentView(R.layout.activity_logout);
            findViewById(R.id.buttonLogout).setOnClickListener(this);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO check if logged in (check SharedPreferences)
        display();
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
//        loggedIn = savedInstanceState.getBoolean("loggedIn");
        display();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putBoolean("loggedIn",loggedIn);
    }


    @Override
    public void onClick(View view) {
        if (!loggedIn()) {
            Log.d(Constants.LOG_TAG, "buttonLogin");
            String  username=editTextLoginUsername.getText().toString(),
                    password=editTextLoginPassword.getText().toString();
            Log.d(Constants.LOG_TAG, "sent auth with username=" + username + ", passwd=" + password);
            // TODO send auth with username,passwd
            GlobalHandler globalHandler = GlobalHandler.getInstance(getApplicationContext());
            globalHandler.httpRequestHandler.requestEsdrToken(username,password);
            globalHandler.settingsHandler.setUserLoggedIn(true);
//            loggedIn = true;
            display();
        } else {
            Log.d(Constants.LOG_TAG, "buttonLogout");
            GlobalHandler globalHandler = GlobalHandler.getInstance(getApplicationContext());
            globalHandler.stopEsdrRefreshService();
            globalHandler.settingsHandler.setUserLoggedIn(false);
//            loggedIn = false;
            display();
        }
    }

}
