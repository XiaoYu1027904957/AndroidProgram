package com.yuxiaobai;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.yuxiaobai.Utils.CacheUtils;

import butterknife.ButterKnife;

public class WelcomeActivity extends AppCompatActivity {

    private RelativeLayout activity_welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.inject(this);

        activity_welcome = (RelativeLayout) findViewById(R.id.activity_welcome);
//      设置三个动画 ： 拉伸、渐变、旋转
        ScaleAnimation sa = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        sa.setFillAfter(true);
        sa.setDuration(2000);
        AlphaAnimation aa = new AlphaAnimation(0, 1);
        aa.setDuration(2000);
        aa.setFillAfter(true);
        RotateAnimation ra = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        ra.setDuration(2000);
        ra.setFillAfter(true);
        AnimationSet set = new AnimationSet(false);
        set.addAnimation(sa);
        set.addAnimation(aa);
        set.addAnimation(ra);
//         添加了三种动画到set中
        activity_welcome.startAnimation(set);
//         对动画进行监听
        aa.setAnimationListener(new Animation.AnimationListener() {


            //           开始时进行的操作
            @Override
            public void onAnimationStart(Animation animation) {

            }

            //            完成时进行的操作
            @Override
            public void onAnimationEnd(Animation animation) {
                //             得到保持，是否进入主界面
                boolean isStartMain = CacheUtils.getBoolean(WelcomeActivity.this, GuideActivity.START_MAIN);
                if (isStartMain) {
                    //进入主页面
                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(WelcomeActivity.this, GuideActivity.class);
                    startActivity(intent);
                }

                Toast.makeText(WelcomeActivity.this, "动画播放完成", Toast.LENGTH_SHORT).show();
                finish();
            }

            //            设置重复次数
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
