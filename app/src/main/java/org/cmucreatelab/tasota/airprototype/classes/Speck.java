package org.cmucreatelab.tasota.airprototype.classes;


/**
 * Created by mike on 8/14/15.
 */
public class Speck extends Feed implements Readable {

    private static final Readable.Type readableType = Readable.Type.SPECK;
    public Readable.Type getReadableType() {
        return readableType;
    }

    public Speck(Feed feed) {
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
    }

    // TODO add speck device-specific attributes

}
