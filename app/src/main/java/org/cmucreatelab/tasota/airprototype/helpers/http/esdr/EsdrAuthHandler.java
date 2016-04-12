package org.cmucreatelab.tasota.airprototype.helpers.http.esdr;

import android.util.Log;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import org.cmucreatelab.tasota.airprototype.helpers.application.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.json.JSONObject;
import java.util.Date;

/**
 * Created by mike on 6/29/15.
 */
public class EsdrAuthHandler {

    private GlobalHandler globalHandler;


    public EsdrAuthHandler(GlobalHandler globalHandler) {
        this.globalHandler = globalHandler;
    }


    public void requestEsdrToken(String username, String password, Response.Listener<JSONObject> response, Response.ErrorListener error) {
        int requestMethod;
        JSONObject requestParams;
        String requestUrl;

        try {
            // (Volley) adds "Content-Type:application/json" to header by default when using JsonObjectRequest
            requestMethod = Request.Method.POST;
            requestUrl = Constants.Esdr.API_URL + "/oauth/token";
            requestParams = new JSONObject();
            requestParams.put("grant_type", Constants.Esdr.GRANT_TYPE_TOKEN);
            requestParams.put("client_id", Constants.AppSecrets.ESDR_CLIENT_ID);
            requestParams.put("client_secret", Constants.AppSecrets.ESDR_CLIENT_SECRET);
            requestParams.put("username", username);
            requestParams.put("password", password);
            globalHandler.httpRequestHandler.sendJsonRequest(requestMethod, requestUrl, requestParams, response, error);
        } catch (Exception e) {
            Log.w(Constants.LOG_TAG, "Failed to request ESDR Token for username=" + username);
            e.printStackTrace();
        }
    }


    public void requestEsdrRefresh(final String refreshToken) {
        Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(Constants.LOG_TAG, "Volley received error from refreshToken=" + refreshToken);
                globalHandler.esdrLoginHandler.updateEsdrTokens("", "", 0);
                globalHandler.servicesHandler.stopEsdrRefreshService();
            }
        };

        requestEsdrRefresh(refreshToken,error);
    }


    public void requestEsdrRefresh(final String refreshToken, Response.ErrorListener error) {
        Response.Listener<JSONObject> response;
        int requestMethod;
        JSONObject requestParams;
        String requestUrl;

        if (!Constants.REFRESHES_ESDR_TOKEN) {
            Log.w(Constants.LOG_TAG, "requested ESDR refresh but REFRESHES_ESDR_TOKEN is not set.");
            return;
        }

        try {
            // (Volley) adds "Content-Type:application/json" to header by default when using JsonObjectRequest
            requestMethod = Request.Method.POST;
            requestUrl = Constants.Esdr.API_URL + "/oauth/token";
            requestParams = new JSONObject();
            requestParams.put("grant_type", Constants.Esdr.GRANT_TYPE_REFRESH);
            requestParams.put("client_id", Constants.AppSecrets.ESDR_CLIENT_ID);
            requestParams.put("client_secret", Constants.AppSecrets.ESDR_CLIENT_SECRET);
            requestParams.put("refresh_token", refreshToken);
            response = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    String accessToken,refreshToken;
                    long timestamp,expiresIn;
                    Log.v(Constants.LOG_TAG,"requestEsdrRefresh: got response="+response.toString());
                    try {
                        timestamp = (long) (new Date().getTime() / 1000.0);
                        expiresIn = response.getLong("expires_in");
                        accessToken = response.getString("access_token");
                        refreshToken = response.getString("refresh_token");
                        globalHandler.esdrLoginHandler.updateEsdrTokens(accessToken, refreshToken, timestamp+expiresIn);
                    } catch (Exception e) {
                        Log.w(Constants.LOG_TAG, "Failed to parse ESDR refresh tokens from JSON=" + response.toString());
                        e.printStackTrace();
                    }
                }
            };
            globalHandler.httpRequestHandler.sendJsonRequest(requestMethod, requestUrl, requestParams, response, error);
        } catch (Exception e) {
            Log.w(Constants.LOG_TAG, "Failed to refresh ESDR Token for refresh_token=" + refreshToken);
            e.printStackTrace();
        }
    }


    // Alert the user that their account has been logged out and will be prompted to re-enter username/password
    public boolean alertLogout() {
        if (globalHandler.esdrLoginHandler.isUserLoggedIn() && globalHandler.esdrAccount.getExpiresAt() <= 0) {
            globalHandler.esdrLoginHandler.removeEsdrAccount();
            globalHandler.servicesHandler.stopEsdrRefreshService();
            return true;
        }
        return false;
    }

}
