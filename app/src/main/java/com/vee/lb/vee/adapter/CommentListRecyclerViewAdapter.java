package com.vee.lb.vee.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vee.lb.vee.R;
import com.vee.lb.vee.util.CommentItemContent;

import java.util.List;

/**
 * Created by Administrator on 2016/5/22.
 */
public class CommentListRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CommentItemContent> list;

    public CommentListRecyclerViewAdapter(List<CommentItemContent> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_list_item,parent,false);
        return new CommentListItem(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CommentListItem commentListItem = (CommentListItem)holder;
        commentListItem.usr_face_bg.setImageResource(list.get(position).usr_face_res);
        commentListItem.usr_name.setText(list.get(position).usr_name);
        commentListItem.comment_data.setText(list.get(position).comment_data);
        commentListItem.comment_content.setText(list.get(position).comment_content);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CommentListItem extends RecyclerView.ViewHolder{
        public ImageView usr_face_bg;
        public TextView usr_name;
        public TextView comment_data;
        public TextView comment_content;

        public CommentListItem(View itemView) {
            super(itemView);
            usr_face_bg = (ImageView)itemView.findViewById(R.id.usr_face_bg);
            usr_name = (TextView)itemView.findViewById(R.id.comment_usr_name);
            comment_content = (TextView)itemView.findViewById(R.id.comment_content);
            comment_data = (TextView)itemView.findViewById(R.id.comment_data);
        }
    }
}
