package org.cmucreatelab.tasota.airprototype.activities.address_list;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;

/**
 * Class is based on example code from SuperSLiM's github repo: https://github.com/TonicArtos/SuperSLiM
 */
class StickyGridViewHolder extends RecyclerView.ViewHolder {

    private View view;
    private boolean isHeader;

    StickyGridViewHolder(View view, boolean isHeader) {
        super(view);
        this.isHeader = isHeader;
        this.view = view;
    }

    public void bindItem(String text) {
        TextView mTextView;
        if (this.isHeader) {
            mTextView = (TextView) view.findViewById(R.id.textViewFragmentTitle);
        } else {
            mTextView = (TextView) view.findViewById(R.id.textView);
        }
        mTextView.setText(text);

//        view.setLongClickable(true);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(Constants.LOG_TAG, "CLICK HANDLER");
            }
        });
//        view.setOnLongClickListener(null);
    }

//    @Override
//    public String toString() {
//        return mTextView.getText().toString();
//    }

}
