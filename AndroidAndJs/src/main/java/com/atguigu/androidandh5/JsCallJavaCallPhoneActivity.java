package com.atguigu.androidandh5;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * 作者：尚硅谷-杨光福 on 2016/7/28 11:19
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：java和js互调
 */
public class JsCallJavaCallPhoneActivity extends Activity {


    private WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_js_call_java_video);

        webview = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);//设置支持javaScript
        //设置
        webSettings.setBuiltInZoomControls(true);//设置缩放按钮
        webSettings.setUseWideViewPort(true);//设置支持双击页面变大变小，页面要支持

        //这个监听有一个作业，点击页面的连接不会打开到系统的浏览器打开页面
        webview.setWebViewClient(new WebViewClient() {
            //当页面加载完成的时候回调
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });

        //添加Javascript接口
        //java调用js，该接口不是必须的，但是js调用java，该接口必须
        webview.addJavascriptInterface(new MyJavascriptInterface(), "Android");

        webview.loadUrl("file:///android_asset/JsCallJavaCallPhone.html");
//        webview.loadUrl("http://192.168.1.165:8080/assets/JsCallJavaCallPhone.html");

    }

    public class MyJavascriptInterface {

        @JavascriptInterface
        public void showcontacts() {

            String json = "[{\"name\":\"尚硅谷\", \"phone\":\"18600012345\"}]";
            // 调用JS中的方法
            webview.loadUrl("javascript:show('" + json + "')");
        }

        @JavascriptInterface
        public void call(String number) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
            if (ActivityCompat.checkSelfPermission(JsCallJavaCallPhoneActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            startActivity(intent);
        }
    }

}
