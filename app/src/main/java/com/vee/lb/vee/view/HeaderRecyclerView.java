package com.vee.lb.vee.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.vee.lb.vee.R;

import java.util.List;

/**
 * Created by Administrator on 2016/5/9.
 * use funtion setHeadImageID to set the header image view
 */
public class HeaderRecyclerView extends RecyclerView {
    private String TAG = "HeaderRecyclerView";
    private final static int TYPE_HEAD = 1;
    private final static int TYPE_NOMAL = 2;
    private InitAdapter initAdapter;
    private List<Integer> HeadImageID;

    public HeaderRecyclerView(Context context) {
        super(context);
    }

    public HeaderRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeaderRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setHeadImageID(List<Integer> HeadImaheID){
        this.HeadImageID = HeadImaheID;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (adapter!=null)
        {
            initAdapter = new InitAdapter(adapter);
        }
        super.swapAdapter(initAdapter,true);
    }

    private class InitAdapter extends Adapter<ViewHolder>{
        private Adapter adapter;

        public InitAdapter(Adapter adapter) {
            this.adapter = adapter;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_HEAD){
                BaseBannerView baseBannerView = new BaseBannerView(LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_lunbo_layout,parent,false));
                if (HeadImageID == null){
                    Log.d(TAG, "onCreateViewHolder: HeadImageID is null");
                }
                /**
                 * set image res list to header view
                 **/
                baseBannerView.setImageViewResIdList(HeadImageID);
                return baseBannerView;
            } else {
                return adapter.onCreateViewHolder(parent,viewType);
            }

        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Log.d(TAG, "onBindViewHolder: " + position);
            int type = getItemViewType(position);
            if (type != TYPE_HEAD ) {
                adapter.onBindViewHolder(holder, position);
            }
        }

        @Override
        public int getItemCount() {
            int mcount = adapter.getItemCount();
            mcount++;
            Log.d(TAG, "getItemCount: " + mcount);
            return mcount;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0){
                return TYPE_HEAD;
            }else {
                return adapter.getItemViewType(position);
            }
        }
    }
}
