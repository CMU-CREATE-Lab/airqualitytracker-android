package org.cmucreatelab.tasota.airprototype.helpers.http.esdr;

import android.util.Log;
import com.android.volley.Request;
import com.android.volley.Response;
import org.cmucreatelab.tasota.airprototype.classes.channels.Channel;
import org.cmucreatelab.tasota.airprototype.classes.readable_values.Pm25_InstantCast;
import org.cmucreatelab.tasota.airprototype.classes.readables.AirQualityFeed;
import org.cmucreatelab.tasota.airprototype.classes.readables.Feed;
import org.cmucreatelab.tasota.airprototype.classes.readables.SimpleAddress;
import org.cmucreatelab.tasota.airprototype.classes.readables.Speck;
import org.cmucreatelab.tasota.airprototype.helpers.application.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.parsers.EsdrJsonParser;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.MapGeometry;
import org.cmucreatelab.tasota.airprototype.helpers.structs.Location;
import org.json.JSONObject;
import java.util.Date;

/**
 * Created by mike on 6/29/15.
 */
public class EsdrFeedsHandler {

    private GlobalHandler globalHandler;


    public EsdrFeedsHandler(GlobalHandler globalHandler) {
        this.globalHandler = globalHandler;
    }


    public void requestFeeds(Location location, double maxTime, Response.Listener<JSONObject> response) {
        int requestMethod;
        String requestUrl;
        double la1,lo1,la2,lo2;  // given lat, long, create a bounding box and search from that

        requestMethod = Request.Method.GET;
        requestUrl = Constants.Esdr.API_URL + "/api/v1/feeds";
        // only request AirNow (11) or ACHD (1)
        requestUrl += "?whereJoin=AND&whereOr=productId=11,productId=1";
        // get bounding box
        la1 = location.latitude-Constants.MapGeometry.BOUNDBOX_LAT;
        la2 = location.latitude+Constants.MapGeometry.BOUNDBOX_LONG;
        lo1 = location.longitude-Constants.MapGeometry.BOUNDBOX_LAT;
        lo2 = location.longitude+Constants.MapGeometry.BOUNDBOX_LONG;
        // within bounds, within time, and exposure=outdoor
        requestUrl += "&whereAnd=latitude>="+la1+",latitude<="+la2+",longitude>="+lo1+",longitude<="+lo2+",maxTimeSecs>="+maxTime+",exposure=outdoor";
        // only request from ESDR the fields that we care about
        requestUrl += "&fields=id,name,exposure,isMobile,latitude,longitude,productId,channelBounds";

        globalHandler.httpRequestHandler.sendJsonRequest(requestMethod, requestUrl, null, response);
    }


