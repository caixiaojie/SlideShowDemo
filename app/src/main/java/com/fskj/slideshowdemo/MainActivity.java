package com.fskj.slideshowdemo;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MyViewPager viewPager;
    private int [] strImgs = new int[]{R.mipmap.img_1296,R.mipmap.img_5220,R.mipmap.img_8923};
    private List<Integer> imgList;
    private ViewPagerAdapter pagerAdapter;
    private LinearLayout llPoints;
    private int prevPosition = 0;
    private boolean isAutoPlay = true;


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (isAutoPlay) {
                Message message = new Message();
                handler.sendMessage(message);
                handler.postDelayed(runnable, 3000);
            }else {
                // 如果处于拖拽状态停止自动播放，会每隔5秒检查一次是否可以正常自动播放。
                handler.postDelayed(runnable, 5000);
            }
        }
    };

    private Handler handler = new Handler() {

        @SuppressLint("HandlerLeak")
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case 0:
                    // 得到mvp当前页面的索引
                    int currentItem = viewPager.getCurrentItem();
                    Log.e("currentItem","currentItem="+currentItem);
                    // 要显示的下一个页面的索引
                    currentItem++;
                    // 设置ViewPager显示的页面
                    viewPager.setCurrentItem(currentItem);
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (MyViewPager) findViewById(R.id.viewPager);
        llPoints = (LinearLayout) findViewById(R.id.llPoints);
        imgList = new ArrayList<>();
        for (int i = 0; i < strImgs.length; i++) {
            imgList.add(strImgs[i]);
        }
        for (int i = 0; i < imgList.size(); i++) {
            //根据图片的数量生成相对应的数量的小圆点
            final View view=new View(this);
            view.setBackgroundResource(R.drawable.dot_unselect);
            DisplayMetrics metrics=new DisplayMetrics();
            float width= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,10, metrics);
            float height=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 10, metrics);
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams((int)width,(int)height);
            params.leftMargin=5;
            view.setLayoutParams(params);
            llPoints.addView(view);
        }


        pagerAdapter = new ViewPagerAdapter(imgList,MainActivity.this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(Integer.MAX_VALUE / 2);
        llPoints.getChildAt(0).setBackgroundResource(R.drawable.dot_select);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int newPosition = position % imgList.size();

                llPoints.getChildAt(prevPosition).setBackgroundResource(R.drawable.dot_unselect);

                llPoints.getChildAt(newPosition).setBackgroundResource(R.drawable.dot_select);

                //把当前点位置做为下一次变化的前一个点的位置
                prevPosition =newPosition;
                handler.removeCallbacks(runnable);
                Log.d("onPageSelected", "onPageSelected，page:" + position);
                handler.postDelayed(runnable,3*1000);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    //拖拽中
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        isAutoPlay = false;
                        break;
                    //闲置中
                    case ViewPager.SCROLL_STATE_IDLE:
                        isAutoPlay = true;
                        break;
                    //设置中
                    case ViewPager.SCROLL_STATE_SETTLING:
                        isAutoPlay = true;
                        break;
                }
            }
        });
        isAutoPlay = viewPager.isAutoPlay();
        handler.postDelayed(runnable,3000);
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isAutoPlay = false;
                        break;
                    case MotionEvent.ACTION_UP:
                        isAutoPlay = true;
                        break;
                }
                return false;
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        isAutoPlay = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isAutoPlay = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return super.onTouchEvent(event);
    }
}
