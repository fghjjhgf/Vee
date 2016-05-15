package com.vee.lb.vee.view;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vee.lb.vee.R;
import com.vee.lb.vee.adapter.BannerPagerBanner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/9.
 */
public class BaseBannerView extends RecyclerView.ViewHolder{

    /** ViewPager中ImageView的容器 */
    private List<ImageView> imageViewContainer = new ArrayList<ImageView>();

    /** 上一个被选中的小圆点的索引，默认值为0 */
    private int preDotPosition = 1;

    /** Banner滚动线程是否销毁的标志，默认不销毁 */
    private boolean isStop = false;

    /** Banner的切换下一个page的间隔时间 */
    private long scrollTimeOffset = 5000;

    private ViewPager m_viewPager;

    /** Banner的文字描述显示控件 */
    private TextView m_tvBannerTextDesc;

    /** 小圆点的父控件 */
    private LinearLayout m_llDotGroup;

    private View m_mainView;

    //private ImageView[] mimageviews;

    private List<ImageView> mimageviews = new ArrayList<ImageView>();

    //int[] m_Banner_ImageIDs;

    private List<Integer> m_Banner_ImageIDs = new ArrayList<Integer>();

    //private int[] m_mmb_Banner_ImageIDs;
    //原始图片数据链表
    private List<Integer> m_mmb_Banner_ImageIDs;

    public BaseBannerView(View itemView) {
        super(itemView);
        m_mainView = itemView;
        getData();

    }


    private void getData(){

    }

    public void setImageViewResIdList(List<Integer> mlist){
        m_mmb_Banner_ImageIDs = mlist;
        initView();
    }

    private void initView(){


        m_viewPager = (ViewPager)m_mainView.findViewById(R.id.banner_viewpager);

        m_llDotGroup = (LinearLayout)m_mainView.findViewById(R.id.banner_ll_dot_group);

        imageViewContainer = new ArrayList<ImageView>();


        for (int i=0;i<m_mmb_Banner_ImageIDs.size()+2;i++)
        {
            if (i==0)
            {
                m_Banner_ImageIDs.add(m_mmb_Banner_ImageIDs.get(m_mmb_Banner_ImageIDs.size()-1));
            }else if (i==m_mmb_Banner_ImageIDs.size()+1)
            {
                m_Banner_ImageIDs.add(m_mmb_Banner_ImageIDs.get(0));
            }else
            {
                m_Banner_ImageIDs.add(m_mmb_Banner_ImageIDs.get(i-1));
            }
        }


        ImageView imageView = null;
        View dot = null;
        LinearLayout.LayoutParams params = null;

        for (int i = 0;i<m_Banner_ImageIDs.size();i++)
        {
            imageView = new ImageView(m_mainView.getContext());
            imageView.setBackgroundResource(m_Banner_ImageIDs.get(i));
            imageViewContainer.add(imageView);
        }

        for (int i=0;i<imageViewContainer.size()-2;i++)
        {
            //每循环一次添加一个点
            dot = new View(m_mainView.getContext());
            dot.setBackgroundResource(R.drawable.dot_bg_selector);
            params = new LinearLayout.LayoutParams(5,5);
            params.leftMargin = 10;
            dot.setEnabled(false);
            dot.setLayoutParams(params);
            m_llDotGroup.addView(dot);
        }


        m_viewPager.setAdapter(new BannerPagerBanner(imageViewContainer));
        m_viewPager.setOnPageChangeListener(new BannerPageChangeListener());

        m_llDotGroup.getChildAt(0).setEnabled(true);
        m_viewPager.setCurrentItem(1);

    }
    private class BannerPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // Nothing to do
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // Nothing to do
        }

        @Override
        public void onPageSelected(int position) {

            int pageindex = position;
            int newDotPosition = position;
            if (position == 0)
            {
                pageindex = imageViewContainer.size()-2;
            }else if (position == imageViewContainer.size()-1)
            {
                pageindex =1;
            }
            if (pageindex != position)
            {
                m_viewPager.setCurrentItem(pageindex, false);
            }

            Log.i("pageindex position", String.valueOf(pageindex) + " " + String.valueOf(position));
            // 把上一个点设置为被选中
            m_llDotGroup.getChildAt(preDotPosition - 1).setEnabled(false);
            // 根据索引设置那个点被选中
            m_llDotGroup.getChildAt(pageindex-1).setEnabled(true);
            preDotPosition = pageindex;

        }

    }
}
