package com.yuxiaobai.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.yuxiaobai.base.BasePager;

/**
 * Created by yuxiaobai on 2016/12/12.
 */

public class SettingPager extends BasePager {
    public SettingPager(Context context) {
        super(context);
        tvTitle.setText("设置");
        TextView textView = new TextView(mContext);
        textView.setTextSize(25);
        textView.setTextColor(Color.RED);
        textView.setGravity(Gravity.CENTER);
        textView.setText("设置内容");
//         添加到帧布局
        flContent.addView(textView);

    }
}
