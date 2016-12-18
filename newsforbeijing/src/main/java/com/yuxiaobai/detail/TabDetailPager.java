package com.yuxiaobai.detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.extras.SoundPullEventListener;
import com.yuxiaobai.NewsDetailActivity;
import com.yuxiaobai.R;
import com.yuxiaobai.Utils.CacheUtils;
import com.yuxiaobai.Utils.ContanUtils;
import com.yuxiaobai.Utils.DensityUtil;
import com.yuxiaobai.base.MenuDetailBasePager;
import com.yuxiaobai.bean.NewsCenterBean;
import com.yuxiaobai.bean.TabDetailBean;
import com.yuxiaobai.view.HorizontalScrollViewPager;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;



/**
 * Created by yuxiaobai on 2016/12/13.
 */

public class TabDetailPager extends MenuDetailBasePager {
    /**
     * JSON中保存chidlren的集合
     */
    private final NewsCenterBean.DataBean.ChildrenBean childrenBean;
    private ListView listView;
    private HorizontalScrollViewPager viewPager;
    private TextView tv_title;
    private LinearLayout ll_point_group;
    private TabDetailPagerListAdapter listviewadapter;
    private PullToRefreshListView pull_refresh_list;
    private String READ_NEWS_ID_ARRAY_KEY = "read_news_id_array_key";
    /**
     * 新闻列表的数据
     */
    private List<TabDetailBean.DataBean.NewsBean> news;

    /**
     * 顶部新闻的数据
     *
     * @param mContext
     * @param childrenBean
     */
    private List<TabDetailBean.DataBean.TopnewsBean> topnews;
    //     记录之前显示的位置的字段
    private int preSelect;
    private String url;
    private String moreUrl;

    /**
     * 写一个字段判断是否需要联网更新
     */
    private boolean isLoadMore = false;


    //  构造器传输了一个集合，同过这个集合来进行数据的传输
    public TabDetailPager(Context mContext, NewsCenterBean.DataBean.ChildrenBean childrenBean) {
        super(mContext);
        this.childrenBean = childrenBean;
    }

    /**
     * 加载视图初始化视图
     *
     * @return
     */
    @Override
    protected View initView() {
        //得到一个视图
        View view = View.inflate(mContext, R.layout.tabdetail_pager, null);
//        listView = (ListView) view.findViewById(R.id.listView);
        pull_refresh_list = (PullToRefreshListView) view.findViewById(R.id.pull_refresh_list);
        listView = pull_refresh_list.getRefreshableView();
        /**
         * 添加上拉下拉刷新的声音
         */
        SoundPullEventListener<ListView> soundListener = new SoundPullEventListener<>(mContext);
        soundListener.addSoundEvent(PullToRefreshBase.State.PULL_TO_REFRESH, R.raw.pull_event);
        soundListener.addSoundEvent(PullToRefreshBase.State.RESET, R.raw.reset_sound);
        soundListener.addSoundEvent(PullToRefreshBase.State.REFRESHING, R.raw.refreshing_sound);
        pull_refresh_list.setOnPullEventListener(soundListener);

        View header = View.inflate(mContext, R.layout.topnews_pager, null);
        viewPager = (HorizontalScrollViewPager) header.findViewById(R.id.viewpager);
        tv_title = (TextView) header.findViewById(R.id.tv_title);
        ll_point_group = (LinearLayout) header.findViewById(R.id.ll_point_group);
//         以头的形式添加
        listView.addHeaderView(header);
//         监听下拉的变化
        pull_refresh_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>(){
            /**
             * 下拉刷新时联网更新,不怎能更加数据
             * @param refreshView
             */
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                Toast.makeText(mContext, "下拉刷新", Toast.LENGTH_SHORT).show();
                Log.e("TAG", "moreUrl--------" + url);
//                联网更新数据
                isLoadMore = false;

                getDataFrom(url);
            }

            /**
             * 上啦刷新时，需要联网联网请求更多的数据
             * @param refreshView
             */
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                Toast.makeText(mContext, "上拉刷新", Toast.LENGTH_SHORT).show();
                if (!TextUtils.isEmpty(moreUrl)) {
                    //                联网获取更多数据
                    isLoadMore = true;
                    getDataFrom(moreUrl);
                } else {
//                     不去请求 ，隐藏刷新效果
                    pull_refresh_list.onRefreshComplete();
                    Toast.makeText(mContext, "没有更多数据了", Toast.LENGTH_SHORT).show();
                }


            }
        });
