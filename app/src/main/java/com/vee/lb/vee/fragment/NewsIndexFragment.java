package com.vee.lb.vee.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vee.lb.vee.R;
import com.vee.lb.vee.adapter.NewsIndexAdapter;
import com.vee.lb.vee.util.NewsIndexItem;
import com.vee.lb.vee.util.TestString;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/13.
 */
public class NewsIndexFragment extends Fragment{
    private String TAG = "NewsIndexFragment";
    private View mview;
    private RecyclerView recyclerView;
    private List<NewsIndexItem> newsIndexItemList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mview = inflater.inflate(R.layout.news_index_fragment,container,false);
        init();
        return mview;
    }

    private void init(){
        findview();
        getData();
        NewsIndexAdapter newsIndexAdapter = new NewsIndexAdapter(getContext(),newsIndexItemList);
        recyclerView.setAdapter(newsIndexAdapter);
    }

    private void findview(){
        recyclerView = (RecyclerView)mview.findViewById(R.id.news_fragment_recyclerview);
    }

    private void getData(){
        try{
            JSONArray jsonArray = new JSONArray(TestString.news_index_json);
            for (int i=0;i<jsonArray.length();i++){
                NewsIndexItem newsIndexItem = new NewsIndexItem();
                JSONArray jsonArray1 = jsonArray.optJSONArray(i);
                for (int j = 0;j<jsonArray1.length();j++){
                    newsIndexItem.title = jsonArray1.getString(0);
                    newsIndexItem.author = jsonArray1.getString(1);
                    newsIndexItem.glance_times = jsonArray1.getString(2);
                    newsIndexItem.date = jsonArray1.getString(3);
                    JSONObject jsonObject = (JSONObject)jsonArray1.get(4);
                    newsIndexItem.content = jsonObject.getString("content");
                    Log.d(TAG, "getData: " + newsIndexItem.content);

                    JSONObject jsonObject1 = (JSONObject)jsonArray1.get(5);

                    List<String> imglist = new ArrayList<>();
                    JSONArray jsonArray2 = jsonObject1.getJSONArray("image_list");
                    for (int k=0;k<jsonArray2.length();k++){
                        imglist.add(jsonArray2.getString(k));
                    }
                    Log.d(TAG, "getData: " + imglist.size());
                    newsIndexItem.img_src_list = imglist;
                }
                newsIndexItemList.add(newsIndexItem);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
