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

import static com.yuxiaobai.Utils.ContanUtils.BASE_URL;

/**
 * Created by yuxiaobai on 2016/12/16.
 */

public class TopicTabDetailPager extends MenuDetailBasePager {
    private static final String READARRAYID = "read_array_id";

    private final NewsCenterBean.DataBean.ChildrenBean childrenBean;
    /**
     * 新闻列表的数据
     */
    private List<TabDetailBean.DataBean.NewsBean> news;

    private ListView listview;
    private TabDetailPagerListAdapter listAdapter;
    private HorizontalScrollViewPager viewpager;
    private TextView tv_title;
    private LinearLayout ll_point_group;
    private PullToRefreshListView pull_refresh_list;
    /**
     * 顶部新闻的数据
     */
    private List<TabDetailBean.DataBean.TopnewsBean> topnews;
    /**
     * 上一次被选中的
     */
    private int preSelect = 0;
    private String url;
    /**
     * 更多的页面的url
     */
    private String moreUrl;

    /**
     * 是否是加载更多
     */
    private boolean isLoadMore = false;

    public TopicTabDetailPager(Context context, NewsCenterBean.DataBean.ChildrenBean childrenBean) {
        super(context);
        //接收数据
        this.childrenBean = childrenBean;
    }

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.topic_tabdetail_pager, null);
        pull_refresh_list = (PullToRefreshListView) view.findViewById(R.id.pull_refresh_list);


        //添加声音
        SoundPullEventListener<ListView> soundListener = new SoundPullEventListener<ListView>(mContext);
        soundListener.addSoundEvent(PullToRefreshBase.State.PULL_TO_REFRESH, R.raw.pull_event);
        soundListener.addSoundEvent(PullToRefreshBase.State.RESET, R.raw.reset_sound);
        soundListener.addSoundEvent(PullToRefreshBase.State.REFRESHING, R.raw.refreshing_sound);
        pull_refresh_list.setOnPullEventListener(soundListener);

//        listview = (ListView) view.findViewById(R.id.listview);
        listview = pull_refresh_list.getRefreshableView();
        //

        listview.setDividerHeight(0);


        View header = View.inflate(mContext, R.layout.topnews_pager, null);
        viewpager = (HorizontalScrollViewPager ) header.findViewById(R.id.viewpager);
        tv_title = (TextView) header.findViewById(R.id.tv_title);
        ll_point_group = (LinearLayout) header.findViewById(R.id.ll_point_group);
        //以头的方式添加
        listview.addHeaderView(header);


        //监听下拉刷新
        pull_refresh_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                isLoadMore = false;
//                Toast.makeText(mContext, "下拉刷新", Toast.LENGTH_SHORT).show();
                getDataFromNet(url);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
