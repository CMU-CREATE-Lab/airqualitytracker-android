package org.cmucreatelab.tasota.airprototype.classes.readables;

import android.util.Log;

import org.cmucreatelab.tasota.airprototype.classes.channels.Channel;
import org.cmucreatelab.tasota.airprototype.classes.channels.HumidityChannel;
import org.cmucreatelab.tasota.airprototype.classes.channels.Pm25Channel;
import org.cmucreatelab.tasota.airprototype.classes.channels.TemperatureChannel;
import org.cmucreatelab.tasota.airprototype.classes.readable_values.ReadableValue;
import org.cmucreatelab.tasota.airprototype.classes.readables.interfaces.HumidityReadable;
import org.cmucreatelab.tasota.airprototype.classes.readables.interfaces.Readable;
import org.cmucreatelab.tasota.airprototype.classes.readables.interfaces.TemperatureReadable;
import org.cmucreatelab.tasota.airprototype.helpers.application.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.structs.Location;
import java.lang.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by mike on 8/14/15.
 */
public class Speck extends Pm25Feed implements HumidityReadable, TemperatureReadable {

    // class attributes
    protected long deviceId;
    private int positionId;
    private long _id;
    private String apiKeyReadOnly;
    private ReadableValue readableHumidityValue, readableTemperatureValue;
    private final ArrayList<HumidityChannel> humidityChannels = new ArrayList<>();
    private final ArrayList<TemperatureChannel> temperatureChannels = new ArrayList<>();
    // getters/setters
//    public void setChannels(Collection<Channel> channels) { this.channels.clear(); this.channels.addAll(channels); }
    public long getDeviceId() { return deviceId; }
    public void setDeviceId(long deviceId) { this.deviceId = deviceId; }
    public int getPositionId() { return positionId; }
    public void setPositionId(int positionId) { this.positionId = positionId; }
    public long get_id() { return _id; }
    public void set_id(long _id) { this._id = _id; }
    public String getApiKeyReadOnly() { return apiKeyReadOnly; }
    public void setApiKeyReadOnly(String apiKeyReadOnly) { this.apiKeyReadOnly = apiKeyReadOnly; }
    public void setReadableHumidityValue(ReadableValue readableValue) { this.readableHumidityValue = readableValue; }
    public void setReadableTemperatureValue(ReadableValue readableValue) { this.readableTemperatureValue = readableValue; }


    // class constructor
    public Speck(Feed feed, long deviceId) {
        this.feed_id = feed.feed_id;
        this.name = feed.name;
        this.exposure = feed.exposure;
        this.isMobile = feed.isMobile;
        this.location = feed.location;
        this.productId = feed.productId;
//        this.channels = feed.channels;
        this.lastTime = feed.lastTime;
        this.deviceId = deviceId;
    }


    // class constructor
    public Speck(String apiKeyReadOnly, long deviceId, String exposure, long feedId, boolean isMobile, Location location, String name, int positionId, long productId) {
        this.apiKeyReadOnly = apiKeyReadOnly;
        this.deviceId = deviceId;
        this.exposure = exposure;
        this.feed_id = feedId;
        this.isMobile = isMobile;
        this.location = location;
        this.name = name;
        this.positionId = positionId;
        this.productId = productId;
    }


    public void addChannel(Channel channel) {
        if (channel.getClass() == Pm25Channel.class) {
            getPm25Channels().add((Pm25Channel)channel);
        } else if (channel.getClass() == HumidityChannel.class) {
            getHumidityChannels().add((HumidityChannel)channel);
        } else if (channel.getClass() == TemperatureChannel.class) {
            getTemperatureChannels().add((TemperatureChannel)channel);
        } else {
            Log.w(Constants.LOG_TAG,"could not add channel to Speck: name="+channel.getName());
        }
    }


//    public ArrayList<Channel> getHumidityChannels() {
//        ArrayList<Channel> result = new ArrayList<>();
//
//        for (Channel channel : this.channels) {
//            if (channel.getClass() == HumidityChannel.class) {
//                result.add(channel);
//            }
//        }
//
//        return result;
//    }


//    public double getHumidityValue() {
//        return getHumidityChannels().get(0).getInstantCastValue();
//    }

    public void requestReadablePm25Reading(final GlobalHandler globalHandler) {
        if (getPm25Channels().size() > 0) {
            long timeRange = (long) (new Date().getTime() / 1000.0 - Constants.SPECKS_MAX_TIME_RANGE);
            globalHandler.esdrFeedsHandler.requestChannelReading(null, getApiKeyReadOnly(), this, getPm25Channels().get(0), timeRange);
        } else {
            Log.e(Constants.LOG_TAG, "No PM25 channels found from speck id=" + this.getFeed_id());
        }
    }
    public void requestReadableHumidityReading(final GlobalHandler globalHandler) {
        if (getHumidityChannels().size() > 0) {
            long timeRange = (long) (new Date().getTime() / 1000.0 - Constants.SPECKS_MAX_TIME_RANGE);
            globalHandler.esdrFeedsHandler.requestChannelReading(null, getApiKeyReadOnly(), this, getHumidityChannels().get(0), timeRange);
        } else {
            Log.e(Constants.LOG_TAG, "No Humidity channels found from speck id=" + this.getFeed_id());
        }
    }
    public void requestReadableTemperatureReading(final GlobalHandler globalHandler) {
        if (getTemperatureChannels().size() > 0) {
            long timeRange = (long) (new Date().getTime() / 1000.0 - Constants.SPECKS_MAX_TIME_RANGE);
            globalHandler.esdrFeedsHandler.requestChannelReading(null, getApiKeyReadOnly(), this, getTemperatureChannels().get(0), timeRange);
        } else {
            Log.e(Constants.LOG_TAG, "No Temperature channels found from speck id=" + this.getFeed_id());
        }
    }


    // Readable implementation


    private static final Type readableType = Readable.Type.SPECK;


    private ArrayList<ReadableValue> generateReadableValues() {
        ArrayList<ReadableValue> result = new ArrayList<>();
        if (hasReadablePm25Value()) {
            result.add(getReadablePm25Value());
        }
        if (hasReadableHumidityValue()) {
            result.add(getReadableHumidityValue());
        }
        if (hasReadableTemperatureValue()) {
            result.add(getReadableTemperatureValue());
        }
        return result;
    }


    public Type getReadableType() {
        return readableType;
    }


    @Override
    public boolean hasReadableValue() {
        return (generateReadableValues().size() > 0);
    }


    @Override
    public List<ReadableValue> getReadableValues() {
        return generateReadableValues();
    }


    // HumidityReadable implementation


    @Override
    public ArrayList<HumidityChannel> getHumidityChannels() {
        return humidityChannels;
    }


    @Override
    public boolean hasReadableHumidityValue() {
        return (readableHumidityValue != null);
    }


    @Override
    public ReadableValue getReadableHumidityValue() {
        return readableHumidityValue;
    }


    // TemperatureReadable implementation


    @Override
    public ArrayList<TemperatureChannel> getTemperatureChannels() {
        return temperatureChannels;
    }


    @Override
    public boolean hasReadableTemperatureValue() {
        return (readableTemperatureValue != null);
    }


    @Override
    public ReadableValue getReadableTemperatureValue() {
        return readableTemperatureValue;
    }

}
