package org.cmucreatelab.tasota.airprototype.views.activities;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import org.cmucreatelab.tasota.airprototype.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by mike on 6/10/15.
 */
public class FetchAddressService extends IntentService {

    protected ResultReceiver resultReceiver;

    public final class Constants {
        private static final int MAX_RESULTS = 1;
        public static final int SUCCESS_RESULT = 0;
        public static final int FAILURE_RESULT = 1;
        public static final String PACKAGE_NAME =
                "org.cmucreatelab.tasota.airprototype";
        public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
        public static final String RESULT_DATA_KEY = PACKAGE_NAME +
                ".RESULT_DATA_KEY";
        public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME +
                ".LOCATION_DATA_EXTRA";
    }


    private void deliverResultToReceiver(int resultCode, String message) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESULT_DATA_KEY, message);
        resultReceiver.send(resultCode, bundle);
    }

    public FetchAddressService(String name) {
        super(name);
    }
    public FetchAddressService() {
        super("");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        double latd,longd;
        Geocoder geocoder;
        List<Address> results = null;

        geocoder = new Geocoder(this, Locale.getDefault());
        latd = intent.getDoubleExtra("latitude",0.0);
        longd = intent.getDoubleExtra("longitude",0.0);
        this.resultReceiver = intent.getParcelableExtra(Constants.RECEIVER);
        try {
            results = geocoder.getFromLocation(latd,longd,Constants.MAX_RESULTS);
        } catch (Exception e) {
            // TODO handle exception
            e.printStackTrace();
        }

        if (results == null || results.size() == 0) {
            deliverResultToReceiver(Constants.FAILURE_RESULT, "FAILED");
        } else {
            Address address = results.get(0);
            ArrayList<String> addressFragments = new ArrayList<String>();

            // Fetch the address lines using getAddressLine,
            // join them, and send them to the thread.
            for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }
            deliverResultToReceiver(Constants.SUCCESS_RESULT, TextUtils.join(System.getProperty("line.separator"), addressFragments));
        }

//        deliverResultToReceiver();
    }

}
