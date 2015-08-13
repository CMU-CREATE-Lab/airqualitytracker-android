package org.cmucreatelab.tasota.airprototype.activities.address_list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.tonicartos.superslim.GridSLM;
import org.cmucreatelab.tasota.airprototype.R;
import java.util.ArrayList;

/**
 * Class is based on example code from SuperSLiM's github repo: https://github.com/TonicArtos/SuperSLiM
 */
public class StickyGridAdapter extends RecyclerView.Adapter<StickyGridViewHolder> {

    private static final int VIEW_TYPE_HEADER = 0x09;
    private static final int VIEW_TYPE_CONTENT = 0x00;
    private final ArrayList<LineItem> mItems;
    private int mHeaderDisplay;
    private boolean mMarginsFixed;
    private final Context mContext;

    private static class LineItem {
        public int sectionFirstPosition;
        public boolean isHeader;
        public String text;

        public LineItem(String text, boolean isHeader, int sectionFirstPosition) {
            this.isHeader = isHeader;
            this.text = text;
            this.sectionFirstPosition = sectionFirstPosition;
        }
    }


    public StickyGridAdapter(Context context, int headerMode, boolean marginsFixed) {
        mContext = context;
        mHeaderDisplay = headerMode;
        mMarginsFixed = marginsFixed;
        mItems = new ArrayList<>();

        final String[] countryNames = context.getResources().getStringArray(R.array.country_names);
        String lastHeader = "";
        int headerCount = 0;
        int sectionFirstPosition = 0;
        for (int i = 0; i < countryNames.length; i++) {
            String header = countryNames[i].substring(0, 1);
            if (!TextUtils.equals(lastHeader, header)) {
                // Insert new header view
                sectionFirstPosition = i + headerCount;
                lastHeader = header;
                headerCount += 1;
                mItems.add(new LineItem(header, true, sectionFirstPosition));
            }
            mItems.add(new LineItem(countryNames[i], false, sectionFirstPosition));
        }
    }


    @Override
    public StickyGridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_HEADER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_title, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_box, parent, false);
        }
        return new StickyGridViewHolder(view, viewType == VIEW_TYPE_HEADER);
    }


    @Override
    public void onBindViewHolder(StickyGridViewHolder holder, int position) {
        final LineItem item = mItems.get(position);
        final View itemView = holder.itemView;

        // bind view with LineItem (populates info at this point)
        holder.bindItem(item.text);

        final GridSLM.LayoutParams lp = GridSLM.LayoutParams.from(itemView.getLayoutParams());
//        // Overrides xml attrs, could use different layouts too.
//        if (item.isHeader) {
//            lp.headerDisplay = mHeaderDisplay;
//            if (lp.isHeaderInline() || (mMarginsFixed && !lp.isHeaderOverlay())) {
//                lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
//            } else {
//                lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
//            }
//
//            lp.headerEndMarginIsAuto = !mMarginsFixed;
//            lp.headerStartMarginIsAuto = !mMarginsFixed;
//        }
        lp.setSlm(GridSLM.ID);
        lp.setNumColumns(2);
        lp.setFirstPosition(item.sectionFirstPosition);
        itemView.setLayoutParams(lp);
    }


    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).isHeader ? VIEW_TYPE_HEADER : VIEW_TYPE_CONTENT;
    }


    @Override
    public int getItemCount() {
        return mItems.size();
    }


//    private void notifyHeaderChanges() {
//        for (int i = 0; i < mItems.size(); i++) {
//            LineItem item = mItems.get(i);
//            if (item.isHeader) {
//                notifyItemChanged(i);
//            }
//        }
//    }

}
