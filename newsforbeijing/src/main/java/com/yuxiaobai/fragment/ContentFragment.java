package com.yuxiaobai.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.yuxiaobai.MainActivity;
import com.yuxiaobai.R;
import com.yuxiaobai.base.BaseFragment;
import com.yuxiaobai.base.BasePager;
import com.yuxiaobai.pager.HomePager;
import com.yuxiaobai.pager.NewsCenterPager;
import com.yuxiaobai.pager.SettingPager;
import com.yuxiaobai.view.NoScrollViewPager;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by yuxiaobai on 2016/12/12.
 */

public class ContentFragment extends BaseFragment {
    @InjectView(R.id.viewpager)
    NoScrollViewPager viewpager;
    @InjectView(R.id.rg_main)
    RadioGroup rgMain;

    private ArrayList<BasePager> pagers;


    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_content, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        //ViewPager绑定界面
        pagers = new ArrayList<>();
        pagers.add(new HomePager(mContext));//添加主页面
        pagers.add(new NewsCenterPager(mContext));//添加新闻界面
        pagers.add(new SettingPager(mContext));//添加设置界面

//         设置适配器
        viewpager.setAdapter(new ContentFragmentAdapter());
//       监RedioGroup状态的变化
        rgMain.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
//         默认选中主界面
        rgMain.check(R.id.rb_home);
// 　　　 监听ViewPager状态的变化
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                pagers.get(position).initData();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        pagers.get(0).initData();

    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    public NewsCenterPager  getNewsCenterPager() {
        return (NewsCenterPager) pagers.get(1);

    }

    private class ContentFragmentAdapter extends PagerAdapter {
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager basePager = pagers.get(position);
            View rootView = basePager.rootView;
//             添加到集合中
            container.addView(rootView);
//             调用basePager
//            basePager.initData();


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
            return object == view;
        }
    }


    private class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
//             设置只有在新闻界面可以拖拽
            MainActivity mainActivity = (MainActivity) mContext;
            mainActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
            switch (checkedId) {
                case R.id.rb_home:
                    viewpager.setCurrentItem(0, false);

                    break;
                case R.id.rb_news:
                    viewpager.setCurrentItem(1, false);
                    mainActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

                    break;
                case R.id.rb_setting:
                    viewpager.setCurrentItem(2, false);

                    break;
            }

        }
    }
}