//      设置某一条数据的点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int realPosition = position - 2;
//                  的到对应点店家出的对象信息，从中取出对应的id进行保存
                TabDetailBean.DataBean.NewsBean newsBean = news.get(realPosition);
                Log.e("TAG", "news.get(realPosition)" + news.get(realPosition).getTitle());
//
                String ReadArrayId = CacheUtils.getString(mContext, READ_NEWS_ID_ARRAY_KEY, "");
                if (!ReadArrayId.contains(newsBean.getId() + "")) {
//                    把点击事件对应得id保存起来
                    CacheUtils.putString(mContext, READ_NEWS_ID_ARRAY_KEY, ReadArrayId + newsBean.getId() + ",");
//                     刷新适配器
                    listviewadapter.notifyDataSetChanged();//getCount-->getView
                }
                Intent intent = new Intent(mContext, NewsDetailActivity.class);
                intent.putExtra("url", ContanUtils.BASE_URL + newsBean.getUrl());
                mContext.startActivity(intent);

            }
        });


        return view;
    }

    @Override
    public void iniData() {
        super.iniData();
//       联网请求的链接
        url = ContanUtils.BASE_URL + childrenBean.getUrl();
        getDataFrom(url);
    }

    private void getDataFrom(String url) {

        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                 解析数据
                processData(result);
                pull_refresh_list.onRefreshComplete();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("TAG", "请求失败888");
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.e("TAG", "请求取消");

            }

            @Override
            public void onFinished() {
                Log.e("TAG", "请求----------------结束");

            }
        });
    }

    /**
     * handler
     */
    class InternetHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //            当前线程实在主线程中， 把轮播图切换到下一个界面
            int CurrentItem = (viewPager.getCurrentItem() + 1) % topnews.size();
            viewPager.setCurrentItem(CurrentItem);

//  递归   4秒后重新发送消息
            handler.postDelayed(new InternetRunnable(), 4000);

        }
    }

    class InternetRunnable implements Runnable {
        @Override
        public void run()     {
            handler.sendEmptyMessage(0);
        }
    }

    private InternetHandler handler;
    private InternetRunnable runnable;

    /**
     * 解析jspn数据
     */

    private void processData(String result) {

//          解析数据 将解析的数据放在TabDetailBean中
        TabDetailBean tabDetailBean = new Gson().fromJson(result, TabDetailBean.class);
//         获取更多数据的部分url
        String more = tabDetailBean.getData().getMore();
        if (!TextUtils.isEmpty(more)) {
            moreUrl = ContanUtils.BASE_URL + more;
        } else {
//              没有下一页
            moreUrl = "";
        }
        if (!isLoadMore) {//==isLoadMode=false;
//          设置顶部适配器
            topnews = tabDetailBean.getData().getTopnews();
            viewPager.setAdapter(new TabDetailPagerTopNewsAdapter());
//       设置页面变化的监听
            viewPager.addOnPageChangeListener(new TopNewsOnPageChangeListener());
//         设置默认的
            tv_title.setText(topnews.get(preSelect).getTitle());
//        初始化之前先把之前的所有的view清掉。
            ll_point_group.removeAllViews();
//         添加指示点
            for (int i = 0; i < topnews.size(); i++) {
                ImageView imageView = new ImageView(mContext);
                imageView.setBackgroundResource(R.drawable.top_point_selector);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, -2);
                if (i == 0) {
                    imageView.setEnabled(true);
                } else {
                    imageView.setEnabled(false);
                    params.leftMargin = DensityUtil.dip2px(mContext, 10);
                }
//             将点设置到线性布局中
                imageView.setLayoutParams(params);

                ll_point_group.addView(imageView);
            }
//         新闻列表
            news = tabDetailBean.getData().getNews();
//     设置适配器--listView的适配器
            listviewadapter = new TabDetailPagerListAdapter();
            listView.setAdapter(listviewadapter);

        } else {
//           需要加载更多数据
            news.addAll(tabDetailBean.getData().getNews());
//            覆盖原来的数据
//            news = tabDetailBean.getData().getNews();
//             刷新适配器
            listviewadapter.notifyDataSetChanged();
        }

        if (handler == null) {
            handler = new InternetHandler();
        } else {
            handler.removeCallbacksAndMessages(null);
        }
        handler.postDelayed(new InternetRunnable(), 4000);


    }

    class TabDetailPagerListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return news.size();

        }

        @Override
        public Object getItem(int position) {
            return news.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_tab_detail_pager, null);
                holder = new ViewHolder(convertView);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            TabDetailBean.DataBean.NewsBean newsBean = news.get(position);
            Glide.with(mContext).load(ContanUtils.BASE_URL + newsBean.getListimage()).into(holder.ivIcon);
            holder.tvTitle.setText(newsBean.getTitle());
            holder.tvTime.setText(newsBean.getPubdate());

