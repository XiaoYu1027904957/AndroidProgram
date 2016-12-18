package com.yuxiaobai.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 作者：尚硅谷-杨光福 on 2016/12/14 09:17
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：自定义ViewPager
 */
public class HorizontalScrollViewPager extends ViewPager {
    public HorizontalScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private float startX;
    private float startY;

    /**
     * 上下滑动
     * getParent().requestDisallowInterceptTouchEvent(false);
     * 水平方向滑动
     * 1.第0个位置，并且是从左到右滑动
     * getParent().requestDisallowInterceptTouchEvent(false);
     * 2.最后一个位置,并且是从右到左的滑动
     * getParent().requestDisallowInterceptTouchEvent(false);
     * 3.中间位置
     * getParent().requestDisallowInterceptTouchEvent(true);
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                //把事件传递给HorizontalScrollViewPager类
//                getParent().requestDisallowInterceptTouchEvent(true);
                //1.按下的时候记录起始坐标
                startX = ev.getX();
                startY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //2.滑动的时候来到新的坐标
                float endX = ev.getX();
                float endY = ev.getY();
                //3.计算绝对值
                float dx = Math.abs(endX - startX);
                float dy = Math.abs(endY - startY);

                //4.判断滑动方向
                if(dx > dy){

                    //水平方向滑动
                    // 1.第0个位置，并且是从左到右滑动
                    if(getCurrentItem()==0&& endX - startX >0 ){
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }

                    // 2.最后一个位置,并且是从右到左的滑动
                    else if(getCurrentItem() ==getAdapter().getCount()-1 && endX - startX <0){
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                    //3.中间位置,要求父类不拦截当前控件的事件
                     else{
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }


                }else{
                    //竖直方向滑动
                    getParent().requestDisallowInterceptTouchEvent(false);
                }

                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
