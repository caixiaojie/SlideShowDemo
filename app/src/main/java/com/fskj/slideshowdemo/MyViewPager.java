package com.fskj.slideshowdemo;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * author: Administrator
 * date: 2018/2/5 0005
 * desc:
 */

public class MyViewPager extends ViewPager {
    boolean isAutoPlay = true;
    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e("onTouchEvent","ACTION_DOWN");
                setAutoPlay(false);
                break;
            case MotionEvent.ACTION_UP:
                Log.e("onTouchEvent","ACTION_UP");
                setAutoPlay(true);
                break;
        }
        return super.onTouchEvent(ev);
    }


    public boolean isAutoPlay() {
        return isAutoPlay;
    }

    public void setAutoPlay(boolean autoPlay) {
        isAutoPlay = autoPlay;
    }
}
