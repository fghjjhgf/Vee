package com.vee.lb.vee;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.vee.lb.vee.adapter.MainPageFragmentAdapter;
import com.vee.lb.vee.fragment.BangumiFinFragment;
import com.vee.lb.vee.fragment.BangumiIndexFragment;
import com.vee.lb.vee.fragment.NewsIndexFragment;
import com.vee.lb.vee.util.PreInitialize;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private List<String> m_main_page_tage_title_List = new ArrayList<>();
    private List<Fragment> fragmentList = new ArrayList<>();//主页面fragment的list

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        findview();
        getData();
        init();
    }

    private void init(){
        //初始化
        new PreInitialize(this);
        //设置标签
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        for (int i=0;i<m_main_page_tage_title_List.size();i++)
            tabLayout.addTab(tabLayout.newTab().setText(m_main_page_tage_title_List.get(i)));

        BangumiIndexFragment bangumiIndexFragment1 = new BangumiIndexFragment();
        BangumiIndexFragment bangumiIndexFragment2 = new BangumiIndexFragment();
        BangumiIndexFragment bangumiIndexFragment3 = new BangumiIndexFragment();
        BangumiFinFragment bangumiFinFragment = new BangumiFinFragment();
        NewsIndexFragment newsIndexFragment = new NewsIndexFragment();
        fragmentList.add(bangumiIndexFragment3);
        fragmentList.add(newsIndexFragment);
        fragmentList.add(bangumiFinFragment);


        MainPageFragmentAdapter mainPageFragmentAdapter = new MainPageFragmentAdapter(getSupportFragmentManager(),fragmentList,m_main_page_tage_title_List);
        viewPager.setAdapter(mainPageFragmentAdapter);
        //bind tablayout to viewpager
        tabLayout.setupWithViewPager(viewPager);
    }

    private void getData(){
        //add tab data
        m_main_page_tage_title_List.add("连载");
        m_main_page_tage_title_List.add("完结");
        m_main_page_tage_title_List.add("资讯");

    }

    private void findview(){
        viewPager = (ViewPager)findViewById(R.id.main_page_viewpager);
        tabLayout = (TabLayout)findViewById(R.id.main_page_tablayout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
