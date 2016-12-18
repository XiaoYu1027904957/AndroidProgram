package com.yuxiaobai.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by yuxiaobai on 2016/12/12.
 */

public class NoScrollViewPager extends ViewPager {
    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    /**
     * 屏幕ViewPager滑动
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return true;
    }
}
