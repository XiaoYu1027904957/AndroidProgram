package com.yuxiaobai.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.yuxiaobai.MainActivity;
import com.yuxiaobai.PhotoShow;
import com.yuxiaobai.R;
import com.yuxiaobai.Utils.BitmapCacheUtils;
import com.yuxiaobai.Utils.ContanUtils;
import com.yuxiaobai.Utils.NetCacheUtils;
import com.yuxiaobai.bean.PhotosMenuBean;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by yuxiaobai on 2016/12/17.
 */

public class photoMenuDetailPagerAdapter extends RecyclerView.Adapter<photoMenuDetailPagerAdapter.MyViewHolder> {
    private final Context mContext;
    private final List<PhotosMenuBean.DataBean.NewsBean> news;
    private final RecyclerView recyclerView;

    private BitmapCacheUtils bitmapCacheUtils;
    private DisplayImageOptions options;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case NetCacheUtils.SUCCESS:
//                     联网成功得到的数据
                    int position = msg.arg1;
                    Bitmap bitmap = (Bitmap) msg.obj;
                    if (recyclerView.isShown()) {
                        ImageView imageView = (ImageView) recyclerView.findViewWithTag(position);
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

    public photoMenuDetailPagerAdapter(Context mContext, List<PhotosMenuBean.DataBean.NewsBean> news, RecyclerView recyclerView) {
        this.mContext = mContext;
        this.news = news;
        this.recyclerView = recyclerView;
        bitmapCacheUtils = new BitmapCacheUtils(handler);
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
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_photo_pager_listview, null);
        return new MyViewHolder(view);
    }

    /**
     * 绑定数据
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //        得到对应位置的数据的对象
        PhotosMenuBean.DataBean.NewsBean newsBean = news.get(position);
        String url = ContanUtils.BASE_URL + newsBean.getListimage();
//        使用ImageLoader加载图片
        ImageLoader.getInstance().displayImage(url, holder.ivPager, options);
        holder.tvTitle.setText(newsBean.getTitle());
    }


    @Override
    public int getItemCount() {
        return news.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.iv_pager)
        ImageView ivPager;
        @InjectView(R.id.tv_title)
        TextView tvTitle;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this,itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    transition(v, getLayoutPosition());
                }
            });

        }
    }

    private void transition(View v, int position) {
        PhotosMenuBean.DataBean.NewsBean newsBean = news.get(position);
        if (Build.VERSION.SDK_INT < 21) {
            Toast.makeText(mContext, "21+ only, keep out", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(mContext, PhotoShow.class);
//               傳入url地址
            intent.putExtra("url", ContanUtils.BASE_URL + newsBean.getListimage());
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation((MainActivity) mContext, v, "test");
            mContext.startActivity(intent, options.toBundle());
        }
    }
}
