package com.vee.lb.vee.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

import com.vee.lb.vee.R;
import com.vee.lb.vee.adapter.CommentListRecyclerViewAdapter;
import com.vee.lb.vee.util.CommentItemContent;
import com.vee.lb.vee.util.TestString;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class WebContentActivity extends AppCompatActivity {
    private String TAG = "WebContentActivity";
    private Toolbar toolbar;
    private WebView webView;
    private RecyclerView recyclerView;
    private Button commit_input_button;
    private EditText commit_edittext;
    private List<CommentItemContent> commentItemContents = new ArrayList<>();
    private String test_content_url = "http://news.missevan.com/news/article?newsid=39834";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_content);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        init();

    }

    private void init(){
        findview();
        getData();
        initialize();
        initlistener();
    }

    private void findview(){
        webView = (WebView)findViewById(R.id.web_content_webview);
        recyclerView = (RecyclerView)findViewById(R.id.web_content_comment_recyclerview);
        commit_input_button = (Button)findViewById(R.id.web_content_commit_input_button);
        commit_edittext = (EditText)findViewById(R.id.web_content_commit_edittext);
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

    private void initialize(){
        CommentListRecyclerViewAdapter commentListRecyclerViewAdapter = new CommentListRecyclerViewAdapter(commentItemContents);
        recyclerView.setAdapter(commentListRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setNestedScrollingEnabled(false);
        commentListRecyclerViewAdapter.notifyDataSetChanged();

        webView.loadUrl(test_content_url);
    }

    private void initlistener(){

    }

}
