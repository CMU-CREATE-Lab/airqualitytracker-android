package org.cmucreatelab.tasota.airprototype.views.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.helpers.Constants;

public class LoginActivity extends ActionBarActivity
        implements View.OnClickListener {

    private boolean loggedIn; // controls the views that you see
    private EditText editTextLoginUsername,editTextLoginPassword;
    private TextView textViewLogoutUsername;
//    private Button buttonLogin,buttonLogout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextLoginUsername = (EditText)findViewById(R.id.editTextLoginUsername);
        editTextLoginPassword = (EditText)findViewById(R.id.editTextLoginPassword);
        findViewById(R.id.buttonLogin).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (view == findViewById(R.id.buttonLogin)) {
            Log.d(Constants.LOG_TAG, "buttonLogin");
            Log.d(Constants.LOG_TAG, "sent auth with username=" + editTextLoginUsername.getText().toString() + ", passwd=" + editTextLoginPassword.getText().toString());
            // TODO send auth with username,passwd
            //String username=editTextLoginUsername.getText().toString(),password=editTextLoginPassword.getText().toString();
            //GlobalHandler.getInstance(getApplicationContext()).httpRequestHandler.requestEsdrToken(username,password);
            setContentView(R.layout.activity_logout);
            findViewById(R.id.buttonLogout).setOnClickListener(this);
        } else if (view == findViewById(R.id.buttonLogout)) {
            Log.d(Constants.LOG_TAG, "buttonLogout");
            setContentView(R.layout.activity_login);
        }
    }

}
