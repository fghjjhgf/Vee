package com.vee.lb.vee.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceView;

/**
 * Created by Administrator on 2016/5/15.
 */
public class VeePlayerSurfaceView extends SurfaceView {
    private String TAG = "VeePlayerSurfaceView";
    private SurfaceViewListener surfaceViewListener;
    private GestureDetector gestureDetector;

    public VeePlayerSurfaceView(Context context) {
        super(context);
        init(context);
    }

    public VeePlayerSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VeePlayerSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        gestureDetector = new GestureDetector(context,new VeePlayerGestureListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return false;
    }

    private class VeePlayerGestureListener extends GestureDetector.SimpleOnGestureListener {

        /** 滑动 */
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {

            Log.d(TAG, "onScroll: " + distanceX + " " + distanceY);
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return onTouchEvent(ev);
    }

    public void setSurfaceViewListener(SurfaceViewListener l){
        this.surfaceViewListener = l;
    }

    public interface SurfaceViewListener{
        public void gesturedown(MotionEvent event);
        public void gesturemove(MotionEvent event);
        public void gestureup(MotionEvent event);
    }

}