//                Toast.makeText(mContext, "上拉刷新", Toast.LENGTH_SHORT).show();
                //联网请求
                if (!TextUtils.isEmpty(moreUrl)) {
                    isLoadMore = true;
                    getDataFromNet(moreUrl);
                } else {
                    //不去请求
                    //把刷新效果隐藏
                    pull_refresh_list.onRefreshComplete();
                    Toast.makeText(mContext, "没有更多数据了", Toast.LENGTH_SHORT).show();
                }

            }
        });


        //设置ListView的item的点击事件
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TabDetailBean.DataBean.NewsBean newsBean = news.get(position - 2);
                Log.e("TAG", newsBean.getTitle() + ",id==" + newsBean.getId());
                //获取保持的id
                String ReadArrayId = CacheUtils.getString(mContext, READARRAYID, "");//"",111,222,333
                if (!ReadArrayId.contains(newsBean.getId() + "")) {
                    //把点击的对应的id保持起来
                    CacheUtils.putString(mContext, READARRAYID, ReadArrayId + newsBean.getId() + ",");
                    //适配器刷新
                    listAdapter.notifyDataSetChanged();//getCount-->getView
                }

                //跳转到新闻浏览页面
                Intent intent = new Intent(mContext, NewsDetailActivity.class);
                intent.putExtra("url", BASE_URL + newsBean.getUrl());
                mContext.startActivity(intent);
            }
        });
        return view;
    }


    public void initData() {
        //把数据给显示出来
        //联网请求的连接
        url = ContanUtils.BASE_URL + childrenBean.getUrl();
        getDataFromNet(url);
    }

    private void getDataFromNet(String url) {
        RequestParams request = new RequestParams(url);
        x.http().get(request, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                Log.e("TAG","TabDetailPager请求成功=="+result);

                processData(result);
                //把刷新效果隐藏
                pull_refresh_list.onRefreshComplete();

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("TAG", "请求失败==" + ex.getMessage());
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
        //解析数据
        TabDetailBean tabDetailBean = new Gson().fromJson(result, TabDetailBean.class);

        String more = tabDetailBean.getData().getMore();
        if (!TextUtils.isEmpty(more)) {
            //有路径-有下一页
            moreUrl = ContanUtils.BASE_URL + more;
        } else {
            //没有下一页
            moreUrl = "";
        }


        if (!isLoadMore) {
            //默认代码

            //设置顶部新闻的适配器--Viewpager
            topnews = tabDetailBean.getData().getTopnews();
            viewpager.setAdapter(new TabDetailPagerTopNewsAdapter());
            //监听ViewPager的变化
            viewpager.addOnPageChangeListener(new TopNewsOnPageChangeListener());
            //设置默认的
            tv_title.setText(topnews.get(preSelect).getTitle());

            //把所有的移除
            ll_point_group.removeAllViews();
            //添加指示点
            for (int i = 0; i < topnews.size(); i++) {

                ImageView imageView = new ImageView(mContext);
                imageView.setBackgroundResource(R.drawable.topnews_point_selector);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, ViewGroup.LayoutParams.WRAP_CONTENT);
                if (i == 0) {
                    imageView.setEnabled(true);
                } else {
                    imageView.setEnabled(false);
                    params.leftMargin = DensityUtil.dip2px(mContext, 10);
                }

                imageView.setLayoutParams(params);

                //添加
                ll_point_group.addView(imageView);
            }


            //新闻列表--------------------

            news = tabDetailBean.getData().getNews();

            //设置适配器--ListView的适配器
            listAdapter = new TabDetailPagerListAdapter();
            listview.setAdapter(listAdapter);

        } else {
            //在原来的集合上添加新数据
            news.addAll(tabDetailBean.getData().getNews());
//            news = tabDetailBean.getData().getNews();//覆盖
            //刷新适配器
            listAdapter.notifyDataSetChanged();

        }


        //添加Handler
        if (handler == null) {
            handler = new InternalHandler();
        }
        //把所有消息移除
        handler.removeCallbacksAndMessages(null);

        //两秒后执行
        handler.postDelayed(new MyRunnable(), 2000);

    }

    private InternalHandler handler;


    class InternalHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            //ViewPager切换到下个页面
            int item = (viewpager.getCurrentItem() + 1) % topnews.size();
            viewpager.setCurrentItem(item);
            handler.postDelayed(new MyRunnable(), 4000);
        }
    }

    class MyRunnable implements Runnable {

        @Override
        public void run() {

            handler.sendEmptyMessage(0);
        }
    }


    class TopNewsOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //1.把之前的设置默认
            ll_point_group.getChildAt(preSelect).setEnabled(false);
            //2.把当前的设置高亮
            ll_point_group.getChildAt(position).setEnabled(true);
            preSelect = position;
        }

        @Override
        public void onPageSelected(int position) {
            tv_title.setText(topnews.get(position).getTitle());


        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                handler.removeCallbacksAndMessages(null);
            } else if (state == ViewPager.SCROLL_STATE_IDLE) {
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(new MyRunnable(), 4000);
            } else if (state == ViewPager.SCROLL_STATE_SETTLING) {
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(new MyRunnable(), 4000);
            }

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

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            ImageView imageView = new ImageView(mContext);
            imageView.setBackgroundResource(R.drawable.home_scroll_default);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            container.addView(imageView);

            //联网请求
            Glide.with(mContext).load(ContanUtils.BASE_URL + topnews.get(position).getTopimage()).into(imageView);

            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            handler.removeCallbacksAndMessages(null);
                            break;
                        case MotionEvent.ACTION_UP:
                            handler.removeCallbacksAndMessages(null);
                            handler.postDelayed(new MyRunnable(), 4000);
                            break;
                    }
                    return true;
                }
            });

            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
            container.removeView((View) object);
        }
    }


    class TabDetailPagerListAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return news.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_tab_detail_pager, null);
                viewHolder = new ViewHolder(convertView);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            //根据位置获取对应的数据
            TabDetailBean.DataBean.NewsBean newsBean = news.get(position);

            Glide.with(mContext).load(ContanUtils.BASE_URL + newsBean.getListimage()).into(viewHolder.ivIcon);
            viewHolder.tvTitle.setText(newsBean.getTitle());
            viewHolder.tvTime.setText(newsBean.getPubdate());

            String readArrayId = CacheUtils.getString(mContext, READARRAYID, "");
            if (readArrayId.contains(newsBean.getId() + "")) {
                //点击过-灰色
                viewHolder.tvTitle.setTextColor(Color.GRAY);
            } else {
                //默认-黑色
                viewHolder.tvTitle.setTextColor(Color.BLACK);
            }

            return convertView;
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


    }

}
