package com.yuxiaobai.detail;

import android.content.Context;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.yuxiaobai.R;
import com.yuxiaobai.Utils.BitmapCacheUtils;
import com.yuxiaobai.Utils.ContanUtils;
import com.yuxiaobai.adapter.photoMenuDetailPagerAdapter;
import com.yuxiaobai.base.MenuDetailBasePager;
import com.yuxiaobai.bean.NewsCenterBean;
import com.yuxiaobai.bean.PhotosMenuBean;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by yuxiaobai on 2016/12/13.
 * 图组item的界面
 */

public class PhotoMenuDetailPager extends MenuDetailBasePager {

    private final NewsCenterBean.DataBean dataBean;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    @InjectView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    private String url;
    private List<PhotosMenuBean.DataBean.NewsBean> news;
    private photoMenuDetailPagerAdapter adapter;
    private PhotosMenuBean.DataBean.NewsBean newsBean;
    private String urla;
    private BitmapCacheUtils bitmapCacheUtils;
    private DisplayImageOptions options;

    private int position;


    public PhotoMenuDetailPager(Context mContext, NewsCenterBean.DataBean dataBean) {
        super(mContext);
        this.dataBean = dataBean;
    }

    @Override
    protected View initView() {
        View view = View.inflate(mContext, R.layout.photos_menu_detail_pager, null);
        ButterKnife.inject(this, view);
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
//         下拉多少距离可刷新
        refreshLayout.setDistanceToTriggerSync(100);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                Toast.makeText(mContext, "下拉刷新了", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getDataFromNet();

                    }
                }, 2000);
            }
        });
        return view;
    }

    /**
     * 装载数据
     */
    @Override
    public void iniData() {
        super.iniData();
//         获取统一资源定位器
        url = ContanUtils.BASE_URL + dataBean.getUrl();
        getDataFromNet();
    }

    private void getDataFromNet() {
        RequestParams request = new RequestParams(url);
        x.http().get(request, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("TAG", "联网成功==" + result);
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("TAG", "联网失败==" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.e("TAG", "onCancelled==" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                Log.e("TAG", "onFinished==");
            }
        });

    }

    private void processData(String result) {
        PhotosMenuBean bean = new Gson().fromJson(result, PhotosMenuBean.class);
        news = bean.getData().getNews();
        if (news != null && news.size() > 0) {
            adapter = new photoMenuDetailPagerAdapter(mContext, news, recyclerView);
            recyclerView.setAdapter(adapter);
            if (isShowListView) {
                recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2, LinearLayoutManager.VERTICAL, false));
            }
        } else {
            Toast.makeText(mContext, "没有请求到数据", Toast.LENGTH_SHORT).show();
        }


    }


    /**
     * true:显示ListView
     * false:显示GridView
     */
    boolean isShowListView = true;

    /**
     * 选择需要显示哪张图片，·
     *
     * @param ibSwich
     */
    public void switchListGrid(ImageButton ibSwich) {
        if (isShowListView) {

            //Grid
            recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2, LinearLayoutManager.VERTICAL, false));

            //按钮- List
            ibSwich.setImageResource(R.drawable.icon_pic_list_type);
            isShowListView = false;
        } else {
            //List
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            //按钮-Grid
            ibSwich.setImageResource(R.drawable.icon_pic_grid_type);
            isShowListView = true;

        }

    }


}


