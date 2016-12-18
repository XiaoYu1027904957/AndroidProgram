package com.yuxiaobai.base;

import android.content.Context;
import android.view.View;

/**
 * Created by yuxiaobai on 2016/12/13.
 */

public abstract class MenuDetailBasePager {
    /**
     * 上下文
     */
    public Context mContext;
    /**
     * 各个孩子页面的实例
     */
    public View rootView;

    public MenuDetailBasePager(Context mContext) {
        this.mContext = mContext;
        rootView = initView();
    }

    /**
     * 孩子的视图，每个孩子的视图不同，所以写成抽象方法。
     * 让孩子分别去实现
     *
     * @return
     */

    protected abstract View initView();

    /**
     * 需要联网的时候
     * 进行数据的加载
     */
    public void iniData() {

    }
}
