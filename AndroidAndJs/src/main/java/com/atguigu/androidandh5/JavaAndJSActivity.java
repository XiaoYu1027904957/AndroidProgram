package com.atguigu.androidandh5;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 作者：尚硅谷-杨光福 on 2016/7/28 11:19
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：java和js互调
 */
public class JavaAndJSActivity extends Activity implements View.OnClickListener {
    private EditText etNumber;
    private EditText etPassword;
    private Button btnLogin;
    /**
     * 加载网页或者说H5页面
     */
    private WebView webview;
    private WebSettings webSettings;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2016-07-28 11:43:37 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        setContentView(R.layout.activity_java_and_js);
        etNumber = (EditText) findViewById(R.id.et_number);
        etPassword = (EditText) findViewById(R.id.et_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);

    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2016-07-28 11:43:37 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if (v == btnLogin) {
            // Handle clicks for btnLogin
            login();
        }
    }

    private void login() {
        String numebr = etNumber.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        if (TextUtils.isEmpty(numebr) || TextUtils.isEmpty(password)) {
            Toast.makeText(JavaAndJSActivity.this, "账号或者密码为空", Toast.LENGTH_SHORT).show();
        } else {
            //登录
            login(numebr);
        }
    }

    /**
     * 传递账号
     * @param numebr
     */
    private void login(String numebr) {

        //把Java数据传递给javascript
        webview.loadUrl("javascript:javaCallJs('"+numebr+"')");

        setContentView(webview);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViews();
        initWebView();
    }

    private void initWebView() {
        webview = new WebView(this);
        webSettings = webview.getSettings();
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
        webview.addJavascriptInterface(new MyJavascriptInterface(), "Android");

        webview.loadUrl("file:///android_asset/JavaAndJavaScriptCall.html");

//        webview.loadUrl("http://192.168.1.165:8080/assets/JavaAndJavaScriptCall.html");
//        setContentView(webview);
    }

    class MyJavascriptInterface{

        @JavascriptInterface
        public void showToast(){
            Toast.makeText(JavaAndJSActivity.this, "我被js调用了", Toast.LENGTH_SHORT).show();
        }

    }


}
