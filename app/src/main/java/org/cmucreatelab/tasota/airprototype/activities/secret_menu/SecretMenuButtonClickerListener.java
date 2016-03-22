package org.cmucreatelab.tasota.airprototype.activities.secret_menu;

import android.content.Context;
import android.view.View;
import org.cmucreatelab.tasota.airprototype.classes.readables.Feed;
import org.cmucreatelab.tasota.airprototype.helpers.application.GlobalHandler;
import java.util.ArrayList;

/**
 * Created by mike on 3/22/16.
 */
public class SecretMenuButtonClickerListener implements View.OnClickListener {

    final private GlobalHandler globalHandler;


    public SecretMenuButtonClickerListener(Context ctx) {
        globalHandler = GlobalHandler.getInstance(ctx.getApplicationContext());
    }


    @Override
    public void onClick(View view) {
        ArrayList<SecretMenuListFeedsAdapter.ListFeedsItem> items = globalHandler.readingsHandler.debugFeedsList;
        for (SecretMenuListFeedsAdapter.ListFeedsItem item : items) {
            if (!item.isHeader) {
                Feed feed = item.feed;
                globalHandler.esdrFeedsHandler.requestChannelReading(feed, feed.getChannels().get(0));
            }
        }
    }

}