/**
 *记录每一次请求的id，记录，判断如果记录过了就显示灰色如果没有记录就记录进去下次显示时判断
 * 是否是已经点击过
 */
            String readNewsIdArray = CacheUtils.getString(mContext, READ_NEWS_ID_ARRAY_KEY, "");
            if (readNewsIdArray.contains(newsBean.getId() + "")) {
                holder.tvTitle.setTextColor(Color.GRAY);
            } else {
                holder.tvTitle.setTextColor(Color.BLACK);
            }

            return convertView;
        }


    }
    class ViewHolder {
        @InjectView(R.id.iv_icon)
        ImageView ivIcon;
        @InjectView(R.id.tv_title)
        TextView tvTitle;
        @InjectView(R.id.tv_time)
        TextView tvTime;
        @InjectView(R.id.icon_news_comment_num)
        ImageView iconNewsCommentNum;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    class TabDetailPagerTopNewsAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return topnews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        /**
         * 初始化viewpager的视图
         *
         * @param container
         * @param position
         * @return
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(mContext);
            imageView.setBackgroundResource(R.drawable.home_scroll_default);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            container.addView(imageView);
            Glide.with(mContext).load(ContanUtils.BASE_URL + topnews.get(position).getTopimage()).into(imageView);
            imageView.setOnTouchListener(new TopNewsTouchListener());
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


    private class TopNewsOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
//            先设置标题
            tv_title.setText(topnews.get(position).getTitle());
//           把之前的设为默认
            ll_point_group.getChildAt(preSelect).setEnabled(false);
//            把当前的设为高亮
            ll_point_group.getChildAt(position).setEnabled(true);
//            重置当前值
            preSelect = position;

        }

        //    解决当滑动ViewPager以后页面不能自动滚动问题
        @Override
        public void onPageScrollStateChanged(int state) {

            if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                if (handler != null) {
                    handler.removeCallbacksAndMessages(null);
                }
            } else if (state == ViewPager.SCROLL_STATE_SETTLING) {
                if (handler != null) {
                    handler.removeCallbacksAndMessages(null);
                    handler.postDelayed(new InternetRunnable(), 4000);
                }

            } else if (state == ViewPager.SCROLL_STATE_IDLE) {
                if (handler != null) {
                    handler.removeCallbacksAndMessages(null);
                    handler.postDelayed(new InternetRunnable(), 4000);

                }

            }

        }
    }

    class TopNewsTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    handler.removeCallbacksAndMessages(null);

                    break;
                case MotionEvent.ACTION_UP:
                    handler.removeCallbacksAndMessages(null);
                    handler.postDelayed(new InternetRunnable(), 4000);

                    break;
                case MotionEvent.ACTION_CANCEL:// 解决事件丢失d
                    handler.postDelayed(new InternetRunnable(), 4000);

                    break;
            }
            return true;
        }
    }
}
