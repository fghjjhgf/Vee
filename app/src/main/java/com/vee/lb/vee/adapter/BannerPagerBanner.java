package com.vee.lb.vee.adapter;

import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Administrator on 2016/2/26.
 */
public class BannerPagerBanner extends PagerAdapter {

    List<ImageView> m_imageViews;

    public BannerPagerBanner(List<ImageView> imageViews){
        this.m_imageViews=imageViews;
    }

    @Override
    public int getCount() {
        return m_imageViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        container.removeView(m_imageViews.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int postion){


        View view = m_imageViews.get(postion%m_imageViews.size());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("ssss", "onClick: 111");

            }
        });

        container.addView(view);
        return view;
    }
}
