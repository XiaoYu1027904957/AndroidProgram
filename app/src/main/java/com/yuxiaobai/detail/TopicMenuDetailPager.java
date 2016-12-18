package com.yuxiaobai.detail;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.yuxiaobai.MainActivity;
import com.yuxiaobai.R;
import com.yuxiaobai.base.MenuDetailBasePager;
import com.yuxiaobai.bean.NewsCenterBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuxiaobai on 2016/12/16.
 */

public class TopicMenuDetailPager extends MenuDetailBasePager {
    /**
     * 专题详情页面的数据
     */
    private final List<NewsCenterBean.DataBean.ChildrenBean> datas;
    /**
     * 页面的集合
     */
    private ArrayList<TopicTabDetailPager> pagers;
    private ViewPager viewPager;
    //    private TabPageIndicator tabPageIndicator;
    private TabLayout tabLayout;
    private ImageButton ib_next;

    public TopicMenuDetailPager(Context context, NewsCenterBean.DataBean dataBean) {
        super(context);
        //接收数据
        datas = dataBean.getChildren();
    }

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.topic_menu_detail_pager, null);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
//        tabPageIndicator = (TabPageIndicator) view.findViewById(tabPageIndicator);
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        ib_next = (ImageButton) view.findViewById(R.id.ib_next);
        return view;
    }

    @Override
    public void iniData() {
        super.iniData();
        //准备数据-页面TabDetailPager的12个实例
        pagers = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            pagers.add(new TopicTabDetailPager(mContext, datas.get(i)));
        }
        //设置ViewPager的适配器
        viewPager.setAdapter(new TopicMenuDetailPagerAdapter());
        //设置ViewPager
//        tabPageIndicator.setViewPager(viewPager);
        //TabLayout和Viewpager关联
        // 注意了setupWithViewPager必须在ViewPager.setAdapter()之后调用
        tabLayout.setupWithViewPager(viewPager);
        //设置模式
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        //设置点击事件
        ib_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
            }
        });

        //监听页面的改变
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                MainActivity mainActivity  = (MainActivity) mContext;
                if(position ==0){
                    //SlidingMenu可以侧滑
                    mainActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                }else{
                    //不可以侧滑
                    mainActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(getTabView(i));
        }


    }


    public View getTabView(int position){
        View view = LayoutInflater.from(mContext).inflate(R.layout.tab_item, null);
        TextView tv= (TextView) view.findViewById(R.id.textView);
        tv.setText(datas.get(position).getTitle());
        ImageView img = (ImageView) view.findViewById(R.id.imageView);
        img.setImageResource(R.drawable.dot_focus);
        return view;
    }

    class TopicMenuDetailPagerAdapter extends PagerAdapter {

        /**
         * 显示标题
         * @param position
         * @return
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return datas.get(position).getTitle();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TopicTabDetailPager tabDetailPager = pagers.get(position);
            View rootView = tabDetailPager.rootView;
            container.addView(rootView);
            //调用initData
            tabDetailPager.initData();
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
