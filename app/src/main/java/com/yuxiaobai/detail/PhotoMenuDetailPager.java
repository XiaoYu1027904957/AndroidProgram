package com.yuxiaobai.detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.yuxiaobai.PhotoShow;
import com.yuxiaobai.R;
import com.yuxiaobai.Utils.BitmapCacheUtils;
import com.yuxiaobai.Utils.ContanUtils;
import com.yuxiaobai.Utils.NetCacheUtils;
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
    @InjectView(R.id.listview)
    ListView listview;
    @InjectView(R.id.gridview)
    GridView gridview;
    private final NewsCenterBean.DataBean dataBean;
    private String url;
    private List<PhotosMenuBean.DataBean.NewsBean> news;
    private PhotosMenuDetailPagerAdapter adapter;
    private PhotosMenuBean.DataBean.NewsBean newsBean;
    private String urla;
    private BitmapCacheUtils bitmapCacheUtils;
    private DisplayImageOptions options;

    private int position;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case NetCacheUtils.SUCCESS:
//             联网成功以后得到的数据
                    position = msg.arg1;
                    Bitmap bitmap = (Bitmap) msg.obj;
                    if (listview.isShown()) {
                        ImageView imageView = (ImageView) listview.findViewWithTag(position);
                        if (imageView != null && bitmap != null) {
                            imageView.setImageBitmap(bitmap);

                        }

                    }
                    if (gridview.isShown()) {
                        ImageView imageView = (ImageView) listview.findViewWithTag(position);
                        if (imageView != null && bitmap != null) {
                            imageView.setImageBitmap(bitmap);

                        }

                    }

                    Log.d("TAG", "图片成功==" + position);
                    break;
                case NetCacheUtils.FAIL:
                    position = msg.arg1;
                    Log.d("TAG", "图片失败=" + position);

                    break;
            }
        }
    };


    public PhotoMenuDetailPager(Context mContext, NewsCenterBean.DataBean dataBean) {
        super(mContext);
        this.dataBean = dataBean;
    }

    @Override
    protected View initView() {
        View view = View.inflate(mContext, R.layout.photos_menu_detail_pager, null);
        ButterKnife.inject(this, view);
        return view;
    }

    /**
     * 装载数据
     */
    @Override
    public void iniData() {
        super.iniData();
//         获取统一资源定位器
        bitmapCacheUtils = new BitmapCacheUtils(handler);
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
            adapter = new PhotosMenuDetailPagerAdapter();
            listview.setAdapter(adapter);
//             点击listview的某一项进行全屏显示！
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                      传递某一位置的对象
                    newsBean = news.get(position);
//                     获取url
                    urla = ContanUtils.BASE_URL + newsBean.getListimage();
                    Intent intent = new Intent(mContext, PhotoShow.class);
//                     使用intent进行数据的携带！
                    intent.putExtra("url", urla);
                    mContext.startActivity(intent);
                }
            });
//             点击gridview的某一项item来进行全屏显示！
            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    newsBean = news.get(position);
                    urla = ContanUtils.BASE_URL + newsBean.getListimage();
                    Intent intent = new Intent(mContext, PhotoShow.class);
                    intent.putExtra("url", urla);
                    mContext.startActivity(intent);
                }
            });

        } else {
            Toast.makeText(mContext, "没有请求到数据", Toast.LENGTH_SHORT).show();
        }


    }

    /**
     * true:显示ListView
     * false:显示GridView
     */
    private boolean isShowListView = true;

    /**
     * 选择需要显示哪张图片，·
     * @param ibSwich
     */
    public void switchListGrid(ImageButton ibSwich) {

        if (isShowListView) {
//            显示GridView
            gridview.setAdapter(new PhotosMenuDetailPagerAdapter());
            listview.setVisibility(View.GONE);
            gridview.setVisibility(View.VISIBLE);
            ibSwich.setImageResource(R.drawable.icon_pic_list_type);
            isShowListView = false;
        } else {
//             显示listView
            listview.setAdapter(new PhotosMenuDetailPagerAdapter());
            gridview.setVisibility(View.GONE);
            listview.setVisibility(View.VISIBLE);
            ibSwich.setImageResource(R.drawable.icon_pic_grid_type);
            isShowListView = true;
        }

    }



    private class PhotosMenuDetailPagerAdapter extends BaseAdapter {
        public PhotosMenuDetailPagerAdapter() {
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.home_scroll_default)
                    .showImageForEmptyUri(R.drawable.home_scroll_default)
                    .showImageOnFail(R.drawable.home_scroll_default)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    //设置图片的解码类型
                    .considerExifParams(true)
                    //设置图片圆形
//                .displayer(new CircleBitmapDisplayer(Color.WHITE, 5))
                    //设置图片圆角
                    .displayer(new RoundedBitmapDisplayer(10))
                    .build();

        }

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
                convertView = View.inflate(mContext, R.layout.item_photo_pager_listview, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
//         根据位置得到相应的数据
            newsBean = news.get(position);
//             使用Glide进行图片缓存
//          Glide.with(mContext).load(ContanUtils.BASE_URL + newsBean.getListimage()).into(holder.ivPager);
//            使用手写的图片三级缓存几次那个数据的加载;
            String imageurl = ContanUtils.BASE_URL + newsBean.getListimage();
//            图片加载工具类
            Bitmap bitmap = bitmapCacheUtils.getBitmapFromNet(imageurl, position);
            holder.tvTitle.setText(newsBean.getTitle());
            if (bitmap != null) {
                holder.ivPager.setImageBitmap(bitmap);

            }


//            用imageLoager请求图片
            ImageLoader.getInstance().displayImage(imageurl,holder.ivPager,options);

            return convertView;
        }

    }

    static class ViewHolder {
        @InjectView(R.id.iv_pager)
        ImageView ivPager;
        @InjectView(R.id.tv_title)
        TextView tvTitle;
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

}


