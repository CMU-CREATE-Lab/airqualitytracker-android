package org.cmucreatelab.tasota.airprototype.activities.address_list;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;

/**
 * Class is based on example code from SuperSLiM's github repo: https://github.com/TonicArtos/SuperSLiM
 */
class StickyGridViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener,View.OnLongClickListener {

    private View view;
    private boolean isHeader;
    private StickyGridAdapter adapter;

    StickyGridViewHolder(View view, boolean isHeader, StickyGridAdapter adapter) {
        super(view);
        this.isHeader = isHeader;
        this.view = view;
        this.adapter = adapter;

        if (!isHeader) {
            TextView textView = (TextView) view.findViewById(R.id.textAddressItemLocationValue);
            Typeface fontAqi = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/Dosis-Light.ttf");
            textView.setTypeface(fontAqi);

            view.findViewById(R.id.frameAddressItemWeatherValue).setVisibility(View.GONE);;
            view.findViewById(R.id.frameAddressItemWeatherIcon).setVisibility(View.GONE);;
        }
    }

    public void bindItem(String text) {
        TextView mTextView;
        if (this.isHeader) {
            mTextView = (TextView) view.findViewById(R.id.textViewFragmentTitle);
            // TODO also handle as header?
//            view.setLongClickable(true);
//            view.setOnClickListener(this);
//            view.setOnLongClickListener(this);
        } else {
            mTextView = (TextView) view.findViewById(R.id.textAddressItemLocationName);
            view.setLongClickable(true);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }
        mTextView.setText(text);

    }


    @Override
    public void onClick(View view) {
        if (!isHeader) {
            Log.i(Constants.LOG_TAG, "CLICK HANDLER: " + ((TextView) view.findViewById(R.id.textAddressItemLocationName)).getText());
        } else {
            Log.i(Constants.LOG_TAG, "CLICK HANDLER (header)");
        }
    }


    @Override
    public boolean onLongClick(View view) {
        Log.i(Constants.LOG_TAG, "LONG-CLICK HANDLER");
        // TODO delete
        adapter.test();
        return true;
    }

}
