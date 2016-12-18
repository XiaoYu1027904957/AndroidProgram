package com.yuxiaobai.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.yuxiaobai.base.BasePager;

/**
 * Created by yuxiaobai on 2016/12/12.
 */

public class HomePager extends BasePager {
    public HomePager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        tvTitle.setText("主页");
//        绑定数据
        TextView textView = new TextView(mContext);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(25);
        textView.setTextColor(Color.RED);
        textView.setText("主页内容");
//         添加到帧布局中形成一个主体
        flContent.addView(textView);


    }
}
