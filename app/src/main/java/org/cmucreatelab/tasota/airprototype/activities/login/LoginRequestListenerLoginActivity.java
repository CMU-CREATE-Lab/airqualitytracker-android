package org.cmucreatelab.tasota.airprototype.activities.login;

import android.util.Log;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import org.cmucreatelab.tasota.airprototype.helpers.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.json.JSONObject;

import java.util.Date;

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
        GlobalHandler globalHandler = GlobalHandler.getInstance(loginActivity.getApplicationContext());
        globalHandler.esdrLoginHandler.setUserLoggedIn(false);
        globalHandler.servicesHandler.stopEsdrRefreshService();

        loginActivity.loggedIn = false;
        loginActivity.display();
    }


    @Override
    public void onResponse(JSONObject response) {
        String accessToken,refreshToken;
        long userId,timestamp,expiresIn;
        Log.v(Constants.LOG_TAG, "requestEsdrToken: got response=" + response.toString());

        try {
            timestamp = (long) (new Date().getTime() / 1000.0);
            expiresIn = response.getLong("expires_in");
            userId = response.getLong("userId");
            accessToken = response.getString("access_token");
            refreshToken = response.getString("refresh_token");
            GlobalHandler globalHandler = GlobalHandler.getInstance(loginActivity.getApplicationContext());
            globalHandler.esdrLoginHandler.updateEsdrAccount(loginActivity.username, userId, accessToken, refreshToken, timestamp+expiresIn);
            globalHandler.esdrLoginHandler.setUserLoggedIn(true);
            globalHandler.servicesHandler.startEsdrRefreshService();
            loginActivity.display();
        } catch (Exception e) {
            Log.e(Constants.LOG_TAG, "Failed to parse ESDR refresh tokens from JSON=" + response.toString());
            e.printStackTrace();
        }
    }

}
