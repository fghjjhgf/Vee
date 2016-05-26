package com.vee.lb.vee.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vee.lb.vee.R;
import com.vee.lb.vee.adapter.CommentListRecyclerViewAdapter;
import com.vee.lb.vee.util.CommentItemContent;
import com.vee.lb.vee.util.TestString;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/25.
 */
public class CommentsFragment extends Fragment {
    private String TAG = "CommentsFragment";

    private View mview;
    private RecyclerView recyclerView;
    private List<CommentItemContent> commentItemContents = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mview = inflater.inflate(R.layout.comments_fragment,container,false);
        init();
        return mview;
    }

    private void init(){
        findview();
        getData();
        CommentListRecyclerViewAdapter commentListRecyclerViewAdapter = new CommentListRecyclerViewAdapter(commentItemContents);
        recyclerView.setAdapter(commentListRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setNestedScrollingEnabled(false);
        commentListRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void findview(){
        recyclerView = (RecyclerView)mview.findViewById(R.id.comment_fragment_recyclerview);
    }

    private void getData(){
        AnalyzeCommontJson(TestString.comments_json);
    }

    private void AnalyzeCommontJson(String jsonstring){
        try{
            JSONArray jsonarray = new JSONArray(jsonstring);
            for (int i=0;i<jsonarray.length();i++)
            {
                CommentItemContent c = new CommentItemContent();
                JSONArray t = jsonarray.optJSONArray(i);
                Log.d(TAG, "AnalyzeJson: " + i + " " + t);
                c.usr_face_res = R.drawable.main_page_toolbar_user_portrait;
                c.usr_name = t.getString(0);
                c.comment_data = t.getString(1);
                c.comment_content = t.getString(2);
                commentItemContents.add(c);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
