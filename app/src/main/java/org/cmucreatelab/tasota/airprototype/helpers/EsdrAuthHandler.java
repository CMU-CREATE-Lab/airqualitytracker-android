package org.cmucreatelab.tasota.airprototype.helpers;

import android.util.Log;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.json.JSONObject;

/**
 * Created by mike on 6/29/15.
 */
public class EsdrAuthHandler {

    private GlobalHandler globalHandler;


    // GlobalHandler accesses the constructor
    protected EsdrAuthHandler(GlobalHandler globalHandler) {
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
        Response.Listener<JSONObject> response;
        int requestMethod;
        JSONObject requestParams;
        String requestUrl;

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
                    Log.v(Constants.LOG_TAG,"requestEsdrRefresh: got response="+response.toString());
                    try {
                        accessToken = response.getString("access_token");
                        refreshToken = response.getString("refresh_token");
                        globalHandler.esdrLoginHandler.updateEsdrTokens(accessToken, refreshToken);
                    } catch (Exception e) {
                        Log.w(Constants.LOG_TAG, "Failed to parse ESDR refresh tokens from JSON=" + response.toString());
                        e.printStackTrace();
                    }
                }
            };
            Response.ErrorListener error = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(Constants.LOG_TAG, "Volley received error from refreshToken=" + refreshToken);
                    globalHandler.esdrLoginHandler.removeEsdrAccount();
                    globalHandler.servicesHandler.stopEsdrRefreshService();
                }
            };
            globalHandler.httpRequestHandler.sendJsonRequest(requestMethod, requestUrl, requestParams, response, error);
        } catch (Exception e) {
            Log.w(Constants.LOG_TAG, "Failed to refresh ESDR Token for refresh_token=" + refreshToken);
            e.printStackTrace();
        }
    }

}
