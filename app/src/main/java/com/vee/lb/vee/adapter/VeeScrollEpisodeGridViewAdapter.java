package com.vee.lb.vee.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vee.lb.vee.R;

import java.util.List;

/**
 * Created by Administrator on 2016/5/25.
 */
public class VeeScrollEpisodeGridViewAdapter extends BaseAdapter {

    private String TAG = "VeeScrollEpisodeGridViewAdapter";
    private Context context;
    private List<String> videolist;

    public VeeScrollEpisodeGridViewAdapter(Context context, List<String> videolist) {
        this.context = context;
        this.videolist = videolist;
    }

    @Override
    public int getCount() {

        return videolist.size();
    }

    @Override
    public Object getItem(int position) {
        return videolist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;
        if (convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.episode_gridview_item_layout,null);
            textView = (TextView)convertView.findViewById(R.id.episodes_gridview_item_textview);
            convertView.setTag(textView);
        }else {
            textView = (TextView)convertView.getTag();
        }
        textView.setText(String.valueOf(position));
        return convertView;
    }
}
