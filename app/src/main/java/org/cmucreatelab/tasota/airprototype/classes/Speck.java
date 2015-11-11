package org.cmucreatelab.tasota.airprototype.classes;

import android.util.Log;
import org.cmucreatelab.tasota.airprototype.helpers.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import java.util.Collection;

/**
 * Created by mike on 8/14/15.
 */
public class Speck extends Feed {

    private static final Type readableType = Readable.Type.SPECK;
    public Type getReadableType() {
        return readableType;
    }

    public void setChannels(Collection<Channel> channels) {
        this.channels.clear();
        this.channels.addAll(channels);
    }


    public Speck(Feed feed, long deviceId) {
        this.feed_id = feed.feed_id;
        this.name = feed.name;
        this.exposure = feed.exposure;
        this.isMobile = feed.isMobile;
        this.latitude = feed.latitude;
        this.longitude = feed.longitude;
        this.productId = feed.productId;
        this.channels = feed.channels;
        this.feedValue = feed.feedValue;
        this.lastTime = feed.lastTime;
        this.deviceId = deviceId;
    }


    public Speck(String apiKeyReadOnly, long deviceId, String exposure, long feedId, boolean isMobile, double latitude, double longitude, String name, int positionId, long productId) {
        this.apiKeyReadOnly = apiKeyReadOnly;
        this.deviceId = deviceId;
        this.exposure = exposure;
        this.feed_id = feedId;
        this.isMobile = isMobile;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.positionId = positionId;
        this.productId = productId;
    }

    // TODO add speck device-specific attributes
    protected long deviceId;
    private int positionId;
    private long _id;
    private String apiKeyReadOnly;

    // getters and setters for device-specific attributes
    public long getDeviceId() {
        return deviceId;
    }
    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }
    public int getPositionId() {
        return positionId;
    }
    public void setPositionId(int positionId) {
        this.positionId = positionId;
    }
    public long get_id() {
        return _id;
    }
    public void set_id(long _id) {
        this._id = _id;
    }
    public String getApiKeyReadOnly() {
        return apiKeyReadOnly;
    }
    public void setApiKeyReadOnly(String apiKeyReadOnly) {
        this.apiKeyReadOnly = apiKeyReadOnly;
    }

    public void requestUpdate(final GlobalHandler globalHandler) {
        if (this.getChannels().size() > 0) {
            // ASSERT all channels in the list of channels are usable readings
            globalHandler.httpRequestHandler.requestAuthorizedChannelReading(globalHandler.settingsHandler.getAccessToken(), this, this.getChannels().get(0));
        } else {
            Log.e(Constants.LOG_TAG, "No channels found from speck id=" + this.getFeed_id());
        }
    }

}
