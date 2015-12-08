package org.cmucreatelab.tasota.airprototype.helpers;

import android.net.Uri;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.cmucreatelab.tasota.airprototype.classes.AuthorizedJsonObjectRequest;
import org.cmucreatelab.tasota.airprototype.classes.Channel;
import org.cmucreatelab.tasota.airprototype.classes.Feed;
import org.cmucreatelab.tasota.airprototype.classes.Speck;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.JsonParser;
import org.json.JSONObject;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by mike on 5/29/15.
 */
public class HttpRequestHandler implements Response.ErrorListener {

    private GlobalHandler globalHandler;
    private RequestQueue queue;
    private EsdrFeedsHandler esdrFeedsHandler;
    private EsdrAuthHandler esdrAuthHandler;
    private static HttpRequestHandler classInstance;


    // Nobody accesses the constructor
    private HttpRequestHandler(GlobalHandler globalHandler) {
        this.globalHandler = globalHandler;
        this.queue = Volley.newRequestQueue(globalHandler.appContext);
        this.esdrFeedsHandler = EsdrFeedsHandler.getInstance(globalHandler);
        this.esdrAuthHandler = EsdrAuthHandler.getInstance(globalHandler);
    }


    // Only way to get instance of class (synchronized means thread-safe)
    // NOT PUBLIC: for public access, use GlobalHandler
    protected static synchronized HttpRequestHandler getInstance(GlobalHandler globalHandler) {
        if (classInstance == null) {
            classInstance = new HttpRequestHandler(globalHandler);
        }
        return classInstance;
    }


    // helper method which assumes that you want to use this instance as the default ErrorListener
    public void sendJsonRequest(int requestMethod, String requestUrl, JSONObject requestParams, Response.Listener<JSONObject> response) {
        sendJsonRequest(requestMethod, requestUrl, requestParams, response, this);
    }


    public void sendJsonRequest(int requestMethod, String requestUrl, JSONObject requestParams, Response.Listener<JSONObject> response, Response.ErrorListener error) {
        JsonObjectRequest jsonRequest = new JsonObjectRequest(requestMethod, requestUrl, requestParams, response, error);
        if (requestParams != null) {
            Log.v(Constants.LOG_TAG, "sending JSON request with requestUrl=" + requestUrl + ", params=" + requestParams.toString());
        } else {
            Log.v(Constants.LOG_TAG, "sending JSON request with requestUrl=" + requestUrl);
        }
        this.queue.add(jsonRequest);
    }


    public void sendAuthorizedJsonRequest(String authToken, int requestMethod, String requestUrl, JSONObject requestParams, Response.Listener<JSONObject> response) {
        AuthorizedJsonObjectRequest jsonRequest = new AuthorizedJsonObjectRequest(authToken, requestMethod, requestUrl, requestParams, response, this);
        if (requestParams != null) {
            Log.v(Constants.LOG_TAG, "sending Authorized JSON request (authToken="+authToken+") with requestUrl=" + requestUrl + ", params=" + requestParams.toString());
        } else {
            Log.v(Constants.LOG_TAG, "sending Authorized JSON request (authToken="+authToken+") with requestUrl=" + requestUrl);
        }
        this.queue.add(jsonRequest);
    }


    public void requestFeeds(double latd, double longd, double maxTime, Response.Listener<JSONObject> response) {
        esdrFeedsHandler.requestFeeds(latd, longd, maxTime, response);
    }


    public void requestSpeckFeeds(String authToken, long userId, Response.Listener<JSONObject> response) {
        esdrFeedsHandler.requestSpeckFeeds(authToken, userId, response);
    }
    public void requestSpeckDevices(String authToken, long userId, Response.Listener<JSONObject> response) {
        esdrFeedsHandler.requestSpeckDevices(authToken, userId, response);
    }


    public void requestChannelReading(final Feed feed, final Channel channel) {
        esdrFeedsHandler.requestChannelReading("", feed, channel, 0);
    }


    public void requestAuthorizedChannelReading(String authToken, final Feed feed, final Channel channel) {
        esdrFeedsHandler.requestChannelReading(authToken, feed, channel,
                (long) (new Date().getTime() / 1000.0) - Constants.SPECKS_MAX_TIME_RANGE);
    }


    public void requestEsdrToken(String username, String password, Response.Listener<JSONObject> response, Response.ErrorListener error) {
        esdrAuthHandler.requestEsdrToken(username, password, response, error);
    }


    public void requestEsdrRefresh(String refreshToken) {
        esdrAuthHandler.requestEsdrRefresh(refreshToken);
    }


    public void requestGeocodingFromApi(String input, Response.Listener<JSONObject> response) {
        String requestUrl = "http://autocomplete.wunderground.com/aq?query="+Uri.encode(input)+"&c=US";
        this.sendJsonRequest(Request.Method.GET, requestUrl, null, response);
    }


    public void requestChannelsForSpeck(final Speck speck) {
        int requestMethod;
        String requestUrl;

        Response.Listener<JSONObject> channelsResponse = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject channels;
                Iterator<String> keys;
                ArrayList<Channel> listChannels = new ArrayList<>();

                try {
                    channels = response.getJSONObject("data").getJSONObject("channelBounds").getJSONObject("channels");
                    keys = channels.keys();
                    while (keys.hasNext()) {
                        // Only grab channels that we care about
                        String channelName = keys.next();
                        for (String cn : Constants.channelNames) {
                            if (channelName.equals(cn)) {
                                // TODO do we check/need-to-check for 24 hours?
                                // NOTICE: we must also make sure that this specific channel
                                // was updated in the past 24 hours ("maxTime").
                                JSONObject channel = channels.getJSONObject(channelName);
                                listChannels.add(JsonParser.parseChannelFromJson(channelName, speck, channel));
                                break;
                            }
                        }
                    }
                    speck.setChannels(listChannels);
                    speck.requestUpdate(globalHandler);
                } catch (Exception e) {
                    Log.e(Constants.LOG_TAG,"failed to request channel for speck apiKeyReadOnly="+speck.getApiKeyReadOnly());
                }
            }
        };

        // generate safe URL
        requestMethod = Request.Method.GET;
        requestUrl = Constants.Esdr.API_URL + "/api/v1/feeds/" + speck.getApiKeyReadOnly();
        sendJsonRequest(requestMethod, requestUrl, null, channelsResponse);
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e(Constants.LOG_TAG, "Received error from Volley in HttpRequestHandler (default): " + error.getLocalizedMessage());
    }

}
