package org.cmucreatelab.tasota.airprototype.activities.readable_list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.tonicartos.superslim.GridSLM;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.classes.Readable;
import org.cmucreatelab.tasota.airprototype.helpers.GlobalHandler;
import java.util.ArrayList;

/**
 * Class is based on example code from SuperSLiM's github repo: https://github.com/TonicArtos/SuperSLiM
 */
public class StickyGridAdapter extends RecyclerView.Adapter<StickyGridView> {

    private static final int VIEW_TYPE_HEADER = 0x09;
    private static final int VIEW_TYPE_CONTENT = 0x00;
    private final ArrayList<LineItem> mItems;
    private int mHeaderDisplay;
    private boolean mMarginsFixed;
    private final ReadableListActivity mContext;

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


    public StickyGridAdapter(ReadableListActivity context, int headerMode, boolean marginsFixed) {
        mContext = context;
        mHeaderDisplay = headerMode;
        mMarginsFixed = marginsFixed;

        GlobalHandler globalHandler = GlobalHandler.getInstance(context);
        globalHandler.gridAdapter = this;
        mItems = globalHandler.headerReadingsHashMap.adapterList;
    }


    @Override
    public StickyGridView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_HEADER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.__readable_list__stickygrid_header, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.__readable_list__stickygrid_item, parent, false);
        }
        return new StickyGridView(view, viewType == VIEW_TYPE_HEADER, this.mContext);
    }


    @Override
    public void onBindViewHolder(StickyGridView holder, int position) {
        final LineItem item = mItems.get(position);
        final View itemView = holder.itemView;

        // bind view with LineItem (populates info at this point)
        if (item.isHeader) {
            holder.bindHeader(item);
        } else {
            holder.bindItem(item);
        }

        final GridSLM.LayoutParams lp = GridSLM.LayoutParams.from(itemView.getLayoutParams());
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

}
