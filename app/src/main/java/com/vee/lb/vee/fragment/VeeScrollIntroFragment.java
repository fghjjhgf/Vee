package com.vee.lb.vee.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.vee.lb.vee.R;
import com.vee.lb.vee.adapter.VeeScrollEpisodeGridViewAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/24.
 */
public class VeeScrollIntroFragment extends Fragment {
    private String TAG = "VeeScrollIntroFragment";

    private View mView;
    private TextView title_textview;
    private TextView content_textview;
    private GridView episodes_gridview;
    private static final String DEFAULT_JSON = "[\"no name\",\"no intro\",{\"videolist\":[]}]";
    private String jsonstring;
    public IntroContent introContent = new IntroContent();

    public VeeScrollIntroFragment() {

    }

    public static VeeScrollIntroFragment newInstance(String json){
        VeeScrollIntroFragment veeScrollIntroFragment = new VeeScrollIntroFragment();
        Bundle b = new Bundle();
        b.putString(DEFAULT_JSON,json);
        veeScrollIntroFragment.setArguments(b);
        return veeScrollIntroFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            jsonstring = getArguments().get(DEFAULT_JSON).toString();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.vee_scroll_intro_fragment,container,false);
        init();
        return mView;
    }

    private void init(){
        Log.d(TAG, "init: " + jsonstring);
        findview();
        analysisjson(jsonstring);
        title_textview.setText(introContent.title);
        Log.d(TAG, "init: " + introContent.title);
        content_textview.setText(introContent.content);
        VeeScrollEpisodeGridViewAdapter veeScrollEpisodeGridViewAdapter = new VeeScrollEpisodeGridViewAdapter(getContext(),introContent.videolist);
        episodes_gridview.setAdapter(veeScrollEpisodeGridViewAdapter);
    }

    private void findview(){
        title_textview = (TextView)mView.findViewById(R.id.vee_scrolling_intro_title);
        content_textview =(TextView)mView.findViewById(R.id.vee_scrolling_intro_intro);
        episodes_gridview = (GridView)mView.findViewById(R.id.vee_scrolling_intro_episode_gridview);
    }

    private void analysisjson(String json){
        try{
            JSONArray jsonArray = new JSONArray(json);
            introContent.title = jsonArray.getString(0);
            introContent.content = jsonArray.getString(1);
            JSONObject jsonObject = new JSONObject(jsonArray.getString(2));
            JSONArray jsonArray1 = jsonObject.getJSONArray("videolist");
            List<String> videolsit = new ArrayList<>();
            for (int i = 0;i<jsonArray1.length();i++){
                videolsit.add(jsonArray1.getString(i));
            }
            introContent.videolist = videolsit;
            Log.d(TAG, "videolsit: " + introContent.videolist.size());
        }catch (Exception e){

        }
    }

    public class IntroContent{
        public String title;
        public String content;
        public List<String> videolist;
    }
}
