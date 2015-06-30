package org.cmucreatelab.tasota.airprototype.views.services;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import org.cmucreatelab.tasota.airprototype.helpers.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.GlobalHandler;

/**
 * Created by mike on 6/30/15.
 */
// TODO this entire class may not even be necessary
public class EsdrRefreshResultReceiver extends ResultReceiver {

    GlobalHandler globalHandler;


    public EsdrRefreshResultReceiver(Handler handler, GlobalHandler globalHandler) {
        super(handler);
        this.globalHandler = globalHandler;
    }


    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
//        Log.d(Constants.LOG_TAG, "EsdrRefreshResultReceiver onReceiveResult: " + resultData.toString());
        Log.d(Constants.LOG_TAG, "EsdrRefreshResultReceiver wants to refresh tokens with refreshToken="+globalHandler.settingsHandler.refreshToken);
    }

}
