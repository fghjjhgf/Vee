package com.vee.lb.vee.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.vee.lb.vee.R;
import com.vee.lb.vee.adapter.MainPageFragmentAdapter;
import com.vee.lb.vee.fragment.CommentsFragment;
import com.vee.lb.vee.fragment.VeeScrollIntroFragment;
import com.vee.lb.vee.util.TestString;
import com.vee.lb.vee.view.AutoFitHeightViewPager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class VeePlayerScrollingActivity extends AppCompatActivity {
    private String TAG = "VeePlayerScrollingActivity";
    private SurfaceView surfaceView;
    private ImageButton playbutton;
    private ImageButton fullscreenbutton;
    private SeekBar seekBar;
    private IjkMediaPlayer ijkMediaPlayer = null;
    private boolean updateseeknar = false;
    private LinearLayout volumelinearlayout;
    private LinearLayout brightlinearlayout;
    private TextView volumetextview;
    private TextView brighttextview;
    private FloatingActionButton fab;
    private Toolbar toolbar;
    private GestureDetector gestureDetector;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private FrameLayout frameLayout;
    private CoordinatorLayout coordinatorLayout;
    private AutoFitHeightViewPager viewPager;
    private TabLayout tabLayout;

    private String remoteurl = "http://cn-gdfs15-dx.acgvideo.com/vg18/0/4a/7755602-1.flv?expires=1464377400&ssig=n7pQfptRvR1AoEDL5uEOog&oi=244766114&player=1&or=3074230573&rate=0";
    private final int STATE_SEEKBAR_BUFF_UPDATE = 101;
    private final int STATE_SEEKBAR_BUTTON_UPDATE = 102;
    private final String video_buff_bundle = "video_buff_bundle";

    private final static int UPDATE_SYS_TIME_MSG = 101;
    private final static int UPDATE_VIDEO_BUFF = 102;
    private final static int UPDATE_VIDEO_SEEKBAR = 103;
    private final static int UPDATE_CONTROL = 104;
    private final static int INITIALIZE_SURFACE_WIDTH = 105;

    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> tabstringlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vee_player_scrolling);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        init();
    }

    private void init(){
        findview();
        initialize();
        initlistener();
    }

    private void findview(){
        fab = (FloatingActionButton) findViewById(R.id.fab);
        seekBar = (SeekBar)findViewById(R.id.vee_player_scroling_seekbar);
        surfaceView = (SurfaceView)findViewById(R.id.vee_player_scroling_surfaceview);
        playbutton = (ImageButton)findViewById(R.id.vee_player_scroling_bofang);
        fullscreenbutton = (ImageButton)findViewById(R.id.vee_player_scroling_quanping);
        volumelinearlayout = (LinearLayout)findViewById(R.id.vee_player_volume_linearlayout);
        brightlinearlayout = (LinearLayout)findViewById(R.id.vee_player_bright_linearlayout);
        volumetextview = (TextView)findViewById(R.id.vee_player_volume_text);
        brighttextview = (TextView)findViewById(R.id.vee_player_bright_text);
        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);
        frameLayout = (FrameLayout)findViewById(R.id.vee_player_scrolling_framelayout);
        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.vee_player_scrolling_coordinatorlayout);
        viewPager = (AutoFitHeightViewPager)findViewById(R.id.vee_scrolling_player_viewpager);
        tabLayout = (TabLayout)findViewById(R.id.vee_scrolling_player_tablayout);
    }

    private void initialize(){
        surfaceView.getHolder().setKeepScreenOn(true);
        surfaceView.getHolder().addCallback(new SurfaceViewCallBack());

        volumelinearlayout.setVisibility(View.GONE);
        brightlinearlayout.setVisibility(View.GONE);



        VeeScrollIntroFragment veeScrollIntroFragment = VeeScrollIntroFragment.newInstance(TestString.intro_content_json);
        CommentsFragment commentsFragment = new CommentsFragment();

        fragmentList.add(veeScrollIntroFragment);
        fragmentList.add(commentsFragment);

        tabstringlist.add("简介");
        tabstringlist.add("评论");

        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        for (int i=0;i<tabstringlist.size();i++)
            tabLayout.addTab(tabLayout.newTab().setText(tabstringlist.get(i)));

        MainPageFragmentAdapter mainPageFragmentAdapter = new MainPageFragmentAdapter(getSupportFragmentManager(),fragmentList,tabstringlist);
        viewPager.setAdapter(mainPageFragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initmedia(){
        try{
            ijkMediaPlayer = new IjkMediaPlayer();
            ijkMediaPlayer.setDisplay(surfaceView.getHolder());
            ijkMediaPlayer.setScreenOnWhilePlaying(true);
            ijkMediaPlayer.setDataSource(remoteurl);
            ijkMediaPlayer.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
            ijkMediaPlayer.setOnErrorListener(mOnErrorListener);
            ijkMediaPlayer.setOnCompletionListener(mOnCompletionListener);
            ijkMediaPlayer.setOnInfoListener(mOnInfoListener);
            ijkMediaPlayer.prepareAsync();
            play();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void initlistener(){
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (ijkMediaPlayer == null) {
                    seekBar.setEnabled(false);
                } else {
                    seekBar.setEnabled(true);
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d(TAG, "onStopTrackingTouch: ");
                if (ijkMediaPlayer != null) {
                    int sp = seekBar.getProgress();
                    long du = ijkMediaPlayer.getDuration() * sp / seekBar.getMax();
                    ijkMediaPlayer.seekTo(du);
                    play();
                }
            }

        });

        playbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ijkMediaPlayer != null) {
                    if (ijkMediaPlayer.isPlaying()) {
                        pause();
                    } else {
                        play();
                    }
                }
            }
        });

        surfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "onTouch: ");
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        Log.d(TAG, "onTouch:ACTION_MOVE ");
                        return true;
                }
                return false;
            }
        });

        fullscreenbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),VeeMainPlayerActivity.class);
                startActivity(i);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //fab.animate()
                //        .alphaBy(1)
                //        .alpha(0)
                //        .setDuration(1000)
                //        .start();
                fab.hide();
                initmedia();
            }
        });
    }

    private void play(){
        settoolbarflag(AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP);
        updateseeknar = true;
        myhandle.sendEmptyMessage(UPDATE_VIDEO_SEEKBAR);
        playbutton.setBackgroundResource(R.drawable.ic_media_pause);
        ijkMediaPlayer.start();
    }

    private void stop(){
        ijkMediaPlayer.stop();
    }

    private void pause(){
        settoolbarflag(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
        updateseeknar = false;
        playbutton.setBackgroundResource(R.drawable.ic_media_play);
        ijkMediaPlayer.pause();
    }

    private void settoolbarflag(int value){
        AppBarLayout.LayoutParams params =(AppBarLayout.LayoutParams)collapsingToolbarLayout.getLayoutParams();
        params.setScrollFlags(value);
        collapsingToolbarLayout.setLayoutParams(params);
    }

    private final Handler myhandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what)
            {
                case UPDATE_SYS_TIME_MSG:
                    long time = System.currentTimeMillis();
                    Calendar mCalendar = Calendar.getInstance();
                    mCalendar.setTimeInMillis(time);
                    int mHour = mCalendar.get(Calendar.HOUR);
                    int mMinuts = mCalendar.get(Calendar.MINUTE);
                    String strmin = String.valueOf(mMinuts);
                    if (mMinuts < 10)
                        strmin = String.valueOf(0) + mMinuts;
                    String str = String.valueOf(mHour) + ":" + strmin;
                    myhandle.sendEmptyMessageDelayed(UPDATE_SYS_TIME_MSG, 60 * 1000);
                    break;
                case UPDATE_VIDEO_BUFF:

                    break;
                case UPDATE_VIDEO_SEEKBAR:
                    if (updateseeknar)
                    {
                        long curpos = ijkMediaPlayer.getCurrentPosition();
                        long duration = ijkMediaPlayer.getDuration();
                        seekBar.setProgress((int) ((float) curpos / duration * seekBar.getMax()));
                    }
                    myhandle.sendEmptyMessageDelayed(UPDATE_VIDEO_SEEKBAR,1000);
                    break;
                case UPDATE_CONTROL:

                    break;
                case INITIALIZE_SURFACE_WIDTH:
                    initmedia();
                    break;
            }

            super.handleMessage(msg);
        }
    };

    private IMediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener = new IMediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(IMediaPlayer iMediaPlayer,int percent) {
            Log.i("onBufferingUpdate", "onBufferingUpdate:" + percent);
            seekBar.setSecondaryProgress((int) ((float) percent / 100 * seekBar.getMax()));
        }
    };

    private IMediaPlayer.OnCompletionListener mOnCompletionListener = new IMediaPlayer.OnCompletionListener(){

        @Override
        public void onCompletion(IMediaPlayer iMediaPlayer) {
            Log.i("onCompletion", "onCompletion:");
            playbutton.setBackgroundResource(R.drawable.ic_media_play);
        }
    };

    private IMediaPlayer.OnErrorListener mOnErrorListener = new IMediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
            return false;
        }
    };

    private IMediaPlayer.OnInfoListener mOnInfoListener  =  new IMediaPlayer.OnInfoListener() {
        public boolean onInfo(IMediaPlayer mp, int arg1, int arg2) {
            switch (arg1) {
                case IMediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
                    Log.d(TAG, "MEDIA_INFO_VIDEO_TRACK_LAGGING:");
                    break;
                case IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                    Log.d(TAG, "MEDIA_INFO_VIDEO_RENDERING_START:");
                    break;
                case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                    Log.d(TAG, "MEDIA_INFO_BUFFERING_START:");
                    break;
                case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                    Log.d(TAG, "MEDIA_INFO_BUFFERING_END:");
                    break;
                case IMediaPlayer.MEDIA_INFO_NETWORK_BANDWIDTH:
                    Log.d(TAG, "MEDIA_INFO_NETWORK_BANDWIDTH: " + arg2);
                    break;
                case IMediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
                    Log.d(TAG, "MEDIA_INFO_BAD_INTERLEAVING:");
                    break;
                case IMediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
                    Log.d(TAG, "MEDIA_INFO_NOT_SEEKABLE:");
                    break;
                case IMediaPlayer.MEDIA_INFO_METADATA_UPDATE:
                    Log.d(TAG, "MEDIA_INFO_METADATA_UPDATE:");
                    break;
                case IMediaPlayer.MEDIA_INFO_UNSUPPORTED_SUBTITLE:
                    Log.d(TAG, "MEDIA_INFO_UNSUPPORTED_SUBTITLE:");
                    break;
                case IMediaPlayer.MEDIA_INFO_SUBTITLE_TIMED_OUT:
                    Log.d(TAG, "MEDIA_INFO_SUBTITLE_TIMED_OUT:");
                    break;
                case IMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED:
                    Log.d(TAG, "MEDIA_INFO_VIDEO_ROTATION_CHANGED: " + arg2);
                    break;
                case IMediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START:
                    Log.d(TAG, "MEDIA_INFO_AUDIO_RENDERING_START:");
                    break;
            }
            return true;
        }
    };

    private class SurfaceViewCallBack implements SurfaceHolder.Callback{

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Log.d(TAG, "surfaceCreated: ");
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Log.d(TAG, "surfaceChanged: ");
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            Log.d(TAG, "surfaceDestroyed: ");
            if (ijkMediaPlayer != null)
            {
                ijkMediaPlayer.reset();
                ijkMediaPlayer.release();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ijkMediaPlayer != null){
            pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ijkMediaPlayer != null)
        {
            play();
        }
    }
}
