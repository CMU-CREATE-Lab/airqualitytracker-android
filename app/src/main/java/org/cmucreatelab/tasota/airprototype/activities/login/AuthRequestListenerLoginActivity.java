package org.cmucreatelab.tasota.airprototype.activities.login;

import android.util.Log;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import org.cmucreatelab.tasota.airprototype.helpers.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.JsonParser;
import org.json.JSONObject;

/**
 * Created by mike on 7/2/15.
 */
public class AuthRequestListenerLoginActivity
        implements Response.Listener<JSONObject>, Response.ErrorListener {

    private LoginActivity loginActivity;


    public AuthRequestListenerLoginActivity(LoginActivity loginActivity) {
        this.loginActivity = loginActivity;
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e(Constants.LOG_TAG, "AuthRequestListenerLoginActivity onErrorResponse");
        GlobalHandler.getInstance(loginActivity.getApplicationContext()).settingsHandler.userFeedsNeedsUpdated = true;
    }


    @Override
    public void onResponse(JSONObject response) {
        Log.v(Constants.LOG_TAG, "AuthRequestListenerLoginActivity handling response=" + response.toString());
        GlobalHandler globalHandler = GlobalHandler.getInstance(loginActivity.getApplicationContext());
        globalHandler.listFeedsUser.clear();
        JsonParser.populateAllFeedsFromJson(globalHandler.listFeedsUser, response);
        loginActivity.populateFeeds();
        globalHandler.settingsHandler.userFeedsNeedsUpdated = false;
    }

}
