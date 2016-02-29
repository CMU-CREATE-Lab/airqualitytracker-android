package org.cmucreatelab.tasota.airprototype.helpers.system.services.gps;

import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.util.Log;

import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.application.GlobalHandler;

/**
 * Created by mike on 6/10/15.
 */
public class AddressResultReceiver extends ResultReceiver {

    GlobalHandler globalHandler;
    // TODO placeholder to avoid errors. Implement later
    public static Parcelable.Creator CREATOR;


    public AddressResultReceiver(Handler handler, GlobalHandler globalHandler) {
        super(handler);
        this.globalHandler = globalHandler;
    }


    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        Log.v(Constants.LOG_TAG, "onReceiveResult: " + resultData.getString(Constants.AddressIntent.RESULT_DATA_KEY));
        globalHandler.readingsHandler.gpsReadingHandler.gpsAddress.setName(resultData.getString(Constants.AddressIntent.RESULT_DATA_KEY));
    }

}
