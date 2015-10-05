package org.cmucreatelab.tasota.airprototype.activities.readable_list;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.activities.readable_show.ReadableShowActivity;
import org.cmucreatelab.tasota.airprototype.classes.*;
import org.cmucreatelab.tasota.airprototype.classes.Readable;
import org.cmucreatelab.tasota.airprototype.helpers.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Converter;

/**
 * Class is based on example code from SuperSLiM's github repo: https://github.com/TonicArtos/SuperSLiM
 */
class StickyGridView extends RecyclerView.ViewHolder
        implements View.OnClickListener,View.OnLongClickListener {

    private View view;
    private boolean isHeader;
    private ReadableListActivity context;
    private StickyGridAdapter.LineItem lineItem;
    private class CellViews {
        TextView textAddressItemLocationName,textAddressItemLocationValue,textAddressAqiLabel;
        LinearLayout background;

        public CellViews(View view) {
            textAddressItemLocationName = (TextView) view.findViewById(R.id.textAddressItemLocationName);
            textAddressItemLocationValue = (TextView) view.findViewById(R.id.textAddressItemLocationValue);
            textAddressAqiLabel = (TextView) view.findViewById(R.id.textAddressAqiLabel);
            background = (LinearLayout) view.findViewById(R.id.linearLayoutBackground);
        }
    }


    private void bindSpeck(Speck speck, CellViews cellViews) {
        int label;
        int index;

        cellViews.textAddressItemLocationName.setText(speck.getName());
        cellViews.textAddressAqiLabel.setVisibility(View.VISIBLE);
        cellViews.textAddressAqiLabel.setText(Constants.Units.MICROGRAMS_PER_CUBIC_METER);

        label = (int)speck.getFeedValue();
        cellViews.textAddressItemLocationValue.setText(String.valueOf(label));
        index = Constants.SpeckReading.getIndexFromReading(label);
        if (index >= 0) {
            try {
                cellViews.background.setBackgroundColor(Color.parseColor(Constants.SpeckReading.normalColors[index]));
            } catch (Exception e) {
                Log.w(Constants.LOG_TAG, "Failed to parse color " + Constants.SpeckReading.normalColors[index]);
            }
        }
    }


    private void bindAddress(SimpleAddress simpleAddress, CellViews cellViews) {
        double aqi;
        int index;

        cellViews.textAddressItemLocationName.setText(simpleAddress.getName());
        cellViews.textAddressAqiLabel.setVisibility(View.VISIBLE);
        cellViews.textAddressAqiLabel.setText(Constants.Units.AQI);

        aqi = Converter.microgramsToAqi(simpleAddress.getClosestFeed().getFeedValue());
        cellViews.textAddressItemLocationValue.setText(String.valueOf((int) aqi));
        index = Constants.AqiReading.getIndexFromReading(aqi);
        if (index >= 0) {
            try {
                cellViews.background.setBackgroundColor(Color.parseColor(Constants.AqiReading.aqiColors[index]));
            } catch (Exception e) {
                Log.w(Constants.LOG_TAG, "Failed to parse color " + Constants.AqiReading.aqiColors[index]);
            }
        }
    }


    private void bindDefault(Readable readable, CellViews cellViews) {
        cellViews.textAddressItemLocationName.setText(readable.getName());
        cellViews.textAddressItemLocationValue.setText(Constants.DefaultReading.DEFAULT_LOCATION);
        cellViews.textAddressAqiLabel.setVisibility(View.GONE);
        cellViews.background.setBackgroundColor(Color.parseColor(Constants.DefaultReading.DEFAULT_COLOR_BACKGROUND));
    }


    public StickyGridView(View view, boolean isHeader, ReadableListActivity context) {
        super(view);
        this.isHeader = isHeader;
        this.view = view;
        this.context = context;

        if (!isHeader) {
            TextView textView = (TextView) view.findViewById(R.id.textAddressItemLocationValue);
            Typeface fontAqi = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/Dosis-Light.ttf");
            textView.setTypeface(fontAqi);

            // TODO hides weather (for now)
            view.findViewById(R.id.frameAddressItemWeatherValue).setVisibility(View.GONE);
            view.findViewById(R.id.frameAddressItemWeatherIcon).setVisibility(View.GONE);
        }
    }


    public void bindItem(StickyGridAdapter.LineItem lineItem) {
        if (lineItem.isHeader) {
            TextView textViewFragmentTitle;

            textViewFragmentTitle = (TextView) view.findViewById(R.id.textViewFragmentTitle);
            textViewFragmentTitle.setText(lineItem.text);
        } else {
            CellViews cellViews = new CellViews(view);
            this.lineItem = lineItem;

            view.setLongClickable(true);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);

            if (lineItem.readable.hasReadableValue()) {
                switch(lineItem.readable.getReadableType()) {
                    case SPECK:
                        bindSpeck((Speck)lineItem.readable, cellViews);
                        break;
                    case ADDRESS:
                        bindAddress((SimpleAddress)lineItem.readable, cellViews);
                        break;
                    default:
                        Log.e(Constants.LOG_TAG,"Unknown readable type");
                        bindDefault(lineItem.readable, cellViews);
                        break;
                }
            } else {
                bindDefault(lineItem.readable, cellViews);
            }
        }
    }


    @Override
    public void onClick(View view) {
        if (!isHeader) {
            Log.v(Constants.LOG_TAG, "CLICK HANDLER: " + ((TextView) view.findViewById(R.id.textAddressItemLocationName)).getText());
            Intent intent = new Intent(context, ReadableShowActivity.class);
            intent.putExtra(Constants.AddressList.ADDRESS_INDEX,
                    GlobalHandler.getInstance(context).headerReadingsHashMap.adapterList.indexOf(this.lineItem));
            context.startActivity(intent);
        } else {
            Log.v(Constants.LOG_TAG, "CLICK HANDLER (header)");
        }
    }


    @Override
    public boolean onLongClick(View view) {
        if (!isHeader) {
            Log.v(Constants.LOG_TAG, "long-click on grid item");
            context.openDialogDelete(this.lineItem);
        } else {
            Log.v(Constants.LOG_TAG, "long-click (header) does nothing");
        }
        return true;
    }

}
