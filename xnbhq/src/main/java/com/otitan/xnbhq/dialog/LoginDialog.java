package com.otitan.xnbhq.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.otitan.xnbhq.MyApplication;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.activity.StartActivity;
import com.otitan.xnbhq.activity.SwdyxActivity;
import com.otitan.xnbhq.activity.YHSWActivity;
import com.otitan.xnbhq.db.DbHelperService;
import com.otitan.xnbhq.entity.User;
import com.otitan.xnbhq.service.Webservice;
import com.otitan.xnbhq.util.BussUtil;
import com.otitan.xnbhq.util.ToastUtil;
import com.titan.baselibrary.customview.DropdownEdittext;
import com.titan.baselibrary.listener.CancleListener;
import com.titan.baselibrary.util.ProgressDialogUtil;

import java.util.List;

/**
 * Created by li on 2016/5/31.
 * 登录dialog
 */

public class LoginDialog extends Dialog {

    private Context mContext;
    private String xtType;

    public LoginDialog(@NonNull Context context, @StyleRes int themeResId,String xtType) {
        super(context, themeResId);
        this.mContext = context;
        this.xtType = xtType;//登录后选择进入哪个系统
    }

    protected LoginDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_login);
        setCanceledOnTouchOutside(false);
        final DbHelperService<User> service = new DbHelperService<User>(mContext, User.class);
        final List<User> list = service.getObjectsByWhere(null);

        final DropdownEdittext name = (DropdownEdittext) findViewById(R.id.name_text);
        String[] nameArray = getStrArray(list);
        name.setAdapter(mContext, nameArray);
        name.tv.setText(MyApplication.sharedPreferences.getString("name", "admin"));
        final EditText pwd = (EditText) findViewById(R.id.password_text);
        pwd.setText("admin");
        Button sureBtn = (Button) findViewById(R.id.radio_btn_sure);

        sureBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (TextUtils.isEmpty(name.getText())) {
                    ToastUtil.setToast(mContext, "用户名不能为空");
                    return;
                }
                if (TextUtils.isEmpty(pwd.getText())) {
                    ToastUtil.setToast(mContext, "密码不能为空");
                    return;
                }

                ProgressDialogUtil.startProgressDialog(mContext);
                String names = name.getText() == null ? "" : name.getText().trim();
                String password = pwd.getText() == null ? "" : pwd.getText().toString();

                loginMethod(names, password, list);

                //检查用户是否已经记录过
                User user = new User(names, password);
                boolean ff = false;
                for (User u : list) {
                    if (u.getName().equals(names)) {
                        ff = true;
                        break;
                    }
                }
                if (!ff) {
                    boolean flag = service.add(user);
                    Log.d("LOG", flag + "");
                }
            }
        });

        ImageView view = (ImageView) findViewById(R.id.login_close);
        view.setOnClickListener(new CancleListener(this));

    }

    /**
     * 用户登录连接后台数据库
     */
    private void loginMethod(final String username,final String password, final List<User> list) {
        boolean checkResult = BussUtil.stringCheck(username);
        if (!checkResult) {
            ProgressDialogUtil.stopProgressDialog(mContext);
            ToastUtil.setToast(mContext, "用户名中含有特殊字符");
            return;
        }
        if (MyApplication.getInstance().netWorkTip()) {
			/* 用户在线验证 */
            String[] params = {"login", username, password};
            final Webservice webservice = new Webservice(mContext);
            final String result = webservice.CheckLogin(params[1], params[2]);
            showResultMsg(result, params[1], params[2]);

        } else {
            if (!username.equals(MyApplication.sharedPreferences.getString("name", ""))) {
                ToastUtil.setToast(mContext, "输入用户名与上次登录用户不同");
                return;
            }
			/* 用户离线验证 */
            if (password.equals(MyApplication.sharedPreferences.getString("psw", ""))) {

                String longResult = MyApplication.sharedPreferences.getString("loginresult", "");
                if (longResult == null)
                    return;
                StartActivity.bsuserbase = BussUtil.getUserInfo(longResult, StartActivity.bsuserbase);
                ProgressDialogUtil.stopProgressDialog(mContext);
                //activity跳转
                new MyAsyncTask().execute(xtType);
            } else {
                ProgressDialogUtil.stopProgressDialog(mContext);
                ToastUtil.setToast(mContext, "密码错误");
            }
        }
    }

    /**
     * 登录结果提示
     */
    private void showResultMsg(final String result, final String name, final String psw) {
        if (result.contains("@#")) {
            ToastUtil.setToast(mContext, "登录成功");
            this.dismiss();
            StartActivity.isLogin = true;
            new Thread(new Runnable() {

                @Override
                public void run() {
                    final String[] str = result.split("@#");
                    MyApplication.sharedPreferences.edit().putString("name", name).apply();
                    MyApplication.sharedPreferences.edit().putString("psw", psw).apply();
                    MyApplication.sharedPreferences.edit().putString("loginresult", str[1]).apply();
                    if (str[1] == null)
                        return;
                    StartActivity.bsuserbase = BussUtil.getUserInfo(str[1], StartActivity.bsuserbase);
                }
            }).start();
            new MyAsyncTask().execute(xtType);
        } else {
            ToastUtil.setToast(mContext, result);
        }
        ProgressDialogUtil.stopProgressDialog(mContext);
    }

    /**
     * 异步事件
     */
    class MyAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(final String... params) {

            if (params[0].equals("longin_yhsw")) {
                Intent intent = new Intent(mContext, YHSWActivity.class);
                intent.putExtra("name", "yhsw");
                mContext.startActivity(intent);
            } else if (params[0].equals("longin_swdyx")) {
                Intent intent = new Intent(mContext, SwdyxActivity.class);
                intent.putExtra("name", "swdyx");
                mContext.startActivity(intent);
            }
            return null;
        }

    }

    /**
     * 用户历史数据转换为String 数组
     */
    private String[] getStrArray(List<User> list) {
        String[] str = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            str[i] = list.get(i).getName();
        }
        return str;
    }
}
