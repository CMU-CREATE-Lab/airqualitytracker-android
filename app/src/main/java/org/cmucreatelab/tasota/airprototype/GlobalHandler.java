package org.cmucreatelab.tasota.airprototype;

import android.content.Context;

import com.android.volley.toolbox.Volley;

import org.cmucreatelab.tasota.airprototype.classes.Feed;

import java.util.ArrayList;

/**
 * Created by mike on 6/2/15.
 */
public class GlobalHandler {

    private static GlobalHandler classInstance;
    private Context appContext;
    public ArrayList<Feed> feeds;

    // Nobody accesses the constructor
    private GlobalHandler(Context ctx) {
        this.appContext = ctx;
        this.feeds = new ArrayList<>();
    }


    // Only public way to get instance of class (synchronized means thread-safe)
    public static synchronized GlobalHandler getInstance(Context ctx) {
        if (classInstance == null) {
            classInstance = new GlobalHandler(ctx);
        }
        return classInstance;
    }

}
