package org.cmucreatelab.tasota.airprototype.views.services;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.ResultReceiver;
import org.cmucreatelab.tasota.airprototype.helpers.Constants;
import java.util.List;
import java.util.Locale;

/**
 * Created by mike on 6/10/15.
 */
public class FetchAddressIntentService extends IntentService {

    protected ResultReceiver resultReceiver;


    private void deliverResultToReceiver(int resultCode, String message) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.AddressIntent.RESULT_DATA_KEY, message);
        resultReceiver.send(resultCode, bundle);
    }


    public FetchAddressIntentService() {
        super("");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        double latd,longd;
        Geocoder geocoder;
        List<Address> results = null;

        geocoder = new Geocoder(this, Locale.getDefault());
        latd = intent.getDoubleExtra("latitude",0.0);
        longd = intent.getDoubleExtra("longitude", 0.0);
        this.resultReceiver = intent.getParcelableExtra(Constants.AddressIntent.RECEIVER);
        try {
            results = geocoder.getFromLocation(latd,longd,Constants.AddressIntent.MAX_RESULTS);
        } catch (Exception e) {
            // TODO handle exception
            e.printStackTrace();
        }

        if (results == null || results.size() == 0) {
            deliverResultToReceiver(Constants.AddressIntent.FAILURE_RESULT, "FAILED");
        } else {
            Address address = results.get(0);
//            ArrayList<String> addressFragments = new ArrayList<String>();
//            // Fetch the address lines using getAddressLine,
//            // join them, and send them to the thread.
//            for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
//                addressFragments.add(address.getAddressLine(i));
//            }
//            deliverResultToReceiver(Constants.SUCCESS_RESULT, TextUtils.join(System.getProperty("line.separator"), addressFragments));

            deliverResultToReceiver(Constants.AddressIntent.SUCCESS_RESULT, address.getAddressLine(address.getMaxAddressLineIndex()-1) );
        }
    }

}
