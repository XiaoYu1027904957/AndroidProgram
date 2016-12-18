package com.yuxiaobai;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class NewsDetailActivity extends Activity {


    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.ib_menu)
    ImageButton ibMenu;
    @InjectView(R.id.ib_back)
    ImageButton ibBack;
    @InjectView(R.id.icon_textsize)
    ImageButton iconTextsize;
    @InjectView(R.id.icon_share)
    ImageButton iconShare;
    @InjectView(R.id.ib_swich)
    ImageButton ibSwich;
    @InjectView(R.id.webview)
    WebView webview;
    @InjectView(R.id.progressbar)
    ProgressBar progressbar;
    @InjectView(R.id.activity_news_detail)
    LinearLayout activityNewsDetail;
    private String url;
    private WebSettings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_news_detail);
        ButterKnife.inject(this);
        url = getIntent().getStringExtra("url");
        initView();
    }

    private void initView() {
        tvTitle.setVisibility(View.GONE);
        ibBack.setVisibility(View.VISIBLE);
        iconTextsize.setVisibility(View.VISIBLE);
        iconShare.setVisibility(View.VISIBLE);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressbar.setVisibility(View.GONE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

        });

//        设置支持js
        settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
//        设置支持双双击变大变小
        settings.setUseWideViewPort(true);
//         设置网页支持
        settings.setBuiltInZoomControls(true);
        webview.loadUrl(url);
    }

    @OnClick({R.id.ib_back, R.id.icon_textsize, R.id.icon_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_back://回退
                Toast.makeText(NewsDetailActivity.this, "退回", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.icon_textsize://设置字体大小
                Toast.makeText(NewsDetailActivity.this, "设置字体大小", Toast.LENGTH_SHORT).show();
                showChangeTextSizeDialog();
                break;
            case R.id.icon_share:
                Toast.makeText(NewsDetailActivity.this, "分享", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private int tempSize = 2;
    private int realSize = tempSize;

    private void showChangeTextSizeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置文字大小");
        String[] items = {"超大字体", "大字体", "正常字体", "小字体", "超小字体"};
        builder.setSingleChoiceItems(items, realSize, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tempSize = which;
            }
        });
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                  点击确定执行这里
                realSize = tempSize;
                changeText(realSize);

            }
        });
        builder.show();

    }

    private void changeText(int realSize) {
        switch (realSize) {
            case 0:
                settings.setTextZoom(200);
                break;
            case 1:
                settings.setTextZoom(150);
                break;
            case 2:
                settings.setTextZoom(100);
                break;
            case 3:
                settings.setTextZoom(75);
                break;
            case 4:
                settings.setTextZoom(50);
                break;
        }

    }
}
