package org.cmucreatelab.tasota.airprototype.classes.channels;

import android.content.Context;

import org.cmucreatelab.tasota.airprototype.classes.readable_values.Pm25_NowCast;
import org.cmucreatelab.tasota.airprototype.classes.readables.Feed;
import org.cmucreatelab.tasota.airprototype.helpers.application.GlobalHandler;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by mike on 6/1/15.
 */
public class Channel<T extends Feed> {

    // class attributes
    protected String name;
    protected T feed;
    protected double minTimeSecs;
    protected double maxTimeSecs;
    protected double minValue;
    protected double maxValue;
    protected double instantCastValue;
    protected double nowCastValue;
    /** Set calculator for PM2.5 by default (12-hour averaging, with piecewise weight factor) */
    protected NowCastCalculator nowCastCalculator = new NowCastCalculator(12, NowCastCalculator.WeightType.PIECEWISE);
    // getters/setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public T getFeed() { return feed; }
    public void setFeed(T feed) { this.feed = feed; }
    public double getMinTimeSecs() { return minTimeSecs; }
    public void setMinTimeSecs(double minTimeSecs) { this.minTimeSecs = minTimeSecs; }
    public double getMaxTimeSecs() { return maxTimeSecs; }
    public void setMaxTimeSecs(double maxTimeSecs) { this.maxTimeSecs = maxTimeSecs; }
    public double getMinValue() { return minValue; }
    public void setMinValue(double minValue) { this.minValue = minValue; }
    public double getMaxValue() { return maxValue; }
    public void setMaxValue(double maxValue) { this.maxValue = maxValue; }
    public double getInstantCastValue() { return instantCastValue; }
    public void setInstantCastValue(double instantCastValue) { this.instantCastValue = instantCastValue; }
    public double getNowCastValue() { return nowCastValue; }
    public void setNowCastValue(double nowCastValue) { this.nowCastValue = nowCastValue; }


    public void onEsdrTilesResponse(Context ctx, Channel channel, HashMap<Integer, ArrayList<Double>> result, int timestamp) {
        // find nowcast
        double nowcast = nowCastCalculator.calculate(result, timestamp);
        this.nowCastValue = nowcast;
        // TODO add readable value to Readable (but really, this method gets overwritten everywhere anyways)
        //this.feed.setPm25ReadableValue(new Pm25_NowCast(nowcast, channel));
        GlobalHandler.getInstance(ctx).notifyGlobalDataSetChanged();
    }


    public void requestNowCast(Context ctx) {
        int timestamp = (int)(new Date().getTime() / 1000.0);

        // request tiles from ESDR
        GlobalHandler.getInstance(ctx).esdrTilesHandler.requestTilesFromChannel(this, timestamp);
    }

}
