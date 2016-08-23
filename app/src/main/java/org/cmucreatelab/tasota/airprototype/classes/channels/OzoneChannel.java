package org.cmucreatelab.tasota.airprototype.classes.channels;

import android.content.Context;
import org.cmucreatelab.tasota.airprototype.classes.readables.Feed;
import org.cmucreatelab.tasota.airprototype.helpers.application.GlobalHandler;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by mike on 8/22/16.
 */
public class OzoneChannel extends Channel {

    public Type channelType = Type.OZONE;
    protected NowCastCalculator nowCastCalculator = new NowCastCalculator(8, NowCastCalculator.WeightType.RATIO);


    public void onEsdrTilesResponse(Context ctx, HashMap<Integer, ArrayList<Double>> result, int timestamp) {
        // find nowcast
        double nowcast = nowCastCalculator.calculate(result, timestamp);
        this.nowCastValue = nowcast;
        this.feed.setReadableValueType(Feed.ReadableValueType.NOWCAST);
        GlobalHandler.getInstance(ctx).notifyGlobalDataSetChanged();
    }


    public void requestNowCast(Context ctx) {
        int timestamp = (int)(new Date().getTime() / 1000.0);

        // request tiles from ESDR
        GlobalHandler.getInstance(ctx).esdrTilesHandler.requestTilesFromChannel(this, timestamp);
    }

}
