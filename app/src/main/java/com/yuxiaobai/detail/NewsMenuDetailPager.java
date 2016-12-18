package com.yuxiaobai.detail;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;
import com.yuxiaobai.MainActivity;
import com.yuxiaobai.R;
import com.yuxiaobai.base.MenuDetailBasePager;
import com.yuxiaobai.bean.NewsCenterBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuxiaobai on 2016/12/13.
 * 新闻item的界面
 */

public class NewsMenuDetailPager extends MenuDetailBasePager {
    /**
     * 新闻详情页面的数据
     */
    private final List<NewsCenterBean.DataBean.ChildrenBean> children;
    /**
     * 也main的集合
     */
    private ArrayList<TabDetailPager> pagers;
    private ViewPager viewPager;
    private TabPageIndicator tabPageIndicator;
    private ImageButton ib_next;

    public NewsMenuDetailPager(Context mContext, NewsCenterBean.DataBean dataBean) {
        super(mContext);
        children = dataBean.getChildren();
    }

    @Override
    protected View initView() {
//       得到试图
        View view = View.inflate(mContext, R.layout.news_menu_detail, null);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        tabPageIndicator = (TabPageIndicator) view.findViewById(R.id.tabPageIndicator);
        ib_next = (ImageButton) view.findViewById(R.id.ib_next);
        ib_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            }
        });
        return view;
    }

    @Override
    public void iniData() {
        super.iniData();
//         设置适配器Viewpager的适配器
//         准备事儿个实例
        pagers = new ArrayList<>();
        for (int i = 0; i < children.size(); i++) {
            pagers.add(new TabDetailPager(mContext, children.get(i)));
        }
        newMenuDetailPagerAdapter adapter = new newMenuDetailPagerAdapter();
        viewPager.setAdapter(adapter);
//     设置viewpager
        tabPageIndicator.setViewPager(viewPager);

//          监听页面的改变

        tabPageIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                MainActivity mainactivity = (MainActivity) mContext;

                if (position == 0) {
                    mainactivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                } else {
                    mainactivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    class newMenuDetailPagerAdapter extends PagerAdapter {
        @Override
        public CharSequence getPageTitle(int position) {
            return children.get(position).getTitle();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabDetailPager tabDetailPager = pagers.get(position);
            View rootView = tabDetailPager.rootView;
            container.addView(rootView);
            tabDetailPager.iniData();

            return rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return pagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

}
