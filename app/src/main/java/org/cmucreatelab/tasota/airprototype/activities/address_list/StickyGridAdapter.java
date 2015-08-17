package org.cmucreatelab.tasota.airprototype.activities.address_list;

import android.content.Context;
import android.provider.Settings;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.tonicartos.superslim.GridSLM;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.classes.*;
import org.cmucreatelab.tasota.airprototype.classes.Readable;
import org.cmucreatelab.tasota.airprototype.helpers.GlobalHandler;

import java.lang.reflect.Array;
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

    public static class LineItem {
        public int sectionFirstPosition;
        public boolean isHeader;
        public String text;
        public Readable readable;

        public LineItem(String text, boolean isHeader, int sectionFirstPosition) {
            this.isHeader = isHeader;
            this.text = text;
            this.sectionFirstPosition = sectionFirstPosition;
        }

        public LineItem(boolean isHeader, int sectionFirstPosition, Readable r) {
            this("", isHeader, sectionFirstPosition);
            this.readable = r;
        }
    }

    // TODO delete (only meant to test dataset changing after creation)
    public void test() {
        GlobalHandler globalHandler = GlobalHandler.getInstance(mContext);
        ArrayList<SimpleAddress> addresses = globalHandler.headerReadingsHashMap.addresses;
        SimpleAddress simpleAddress = addresses.get(addresses.size()-1);
        int sectionFirstPosition = mItems.get(mItems.size()-1).sectionFirstPosition;
        // Test change of entire structure
        ArrayList<LineItem> items = (ArrayList<LineItem>)mItems.clone();
        mItems.clear();
        mItems.addAll(items);
        //
        mItems.add(new LineItem(false, sectionFirstPosition, simpleAddress));
        notifyDataSetChanged();
    }


    public StickyGridAdapter(Context context, int headerMode, boolean marginsFixed) {
        mContext = context;
        mHeaderDisplay = headerMode;
        mMarginsFixed = marginsFixed;
//        mItems = new ArrayList<>();

        GlobalHandler globalHandler = GlobalHandler.getInstance(context);
        globalHandler.gridAdapter = this;
        mItems = globalHandler.headerReadingsHashMap.adapterList;
//        ArrayList<SimpleAddress> addresses = globalHandler.requestAddressesForDisplay();
////        ArrayList<SimpleAddress> specks;
//
//        int headerCount = 0;
//        int sectionFirstPosition = 0;
//        int position = 0;
//        // header 1
//        // Insert new header view
////        sectionFirstPosition = headerCount + position;
//        headerCount += 1;
//        mItems.add(new LineItem("SPECK DEVICES", true, sectionFirstPosition));
//        // grid cells
//        // TODO use real specks object
//        for (SimpleAddress simpleAddress : addresses) {
//            position += 1;
//            mItems.add(new LineItem(simpleAddress.getName(), false, sectionFirstPosition));
//        }
//        // header 2
//        // Insert new header view
//        sectionFirstPosition = headerCount + position;
//        headerCount += 1;
//        mItems.add(new LineItem("CITIES", true, sectionFirstPosition));
//        // grid cells
//        for (SimpleAddress simpleAddress : addresses) {
//            position += 1;
//            mItems.add(new LineItem(simpleAddress.getName(), false, sectionFirstPosition));
//        }
//
////        final String[] countryNames = context.getResources().getStringArray(R.array.country_names);
////        String lastHeader = "";
////        int headerCount = 0;
////        int sectionFirstPosition = 0;
////        for (int i = 0; i < countryNames.length; i++) {
////            String header = countryNames[i].substring(0, 1);
////            if (!TextUtils.equals(lastHeader, header)) {
////                // Insert new header view
////                sectionFirstPosition = i + headerCount;
////                lastHeader = header;
////                headerCount += 1;
////                mItems.add(new LineItem(header, true, sectionFirstPosition));
////            }
////            mItems.add(new LineItem(countryNames[i], false, sectionFirstPosition));
////        }
    }


    @Override
    public StickyGridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_HEADER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_title, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.__address_list__grid_item_address, parent, false);
        }
        return new StickyGridViewHolder(view, viewType == VIEW_TYPE_HEADER, this);
    }


    @Override
    public void onBindViewHolder(StickyGridViewHolder holder, int position) {
        final LineItem item = mItems.get(position);
        final View itemView = holder.itemView;

        // bind view with LineItem (populates info at this point)
//        holder.bindItem(item.text);
        if (item.isHeader) {
            holder.bindHeader(item.text);
        } else {
            holder.bindItem(item.readable);
        }

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
