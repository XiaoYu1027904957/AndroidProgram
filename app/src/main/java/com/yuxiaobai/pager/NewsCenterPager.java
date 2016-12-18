package com.yuxiaobai.pager;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.yuxiaobai.MainActivity;
import com.yuxiaobai.Utils.CacheUtils;
import com.yuxiaobai.Utils.ContanUtils;
import com.yuxiaobai.base.BasePager;
import com.yuxiaobai.base.MenuDetailBasePager;
import com.yuxiaobai.bean.NewsCenterBean;
import com.yuxiaobai.detail.InteractMenuDetailPager;
import com.yuxiaobai.detail.NewsMenuDetailPager;
import com.yuxiaobai.detail.PhotoMenuDetailPager;
import com.yuxiaobai.detail.TopicMenuDetailPager;
import com.yuxiaobai.fragment.LeftMenuFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by yuxiaobai on 2016/12/12.
 */

public class NewsCenterPager extends BasePager {

    private List<MenuDetailBasePager> pagers;
    /**
     * 左侧菜单对应的数据
     */
    private List<NewsCenterBean.DataBean> leftMenudata;

    public NewsCenterPager(Context context) {
        super(context);

    }

    //     联网请求的具体方法
    private void getDataFromNet() {
        RequestParams request = new RequestParams(ContanUtils.NEWSCENTER_PAGER_URL);
        //用xutils进行联网请求
        x.http().get(request, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("TAG", "NewsCenterPager - SUCCESS");
//                 进行数据的缓存- 下次就可以直接先从sp中读取，而不进行联网那个操作，当有数据进行更新时
//                才进行联网更新
                CacheUtils.putString(mContext, ContanUtils.NEWSCENTER_PAGER_URL, result);
//                解析Json数据
                procressData(result);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("TAG", "error" + ex.getMessage());

            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.e("TAG", "onCancelled");

            }
            @Override
            public void onFinished() {
                Log.e("TAG", "onFinished");

            }
        });

    }

    private void procressData(String json) {
//         解析json数据,并绑定到数据上
        NewsCenterBean newsCenterBean = paraseJson2(json);
        //把数据传到左测菜单
        MainActivity mainActivity = (MainActivity) mContext;
        leftMenudata = newsCenterBean.getData();
//          实例化页面,将各个界面添加到集合中。
        pagers = new ArrayList<>();
        pagers.add(new NewsMenuDetailPager(mContext, leftMenudata.get(0)));
        pagers.add(new TopicMenuDetailPager(mContext, leftMenudata.get(0)));
        pagers.add(new PhotoMenuDetailPager(mContext, leftMenudata.get(2)));
        pagers.add(new InteractMenuDetailPager(mContext));
//        通过MainActiivty的实例找到其子视图leftfragment
        LeftMenuFragment leftMenuFragment = mainActivity.getLeftMenuFragment();
//         把数据传输到fragment中
        leftMenuFragment.setData(leftMenudata);
    }

    /**
     * 使用Gson解析json数据
     *
     * @param json
     * @return
     */
//    private NewsCenterBean paraJson(String json) {
////          将json数据解析到相应得类中
//        return new Gson().fromJson(json, NewsCenterBean.class);
//    }

    /**
     * 根据位置进行切换
     *
     * @param position
     */
    public void switchPager(int position) {
//         修改标题
        tvTitle.setText(leftMenudata.get(position).getTitle());
//          切换页面去除重复
        final MenuDetailBasePager pager = pagers.get(position);//得到某个累的实例
//         初始化数据
        pager.iniData();
//    得到视图
        View rootView = pager.rootView;
        flContent.removeAllViews();
        flContent.addView(rootView);

        if (position == 2) {
            ibSwich.setVisibility(View.VISIBLE);
            ibSwich.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PhotoMenuDetailPager photoMenuDetailPager = (PhotoMenuDetailPager) pagers.get(2);
                    photoMenuDetailPager.switchListGrid(ibSwich);

                }
            });

        }
    }

    @Override
    public void initData() {
        super.initData();
        ibMenu.setVisibility(View.VISIBLE);
//        设置标题
        tvTitle.setText("新闻");
        TextView textView = new TextView(mContext);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(25);
        textView.setTextColor(Color.RED);
        textView.setText("新闻内容");
//         添加到帧布局


        flContent.addView(textView);
        getDataFromNet();
        String json = CacheUtils.getString(mContext, ContanUtils.NEWSCENTER_PAGER_URL, null);
        if (!TextUtils.isEmpty(json)) {
            procressData(json);
        }
        //获取网络数据

    }


    /**
     * 手动 解析Json数据
     */
    private NewsCenterBean paraseJson2(String json) {
//        获取NewsCenterBean的对象
        NewsCenterBean newsCenterBean = new NewsCenterBean();
        try {
            JSONObject jsonObject = new JSONObject(json);
//             从这个字段中获取int型数值
            int retcode = jsonObject.optInt("retcode");
//             ?
            newsCenterBean.setRetcode(retcode);
//             从data中获取一个json数组，
            JSONArray data = jsonObject.optJSONArray("data");
//             定义一个集合,只装载NewsCenterBean.DataBean下的数据
            List<NewsCenterBean.DataBean> dataBeans = new ArrayList<>();
            newsCenterBean.setData(dataBeans);
//              循环获取JsonArray中的对象的属性的值
            for (int i = 0; i < data.length(); i++) {
//                  获取对应位置的jso对象
                JSONObject dataItem = (JSONObject) data.get(i);
//                同时创建NewsCenterBean.DataBean 的对象
                NewsCenterBean.DataBean dataBean = new NewsCenterBean.DataBean();
//                把databaen壮哉到对应的集合中
                dataBeans.add(dataBean);
//                  分别获取对应位置的属性值,并且把值databean对象中
                int id = dataItem.optInt("id");
                dataBean.setId(id);
                int type = dataItem.optInt("type");
                dataBean.setType(type);
                String title = dataItem.optString("title");
                dataBean.setTitle(title);
                String url = dataItem.optString("url");
                dataBean.setUrl(url);
                String url1 = dataItem.optString("url1");
                dataBean.setUrl1(url1);
                String excurl = dataItem.optString("excurl");
                dataBean.setExcurl(excurl);
                String dayurl = dataItem.optString("dayurl");
                dataBean.setDayurl(dayurl);
                String weekurl = dataItem.optString("weekurl");
                dataBean.setWeekurl(weekurl);

//                 然后单独解析children对应的json数组
                JSONArray childrenArray = dataItem.optJSONArray("children");
//                  创建盛装children的集合
                List<NewsCenterBean.DataBean.ChildrenBean> Children = new ArrayList<>();
//                添加到集合中
                dataBean.setChildren(Children);
                if (childrenArray != null && childrenArray.length() > 0) {

                    for (int j = 0; j < childrenArray.length(); j++) {
                        JSONObject childrenItem = (JSONObject) childrenArray.get(j);
                        NewsCenterBean.DataBean.ChildrenBean childrenBean = new NewsCenterBean.DataBean.ChildrenBean();
//                    添加到集合中
                        Children.add(childrenBean);
                        int idc = childrenItem.optInt("id");
                        childrenBean.setId(idc);
                        int typec = childrenItem.optInt("type");
                        childrenBean.setType(typec);
                        String titlec = childrenItem.optString("title");
                        childrenBean.setTitle(titlec);
                        String urlc = childrenItem.optString("url");
                        childrenBean.setUrl(urlc);


                    }

                }


            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return newsCenterBean;

    }


}
