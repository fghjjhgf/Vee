package com.vee.lb.vee.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.vee.lb.vee.R;
import com.vee.lb.vee.util.PreInitialize;

import java.util.List;

/**
 * Created by Administrator on 2016/5/13.
 */
public class NewsIndexGridViewAdapter extends BaseAdapter {
    private Context context;
    private List<String> imglist;
    private View.OnClickListener onClickListener;

    public NewsIndexGridViewAdapter(Context context,List<String> imglist) {
        this.context = context;
        this.imglist = imglist;
    }

    @Override
    public int getCount() {
        return imglist.size();
    }

    @Override
    public Object getItem(int position) {
        return imglist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       ImageView imageview;

       if (convertView == null) {
           LayoutInflater layoutInflater = (LayoutInflater) context
                   .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           convertView = layoutInflater.inflate(R.layout.news_index_gridview_item,null);
           imageview = (ImageView)convertView.findViewById(R.id.news_index_gridview_item_imageview);
           convertView.setTag(imageview);
       }else {
           imageview = (ImageView)convertView.getTag();
       }

       PreInitialize.imageLoader.displayImage(imglist.get(position), imageview);

        return convertView;
    }

    public void setImageViewClickListner(View.OnClickListener l){
        this.onClickListener = l;
    }
}
