package org.cmucreatelab.tasota.airprototype.activities.login;

import android.util.Log;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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

    }

    @Override
    public void onResponse(JSONObject response) {
        Log.d(Constants.LOG_TAG, "GOT RESPONSE! " + response.toString());
        loginActivity.listFeedsUser.clear();
        JsonParser.populateFeedsFromJson(loginActivity.listFeedsUser, response);
        loginActivity.populateFeeds();
    }

}
