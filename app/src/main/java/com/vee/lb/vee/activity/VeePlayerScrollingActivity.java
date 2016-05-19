package com.vee.lb.vee.activity;

import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
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
import com.vee.lb.vee.util.PreInitialize;
import com.vee.lb.vee.view.VeePlayerSurfaceView;

import java.util.Calendar;

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

    private String remoteurl = "http://192.168.1.100/6649961-1.flv";
    private final int STATE_SEEKBAR_BUFF_UPDATE = 101;
    private final int STATE_SEEKBAR_BUTTON_UPDATE = 102;
    private final String video_buff_bundle = "video_buff_bundle";

    private final static int UPDATE_SYS_TIME_MSG = 101;
    private final static int UPDATE_VIDEO_BUFF = 102;
    private final static int UPDATE_VIDEO_SEEKBAR = 103;
    private final static int UPDATE_CONTROL = 104;
    private final static int INITIALIZE_SURFACE_WIDTH = 105;

    private float gestureoldX;
    private float gestureoldY;
    private float gesturenewX;
    private float gesturenewY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vee_player_scrolling);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        init();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initmedia();
            }
        });
    }

    private void init(){
        new PreInitialize(this);
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
    }

    private void initialize(){
        surfaceView.getHolder().setKeepScreenOn(true);
        surfaceView.getHolder().addCallback(new SurfaceViewCallBack());

        volumelinearlayout.setVisibility(View.GONE);
        brightlinearlayout.setVisibility(View.GONE);

        gestureDetector = new GestureDetector(this,new VeePlayerGestureListener());
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
    }

    private VeePlayerSurfaceView.SurfaceViewListener surfaceviewlistener = new VeePlayerSurfaceView.SurfaceViewListener() {
        @Override
        public void gesturedown(MotionEvent event) {
            gestureoldX = event.getX();
            gestureoldY = event.getY();
        }

        @Override
        public void gesturemove(MotionEvent event) {
            Log.d(TAG, "onTouch: MOVE");
            gesturenewX = event.getX();
            gesturenewY = event.getY();
            if (gesturenewX > PreInitialize.winrect.width()/2){
                /**
                 *set volume
                 */
                int curvolume = PreInitialize.audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                int percent = curvolume*100/PreInitialize.maxvolume;
                Log.d(TAG, "onTouch: MOVE " + PreInitialize.winrect.width());
                volumetextview.setText(String.valueOf(percent));
                int setting = (int)(gesturenewY - gestureoldY);
                Log.d(TAG, "onTouch: MOVE " + setting);
            }else {
                /**
                 * set bright
                 */
            }
        }

        @Override
        public void gestureup(MotionEvent event) {
            Log.d(TAG, "onTouch: " + "UP");

        }
    };

    private void play(){
        updateseeknar = true;
        myhandle.sendEmptyMessage(UPDATE_VIDEO_SEEKBAR);
        playbutton.setBackgroundResource(R.drawable.ic_media_pause);
        ijkMediaPlayer.start();
    }

    private void stop(){
        ijkMediaPlayer.stop();
    }

    private void pause(){
        updateseeknar = false;
        playbutton.setBackgroundResource(R.drawable.ic_media_play);
        ijkMediaPlayer.pause();
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

    private class VeePlayerGestureListener extends GestureDetector.SimpleOnGestureListener {

        /** 双击 */
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.d(TAG, "onDoubleTap: ");
            return true;
        }

        /** 滑动 */
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {

            Log.d(TAG, "onScroll: ");
            return super.onScroll(e1, e2, distanceX, distanceY);
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
