package org.cmucreatelab.tasota.airprototype.classes.channels;

import android.content.Context;
import org.cmucreatelab.tasota.airprototype.classes.readables.Feed;
import org.cmucreatelab.tasota.airprototype.helpers.application.GlobalHandler;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mike on 8/22/16.
 */
public class Pm25Channel extends Channel {


    public void onEsdrTilesResponse(Context ctx, HashMap<Integer, ArrayList<Double>> result, int timestamp) {
        // find nowcast
        double nowcast = nowCastCalculator.calculate(result, timestamp);
        this.nowCastValue = nowcast;
        this.feed.setReadableValueType(Feed.ReadableValueType.NOWCAST);
        GlobalHandler.getInstance(ctx).notifyGlobalDataSetChanged();
    }

}
