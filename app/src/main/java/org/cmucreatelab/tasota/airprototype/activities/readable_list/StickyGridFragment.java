package org.cmucreatelab.tasota.airprototype.activities.readable_list;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.tonicartos.superslim.LayoutManager;
import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;

/**
 * Class is based on example code from SuperSLiM's github repo: https://github.com/TonicArtos/SuperSLiM
 */
public class StickyGridFragment extends Fragment {

    private static class ViewHolder {
        private final RecyclerView recyclerView;

        public ViewHolder(View view) {
            recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        }
        public void initViews(LayoutManager lm) {
            recyclerView.setLayoutManager(lm);
        }
        public void setAdapter(RecyclerView.Adapter<?> adapter) {
            recyclerView.setAdapter(adapter);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.__readable_list__stickygrid_fragment, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewHolder viewHolder;
        StickyGridAdapter adapter;

        // ASSERT: we can cast getActivity to be ReadableListActivity
        adapter = new StickyGridAdapter((ReadableListActivity)getActivity());

        viewHolder = new ViewHolder(view);
        viewHolder.initViews(new LayoutManager(getActivity()));
        viewHolder.setAdapter(adapter);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(Constants.StickyGrid.KEY_HEADER_POSITIONING, Constants.StickyGrid.HEADER_DISPLAY);
        outState.putBoolean(Constants.StickyGrid.KEY_MARGINS_FIXED, Constants.StickyGrid.MARGINS_ARE_FIXED);
    }

}
