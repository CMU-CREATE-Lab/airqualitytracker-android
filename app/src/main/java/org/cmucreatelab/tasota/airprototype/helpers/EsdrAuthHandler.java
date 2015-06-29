package org.cmucreatelab.tasota.airprototype.helpers;

import android.content.Context;
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


    public void requestEsdrToken(String username, String password) {
        // TODO calls to esdr from obtain_tokens.sh
        // curl -X POST -H "Content-Type:application/json" https://esdr.cmucreatelab.org/oauth/token -d @my_client.json
        //        my_client.json
        //        {
        //            "grant_type" : "password",
        //                "client_id" : "client_id",
        //                "client_secret" : "This will never work",
        //                "username" : "name@example.com",
        //                "password" : "password"
        //        }
        //
        try {
            JSONObject requestParams = new JSONObject();
            requestParams.put("username", username);
            // ...
            //httpRequestHandler.sendJsonRequest(requestMethod, requestUrl, requestParams, response);
        } catch (Exception e) {
            // TODO handle errors
        }
    }


    public void requestEsdrRefresh(String refreshToken) {
        // TODO calls to esdr from refresh_tokens.sh
        // curl -X POST -H "Content-Type:application/json" https://esdr.cmucreatelab.org/oauth/token -d @refresh_client.json
        //        {
        //            "grant_type" : "refresh_token",
        //                "client_id" : "client_id",
        //                "client_secret" : "This will never work",
        //                "refresh_token" : "d1053fc68ae8b1e35bbaba56d45f34b2fef0cc8788f56130e9cc08e500589e19"
        //        }
        //
        try {
            JSONObject requestParams = new JSONObject();
            requestParams.put("refresh_token", refreshToken);
            // ...
            //httpRequestHandler.sendJsonRequest(requestMethod, requestUrl, requestParams, response);
        } catch (Exception e) {
            // TODO handle errors
        }
    }

}
