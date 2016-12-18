package com.yuxiaobai;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.WindowManager;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.yuxiaobai.Utils.DensityUtil;
import com.yuxiaobai.fragment.ContentFragment;
import com.yuxiaobai.fragment.LeftMenuFragment;

/**
 * Created by yuxiaobai on 2016/12/15.
 */
public class MainActivity extends SlidingFragmentActivity {

    public static final String LEFT_MAIN = "left_main";
    public static final String CONTENT_MAIN = "content_main";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        initSlidingMenu();
        //  初始化Fragment
        initFragment();
    }

    private void initFragment() {
//        getSupportFragmentManager().beginTransaction().replace(R.id.left_main,new LeftMenuFragment(), LEFT_MAIN)
//                .replace(R.id.fl_main_one,new ContentFragment(), CONTENT_MAIN).commit();

        //        //1.得到FragmentMnager
        FragmentManager fm = getSupportFragmentManager();
        //2.开始事务
        FragmentTransaction ft = fm.beginTransaction();
        //3.替换两个Fragment
        ft.replace(R.id.left_main, new LeftMenuFragment(), LEFT_MAIN);
        ft.replace(R.id.fl_main_one, new ContentFragment(), CONTENT_MAIN);
        //4.提交代码
        ft.commit();

    }

    private void initSlidingMenu() {
//         设置主界面中间布局
        setContentView(R.layout.activity_main);
//        设置左侧菜单(注意细心)
        setBehindContentView(R.layout.left_menu);
//         设置右侧菜单
        SlidingMenu slidingMenu = getSlidingMenu();
//        slidingMenu.setSecondaryMenu(R.layout.right_menu);
//         设置试图模式 有三种
        slidingMenu.setMode(SlidingMenu.LEFT);
//          设置触摸模式
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
//        设置站主界面200dp
        slidingMenu.setBehindOffset(DensityUtil.dip2px(this, 200));


    }

    /**
     * 得到左侧菜单
     *
     * @return
     */

    public LeftMenuFragment getLeftMenuFragment() {
        return (LeftMenuFragment) getSupportFragmentManager().findFragmentByTag(LEFT_MAIN);
    }

    public ContentFragment  getContentFragment() {
        return (ContentFragment) getSupportFragmentManager().findFragmentByTag(CONTENT_MAIN);

    }
}
