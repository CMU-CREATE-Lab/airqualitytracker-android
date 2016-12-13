package org.cmucreatelab.tasota.airprototype.classes.readables;

import android.util.Log;

import org.cmucreatelab.tasota.airprototype.classes.channels.Channel;
import org.cmucreatelab.tasota.airprototype.classes.channels.OzoneChannel;
import org.cmucreatelab.tasota.airprototype.classes.channels.Pm25Channel;
import org.cmucreatelab.tasota.airprototype.classes.readable_values.AqiReadableValue;
import org.cmucreatelab.tasota.airprototype.classes.readable_values.ReadableValue;
import org.cmucreatelab.tasota.airprototype.classes.readables.interfaces.OzoneReadable;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mike on 11/30/16.
 */

public class AirQualityFeed extends Pm25Feed implements OzoneReadable {

    // class attributes
    private AqiReadableValue ozoneReadableValue;
    private final ArrayList<OzoneChannel> ozoneChannels = new ArrayList<>();

    // tracking SimpleAddress
    private SimpleAddress simpleAddress;
    public SimpleAddress getAddress(){ return simpleAddress; }
    public void setAddress(SimpleAddress simpleAddress) { this.simpleAddress = simpleAddress; }

    // getters/setters
    public void setReadableOzoneValue(AqiReadableValue readableValue) {
        this.ozoneReadableValue = readableValue;
        if (getAddress() != null) {
            getAddress().setReadableOzoneValue(readableValue);
        }
    }
    public void setReadablePm25Value(AqiReadableValue readableValue) {
        super.setReadablePm25Value(readableValue);
        if (getAddress() != null) {
            getAddress().setReadablePm25Value(readableValue);
        }
    }


    public AirQualityFeed(SimpleAddress simpleAddress) {
        super();
        this.setAddress(simpleAddress);
    }


    public void addChannel(Channel channel) {

        if (channel.getClass() == Pm25Channel.class) {
            getPm25Channels().add((Pm25Channel)channel);
        } else if (channel.getClass() == OzoneChannel.class) {
            getOzoneChannels().add((OzoneChannel) channel);
        } else {
            Log.w(Constants.LOG_TAG,"could not add channel to AirQualityFeed: name="+channel.getName());
        }
    }


    // OzoneReadable implementation


    @Override
    public ArrayList<OzoneChannel> getOzoneChannels() {
        return ozoneChannels;
    }


    @Override
    public boolean hasReadableOzoneValue() {
        return (ozoneReadableValue != null);
    }


    @Override
    public AqiReadableValue getReadableOzoneValue() {
        return ozoneReadableValue;
    }


    // Readable implementation


    private ArrayList<ReadableValue> generateReadableValues() {
        ArrayList<ReadableValue> result = new ArrayList<>();
        if (hasReadablePm25Value()) {
            result.add(getReadablePm25Value());
        }
        if (hasReadableOzoneValue()) {
            result.add(ozoneReadableValue);
        }
        return result;
    }


    public boolean hasReadableValue() {
        return (generateReadableValues().size() > 0);
    }


    public List<ReadableValue> getReadableValues() {
        return generateReadableValues();
    }

}
