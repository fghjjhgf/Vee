package com.vee.lb.vee.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.vee.lb.vee.R;
import com.vee.lb.vee.adapter.PicGalleryAdapter;
import com.vee.lb.vee.util.CommonString;
import com.vee.lb.vee.util.PreInitialize;

import java.util.ArrayList;
import java.util.List;

public class PicGalleryActivity extends AppCompatActivity {
    private String TAG = "PicGalleryActivity";
    private ViewPager viewPager;
    private LinearLayout dotlinearLayout;
    private List<String> imagelist = new ArrayList<>();
    private ArrayList<ImageView> imageViewList = new ArrayList<>();
    private int predotpos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*set it to be no title*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        /*set it to be full screen*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        imagelist = getIntent().getExtras().getStringArrayList(CommonString.GALLERY_IMAGELIST);
        setContentView(R.layout.activity_pic_gallery);

        init();
    }

    private void  init(){
        findview();
        initview();
        viewPager.setAdapter(new PicGalleryAdapter(imageViewList));
        viewPager.addOnPageChangeListener(new PicGalleryChangerListener());
        dotlinearLayout.getChildAt(0).setEnabled(true);
    }

    private void findview(){
        viewPager =(ViewPager)findViewById(R.id.pic_gallery_viewpager);
        dotlinearLayout = (LinearLayout)findViewById(R.id.pic_gallery_dot_group);
    }

    private void initview(){
        ImageView imageView = null;
        View dot = null;
        LinearLayout.LayoutParams params = null;

        for (int i = 0;i<imagelist.size();i++)
        {
            imageView = new ImageView(this);
            PreInitialize.imageLoader.displayImage(imagelist.get(i),imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageViewList.add(imageView);
        }

        for (int i=0;i<imagelist.size();i++)
        {
            //每循环一次添加一个点
            dot = new View(this);
            dot.setBackgroundResource(R.drawable.dot_bg_selector);
            params = new LinearLayout.LayoutParams(10,10);
            params.leftMargin = 10;
            dot.setEnabled(false);
            dot.setLayoutParams(params);
            dotlinearLayout.addView(dot);
        }
    }

    private class PicGalleryChangerListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            dotlinearLayout.getChildAt(position).setEnabled(true);
            dotlinearLayout.getChildAt(predotpos).setEnabled(false);
            predotpos = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
