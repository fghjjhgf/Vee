package com.vee.lb.vee.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.nostra13.universalimageloader.utils.MemoryCacheUtils;
import com.vee.lb.vee.R;
import com.vee.lb.vee.adapter.PicGalleryAdapter;
import com.vee.lb.vee.util.CommonString;
import com.vee.lb.vee.util.PreInitialize;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class PicGalleryActivity extends AppCompatActivity {
    private String TAG = "PicGalleryActivity";
    private ViewPager viewPager;
    private LinearLayout dotlinearLayout;
    private List<String> imagelist = new ArrayList<>();
    private ArrayList<ImageView> imageViewList = new ArrayList<>();
    private int predotpos = 0;
    private int nowpicpos;
    private PopupWindow popupWindow;
    private Button savebutton;

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
            PreInitialize.imageLoader.displayImage(imagelist.get(i), imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Log.d(TAG, "onLongClick: " + "imageView");
                    getpopwin();
                    openpopupwindow(v);
                    return false;
                }
            });

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

    private void initpopwin(){
        View view = getLayoutInflater().inflate(R.layout.pic_gallery_popwin_layout,null);
        savebutton = (Button)view.findViewById(R.id.pic_gallery_save_file);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                closepopupwindow();
                return false;
            }
        });

        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: savebutton");
                try{
                    List<String> ls = MemoryCacheUtils.findCacheKeysForImageUri(imagelist.get(nowpicpos), PreInitialize.imageLoader.getMemoryCache());
                    File sd = Environment.getExternalStorageDirectory();
                    String path = sd.getPath() + CommonString.APP_PIC_PATH;
                    Bitmap bm = PreInitialize.imageLoader.getMemoryCache().get(ls.get(0));
                    if(bm == null){
                        Log.d(TAG, "onClick: bm is null");
                        return;
                    }
                    //String filename = String.valueOf(System.currentTimeMillis()) + ".jpg";
                    String filename = imagelist.get(nowpicpos).substring(imagelist.get(nowpicpos).lastIndexOf("/")+1);
                    Log.d(TAG, "filename: " + filename);
                    File pic = new File(path,filename);
                    if (pic.exists()){
                        Toast.makeText(getApplicationContext(), "图片保存在" + path, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!pic.getParentFile().exists()) {
                        pic.getParentFile().mkdirs();
                        pic.createNewFile();
                    }
                    FileOutputStream out = new FileOutputStream(pic);
                    bm.compress(Bitmap.CompressFormat.JPEG,100,out);
                    out.flush();
                    out.close();
                    Toast.makeText(getApplicationContext(), "图片保存在" + path, Toast.LENGTH_SHORT).show();

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"图片保存失败",Toast.LENGTH_SHORT).show();
                }
                closepopupwindow();
            }
        });

    }

    private void getpopwin(){
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
        }else {
            initpopwin();
        }
    }

    private void openpopupwindow(View v){
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
        WindowManager.LayoutParams params=getWindow().getAttributes();
        params.alpha=0.7f;
        getWindow().setAttributes(params);
    }

    private void closepopupwindow(){
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
            WindowManager.LayoutParams params=getWindow().getAttributes();
            params.alpha=1f;
            getWindow().setAttributes(params);
        }
    }

    private void initPopupWindow(){
        View view = this.getLayoutInflater().inflate(R.layout.pic_gallery_popwin_layout, null);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.d(TAG, "onClick: ");
                popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, 0, 0);
            }
        });
    }

    private class PicGalleryChangerListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            nowpicpos = position;
            dotlinearLayout.getChildAt(position).setEnabled(true);
            dotlinearLayout.getChildAt(predotpos).setEnabled(false);
            predotpos = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
