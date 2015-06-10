package org.cmucreatelab.tasota.airprototype.helpers;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.views.activities.FetchAddressService;

/**
 * Created by mike on 6/10/15.
 */
public class AddressResultReceiver extends ResultReceiver{
    public AddressResultReceiver(Handler handler) {
        super(handler);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {

        // Display the address string
        // or an error message sent from the intent service.
        Log.i("RESULTS", resultData.getString(FetchAddressService.Constants.RESULT_DATA_KEY));
//        displayAddressOutput();

    }

}
