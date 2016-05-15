package com.vee.lb.vee.util;

import android.content.Context;
import android.graphics.Rect;
import android.media.AudioManager;
import android.view.WindowManager;

import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

/**
 * Created by Administrator on 2016/5/12.
 */
public class PreInitialize {
    private Context context;
    public static ImageLoader imageLoader;
    public int winwidth;
    public int winheight;
    public static Rect winrect = new Rect();
    public static AudioManager audioManager;
    public static int maxvolume;
    public static int maxbright = 255;

    public PreInitialize(Context context) {
        this.context = context;
        init();
    }

    private void init(){
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(context)
                .memoryCacheExtraOptions(1920, 1920) // max width, max height，即保存的每个缓存文件的最大长宽
                .threadPoolSize(5)//线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                .writeDebugLogs() // Remove for release app
                .build();//开始构建
        ImageLoader.getInstance().init(config);
        imageLoader = ImageLoader.getInstance();

        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        wm.getDefaultDisplay().getRectSize(winrect);

        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        maxvolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }
}
