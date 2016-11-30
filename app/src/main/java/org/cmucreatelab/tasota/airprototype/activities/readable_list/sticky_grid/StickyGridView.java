package org.cmucreatelab.tasota.airprototype.activities.readable_list.sticky_grid;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.activities.readable_list.ReadableListActivity;
import org.cmucreatelab.tasota.airprototype.activities.readable_show.ReadableShowActivity;
import org.cmucreatelab.tasota.airprototype.classes.aqi_scales.AQIReading;
import org.cmucreatelab.tasota.airprototype.classes.aqi_scales.SpeckReading;
import org.cmucreatelab.tasota.airprototype.classes.readables.Readable;
import org.cmucreatelab.tasota.airprototype.classes.readables.SimpleAddress;
import org.cmucreatelab.tasota.airprototype.classes.readables.Speck;
import org.cmucreatelab.tasota.airprototype.helpers.application.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.AqiConverter;

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
        TextView textAddressItemLocationName,textAddressItemLocationValue,textAddressAqiLabel,textCurrentLocation;
        LinearLayout background;

        public CellViews(View view) {
            textAddressItemLocationName = (TextView) view.findViewById(R.id.textAddressItemLocationName);
            textAddressItemLocationValue = (TextView) view.findViewById(R.id.textAddressItemLocationValue);
            textAddressAqiLabel = (TextView) view.findViewById(R.id.textAddressAqiLabel);
            textCurrentLocation = (TextView) view.findViewById(R.id.textCurrentLocation);
            background = (LinearLayout) view.findViewById(R.id.linearLayoutBackground);
        }

        public void showCurrentLocation(boolean visible) {
            if (visible) {
                textCurrentLocation.setVisibility(View.VISIBLE);
            } else {
                textCurrentLocation.setVisibility(View.INVISIBLE);
            }
        }
    }


    private void bindSpeck(Speck speck, CellViews cellViews) {
        int label;

        cellViews.textAddressItemLocationName.setText(speck.getName());
        cellViews.textAddressAqiLabel.setVisibility(View.VISIBLE);
        cellViews.textAddressAqiLabel.setText(Constants.Units.MICROGRAMS_PER_CUBIC_METER);

        label = speck.hasReadableValue() ? (int)speck.getReadableValues().get(0).getValue() : 0;
        cellViews.textAddressItemLocationValue.setText(String.valueOf(label));
        SpeckReading speckReading = new SpeckReading(label);
        cellViews.background.setBackgroundColor(Color.parseColor(speckReading.getColor()));
    }


    private void bindAddress(SimpleAddress simpleAddress, CellViews cellViews) {
        double aqi,micrograms;

        cellViews.textAddressItemLocationName.setText(simpleAddress.getName());
        cellViews.textAddressAqiLabel.setVisibility(View.VISIBLE);
        cellViews.textAddressAqiLabel.setText(Constants.Units.AQI);

        if (simpleAddress.isCurrentLocation()) {
            cellViews.textCurrentLocation.setVisibility(View.VISIBLE);
        }

        micrograms = simpleAddress.hasReadableValue() ? simpleAddress.getClosestFeed().getReadableValues().get(0).getValue() : 0.0;
        aqi = AqiConverter.microgramsToAqi(micrograms);
        cellViews.textAddressItemLocationValue.setText(String.valueOf((int) aqi));
        AQIReading aqiReading = new AQIReading(micrograms);
        cellViews.background.setBackgroundColor(Color.parseColor(aqiReading.getColor()));
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

            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    int action = motionEvent.getActionMasked();
                    switch (action) {
                        case MotionEvent.ACTION_POINTER_DOWN:
                            Log.i(Constants.LOG_TAG,"ACTION_POINTER_DOWN");
                            if (motionEvent.getPointerCount() == 2) {
                                context.longPressTimer.startTimer();
                            } else {
                                context.longPressTimer.stopTimer();
                            }
                            break;
                        case MotionEvent.ACTION_POINTER_UP:
                            context.longPressTimer.stopTimer();
                            break;
                    }
                    return true;
                }
            });
        } else {
            CellViews cellViews = new CellViews(view);
            this.lineItem = lineItem;

            view.setLongClickable(true);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            cellViews.textCurrentLocation.setVisibility(View.INVISIBLE);

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
                    GlobalHandler.getInstance(context).readingsHandler.adapterList.indexOf(this.lineItem));
            context.startActivity(intent);
        } else {
            Log.v(Constants.LOG_TAG, "CLICK HANDLER (header)");
        }
    }


    @Override
    public boolean onLongClick(View view) {
        if (!isHeader) {
            Log.v(Constants.LOG_TAG, "long-click on grid item");
            context.openDeleteDialog(this.lineItem);
        } else {
            Log.v(Constants.LOG_TAG, "long-click (header) does nothing");
        }
        return true;
    }

}
