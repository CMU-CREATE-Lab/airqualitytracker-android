package org.cmucreatelab.tasota.airprototype.helpers;

import android.content.Context;
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

    private Context appContext;
    private HttpRequestHandler httpRequestHandler;
    private static EsdrAuthHandler classInstance;


    // Nobody accesses the constructor
    private EsdrAuthHandler(Context ctx, HttpRequestHandler httpRequestHandler) {
        this.appContext = ctx;
        this.httpRequestHandler = httpRequestHandler;
    }


    // Only way to get instance of class (synchronized means thread-safe)
    // NOT PUBLIC: for public access, use GlobalHandler
    protected static synchronized EsdrAuthHandler getInstance(Context ctx, HttpRequestHandler httpRequestHandler) {
        if (classInstance == null) {
            classInstance = new EsdrAuthHandler(ctx, httpRequestHandler);
        }
        return classInstance;
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
            requestParams.put("client_id", Constants.Esdr.CLIENT_ID);
            requestParams.put("client_secret", Constants.Esdr.CLIENT_SECRET);
            requestParams.put("username", username);
            requestParams.put("password", password);
            httpRequestHandler.sendJsonRequest(requestMethod, requestUrl, requestParams, response, error);
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
            requestParams.put("client_id", Constants.Esdr.CLIENT_ID);
            requestParams.put("client_secret", Constants.Esdr.CLIENT_SECRET);
            requestParams.put("refresh_token", refreshToken);
            response = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    String accessToken,refreshToken;
                    Log.v(Constants.LOG_TAG,"requestEsdrRefresh: got response="+response.toString());
                    try {
                        accessToken = response.getString("access_token");
                        refreshToken = response.getString("refresh_token");
                        GlobalHandler.getInstance(appContext).settingsHandler.updateEsdrTokens(accessToken, refreshToken);
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
                    GlobalHandler globalHandler = GlobalHandler.getInstance(appContext);
                    globalHandler.settingsHandler.removeEsdrAccount();
                    globalHandler.servicesHandler.stopEsdrRefreshService();
                }
            };
            httpRequestHandler.sendJsonRequest(requestMethod, requestUrl, requestParams, response, error);
        } catch (Exception e) {
            Log.w(Constants.LOG_TAG, "Failed to refresh ESDR Token for refresh_token=" + refreshToken);
            e.printStackTrace();
        }
    }

}