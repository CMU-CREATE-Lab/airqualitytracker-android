package org.cmucreatelab.tasota.airprototype.helpers;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import org.cmucreatelab.tasota.airprototype.classes.SimpleAddress;
import org.cmucreatelab.tasota.airprototype.views.activities.FetchAddressService;

/**
 * Created by mike on 6/10/15.
 */
public class AddressResultReceiver extends ResultReceiver {

    GlobalHandler globalHandler;


    public AddressResultReceiver(Handler handler, GlobalHandler globalHandler) {
        super(handler);
        this.globalHandler = globalHandler;
    }


    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        Log.i("RESULTS", resultData.getString(FetchAddressService.Constants.RESULT_DATA_KEY));
        SimpleAddress simpleAddress = globalHandler.addresses.get(0);
        simpleAddress.setName( resultData.getString(FetchAddressService.Constants.RESULT_DATA_KEY) );
    }

}
