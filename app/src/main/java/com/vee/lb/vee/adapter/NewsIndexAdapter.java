package com.vee.lb.vee.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vee.lb.vee.R;
import com.vee.lb.vee.util.NewsIndexItem;
import com.vee.lb.vee.view.AutoFitGridView;

import java.util.List;

/**
 * Created by Administrator on 2016/5/13.
 */
public class NewsIndexAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<NewsIndexItem> newsIndexItemList;
    private Context context;


    public NewsIndexAdapter(Context context,List<NewsIndexItem> newsIndexItemList) {
        this.context = context;
        this.newsIndexItemList = newsIndexItemList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_index_item_layout,parent,false);

        return new NewsIndexItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        NewsIndexItemViewHolder newsIndexItemViewHolder = (NewsIndexItemViewHolder)holder;
        newsIndexItemViewHolder.title.setText(newsIndexItemList.get(position).title);
        newsIndexItemViewHolder.author.setText(newsIndexItemList.get(position).author);
        newsIndexItemViewHolder.glance_time.setText(newsIndexItemList.get(position).glance_times);
        newsIndexItemViewHolder.update_time.setText(newsIndexItemList.get(position).date);
        newsIndexItemViewHolder.content.setText(newsIndexItemList.get(position).content);
        newsIndexItemViewHolder.textLinearLayout.setOnClickListener(textonclicklistener);
        NewsIndexGridViewAdapter newsIndexGridViewAdapter = new NewsIndexGridViewAdapter(context,newsIndexItemList.get(position).img_src_list);
        newsIndexGridViewAdapter.setImageViewClickListner(imageonclicklistener);
        newsIndexItemViewHolder.autoFitGridView.setAdapter(newsIndexGridViewAdapter);
    }

    private View.OnClickListener textonclicklistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private View.OnClickListener imageonclicklistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };


    @Override
    public int getItemCount() {
        return newsIndexItemList.size();
    }


    public class NewsIndexItemViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public TextView author;
        public TextView glance_time;
        public TextView update_time;
        public TextView content;
        public LinearLayout textLinearLayout;
        public AutoFitGridView autoFitGridView;

        public NewsIndexItemViewHolder(View itemView) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.news_index_item_title);
            author = (TextView)itemView.findViewById(R.id.up_author);
            glance_time = (TextView)itemView.findViewById(R.id.glance_times);
            update_time = (TextView)itemView.findViewById(R.id.update_time);
            content = (TextView)itemView.findViewById(R.id.news_index_item_introduce);
            textLinearLayout = (LinearLayout)itemView.findViewById(R.id.news_index_text_linearlayout);
            autoFitGridView = (AutoFitGridView)itemView.findViewById(R.id.news_index_item_pic_container);
        }
    }
}
