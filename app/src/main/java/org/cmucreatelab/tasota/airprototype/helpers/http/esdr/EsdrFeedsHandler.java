package org.cmucreatelab.tasota.airprototype.helpers.http.esdr;

import android.util.Log;
import com.android.volley.Request;
import com.android.volley.Response;
import org.cmucreatelab.tasota.airprototype.classes.channels.Channel;
import org.cmucreatelab.tasota.airprototype.classes.channels.HumidityChannel;
import org.cmucreatelab.tasota.airprototype.classes.channels.OzoneChannel;
import org.cmucreatelab.tasota.airprototype.classes.channels.Pm25Channel;
import org.cmucreatelab.tasota.airprototype.classes.channels.TemperatureChannel;
import org.cmucreatelab.tasota.airprototype.classes.readable_values.HumidityValue;
import org.cmucreatelab.tasota.airprototype.classes.readable_values.Ozone_InstantCast;
import org.cmucreatelab.tasota.airprototype.classes.readable_values.Pm25_InstantCast;
import org.cmucreatelab.tasota.airprototype.classes.readable_values.TemperatureValue;
import org.cmucreatelab.tasota.airprototype.classes.readables.AirQualityFeed;
import org.cmucreatelab.tasota.airprototype.classes.readables.Feed;
import org.cmucreatelab.tasota.airprototype.classes.readables.Pm25Feed;
import org.cmucreatelab.tasota.airprototype.classes.readables.SimpleAddress;
import org.cmucreatelab.tasota.airprototype.classes.readables.Speck;
import org.cmucreatelab.tasota.airprototype.classes.readables.interfaces.Readable;
import org.cmucreatelab.tasota.airprototype.helpers.application.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.structs.Location;
import org.json.JSONObject;

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
    public void requestChannelReading(String authToken, String feedApiKey, final Pm25Feed feed, final Channel channel, final long maxTime) {
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
                        if (channel.getClass() == Pm25Channel.class) {
                            feed.setReadablePm25Value(new Pm25_InstantCast(Double.parseDouble(resultValue), channel));
                        } else if (channel.getClass() == OzoneChannel.class) {
                            ((AirQualityFeed)feed).setReadableOzoneValue(new Ozone_InstantCast(Double.parseDouble(resultValue), channel));
                        } else if (channel.getClass() == HumidityChannel.class) {
                            ((Speck)feed).setReadableHumidityValue(new HumidityValue(Double.parseDouble(resultValue), (HumidityChannel)channel));
                        } else if (channel.getClass() == TemperatureChannel.class) {
                            ((Speck)feed).setReadableTemperatureValue(new TemperatureValue(Double.parseDouble(resultValue), (TemperatureChannel)channel));
                        }
                        feed.setLastTime(Double.parseDouble(resultTime));
                    } else {
                        // TODO there might be a better (more organized) way to verify a channel's maxTime
                        Log.e(Constants.LOG_TAG,"COMPARE maxTime="+maxTime+", resultTime="+resultTime);
                        if (maxTime <= Long.parseLong(resultTime)) {
                            if (channel.getClass() == Pm25Channel.class) {
                                feed.setReadablePm25Value(new Pm25_InstantCast(Double.parseDouble(resultValue), channel));
                            } else if (channel.getClass() == OzoneChannel.class) {
                                ((AirQualityFeed)feed).setReadableOzoneValue(new Ozone_InstantCast(Double.parseDouble(resultValue), channel));
                            } else if (channel.getClass() == HumidityChannel.class) {
                                ((Speck)feed).setReadableHumidityValue(new HumidityValue(Double.parseDouble(resultValue), (HumidityChannel)channel));
                            } else if (channel.getClass() == TemperatureChannel.class) {
                                ((Speck)feed).setReadableTemperatureValue(new TemperatureValue(Double.parseDouble(resultValue), (TemperatureChannel)channel));
                            }
                            feed.setLastTime(Double.parseDouble(resultTime));
                        } else {
                            if (channel.getClass() == Pm25Channel.class) {
                                feed.setReadablePm25Value(null);
                            } else if (channel.getClass() == OzoneChannel.class) {
                                ((AirQualityFeed)feed).setReadableOzoneValue(null);
                            } else if (channel.getClass() == HumidityChannel.class) {
                                ((Speck)feed).setReadableHumidityValue(null);
                            } else if (channel.getClass() == TemperatureChannel.class) {
                                ((Speck)feed).setReadableTemperatureValue(null);
                            }
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
    public void requestChannelReading(final Pm25Feed feed, final Channel channel) {
        if (Constants.DEFAULT_ADDRESS_PM25_READABLE_VALUE_TYPE == Feed.ReadableValueType.INSTANTCAST) {
            requestChannelReading(null, null, feed, channel, 0);
        } else if (Constants.DEFAULT_ADDRESS_PM25_READABLE_VALUE_TYPE == Feed.ReadableValueType.NOWCAST) {
            channel.requestNowCast(globalHandler.appContext);
        }
    }


    public void requestUpdate(Readable readable) {
        if (readable.getClass() == SimpleAddress.class) {
            SimpleAddress simpleAddress = (SimpleAddress)readable;
            simpleAddress.requestReadablePm25Reading(globalHandler);
            simpleAddress.requestReadableOzoneReading(globalHandler);
        } else if (readable.getClass() == Speck.class) {
            Speck speck = (Speck)readable;
            speck.requestReadablePm25Reading(globalHandler);
            speck.requestReadableHumidityReading(globalHandler);
            speck.requestReadableTemperatureReading(globalHandler);
        } else {
            Log.e(Constants.LOG_TAG, "Tried to requestUpdate on Readable "+readable.getName()+" but class not supported: "+readable.getClass());
        }
    }

}
