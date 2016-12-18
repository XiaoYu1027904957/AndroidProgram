package com.yuxiaobai.fragment;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.yuxiaobai.MainActivity;
import com.yuxiaobai.R;
import com.yuxiaobai.Utils.DensityUtil;
import com.yuxiaobai.base.BaseFragment;
import com.yuxiaobai.bean.NewsCenterBean;
import com.yuxiaobai.pager.NewsCenterPager;

import java.util.List;

/**
 * Created by yuxiaobai on 2016/12/12.
 */

public class LeftMenuFragment extends BaseFragment {
    private ListView listView;
    private List<NewsCenterBean.DataBean> leftData;
    private int top;
    private LeftMenuAdapter adapter;

    /**
     * 设置当前选中条目的位置
     *
     * @return
     */
    private int CurrentSelect;

    @Override
    public View initView() {
        top = DensityUtil.dip2px(mContext, 40);
//         实例化listView
        listView = new ListView(mContext);
//          设置listView在布局中的位置
        listView.setPadding(0, top, 0, 0);
//          设置没有分割线
        listView.setDividerHeight(0);
//           设置背景颜色
        listView.setBackgroundColor(Color.BLACK);


        return listView;
    }

    @Override
    public void initData() {
        super.initData();

    }

    public void setData(List<NewsCenterBean.DataBean> leftData) {
//          接受数据
        this.leftData = leftData;
        CurrentSelect = 0;
        switchPager(0);
        adapter = new LeftMenuAdapter();
        listView.setAdapter(adapter);
// 　    设置点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                1. 重新赋值，
                CurrentSelect = position;
//                设置adapter状态的更新
                adapter.notifyDataSetChanged();

//                2. 关闭左侧菜单
                MainActivity mainActivity = (MainActivity) mContext;
                mainActivity.getSlidingMenu().toggle();
//                 3. 切换到对应的详情界面
                switchPager(position);
            }
        });
//          调用默认的新闻界面
        switchPager(CurrentSelect);
    }

    /**
     * 根据位置切换到相应的界面
     * @param position
     */
    private void switchPager(int position) {
        MainActivity mainActivity = (MainActivity) mContext;
//         从MainActivity的到contentFragment 的实例
        ContentFragment contentFragment = mainActivity.getContentFragment();
//          再从集合中获取NewsCenterPager 的实例；
        NewsCenterPager newsCenterPager = contentFragment.getNewsCenterPager();
        newsCenterPager.switchPager(position);

    }

    class LeftMenuAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return leftData.size();
        }

        @Override
        public Object getItem(int position) {
            return leftData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = (TextView) View.inflate(mContext, R.layout.item_left_menu, null);
            textView.setText(leftData.get(position).getTitle());
            return textView;
        }
    }
}
