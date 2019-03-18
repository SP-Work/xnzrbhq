package com.otitan.xnbhq.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.otitan.xnbhq.MyApplication;
import com.otitan.xnbhq.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 闪屏页
 */
public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.iv_splash)
    ImageView mIv_Splash;

    public static final String PREFS_NAME = "MyPrefsFile";
    SharedPreferences titansp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);

        titansp = this.getSharedPreferences(PREFS_NAME, 0);

        initView();
    }

    private void initView() {
        // 使用Handler的postDelayed方法，3秒后执行跳转到MainActivity
        new Handler().postDelayed(new Runnable() {
            public void run() {
                goHome();
            }
        }, 2000);
    }

    /**
     * 页面跳转判断（第一次使用，跳到登录页面；不是第一次，直接跳到主页面）
     */
    private void goHome() {
        //判断是否为第一次使用
        if (titansp.getString("isLogin", "").equals("1")) {
            startActivity(new Intent(this, YzlActivity.class));
            finish();
        } else {//第一次使用，需登录
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }
}
