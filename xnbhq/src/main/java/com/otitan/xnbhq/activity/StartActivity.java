package com.otitan.xnbhq.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import com.otitan.xnbhq.MyApplication;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.dialog.LoginDialog;
import com.otitan.xnbhq.dialog.MobileInputDialog;
import com.otitan.xnbhq.entity.Bsuserbase;
import com.otitan.xnbhq.service.RetrofitHelper;
import com.otitan.xnbhq.util.BussUtil;
import com.otitan.xnbhq.util.ToastUtil;
import com.titan.baselibrary.permission.PermissionsActivity;
import com.titan.baselibrary.permission.PermissionsChecker;
import com.titan.baselibrary.util.ProgressDialogUtil;
import com.titan.gzzhjc.MainActivity;
import com.titan.versionupdata.VersionUpdata;

import java.io.File;

import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by li on 2016/5/26.
 * 启动开始页面，系统选择
 */
public class StartActivity extends Activity implements OnClickListener {

    Context mContext;
    public static boolean isLogin = false;
    public static Bsuserbase bsuserbase;

    private PermissionsChecker mPermissionsChecker; // 权限检测器
    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    private static final int PERMISSIONS_REQUEST_CODE = 0; // 请求码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);
        mContext = StartActivity.this;
        mPermissionsChecker = new PermissionsChecker(this);
		/*初始化数据*/
        initData();
		/*获取设备内存*/
        getAvailableMemorySize();
        /*版本更新*/
        checkVersion();
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.start_sdzy:
				/*湿地资源*/
                ProgressDialogUtil.startProgressDialog(mContext);
                intent = new Intent(mContext, SdzyActivity.class);
                intent.putExtra("name", "sdzy");
                startActivity(intent);
                break;
            case R.id.start_zfxc:
				/*巡查执法*/
                ProgressDialogUtil.startProgressDialog(mContext);
                intent = new Intent(mContext, XczfActivity.class);
                intent.putExtra("name", "xczf");
                startActivity(intent);
                break;
            case R.id.start_gsmm:
				/*古树名木*/
                ProgressDialogUtil.startProgressDialog(mContext);
                intent = new Intent(mContext, GsmmActivity.class);
                intent.putExtra("name", "gsmm");
                startActivity(intent);
                break;
            case R.id.start_yhsw:
				/*有害生物*/
//			if (!isLogin) {
//				//ArrayList<Map<String, String>> list = DataBaseHelper.getUserList(mContext, "db.sqlite");
//				if(app.onNetChange()){
//					loginDialog("longin_yhsw");
//				}else{
//					ToastUtil.setToast(mContext, "没有连接网络");
//				}
//			} else {
//				startprogressDialog();
//				intent = new Intent(mContext, YHSWActivity.class);
//				intent.putExtra("name", "yhsw");
//				startActivity(intent);
//			}

                ProgressDialogUtil.startProgressDialog(mContext);
                intent = new Intent(mContext, YHSWActivity.class);
                intent.putExtra("name", "yhsw");
                startActivity(intent);

                break;
            case R.id.start_swdyx:
				/*生物多样性*/
//                if (!isLogin) {
//                    if(MyApplication.getInstance().netWorkTip()){
//                        loginDialog("longin_swdyx");
//                    }
//                } else {
//                    ProgressDialogUtil.startProgressDialog(mContext);
//                    intent = new Intent(mContext, SwdyxActivity.class);
//                    intent.putExtra("name", "swdyx");
//                    startActivity(intent);
//                }

                ProgressDialogUtil.startProgressDialog(mContext);
                intent = new Intent(mContext, SwdyxActivity.class);
                intent.putExtra("name", "swdyx");
                startActivity(intent);

                break;
            case R.id.start_yzl:
				/*营造林*/
                ProgressDialogUtil.startProgressDialog(mContext);
                Intent intent_yzl = new Intent(mContext, YzlActivity.class);
                intent_yzl.putExtra("name", "yzl");
                startActivity(intent_yzl);
                break;
            case R.id.start_ed:
				/*二调 修改为综合决策*/
                //ProgressDialogUtil.startProgressDialog(mContext);
                intent = new Intent(mContext, ErDiaoActivity.class);
                intent.putExtra("name", "erdiao");
                startActivity(intent);
                break;
            case R.id.start_lxqc:
				/*连续清查 修改为木材经营*/
//                ProgressDialogUtil.startProgressDialog(mContext);
//                intent = new Intent(mContext, SlzylxqcActivity.class);
//                intent.putExtra("name", "lxqc");
//                startActivity(intent);

