package com.vee.lb.vee.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vee.lb.vee.R;
import com.vee.lb.vee.util.BangumiIndexItem;

import java.util.List;

/**
 * Created by Administrator on 2016/5/10.
 */
public class BangumiListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private String TAG = "BangumiListAdapter";
    private final int TYPE_WEEK = 101;
    private final int TYPE_BANGUMI = 102;
    private final int TYPE_DEFAULT = 1000;

    private List<BangumiIndexItem> bangumiIndexList;

    public BangumiListAdapter(List<BangumiIndexItem> l) {
        this.bangumiIndexList = l;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: " + viewType);
        if (viewType == TYPE_WEEK){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bangumi_index_week,parent,false);
            return new BangumiWeek(view);
        }else
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bangumi_index_bangumi,parent,false);
            return new BangumiName(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: "+ getItemViewType(position));
        if (position != 0)
        {
            if (getItemViewType(position) == TYPE_WEEK){
                BangumiWeek bangumiWeek = (BangumiWeek)holder;
                bangumiWeek.week_textview.setText(bangumiIndexList.get(position-1).lefttext);
                bangumiWeek.date_textview.setText(bangumiIndexList.get(position-1).righttext);
            }else if (getItemViewType(position) == TYPE_BANGUMI){
                BangumiName bangumiName = (BangumiName)holder;
                bangumiName.banguymi_textview.setText(bangumiIndexList.get(position-1).lefttext);
                bangumiName.episode_textview.setText(bangumiIndexList.get(position-1).righttext);
            }

        }

    }

    @Override
    public int getItemCount() {
        return bangumiIndexList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Log.d(TAG, "getItemViewType: " + bangumiIndexList.get(position-1).type);
        if (bangumiIndexList.get(position-1).type.equals("1")){
            return TYPE_WEEK;
        }
        if (bangumiIndexList.get(position-1).type.equals("2")){
            return TYPE_BANGUMI;
        }
        return TYPE_DEFAULT;

    }

    public class BangumiWeek extends RecyclerView.ViewHolder{
        public TextView week_textview;
        public TextView date_textview;
        public BangumiWeek(View itemView) {
            super(itemView);
            week_textview = (TextView)itemView.findViewById(R.id.bangumi_index_week_weekname);
            date_textview = (TextView)itemView.findViewById(R.id.bangumi_index_week_date);
        }
    }

    public class BangumiName extends RecyclerView.ViewHolder{
        public TextView banguymi_textview;
        public TextView episode_textview;
        public BangumiName(View itemView) {
            super(itemView);
            banguymi_textview = (TextView)itemView.findViewById(R.id.bangumi_index_bangumi_name);
            episode_textview = (TextView)itemView.findViewById(R.id.bangumi_index_bangumi_episodes);
        }
    }
}
