package com.yuxiaobai.base;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.yuxiaobai.MainActivity;
import com.yuxiaobai.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by yuxiaobai on 2016/12/12.
 */

public class BasePager {
    @InjectView(R.id.ib_swich)
    public ImageButton ibSwich;
    @InjectView(R.id.tv_title)
    public TextView tvTitle;
    @InjectView(R.id.ib_menu)
    public ImageButton ibMenu;
    @InjectView(R.id.fl_content)
    public FrameLayout flContent;
    public Context mContext;
    public View rootView;


    public BasePager(Context context) {
        this.mContext = context;
        rootView = initView();
    }


    /**
     * 把内置的标题栏和frameLAYOUT实例化
     */
    private View initView() {
        View view = View.inflate(mContext, R.layout.basepager, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @OnClick(R.id.ib_menu)
    public void onClick() {
        MainActivity mainActivity = (MainActivity) mContext;
        mainActivity.getSlidingMenu().toggle();//关<-->开
    }

    /**
     * 1.当需要联网需要时
     * 2.需要绑定数据到ui时
     */
    public void initData() {

    }
}
