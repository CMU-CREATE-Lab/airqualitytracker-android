package org.cmucreatelab.tasota.airprototype.activities.readable_list.sticky_grid;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.tonicartos.superslim.GridSLM;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.activities.readable_list.ReadableListActivity;
import org.cmucreatelab.tasota.airprototype.classes.readables.interfaces.Readable;
import org.cmucreatelab.tasota.airprototype.helpers.application.GlobalHandler;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import java.util.ArrayList;

/**
 * Class is based on example code from SuperSLiM's github repo: https://github.com/TonicArtos/SuperSLiM
 */
public class StickyGridAdapter extends RecyclerView.Adapter<StickyGridView> {

    private final ArrayList<LineItem> lineItemsList;
    private final ReadableListActivity context;

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


    public StickyGridAdapter(ReadableListActivity context) {
        this.context = context;
        GlobalHandler globalHandler = GlobalHandler.getInstance(context);
        globalHandler.gridAdapter = this;
        lineItemsList = globalHandler.readingsHandler.adapterList;
    }


    @Override
    public StickyGridView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == Constants.StickyGrid.VIEW_TYPE_HEADER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.__readable_list__stickygrid_header, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.__readable_list__stickygrid_item, parent, false);
        }
        return new StickyGridView(view, viewType == Constants.StickyGrid.VIEW_TYPE_HEADER, this.context);
    }


    @Override
    public void onBindViewHolder(StickyGridView holder, int position) {
        final LineItem item = lineItemsList.get(position);
        final View itemView = holder.itemView;
        final GridSLM.LayoutParams lp = GridSLM.LayoutParams.from(itemView.getLayoutParams());

        // bind view with LineItem (populates info at this point)
        holder.bindItem(item);
        // other layout params
        lp.setSlm(GridSLM.ID);
        lp.setNumColumns(2);
        lp.setFirstPosition(item.sectionFirstPosition);
        itemView.setLayoutParams(lp);
    }


    @Override
    public int getItemViewType(int position) {
        return lineItemsList.get(position).isHeader ? Constants.StickyGrid.VIEW_TYPE_HEADER : Constants.StickyGrid.VIEW_TYPE_CONTENT;
    }


    @Override
    public int getItemCount() {
        return lineItemsList.size();
    }

}
