package com.vee.lb.vee.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/5/9.
 */
public class MainPageFragmentAdapter extends FragmentPagerAdapter {

    List<Fragment> mListFragment;
    List<String> mListTitleName;

    public MainPageFragmentAdapter(FragmentManager fm,List<Fragment> mListFragment,List<String> mListTitleName)
    {
        super(fm);
        this.mListFragment = mListFragment;
        this.mListTitleName = mListTitleName;
    }

    @Override
    public int getCount() {
        return mListTitleName.size();
    }

    @Override
    public Fragment getItem(int arg0) {
        return mListFragment.get(arg0);
    }

    @Override
    public CharSequence getPageTitle(int position){
        return mListTitleName.get(position);
    }
}
