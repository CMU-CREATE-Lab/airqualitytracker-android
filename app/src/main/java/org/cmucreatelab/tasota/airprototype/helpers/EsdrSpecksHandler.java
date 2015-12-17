package org.cmucreatelab.tasota.airprototype.helpers;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;

import org.cmucreatelab.tasota.airprototype.classes.Channel;
import org.cmucreatelab.tasota.airprototype.classes.Speck;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.JsonParser;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by mike on 12/17/15.
 */
public class EsdrSpecksHandler {

    private GlobalHandler globalHandler;
    private static EsdrSpecksHandler classInstance;


    // Nobody accesses the constructor
    private EsdrSpecksHandler(GlobalHandler globalHandler) {
        this.globalHandler = globalHandler;
    }


    // Only way to get instance of class (synchronized means thread-safe)
    // NOT PUBLIC: for public access, use GlobalHandler
    protected static synchronized EsdrSpecksHandler getInstance(GlobalHandler globalHandler) {
        if (classInstance == null) {
            classInstance = new EsdrSpecksHandler(globalHandler);
        }
        return classInstance;
    }


    public void requestSpeckFeeds(String authToken, long userId, Response.Listener<JSONObject> response) {
        int requestMethod = Request.Method.GET;
        // TODO only request fields that we want?
        String requestUrl = Constants.Esdr.API_URL + "/api/v1/feeds?whereAnd=userId="+userId+",productId=9";
        String listDevices = "";

        for (Long id: globalHandler.settingsHandler.blacklistedDevices) {
            listDevices += ",deviceId<>"+id;
        }
        requestUrl += listDevices;

        globalHandler.httpRequestHandler.sendAuthorizedJsonRequest(authToken, requestMethod, requestUrl, null, response);
    }


    public void requestSpeckDevices(String authToken, long userId, Response.Listener<JSONObject> response) {
        // generate safe URL
        String requestUrl = Constants.Esdr.API_URL + "/api/v1/devices?whereAnd=userId="+userId+",productId=9";
        String listDevices = "";

        for (Long id: globalHandler.settingsHandler.blacklistedDevices) {
            listDevices += ",id<>"+id;
        }
        requestUrl += listDevices;

        // create and send request
        int requestMethod = Request.Method.GET;
        globalHandler.httpRequestHandler.sendAuthorizedJsonRequest(authToken, requestMethod, requestUrl, null, response);
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
                    Log.e(Constants.LOG_TAG, "failed to request channel for speck apiKeyReadOnly=" + speck.getApiKeyReadOnly());
                }
            }
        };

        // generate safe URL
        requestMethod = Request.Method.GET;
        requestUrl = Constants.Esdr.API_URL + "/api/v1/feeds/" + speck.getApiKeyReadOnly();
        globalHandler.httpRequestHandler.sendJsonRequest(requestMethod, requestUrl, null, channelsResponse);
    }

}
