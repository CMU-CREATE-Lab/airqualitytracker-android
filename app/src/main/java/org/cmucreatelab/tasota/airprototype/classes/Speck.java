package org.cmucreatelab.tasota.airprototype.classes;

import android.util.Log;
import org.cmucreatelab.tasota.airprototype.helpers.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;

/**
 * Created by mike on 8/14/15.
 */
public class Speck extends Feed {

    private static final Type readableType = Readable.Type.SPECK;
    public Type getReadableType() {
        return readableType;
    }


    public Speck(Feed feed, int deviceId) {
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

    // TODO add speck device-specific attributes
    protected int deviceId;

    // getters and setters for device-specific attributes
    public int getDeviceId() {
        return deviceId;
    }
    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
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
