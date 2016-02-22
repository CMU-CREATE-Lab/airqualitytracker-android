package org.cmucreatelab.tasota.airprototype.classes;


import android.content.Context;
import android.util.Log;

import org.cmucreatelab.tasota.airprototype.helpers.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.NowCastCalculator;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by mike on 6/1/15.
 */
public class Channel {

    public class EsdrTilesResponseHandler {
        public void onResponse(Context ctx, HashMap<Integer, ArrayList<Double>> result, int timestamp) {
            // construct array of values
            Double[] array = NowCastCalculator.constructArrayFromHash(result, timestamp);

            // find nowcast
            double nowcast = NowCastCalculator.calculate(array);
            Channel.this.nowCastValue = nowcast;
            Channel.this.feed.setReadableValueType(Feed.ReadableValueType.NOWCAST);
            GlobalHandler.getInstance(ctx).notifyGlobalDataSetChanged();
        }
    }

    private String name;
    private Feed feed;
    private double minTimeSecs;
    private double maxTimeSecs;
    private double minValue;
    private double maxValue;
    private double instantCastValue;
    private double nowCastValue;
    public final EsdrTilesResponseHandler responseHandler = new EsdrTilesResponseHandler();

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getMinTimeSecs() {
        return minTimeSecs;
    }
    public void setMinTimeSecs(double minTimeSecs) {
        this.minTimeSecs = minTimeSecs;
    }
    public double getMaxTimeSecs() {
        return maxTimeSecs;
    }
    public void setMaxTimeSecs(double maxTimeSecs) {
        this.maxTimeSecs = maxTimeSecs;
    }
    public double getMinValue() {
        return minValue;
    }
    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }
    public double getMaxValue() {
        return maxValue;
    }
    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }
    public Feed getFeed() {
        return feed;
    }
    public void setFeed(Feed feed) {
        this.feed = feed;
    }
    public long getFeed_id() {
        return feed.getFeed_id();
    }
    public double getNowCastValue() {
        return nowCastValue;
    }
    public void setNowCastValue(double nowCastValue) {
        this.nowCastValue = nowCastValue;
    }
    public double getInstantCastValue() {
        return instantCastValue;
    }
    public void setInstantCastValue(double instantCastValue) {
        this.instantCastValue = instantCastValue;
    }

    public void requestNowCast(Context ctx) {
        int timestamp = (int)(new Date().getTime() / 1000.0);

        // request tiles from ESDR
        GlobalHandler.getInstance(ctx).esdrTilesHandler.requestTilesFromChannel(this, timestamp);
    }

}
