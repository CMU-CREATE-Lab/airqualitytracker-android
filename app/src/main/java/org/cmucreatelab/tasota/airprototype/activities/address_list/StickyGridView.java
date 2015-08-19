package org.cmucreatelab.tasota.airprototype.activities.address_list;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.activities.address_show.AddressShowActivity;
import org.cmucreatelab.tasota.airprototype.classes.*;
import org.cmucreatelab.tasota.airprototype.helpers.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.classes.Readable;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Converter;

/**
 * Class is based on example code from SuperSLiM's github repo: https://github.com/TonicArtos/SuperSLiM
 */
class StickyGridView extends RecyclerView.ViewHolder
        implements View.OnClickListener,View.OnLongClickListener {

    private View view;
    private boolean isHeader;
    private AddressListActivity context;
    private StickyGridAdapter.LineItem lineItem;

    StickyGridView(View view, boolean isHeader, AddressListActivity context) {
        super(view);
        this.isHeader = isHeader;
        this.view = view;
        this.context = context;

        if (!isHeader) {
            TextView textView = (TextView) view.findViewById(R.id.textAddressItemLocationValue);
            Typeface fontAqi = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/Dosis-Light.ttf");
            textView.setTypeface(fontAqi);

            // TODO hides weather (for now)
            view.findViewById(R.id.frameAddressItemWeatherValue).setVisibility(View.GONE);;
            view.findViewById(R.id.frameAddressItemWeatherIcon).setVisibility(View.GONE);;
        }
    }


    public void bindHeader(StickyGridAdapter.LineItem lineItem) {
        TextView textViewFragmentTitle;
        textViewFragmentTitle = (TextView) view.findViewById(R.id.textViewFragmentTitle);
        textViewFragmentTitle.setText(lineItem.text);
        // TODO also handle as header?
//        view.setLongClickable(true);
//        view.setOnClickListener(this);
//        view.setOnLongClickListener(this);
    }


    public void bindItem(StickyGridAdapter.LineItem lineItem) {
        TextView textAddressItemLocationName,textAddressItemLocationValue,textAddressAqiLabel;
        LinearLayout background;
        Readable readable;

        this.lineItem = lineItem;
        readable = lineItem.readable;
        textAddressItemLocationName = (TextView) view.findViewById(R.id.textAddressItemLocationName);
        textAddressItemLocationValue = (TextView) view.findViewById(R.id.textAddressItemLocationValue);
        textAddressAqiLabel = (TextView) view.findViewById(R.id.textAddressAqiLabel);
        background = (LinearLayout) view.findViewById(R.id.linearLayoutBackground);

        view.setLongClickable(true);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);

        switch(readable.getReadableType()) {
            case SPECK:
                Speck speck = (Speck)readable;
                Log.i(Constants.LOG_TAG,"Found Speck name="+speck.getName());

                textAddressItemLocationName.setText(speck.getName());
                if (speck.getFeedValue() <= 0.0) {
                    textAddressItemLocationValue.setText("N/A");
                    textAddressAqiLabel.setVisibility(View.GONE);
                    background.setBackgroundColor(Color.parseColor("#404041"));
                } else {
                    int label = (int)speck.getFeedValue();
                    textAddressItemLocationValue.setText(String.valueOf(label));

                    double aqi = Converter.microgramsToAqi(label);
                    textAddressAqiLabel.setVisibility(View.VISIBLE);
                    textAddressAqiLabel.setText("µg/m³");
                    int index = Constants.AqiReading.getIndexFromReading(aqi);
                    if (index >= 0) {
                        try {
                            background.setBackgroundColor(Color.parseColor(Constants.SpeckReading.normalColors[index]));
                        } catch (Exception e) {
                            // Has to catch failure to parse (0x doesn't work but # does because Java is trash)
                            Log.w(Constants.LOG_TAG, "Failed to parse color " + Constants.SpeckReading.normalColors[index]);
                        }
                    }
                }
                break;
            case ADDRESS:
                SimpleAddress simpleAddress = (SimpleAddress)readable;

                textAddressItemLocationName.setText(simpleAddress.getName());
                if (simpleAddress.getClosestFeed() == null) {
                    textAddressItemLocationValue.setText("N/A");
                } else {
                    double label = Converter.microgramsToAqi(simpleAddress.getClosestFeed().getFeedValue());
                    textAddressItemLocationValue.setText(String.valueOf((int)label));
                }

                // Description info based on closest feed reading
                if (simpleAddress.getClosestFeed() == null) {
                    textAddressAqiLabel.setVisibility(View.GONE);
                    background.setBackgroundColor(Color.parseColor("#404041"));
                } else {
                    double aqi = Converter.microgramsToAqi(simpleAddress.getClosestFeed().getFeedValue());
                    textAddressAqiLabel.setVisibility(View.VISIBLE);
                    textAddressAqiLabel.setText("AQI");
                    int index = Constants.AqiReading.getIndexFromReading(aqi);
                    if (index >= 0) {
                        try {
                            background.setBackgroundColor(Color.parseColor(Constants.AqiReading.aqiColors[index]));
                        } catch (Exception e) {
                            // Has to catch failure to parse (0x doesn't work but # does because Java is trash)
                            Log.w(Constants.LOG_TAG, "Failed to parse color " + Constants.AqiReading.aqiColors[index]);
                        }
                    }
                }
                break;
            default:
                Log.e(Constants.LOG_TAG,"Unknown readable type");
                break;
        }
    }


    @Override
    public void onClick(View view) {
        if (!isHeader) {
            Log.i(Constants.LOG_TAG, "CLICK HANDLER: " + ((TextView) view.findViewById(R.id.textAddressItemLocationName)).getText());
            Intent intent = new Intent(context, AddressShowActivity.class);
            intent.putExtra(Constants.AddressList.ADDRESS_INDEX,
                    GlobalHandler.getInstance(context).headerReadingsHashMap.adapterList.indexOf(this.lineItem));
            context.startActivity(intent);
        } else {
            Log.i(Constants.LOG_TAG, "CLICK HANDLER (header)");
        }
    }


    @Override
    public boolean onLongClick(View view) {
        if (!isHeader) {
            Log.i(Constants.LOG_TAG, "long-click on grid item");
            context.openDialogDelete(this.lineItem);
        } else {
            Log.i(Constants.LOG_TAG, "long-click (header) does nothing");
        }
        return true;
    }

}
