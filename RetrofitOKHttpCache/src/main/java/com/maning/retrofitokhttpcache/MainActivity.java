package com.maning.retrofitokhttpcache;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.maning.retrofitokhttpcache.bean.GankModel;
import com.maning.retrofitokhttpcache.http.BaseRetrofit;
import com.maning.retrofitokhttpcache.http.MNApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);

    }

    public void btn01(View view) {
        MNApiService mnApiService = BaseRetrofit.getMNApiService();
        Call<GankModel> android = mnApiService.getGankCommonData("Android", 20, 1);
        android.enqueue(new Callback<GankModel>() {
            @Override
            public void onResponse(Call<GankModel> call, Response<GankModel> response) {
                if (response.isSuccessful()) {
                    GankModel body = response.body();
                    if (body != null) {
                        textView.setText(body.toString());
                    } else {
                        textView.setText("空");
                    }
                } else {
                    textView.setText("失败");
                }
            }
            @Override
            public void onFailure(Call<GankModel> call, Throwable t) {
                textView.setText("onFailure:" + t.toString());
            }
        });

    }

}
