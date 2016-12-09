package org.cmucreatelab.tasota.airprototype.classes.channels;

import android.content.Context;

import org.cmucreatelab.tasota.airprototype.classes.readable_values.Ozone_NowCast;
import org.cmucreatelab.tasota.airprototype.classes.readable_values.Pm25_NowCast;
import org.cmucreatelab.tasota.airprototype.classes.readables.AirQualityFeed;
import org.cmucreatelab.tasota.airprototype.classes.readables.Feed;
import org.cmucreatelab.tasota.airprototype.helpers.application.GlobalHandler;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by mike on 8/22/16.
 */
public class OzoneChannel extends Channel<AirQualityFeed> {

    protected NowCastCalculator nowCastCalculator = new NowCastCalculator(8, NowCastCalculator.WeightType.RATIO);


    public void onEsdrTilesResponse(Context ctx, Channel channel, HashMap<Integer, ArrayList<Double>> result, int timestamp) {
        // find nowcast
        // TODO calculate the Ozone nowcast (uses different calc)
        double nowcast = nowCastCalculator.calculate(result, timestamp);
        this.nowCastValue = nowcast;
        this.feed.setReadableOzoneValue(new Ozone_NowCast(nowcast, channel));
        GlobalHandler.getInstance(ctx).notifyGlobalDataSetChanged();
    }


    public void requestNowCast(Context ctx) {
        int timestamp = (int)(new Date().getTime() / 1000.0);

        // request tiles from ESDR
        GlobalHandler.getInstance(ctx).esdrTilesHandler.requestTilesFromChannel(this, timestamp);
    }

}
