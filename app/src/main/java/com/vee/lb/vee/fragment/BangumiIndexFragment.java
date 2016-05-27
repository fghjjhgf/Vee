package com.vee.lb.vee.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vee.lb.vee.R;
import com.vee.lb.vee.adapter.BangumiListAdapter;
import com.vee.lb.vee.util.BangumiIndexItem;
import com.vee.lb.vee.util.TestString;
import com.vee.lb.vee.view.HeaderRecyclerView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/9.
 */
public class BangumiIndexFragment extends Fragment {
    private String TAG = "BangumiFragment";
    private View mView;
    private HeaderRecyclerView recyclerView;
    private List<Integer> headreslist = new ArrayList<>();
    private List<BangumiIndexItem> bangumiIndexList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        mView = inflater.inflate(R.layout.bangumi_fragment,null);
        init();
        return mView;
    }

    private void init(){
        getData();
        findview();
        BangumiListAdapter bangumiListAdapter = new BangumiListAdapter(getActivity(),bangumiIndexList);

        recyclerView.setAdapter(bangumiListAdapter);
        recyclerView.setHeadImageID(headreslist);
    }

    private void getData(){
        try{
            JSONArray jsonArray = new JSONArray(TestString.bangunmi_index_json);
            for (int i = 0;i<jsonArray.length();i++)
            {
                JSONArray t = jsonArray.optJSONArray(i);
                BangumiIndexItem t1 = new BangumiIndexItem();
                t1.type = t.getString(0);
                t1.lefttext = t.getString(1);
                t1.righttext = t.getString(2);
                bangumiIndexList.add(t1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        headreslist.add(R.drawable.banner_1);
        headreslist.add(R.drawable.banner_2);
        headreslist.add(R.drawable.banner_3);
    }

    private void findview(){
        recyclerView = (HeaderRecyclerView)mView.findViewById(R.id.bangumi_recyclerview);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
}