                intent = new Intent(mContext,McjyActivity.class);
                intent.putExtra("name", "mcjy");
                startActivity(intent);

//			intent = getPackageManager().getLaunchIntentForPackage("com.otitan.gyslfh");
//			if(intent != null){
//				startActivity(intent);
//			}else{
//				ToastUtil.setToast(mContext, "系统不存在,请先安装系统");
//			}
                break;
            case R.id.start_zhjc:
                /*链接综合决策系统*/

                intent = new Intent(mContext, MainActivity.class);
                intent.putExtra("name", "zhjc");
                startActivity(intent);
//                try {
//                    String path = ResourcesManager.otms + "/config.xml";
//                    path = MyApplication.resourcesManager.getFilePath(path);
//                    File file = new File(path);
//                    FileInputStream inputStream = new FileInputStream(file);
//                    PullParseXml parseXml = new PullParseXml();
//                    List<Row> lst = parseXml.PullParseXML(inputStream, "zhjcxt");
//                    String path1 = "";
//                    if (lst != null && lst.size() > 0) {
//                        path1 = lst.get(0).getName();
//                    } else {
//                        path1 = mContext.getResources().getString(R.string.zhjcurl);
//                    }
//
//                    Uri uri = Uri.parse(path1);
//                    Intent it = new Intent(Intent.ACTION_VIEW, uri);
//                    startActivity(it);
//                } catch (Throwable e) {
//                    e.printStackTrace();
//                }

                break;
            default:
                break;
        }
    }

    /**
     * 用户登录窗口
     */
    private void loginDialog(final String type) {
        LoginDialog loginDialog = new LoginDialog(mContext,R.style.Dialog,type);
        BussUtil.setDialogParams(mContext, loginDialog, 0.5, 0.6);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        if (MyApplication.getInstance().netWorkTip()) {
            boolean flag = MyApplication.sharedPreferences.getBoolean(MyApplication.macAddress, false);
            if (flag) {
                new MyAsyncTask().execute("selMobileSysInfo");
            }
        }

    }

    /**
     * 弹出录入设备使用者信息窗口
     */
    private void showMobileSysInfoView() {
        MobileInputDialog mobileInputDialog = new MobileInputDialog(mContext,R.style.Dialog);
        BussUtil.setDialogParams(mContext, mobileInputDialog, 0.5, 0.6);
    }

    /**
     * 异步事件
     */
    class MyAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(final String... params) {
           if (params[0].equals("selMobileSysInfo")) {

               selMobileSysInfo(MyApplication.macAddress, MyApplication.mobileXlh,MyApplication.mobileType);

            }
            return null;
        }

    }

    /**
     * 获取手机内部剩余存储空间
     */
    private double getAvailableMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        long sss = (availableBlocks * blockSize) / (1024 * 1024 * 1024);
        Log.d("=============", sss + "G");
        return sss;
    }

    /**发送数据到后台*/
    private void selMobileSysInfo(String sbh, String xlh, String type){
        Observable<String> observable = RetrofitHelper.getInstance(mContext).getServer().selMobileSysInfo(sbh, xlh, type);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.setToast(mContext,"网络错误");
                    }

                    @Override
                    public void onNext(String result) {
                        if (result.equals("false")) {
                            // 弹出设备使用者录入信息窗口
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    showMobileSysInfoView();
                                }
                            });
                        }
                    }
                });
    }
    /*版本更新检查*/
    private void checkVersion(){
        new UpdataThread().start();
    }

    /*检查版本更新*/
    private class UpdataThread extends Thread{

        @Override
        public void run() {
            super.run();
            if(MyApplication.getInstance().netWorkTip()){
                // 获取当前版本号 是否是最新版本
                String updateurl = mContext.getResources().getString(R.string.updateurl);
                boolean flag = new VersionUpdata(StartActivity.this).checkVersion(updateurl);
                if(flag){
                    //提示更新
                }else{
                    //已是最新版本
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(Build.VERSION.SDK_INT >= 23){
            // 缺少权限时, 进入权限配置页面
            boolean flag = mPermissionsChecker.lacksPermissions(PERMISSIONS);
            if (flag) {
                PermissionsActivity.startActivityForResult(this, PERMISSIONS_REQUEST_CODE, PERMISSIONS);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
