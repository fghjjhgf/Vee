package com.vee.lb.vee.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Administrator on 2016/6/11.
 */
public class PicGalleryAdapter extends PagerAdapter {
    private String TAG = "PicGalleryAdapter";
    private List<ImageView> imageViewList;

    public PicGalleryAdapter(List<ImageView> imageViewList) {
        this.imageViewList = imageViewList;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(imageViewList.get(position));
    }

    @Override
    public int getCount() {
        return imageViewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = imageViewList.get(position);

        container.addView(view);
        return view;
    }
}
