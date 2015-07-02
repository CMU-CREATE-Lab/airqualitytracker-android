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
import java.util.ArrayList;

public class LoginActivity extends ActionBarActivity
        implements View.OnClickListener, Response.ErrorListener {

    public EditText editTextLoginUsername,editTextLoginPassword;
    public TextView textViewLogoutUsername;
    public ListView listViewLoginFeeds;
    public final AuthRequestListenerLoginActivity authRequestListenerLoginActivity = new AuthRequestListenerLoginActivity(this);
    public final ArrayList<Feed> listFeedsUser = new ArrayList<>();


    private boolean loggedIn() {
        return GlobalHandler.getInstance(getApplicationContext()).settingsHandler.userLoggedIn;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            ArrayAdapter<Feed> feedsListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listFeedsUser);
            listViewLoginFeeds.setAdapter(feedsListAdapter);
        }
    }


    public void display() {
        if (!loggedIn()) {
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
                globalHandler.httpRequestHandler.requestPrivateFeeds(globalHandler.settingsHandler.accessToken, authRequestListenerLoginActivity);
            }
            findViewById(R.id.buttonLogout).setOnClickListener(this);
        }
    }


    @Override
    public void onClick(View view) {
        if (!loggedIn()) {
            Log.v(Constants.LOG_TAG, "Login button clicked.");
            GlobalHandler globalHandler = GlobalHandler.getInstance(getApplicationContext());
            globalHandler.httpRequestHandler.requestEsdrToken(this);
            globalHandler.settingsHandler.setUserLoggedIn(true);
            display();
        } else {
            Log.v(Constants.LOG_TAG, "Logout button clicked.");
            GlobalHandler globalHandler = GlobalHandler.getInstance(getApplicationContext());
            globalHandler.servicesHandler.stopEsdrRefreshService();
            globalHandler.settingsHandler.setUserLoggedIn(false);
            display();
        }
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        // ASSERT onErrorResponse implies that we failed to log in.
        Toast.makeText(LoginActivity.this, "Authorization Failed to log in", Toast.LENGTH_SHORT).show();
        GlobalHandler.getInstance(getApplicationContext()).settingsHandler.setUserLoggedIn(false);
        display();
    }

}
