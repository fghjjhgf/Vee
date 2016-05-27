package com.vee.lb.vee.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vee.lb.vee.R;
import com.vee.lb.vee.adapter.BangumiFinAdapter;
import com.vee.lb.vee.util.BangumiFinGridLayoutItem;
import com.vee.lb.vee.util.BangumiFinItem;
import com.vee.lb.vee.util.TestString;
import com.vee.lb.vee.view.HeaderRecyclerView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/11.
 */
public class BangumiFinFragment extends Fragment {
    private String TAG = "BangumiFinFragment";
    private View mview;
    private HeaderRecyclerView recyclerview;
    private List<BangumiFinItem> bangumiFinItemList = new ArrayList<>();
    private List<Integer> headreslist = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mview = inflater.inflate(R.layout.bangumi_fin_fragment,container,false);
        init();
        return mview;
    }

    private void init(){
        findview();
        getData();
        BangumiFinAdapter bangumiFinAdapter = new BangumiFinAdapter(getActivity(),bangumiFinItemList);
        recyclerview.setAdapter(bangumiFinAdapter);
        recyclerview.setHeadImageID(headreslist);
    }

    private void getData(){
        try{
            JSONArray jsonArray = new JSONArray(TestString.bangumi_fin_json);
            for (int i= 0;i < jsonArray.length();i++){
                BangumiFinItem bangumiFinItem = new BangumiFinItem();
                JSONArray jsonArray1 = jsonArray.optJSONArray(i);
                bangumiFinItem.title_name = jsonArray1.getString(0);
                Log.d(TAG, "getData: " + bangumiFinItem.title_name);
                JSONArray jsonArray2 = jsonArray1.optJSONArray(1);
                for (int j = 0;j<jsonArray2.length();j++){
                    BangumiFinGridLayoutItem b = new BangumiFinGridLayoutItem();
                    b.img_src = jsonArray2.optString(0);
                    b.bangumi_name = jsonArray2.optString(1);
                    b.bangumi_episodes = jsonArray2.optString(2);
                    b.play_num = jsonArray2.optString(3);
                    bangumiFinItem.bangumiFinGridLayoutItemList.add(b);
                }
                bangumiFinItemList.add(bangumiFinItem);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        headreslist.add(R.drawable.banner_1);
        headreslist.add(R.drawable.banner_2);
        headreslist.add(R.drawable.banner_3);
    }

    private void findview(){
        recyclerview = (HeaderRecyclerView)mview.findViewById(R.id.bangumi_fin_recyclerview);
    }

}
