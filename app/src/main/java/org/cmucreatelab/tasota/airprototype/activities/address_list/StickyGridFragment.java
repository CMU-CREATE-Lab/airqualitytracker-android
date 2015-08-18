package org.cmucreatelab.tasota.airprototype.activities.address_list;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.tonicartos.superslim.LayoutManager;
import org.cmucreatelab.tasota.airprototype.R;

/**
 * Class is based on example code from SuperSLiM's github repo: https://github.com/TonicArtos/SuperSLiM
 */
public class StickyGridFragment extends Fragment {

    private static final String KEY_HEADER_POSITIONING = "key_header_mode";
    private static final String KEY_MARGINS_FIXED = "key_margins_fixed";
    private final boolean mAreMarginsFixed = true;
    private ViewHolder mViews;
    private StickyGridAdapter mAdapter;
    private int mHeaderDisplay = 17;

    private static class ViewHolder {
        private final RecyclerView mRecyclerView;

        public ViewHolder(View view) {
            mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        }
        public void initViews(LayoutManager lm) {
            mRecyclerView.setLayoutManager(lm);
        }
        public void setAdapter(RecyclerView.Adapter<?> adapter) {
            mRecyclerView.setAdapter(adapter);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.__address_list__stickygrid_fragment, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViews = new ViewHolder(view);
        mViews.initViews(new LayoutManager(getActivity()));
        // ASSERT: we can cast getActivity to be AddressListActivity
        mAdapter = new StickyGridAdapter((AddressListActivity)getActivity(), mHeaderDisplay, mAreMarginsFixed);
        mViews.setAdapter(mAdapter);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(KEY_HEADER_POSITIONING, mHeaderDisplay);
        outState.putBoolean(KEY_MARGINS_FIXED, mAreMarginsFixed);
    }

}
