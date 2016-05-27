package com.vee.lb.vee.activity;

import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.vee.lb.vee.R;
import com.vee.lb.vee.util.PreInitialize;

import java.util.Calendar;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class VeeMainPlayerActivity extends AppCompatActivity {
    private String TAG = "VeeMainPlayerActivity";
    private SurfaceView surfaceView;
    private ImageButton back_button;
    private TextView banguminame_textview;
    private TextView systime_textview;
    private SeekBar seekBar;
    private ImageButton play_button;
    private TextView playtime_textview;
    private ImageView volume_bright_imageview;
    private TextView volume_bright_textview;
    private RelativeLayout top_control_relativelayout;
    private LinearLayout bottom_control_linearlayout;
    private LinearLayout volume_bright_linearlayout;

    private IjkMediaPlayer ijkMediaPlayer;
    private SurfaceHolder surfaceHolder;
    private boolean updateseeknar = false;
    private int controlstatus = View.GONE;

    private final static int UPDATE_SYS_TIME_MSG = 101;
    private final static int UPDATE_VIDEO_BUFF = 102;
    private final static int UPDATE_VIDEO_SEEKBAR = 103;
    private final static int UPDATE_CONTROL = 104;
    private final static int INITIALIZE_SURFACE_WIDTH = 105;
    private final static int UPDATE_CONTROL_IM = 106;
    private final static int EMPTY_MES = 200;
    private String remoteurl = "http://cn-gdfs15-dx.acgvideo.com/vg18/0/4a/7755602-1.flv?expires=1464377400&ssig=n7pQfptRvR1AoEDL5uEOog&oi=244766114&player=1&or=3074230573&rate=0";

    private float oldX = 0;
    private float oldY = 0;
    private float newY = 0;
    private float newX = 0;
    private int volumevalue = 0;
    private int brightvalue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_vee_main_player);
        init();
    }

    private void init(){
        findview();
        getData();
        initlistener();
        initialize();

        myhandle.sendEmptyMessage(UPDATE_SYS_TIME_MSG);
        myhandle.sendEmptyMessageDelayed(INITIALIZE_SURFACE_WIDTH, 500);
        myhandle.sendEmptyMessageDelayed(UPDATE_CONTROL,3000);
    }

    private void findview(){
        surfaceView = (SurfaceView)findViewById(R.id.vee_main_player_surfaceview);
        back_button = (ImageButton)findViewById(R.id.vee_main_player_backbutton);
        banguminame_textview = (TextView)findViewById(R.id.vee_main_player_video_name);
        systime_textview = (TextView)findViewById(R.id.vee_main_player_sys_time);
        seekBar = (SeekBar)findViewById(R.id.vee_main_player_video_seekbar);
        play_button = (ImageButton)findViewById(R.id.vee_main_player_playbutton);
        playtime_textview = (TextView)findViewById(R.id.vee_main_player_playtime);
        volume_bright_imageview = (ImageView)findViewById(R.id.vee_main_player_volume_bright_image);
        volume_bright_textview = (TextView)findViewById(R.id.vee_main_player_volume_bright_text);
        volume_bright_linearlayout = (LinearLayout)findViewById(R.id.vee_main_player_volume_bright_linearlayout);
        bottom_control_linearlayout = (LinearLayout)findViewById(R.id.vee_main_player_bottom_control);
        top_control_relativelayout = (RelativeLayout)findViewById(R.id.vee_main_player_top_control);

        surfaceHolder = surfaceView.getHolder();
    }

    private void getData(){

    }

    private void initialize(){

    }

    private void initlistener(){
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ijkMediaPlayer != null){
                    if (ijkMediaPlayer.isPlaying()){
                        pause();
                    }else {
                        play();
                    }
                }
            }
        });

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
                if (ijkMediaPlayer != null) {
                    int sp = seekBar.getProgress();
                    long du = ijkMediaPlayer.getDuration() * sp / seekBar.getMax();
                    ijkMediaPlayer.seekTo(du);
                    play();

                    String strcur = formatTime(ijkMediaPlayer.getCurrentPosition());
                    String total = formatTime(ijkMediaPlayer.getDuration());
                    playtime_textview.setText(strcur + "|" + total);
                }
            }
        });

        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if (ijkMediaPlayer != null) {
                    ijkMediaPlayer.reset();
                    ijkMediaPlayer.release();
                }
            }
        });

        surfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        myhandle.sendEmptyMessage(UPDATE_CONTROL);
                        oldX = event.getX();
                        oldY = event.getY();
                        volumevalue = PreInitialize.audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                        try {
                            brightvalue = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
                        } catch (Exception e) {
                        }
                        Log.d(TAG, "onTouch: ACTION_DOWN " + volumevalue + " " + brightvalue);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        newX = event.getX();
                        newY = event.getY();
                        int dis = (int) (newY - oldY);
                        if (Math.abs(dis) > PreInitialize.dis_of_conflict) {
                            if (newX < PreInitialize.winrect.height() / 2) {
                                /**
                                 * deal with bright
                                 */
                                String str;

                                if (brightvalue <= PreInitialize.maxbright) {
                                    int percent = brightvalue * 100 / PreInitialize.maxbright;
                                    int add_percent = dis * 100 / PreInitialize.winrect.height();
                                    percent -= add_percent;
                                    if (percent > 0 && percent < 100) {
                                        str = String.valueOf(percent) + "%";
                                        Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, PreInitialize.maxbright * percent / 100);
                                    } else if (percent <= 0) {
                                        percent = 0;
                                        str = String.valueOf(percent) + "%";
                                        Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, PreInitialize.maxbright * percent / 100);
                                    } else {
                                        percent = 100;
                                        str = String.valueOf(percent) + "%";
                                        Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, PreInitialize.maxbright * percent / 100);
                                    }
                                } else {
                                    str = "100%";
                                }
                                volume_bright_textview.setText(str);
                                volume_bright_imageview.setBackgroundResource(R.drawable.ic_bright);
                                volume_bright_linearlayout.setVisibility(View.VISIBLE);


                            } else if (newX > PreInitialize.winrect.height() / 2) {
                                /**
                                 * deal with volume
                                 */

                                String str;
                                Log.d(TAG, "onTouch: volume " + volumevalue + " " + PreInitialize.maxvolume);
                                if (volumevalue <= PreInitialize.maxvolume) {
                                    int percent = volumevalue * 100 / PreInitialize.maxvolume;
                                    int add_percent = dis * 100 / PreInitialize.winrect.height();
                                    percent -= add_percent;
                                    if (percent > 0 && percent < 100) {
                                        str = String.valueOf(percent) + "%";
                                        PreInitialize.audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, PreInitialize.maxvolume * percent / 100, 0);
                                    } else if (percent <= 0) {
                                        percent = 0;
                                        str = String.valueOf(percent) + "%";
                                        PreInitialize.audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                                    } else {
                                        percent = 100;
                                        str = String.valueOf(percent) + "%";
                                        PreInitialize.audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, PreInitialize.maxvolume, 0);
                                    }
                                } else {
                                    str = "100%";
                                }
                                volume_bright_textview.setText(str);
                                volume_bright_imageview.setBackgroundResource(R.drawable.ic_volume);
                                volume_bright_linearlayout.setVisibility(View.VISIBLE);
                            }
                        }

                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "onTouch: ACTION_UP");
                        volume_bright_linearlayout.setVisibility(View.GONE);
                        break;
                }
                return false;
            }
        });

        surfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        top_control_relativelayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        bottom_control_linearlayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initIjkMedia(){
        try{
            ijkMediaPlayer = new IjkMediaPlayer();
            ijkMediaPlayer.setDisplay(surfaceView.getHolder());
            ijkMediaPlayer.setScreenOnWhilePlaying(true);
            ijkMediaPlayer.setDataSource(remoteurl);
            ijkMediaPlayer.setOnBufferingUpdateListener(onBufferingUpdateListener);
            ijkMediaPlayer.prepareAsync();
            play();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private IjkMediaPlayer.OnBufferingUpdateListener onBufferingUpdateListener = new IMediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(IMediaPlayer mp, int percent) {
            seekBar.setSecondaryProgress((int)((float)percent / 100 * seekBar.getMax()));
        }
    };

    private void play(){
        if (ijkMediaPlayer != null){
            updateseeknar = true;
            play_button.setBackgroundResource(R.drawable.ic_media_pause);
            myhandle.sendEmptyMessage(UPDATE_VIDEO_SEEKBAR);
            myhandle.sendEmptyMessageDelayed(UPDATE_CONTROL,3000);
            ijkMediaPlayer.start();
        }
    }

    private void pause(){
        if (ijkMediaPlayer != null){
            updateseeknar = false;
            play_button.setBackgroundResource(R.drawable.ic_media_play);
            ijkMediaPlayer.pause();
        }
    }

    private String formatTime(long time){
        String minutestr;
        String secondstr;
        long second = time / 1000;
        long minute = second / 60;
        if (minute == 0)
        {
            return "00:"+String.valueOf(second%60);
        }else if (minute < 10)
        {
            return "0" + minute + ":" + String.valueOf(second%60);
        }else
        {
            return String.valueOf(minute) + ":" + String.valueOf(second%60);
        }

    }

    private int getControlstatus(){
        return controlstatus;
    }

    private void setControlStatus(int visible){
        top_control_relativelayout.setVisibility(visible);
        bottom_control_linearlayout.setVisibility(visible);
    }

    private final Handler myhandle = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
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
                    systime_textview.setText(str);
                    myhandle.sendEmptyMessageDelayed(UPDATE_SYS_TIME_MSG, 60 * 1000);
                    break;
                case UPDATE_VIDEO_BUFF:

                    break;
                case UPDATE_VIDEO_SEEKBAR:
                    if (updateseeknar)
                    {
                        long curpos = ijkMediaPlayer.getCurrentPosition();
                        long duration = ijkMediaPlayer.getDuration();
                        seekBar.setProgress((int) ((float)curpos / duration * seekBar.getMax()));

                        String strcur = formatTime(ijkMediaPlayer.getCurrentPosition());
                        String total = formatTime(ijkMediaPlayer.getDuration());
                        playtime_textview.setText(strcur + "|" + total);
                    }
                    myhandle.sendEmptyMessageDelayed(UPDATE_VIDEO_SEEKBAR,1000);
                    break;
                case UPDATE_CONTROL:
                    Log.d(TAG, "handleMessage: UPDATE_CONTROL" + getControlstatus());
                    try{
                        if (getControlstatus() == View.VISIBLE)
                        {
                            setControlStatus(View.GONE);
                            controlstatus = View.GONE;
                            myhandle.removeMessages(UPDATE_CONTROL);
                        }else if (getControlstatus() == View.GONE){
                            controlstatus = View.VISIBLE;
                            setControlStatus(View.VISIBLE);
                            myhandle.sendEmptyMessageDelayed(UPDATE_CONTROL,3000);
                        }
                    }catch(Exception e)
                    {
                        Log.d(TAG, "handleMessage: UPDATE_CONTROL");
                        e.printStackTrace();
                    }
                    break;
                case UPDATE_CONTROL_IM:
                    if (getControlstatus() == View.VISIBLE)
                    {
                        setControlStatus(View.GONE);
                        controlstatus = View.GONE;
                    }else if (getControlstatus() == View.GONE){
                        controlstatus = View.VISIBLE;
                        setControlStatus(View.VISIBLE);
                    }
                    break;
                case INITIALIZE_SURFACE_WIDTH:
                    initIjkMedia();
                    break;
                case EMPTY_MES:
                    break;
            }
            super.handleMessage(msg);
        }
    };
}
