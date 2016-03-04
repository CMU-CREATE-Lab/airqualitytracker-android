package org.cmucreatelab.tasota.airprototype.activities.readable_show;

import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.classes.readables.Readable;
import org.cmucreatelab.tasota.airprototype.classes.readables.SimpleAddress;
import org.cmucreatelab.tasota.airprototype.classes.readables.Speck;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.AqiConverter;

/**
 * Created by mike on 6/18/15.
 */
public class LinearViewReadableShow {

    private Readable readable;
//    private TextView textShowAddressName;
    private TextView textShowAddressAqiValue;
    private TextView textShowAddressAqiRange;
    private TextView textShowAddressAqiTitle;
    private TextView textShowAddressAqiDescription;
    private TextView textShowAddressAqiLabel;
    private RelativeLayout layoutShowAddress;
    private FrameLayout frameClosestFeed;
    private TextView textViewClosestFeedName;
    private Button buttonAqiExplanation;
    private Button buttonAirNow;
    final private ReadableShowActivity context;
    private AlertDialogReadableShow alertDialogHelper;


    private void defaultView() {
        Log.w(Constants.LOG_TAG, "warning: using default populateLinearView");
        this.textShowAddressAqiValue.setText("");
        textShowAddressAqiRange.setText("");
        textShowAddressAqiLabel.setVisibility(View.INVISIBLE);
        textShowAddressAqiTitle.setText(Constants.DefaultReading.DEFAULT_TITLE);
        textShowAddressAqiDescription.setText(Constants.DefaultReading.DEFAULT_DESCRIPTION);
        layoutShowAddress.setBackgroundColor(Color.parseColor(Constants.DefaultReading.DEFAULT_COLOR_BACKGROUND));
        this.frameClosestFeed.setVisibility(View.INVISIBLE);

        // hide buttons
        buttonAirNow.setVisibility(View.INVISIBLE);
        buttonAqiExplanation.setVisibility(View.INVISIBLE);
    }


    private void addressView(final SimpleAddress address) {
        int aqi,index;

        aqi = (int) AqiConverter.microgramsToAqi(address.getReadableValue());
        this.textShowAddressAqiValue.setText(String.valueOf(aqi));
        index = Constants.AqiReading.getIndexFromReading(aqi);
        if (index < 0) {
            this.defaultView();
        } else {
            textShowAddressAqiRange.setText(Constants.AqiReading.getRangeFromIndex(index) + " AQI");
            textShowAddressAqiTitle.setText(Constants.AqiReading.titles[index]);
            textShowAddressAqiDescription.setText(Constants.AqiReading.descriptions[index]);
            layoutShowAddress.setBackgroundResource(Constants.AqiReading.aqiDrawableGradients[index]);
            textShowAddressAqiLabel.setText(Constants.Units.RANGE_AQI);
            this.frameClosestFeed.setVisibility(View.VISIBLE);
            this.textViewClosestFeedName.setText(address.getClosestFeed().getName());
            this.frameClosestFeed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i(Constants.LOG_TAG, "Just clicked frameClosestFeed!");
                    alertDialogHelper = new AlertDialogReadableShow(context, address.getClosestFeed());
                    alertDialogHelper.getAlertDialog().show();
                }
            });
        }
    }


    private void speckView(Speck speck) {
        int index;

        this.textShowAddressAqiValue.setText(String.valueOf((long)speck.getReadableValue()));
        index = Constants.SpeckReading.getIndexFromReading((long)speck.getReadableValue());
        if (index < 0) {
            this.defaultView();
        } else {
            textShowAddressAqiRange.setText(Constants.SpeckReading.getRangeFromIndex(index) + " Micrograms");
            textShowAddressAqiTitle.setText(Constants.SpeckReading.titles[index]);
            // TODO descriptions for speck
//                    textShowAddressAqiDescription.setText(Constants.SpeckReading.descriptions[ugIndex]);
            textShowAddressAqiDescription.setText(Constants.AqiReading.descriptions[index]);
            layoutShowAddress.setBackgroundColor(Color.parseColor(Constants.SpeckReading.normalColors[index]));
            textShowAddressAqiLabel.setText(Constants.Units.RANGE_MICROGRAMS_PER_CUBIC_METER);
            this.frameClosestFeed.setVisibility(View.INVISIBLE);

            // hide buttons
            buttonAirNow.setVisibility(View.INVISIBLE);
            buttonAqiExplanation.setVisibility(View.INVISIBLE);
        }
    }


    public LinearViewReadableShow(ReadableShowActivity activity, Readable readable) {
        this.context = activity;
        this.readable = readable;
        this.textShowAddressAqiValue = (TextView)activity.findViewById(R.id.textShowAddressAqiValue);
        this.textShowAddressAqiRange = (TextView)activity.findViewById(R.id.textShowAddressAqiRange);
        this.textShowAddressAqiTitle = (TextView)activity.findViewById(R.id.textShowAddressAqiTitle);
        this.textShowAddressAqiDescription = (TextView)activity.findViewById(R.id.textShowAddressAqiDescription);
        this.textShowAddressAqiLabel = (TextView)activity.findViewById(R.id.textShowAddressAqiLabel);
        this.layoutShowAddress = (RelativeLayout)activity.findViewById(R.id.layoutShowAddress);
        this.frameClosestFeed = (FrameLayout)activity.findViewById(R.id.frameClosestFeed);
        this.textViewClosestFeedName = (TextView)activity.findViewById(R.id.textViewClosestFeedName);
        this.buttonAqiExplanation = (Button)activity.findViewById(R.id.buttonAqiExplanation);
        this.buttonAirNow = (Button)activity.findViewById(R.id.buttonAirNow);

        // use custom fonts
        Typeface fontAqi = Typeface.createFromAsset(activity.getAssets(), "fonts/Dosis-Light.ttf");
        textShowAddressAqiValue.setTypeface(fontAqi);

        // click listeners
        buttonAqiExplanation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(Constants.LOG_TAG, "clicked buttonAqiExplanation");
            }
        });

        buttonAirNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(Constants.LOG_TAG, "clicked buttonAirNow");
            }
        });
    }


    public void populateLinearView() {
//        this.textShowAddressName.setText(readable.getName());

        if (readable.hasReadableValue()) {
            double readableValue = readable.getReadableValue();

            if (readable.getReadableType() == Readable.Type.ADDRESS) {
                addressView((SimpleAddress)readable);
            } else if (readable.getReadableType() == Readable.Type.SPECK) {
                speckView((Speck)readable);
            } else {
                Log.w(Constants.LOG_TAG, "Tried to populate LinearView in AddressShow with unimplemented Readable object.");
                defaultView();
            }
        } else {
            defaultView();
        }
    }

}
