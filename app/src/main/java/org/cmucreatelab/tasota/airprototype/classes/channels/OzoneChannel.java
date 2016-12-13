package org.cmucreatelab.tasota.airprototype.classes.channels;

import android.content.Context;
import android.util.Log;
import org.cmucreatelab.tasota.airprototype.classes.readable_values.Ozone_InstantCast;
import org.cmucreatelab.tasota.airprototype.classes.readable_values.Ozone_NowCast;
import org.cmucreatelab.tasota.airprototype.classes.readables.AirQualityFeed;
import org.cmucreatelab.tasota.airprototype.helpers.application.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by mike on 8/22/16.
 */
public class OzoneChannel extends Channel<AirQualityFeed> {

    protected NowCastCalculator nowCastCalculator = new NowCastCalculator(8, NowCastCalculator.WeightType.RATIO);


    public void onEsdrTilesResponse(Context ctx, Channel channel, HashMap<Integer, ArrayList<Double>> result, int timestamp) {
        // find ozone nowcast and instantcast
        double nowcast = nowCastCalculator.calculate(result, timestamp);
        Ozone_NowCast ozoneNowCast = new Ozone_NowCast(nowcast, channel);
        Ozone_InstantCast ozoneInstantCast = new Ozone_InstantCast(nowCastCalculator.getMostRecent(result, timestamp), channel);

        // compare ozone AQIs, setting the one with a higher AQI value
        Log.d(Constants.LOG_TAG,"Comparing Ozone NowCast vs. InstantCast AQIs: " + ozoneNowCast.getAqiValue() + ", " + ozoneInstantCast.getAqiValue());
        if (ozoneInstantCast.getAqiValue() > ozoneNowCast.getAqiValue()) {
            this.feed.setReadableOzoneValue(ozoneInstantCast);
        } else {
            this.feed.setReadableOzoneValue(ozoneNowCast);
        }
        GlobalHandler.getInstance(ctx).notifyGlobalDataSetChanged();
    }


    public void requestNowCast(Context ctx) {
        int timestamp = (int)(new Date().getTime() / 1000.0);

        // request tiles from ESDR
        GlobalHandler.getInstance(ctx).esdrTilesHandler.requestTilesFromChannel(this, timestamp);
    }

}
