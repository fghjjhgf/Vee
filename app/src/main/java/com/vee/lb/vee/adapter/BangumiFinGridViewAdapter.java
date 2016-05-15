package com.vee.lb.vee.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vee.lb.vee.R;
import com.vee.lb.vee.util.BangumiFinGridLayoutItem;
import com.vee.lb.vee.util.FinishLastUpdateItemView;
import com.vee.lb.vee.util.PreInitialize;

import java.util.List;

/**
 * Created by Administrator on 2016/5/12.
 */
public class BangumiFinGridViewAdapter extends BaseAdapter {
    private String TAG = "BangumiFinGridViewAdapter";
    private Context context;
    private List<BangumiFinGridLayoutItem> bangumiFinGridLayoutItemList;

    public BangumiFinGridViewAdapter(Context context,List<BangumiFinGridLayoutItem> bangumiFinGridLayoutItemList) {
        this.context = context;
        this.bangumiFinGridLayoutItemList = bangumiFinGridLayoutItemList;
    }

    @Override
    public int getCount() {
        return bangumiFinGridLayoutItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return bangumiFinGridLayoutItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FinishLastUpdateItemView m_fl_view ;

        if (convertView == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInflater.inflate(R.layout.bangumi_fin_gridlayout_item_layout,null);
            m_fl_view = new FinishLastUpdateItemView();
            m_fl_view.m_imageview = (ImageView)convertView.findViewById(R.id.normal_gridview_item_imageview);
            m_fl_view.m_animatename_textview = (TextView)convertView.findViewById(R.id.normal_gridview_item_animate_name);
            m_fl_view.m_episode_textview = (TextView)convertView.findViewById(R.id.normal_gridview_item_episode);
            m_fl_view.m_playnum_textview = (TextView)convertView.findViewById(R.id.normal_gridview_item_playnum);
            convertView.setTag(m_fl_view);

        }else
        {
            m_fl_view = (FinishLastUpdateItemView)convertView.getTag();
        }

        PreInitialize.imageLoader.displayImage(bangumiFinGridLayoutItemList.get(position).img_src,m_fl_view.m_imageview);
        m_fl_view.m_animatename_textview.setText(bangumiFinGridLayoutItemList.get(position).bangumi_name);
        m_fl_view.m_episode_textview.setText(bangumiFinGridLayoutItemList.get(position).bangumi_episodes);
        m_fl_view.m_playnum_textview.setText(bangumiFinGridLayoutItemList.get(position).play_num);

        return convertView;
    }
}
