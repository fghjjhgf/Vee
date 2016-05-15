package com.vee.lb.vee.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vee.lb.vee.R;
import com.vee.lb.vee.util.BangumiFinItem;
import com.vee.lb.vee.view.AutoFitGridView;

import java.util.List;

/**
 * Created by Administrator on 2016/5/12.
 */
public class BangumiFinAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private String TAG = "BangumiFinAdapter";
    private List<BangumiFinItem> bangumiFinItemList;
    private Context context;

    public BangumiFinAdapter(Context context,List<BangumiFinItem> bangumiFinItemList) {
        this.context = context;
        this.bangumiFinItemList = bangumiFinItemList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bangumi_fin_item_layout,parent,false);
        return new BangumiFinItemVeiwHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position != 0)
        {
            BangumiFinItemVeiwHolder bangumiFinItemVeiwHolder = (BangumiFinItemVeiwHolder)holder;
            bangumiFinItemVeiwHolder.tag_textview.setText(bangumiFinItemList.get(position - 1).title_name);
            BangumiFinGridViewAdapter bangumiFinGridViewAdapter = new BangumiFinGridViewAdapter(context,bangumiFinItemList.get(position - 1).bangumiFinGridLayoutItemList);
            bangumiFinItemVeiwHolder.gridView.setAdapter(bangumiFinGridViewAdapter);
        }
    }

    @Override
    public int getItemCount() {
        return bangumiFinItemList.size();
    }


    private class BangumiFinItemVeiwHolder extends RecyclerView.ViewHolder{
        public TextView tag_textview;
        public TextView more_textview;
        public AutoFitGridView gridView;

        public BangumiFinItemVeiwHolder(View itemView) {
            super(itemView);
            tag_textview = (TextView)itemView.findViewById(R.id.bangumi_fin_tag_name);
            more_textview = (TextView)itemView.findViewById(R.id.bangumi_fin_more);
            gridView = (AutoFitGridView)itemView.findViewById(R.id.bangumi_fin_gridview);
        }
    }
}
