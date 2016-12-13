package org.cmucreatelab.tasota.airprototype.classes.channels;

import android.content.Context;
import org.cmucreatelab.tasota.airprototype.classes.readables.Speck;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mike on 8/24/16.
 */
public class TemperatureChannel extends Channel<Speck> {


    @Override
    public void onEsdrTilesResponse(Context ctx, Channel channel, HashMap<Integer, ArrayList<Double>> result, int timestamp) {
        // TODO actions
    }

}
