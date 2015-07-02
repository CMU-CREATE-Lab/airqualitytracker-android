package org.cmucreatelab.tasota.airprototype.activities.login;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.classes.Feed;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.GlobalHandler;
import org.json.JSONObject;

public class LoginActivity extends ActionBarActivity
        implements View.OnClickListener,
        Response.Listener<JSONObject>, Response.ErrorListener {

    private EditText editTextLoginUsername,editTextLoginPassword;
    private String username="",password="";
    private boolean loggedIn=false; // locally determines if you should be viewing the Login or Logout view.
    public TextView textViewLogoutUsername;
    public ListView listViewLoginFeeds;
    public final AuthRequestListenerLoginActivity authRequestListenerLoginActivity = new AuthRequestListenerLoginActivity(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loggedIn = GlobalHandler.getInstance(getApplicationContext()).settingsHandler.userLoggedIn;
        display();
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        display();
    }


    // populates the list of feeds
    public void populateFeeds() {
        if (listViewLoginFeeds != null) {
            ArrayAdapter<Feed> feedsListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, GlobalHandler.getInstance(getApplicationContext()).listFeedsUser);
            listViewLoginFeeds.setAdapter(feedsListAdapter);
        }
    }


    public void display() {
        if (!loggedIn) {
            setContentView(R.layout.activity_login);
            editTextLoginUsername = (EditText) findViewById(R.id.editTextLoginUsername);
            editTextLoginPassword = (EditText) findViewById(R.id.editTextLoginPassword);
            findViewById(R.id.buttonLogin).setOnClickListener(this);
        } else {
            setContentView(R.layout.activity_logout);
            textViewLogoutUsername = (TextView) findViewById(R.id.textViewLogoutUsername);
            listViewLoginFeeds = (ListView) findViewById(R.id.listViewLoginFeeds);
            GlobalHandler globalHandler = GlobalHandler.getInstance(getApplicationContext());
            if (globalHandler.settingsHandler.userLoggedIn) {
                textViewLogoutUsername.setText(globalHandler.settingsHandler.username);
                if (globalHandler.settingsHandler.userFeedsNeedsUpdated) {
                    globalHandler.httpRequestHandler.requestPrivateFeeds(globalHandler.settingsHandler.accessToken, authRequestListenerLoginActivity);
                } else {
                    populateFeeds();
                }
            }
            findViewById(R.id.buttonLogout).setOnClickListener(this);
        }
    }


    @Override
    public void onClick(View view) {
        if (!loggedIn) {
            Log.v(Constants.LOG_TAG, "Login button clicked.");
            GlobalHandler globalHandler = GlobalHandler.getInstance(getApplicationContext());
            this.username = editTextLoginUsername.getText().toString();
            this.password = editTextLoginPassword.getText().toString();
            globalHandler.settingsHandler.userFeedsNeedsUpdated = true;
            globalHandler.httpRequestHandler.requestEsdrToken(username,password,this,this);
            this.loggedIn = true;
            display();
        } else {
            Log.v(Constants.LOG_TAG, "Logout button clicked.");
            GlobalHandler globalHandler = GlobalHandler.getInstance(getApplicationContext());
            globalHandler.servicesHandler.stopEsdrRefreshService();
            globalHandler.settingsHandler.setUserLoggedIn(false);
            this.loggedIn = false;
            globalHandler.settingsHandler.userFeedsNeedsUpdated = true;
            display();
        }
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        // ASSERT onErrorResponse implies that we failed to log in.
        Toast.makeText(LoginActivity.this, "Authorization Failed to log in", Toast.LENGTH_SHORT).show();
        GlobalHandler globalHandler = GlobalHandler.getInstance(getApplicationContext());
        globalHandler.settingsHandler.setUserLoggedIn(false);
        globalHandler.servicesHandler.stopEsdrRefreshService();
        this.loggedIn = false;
        display();
    }


    @Override
    public void onResponse(JSONObject response) {
        String accessToken,refreshToken;
        Log.d(Constants.LOG_TAG,"requestEsdrToken: got response="+response.toString());
        try {
            accessToken = response.getString("access_token");
            refreshToken = response.getString("refresh_token");
            GlobalHandler globalHandler = GlobalHandler.getInstance(this.getApplicationContext());
            globalHandler.settingsHandler.updateEsdrAccount(this.username, accessToken, refreshToken);
            globalHandler.settingsHandler.setUserLoggedIn(true);
            globalHandler.servicesHandler.startEsdrRefreshService();
            display();
        } catch (Exception e) {
            Log.e(Constants.LOG_TAG, "Failed to parse ESDR refresh tokens from JSON=" + response.toString());
            e.printStackTrace();
        }
    }

}
