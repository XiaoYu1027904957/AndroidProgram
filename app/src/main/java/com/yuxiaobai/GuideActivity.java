package com.yuxiaobai;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.yuxiaobai.Utils.CacheUtils;
import com.yuxiaobai.Utils.DensityUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by yuxiaobai on 2016/12/15.
 */
public class GuideActivity extends Activity {
    public static final String START_MAIN = "start_main";

    @InjectView(R.id.viewpager)
    ViewPager viewpager;
    @InjectView(R.id.btn_start_main)
    Button btnStartMain;
    @InjectView(R.id.ll_point_group)
    LinearLayout llPointGroup;
    @InjectView(R.id.iv_red_point)
    ImageView ivRedPoint;
    int[] ids = {R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3};
    private int margin;

    int width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.inject(this);
        initData();

    }

    private void initData() {
        width= DensityUtil.dip2px(this,10);
//         添加咱个灰点
        for (int i = 0; i < ids.length; i++) {
            ImageView point = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, width);
//            如果不是第一个就距离上一个有一定的距离
            if (i != 0) {
                params.leftMargin = width;
            }
            point.setLayoutParams(params);
//             设置灰色图片
            point.setImageResource(R.drawable.shape_guide_normal_point);
//            添加线性布局
            llPointGroup.addView(point);


        }
//          设置viewPager
        viewpager.setAdapter(new GuideAdapter());
//         监听页面的滑动　
        viewpager.addOnPageChangeListener(new MyPagerListener());
//        构造方法--- omMeasure  -- onLayout--- onDraw
        ivRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(new MyOnGlobalLayoutListener());


    }

    @OnClick(R.id.btn_start_main)
    public void onClick() {
        CacheUtils.putBoolean(this,START_MAIN,true);

        startActivity(new Intent(this, MainActivity.class));
//         保持已经进入主界面
        finish();

    }

    //  些一个类继承PagerAdapter
    private class GuideAdapter extends PagerAdapter {
        //      初始化界面
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView pager = new ImageView(GuideActivity.this);
            pager.setBackgroundResource(ids[position]);
//             添加倒容器中
            container.addView(pager);
            return pager;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return ids.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }


    //     页面滑动变化的监听方法
    private class MyPagerListener implements ViewPager.OnPageChangeListener {

        /**
         * @param position             正在滑动的页面的位置
         * @param positionOffset       滑动页面的百分比
         * @param positionOffsetPixels 滑动的像数
         */
        //         界面滑动以后的监听
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//           红点移动的距离 = 间距*当前页面的移动百分比
//            int marginleft = (int) (margin * positionOffset);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivRedPoint.getLayoutParams();
//            真实的坐标　＝　　原来的＋　红点移动的距离。
            int marginLeft = (position * margin + (int) (margin * positionOffset));
//          距离左边的距离
            params.leftMargin = marginLeft;
            ivRedPoint.setLayoutParams(params);

        }

        //           界面选择以后的监听
        @Override
        public void onPageSelected(int position) {
            if (position == ids.length - 1) {
                btnStartMain.setVisibility(View.VISIBLE);
            } else {
                btnStartMain.setVisibility(View.GONE);
            }

        }

        //         滑动界面改变的监听
        @Override
        public void onPageScrollStateChanged(int state) {


        }
    }

    private class MyOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        @Override
        public void onGlobalLayout() {
            ivRedPoint.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//              求间距,把点绘制到主界面上
//             第一个点距离左边的距离-第0个点距离左边的距离
            margin = llPointGroup.getChildAt(1).getLeft() - llPointGroup.getChildAt(0).getLeft();

        }
    }

}
