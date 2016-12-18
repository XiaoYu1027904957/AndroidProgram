package com.yuxiaobai.detail;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.yuxiaobai.base.MenuDetailBasePager;

/**
 * Created by yuxiaobai on 2016/12/13.
 * 互动的的item的界面，
 */

public class InteractMenuDetailPager extends MenuDetailBasePager {
    private TextView textView;

    public InteractMenuDetailPager(Context mContext) {
        super(mContext);
        textView.setText("互动详情界面");
    }

    @Override
    protected View initView() {
         textView = new TextView(mContext);
        textView.setTextColor(Color.RED);
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }
}
