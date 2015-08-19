package org.cmucreatelab.tasota.airprototype.activities.login;

import android.util.Log;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import org.cmucreatelab.tasota.airprototype.helpers.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.json.JSONObject;

/**
 * Created by mike on 7/2/15.
 */
public class LoginRequestListenerLoginActivity
        implements Response.Listener<JSONObject>, Response.ErrorListener {

    private LoginActivity loginActivity;


    protected LoginRequestListenerLoginActivity(LoginActivity loginActivity) {
        this.loginActivity = loginActivity;
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        // ASSERT onErrorResponse implies that we failed to log in.
        Toast.makeText(loginActivity, "Authorization Failed to log in", Toast.LENGTH_SHORT).show();
        GlobalHandler globalHandler = GlobalHandler.getInstance(loginActivity.getApplicationContext());
        globalHandler.settingsHandler.setUserLoggedIn(false);
        globalHandler.servicesHandler.stopEsdrRefreshService();
        loginActivity.loggedIn = false;
        loginActivity.display();
    }


    @Override
    public void onResponse(JSONObject response) {
        String accessToken,refreshToken;
        Log.v(Constants.LOG_TAG, "requestEsdrToken: got response=" + response.toString());
        try {
            accessToken = response.getString("access_token");
            refreshToken = response.getString("refresh_token");
            GlobalHandler globalHandler = GlobalHandler.getInstance(loginActivity.getApplicationContext());
            globalHandler.settingsHandler.updateEsdrAccount(loginActivity.username, accessToken, refreshToken);
            globalHandler.settingsHandler.setUserLoggedIn(true);
            globalHandler.servicesHandler.startEsdrRefreshService();
            loginActivity.display();
            // grabs specks on successful login
            globalHandler.headerReadingsHashMap.populateSpecks();
        } catch (Exception e) {
            Log.e(Constants.LOG_TAG, "Failed to parse ESDR refresh tokens from JSON=" + response.toString());
            e.printStackTrace();
        }
    }
}