    // ASSERT: cannot pass both authToken and feedApiKey (if so, authToken takes priority)
    private void requestChannelReading(String authToken, String feedApiKey, final Feed feed, final Channel channel, final long maxTime) {
        int requestMethod;
        String requestUrl;
        Response.Listener<JSONObject> response;
        final String channelName = channel.getName();

        requestMethod = Request.Method.GET;
        response = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String resultValue = null;
                String resultTime = null;
                try {
                    // NOTE (from Chris)
                    // "don't expect mostRecentDataSample to always exist in the response for every channel,
                    // and don't expect channelBounds.maxTimeSecs to always equal mostRecentDataSample.timeSecs"
                    resultValue = response.getJSONObject("data")
                            .getJSONObject("channels")
                            .getJSONObject(channelName)
                            .getJSONObject("mostRecentDataSample")
                            .getString("value");
                    resultTime = response.getJSONObject("data")
                            .getJSONObject("channels")
                            .getJSONObject(channelName)
                            .getJSONObject("mostRecentDataSample")
                            .getString("timeSecs");
                } catch (Exception e) {
                    Log.w(Constants.LOG_TAG, "Failed to request Channel Readable for " + channelName);
                    e.printStackTrace();
                }
                if (resultValue != null && resultTime != null) {
                    Log.i(Constants.LOG_TAG, "got value \"" + resultValue + "\" at time " + resultTime + " for Channel " + channelName);
                    if (maxTime <= 0) {
                        feed.clearReadableValues();
                        feed.addReadableValue(new Pm25_InstantCast(Double.parseDouble(resultValue),channel));
                        feed.setLastTime(Double.parseDouble(resultTime));
                    } else {
                        // TODO there might be a better (more organized) way to verify a channel's maxTime
                        Log.e(Constants.LOG_TAG,"COMPARE maxTime="+maxTime+", resultTime="+resultTime);
                        if (maxTime <= Long.parseLong(resultTime)) {
                            feed.clearReadableValues();
                            feed.addReadableValue(new Pm25_InstantCast(Double.parseDouble(resultValue), channel));
                            feed.setLastTime(Double.parseDouble(resultTime));
                        } else {
                            feed.clearReadableValues();
                            feed.setLastTime(Double.parseDouble(resultTime));
                            Log.i(Constants.LOG_TAG,"Ignoring channel updated later than maxTime.");
                        }
                    }
                    globalHandler.notifyGlobalDataSetChanged();
                }
            }
        };

        if (authToken != null) {
            requestUrl = Constants.Esdr.API_URL + "/api/v1/feeds/" + channel.getFeed().getFeed_id() + "/channels/" + channelName + "/most-recent";
            globalHandler.httpRequestHandler.sendAuthorizedJsonRequest(authToken, requestMethod, requestUrl, null, response);
        } else if (feedApiKey != null) {
            requestUrl = Constants.Esdr.API_URL + "/api/v1/feeds/" + feedApiKey + "/channels/" + channelName + "/most-recent";
            globalHandler.httpRequestHandler.sendJsonRequest(requestMethod, requestUrl, null, response);
        } else {
            requestUrl = Constants.Esdr.API_URL + "/api/v1/feeds/" + channel.getFeed().getFeed_id() + "/channels/" + channelName + "/most-recent";
            globalHandler.httpRequestHandler.sendJsonRequest(requestMethod, requestUrl, null, response);
        }
    }


    // TODO we want to specify our requests (in particular, for 1-hour OZONE or HUMIDITY)
    public void requestChannelReading(final Feed feed, final Channel channel) {
        if (Constants.DEFAULT_ADDRESS_READABLE_VALUE_TYPE == Feed.ReadableValueType.INSTANTCAST) {
            requestChannelReading(null, null, feed, channel, 0);
        } else if (Constants.DEFAULT_ADDRESS_READABLE_VALUE_TYPE == Feed.ReadableValueType.NOWCAST) {
            channel.requestNowCast(globalHandler.appContext);
        }
    }


    public void requestUpdateFeeds(final SimpleAddress address) {
        address.feeds.clear();
        // the past 24 hours
        final double maxTime = (new Date().getTime() / 1000.0) - Constants.READINGS_MAX_TIME_RANGE;

        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                AirQualityFeed closestFeed;

                EsdrJsonParser.populateFeedsFromJson(address.feeds, response, maxTime);
                if (address.feeds.size() > 0) {
                    closestFeed = MapGeometry.getClosestFeedToAddress(address, address.feeds);
                    if (closestFeed != null) {
                        address.setClosestFeed(closestFeed);

                        // Responsible for calculating the value to be displayed
                        if (Constants.DEFAULT_ADDRESS_READABLE_VALUE_TYPE == Feed.ReadableValueType.NOWCAST) {
                            closestFeed.getPmChannels().get(0).requestNowCast(globalHandler.appContext);
                        } else if (Constants.DEFAULT_ADDRESS_READABLE_VALUE_TYPE == Feed.ReadableValueType.INSTANTCAST) {
                            // ASSERT all channels in the list of channels are usable readings
                            requestChannelReading(null, null, closestFeed, closestFeed.getPmChannels().get(0), (long)maxTime);
                        }
                    }
                } else {
                    Log.e(Constants.LOG_TAG, "result size is 0 in pullFeeds.");
                }
            }
        };
        requestFeeds(address.getLocation(), maxTime, response);
    }


    public void requestUpdate(final Speck speck) {
        if (speck.getPmChannels().size() > 0) {
            long timeRange = (long) (new Date().getTime() / 1000.0 - Constants.SPECKS_MAX_TIME_RANGE);
            requestChannelReading(null, speck.getApiKeyReadOnly(), speck, speck.getPmChannels().get(0), timeRange);
        } else {
            Log.e(Constants.LOG_TAG, "No channels found from speck id=" + speck.getFeed_id());
        }
    }

}
