package com.otitan.xnbhq.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.otitan.xnbhq.R;
import com.otitan.xnbhq.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 登录页
 */
public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.et_username)
    EditText mEt_username;
    @BindView(R.id.et_password)
    EditText mEt_passwoed;
    @BindView(R.id.tv_login)
    TextView mTv_login;

    public static final String PREFS_NAME = "MyPrefsFile";
    public static SharedPreferences titansp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        titansp= this.getSharedPreferences(PREFS_NAME, 0);

        initView();
    }

    private void initView() {
        mTv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = mEt_username.getText().toString().trim();
                String pass = mEt_passwoed.getText().toString().trim();
                if (!user.equals("") && !pass.equals("")) {
                    startActivity(new Intent(LoginActivity.this, YzlActivity.class));
                    titansp.edit().putString("isLogin", "1").apply();

                    // 保存上次登陆信息
                    titansp.edit().putString("lastusername", user).apply(); // 账号
                    titansp.edit().putString("lastpassword", pass).apply(); // 密码

                    finish();
                } else {
                    ToastUtil.setToast(LoginActivity.this, "请输入用户名密码");
                }
            }
        });
    }
}
