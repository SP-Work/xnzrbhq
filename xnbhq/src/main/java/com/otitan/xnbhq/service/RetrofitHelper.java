package com.otitan.xnbhq.service;

import android.content.Context;

import com.otitan.xnbhq.R;
import com.otitan.xnbhq.util.ToastUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Created by li on 2017/5/5.
 *
 * 初始化 Retrofit
 */

public class RetrofitHelper {

    private Context mContext;
    public static NetworkMonitor networkMonitor;
    static RetrofitHelper instance = null;
    private Retrofit mRetrofit = null;

    public static RetrofitHelper getInstance(Context context){
        if(instance == null){
            instance = new RetrofitHelper(context);
            networkMonitor=new LiveNetworkMonitor(context);
        }
        return instance;
    }

    private RetrofitHelper(Context ctx){
        this.mContext = ctx;
        init();
    }

    private void init(){
        resetApp();
    }

    private void resetApp() {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

        // 看这里 ！！！ 我们添加了一个网络监听拦截器
        /*okHttpClientBuilder.addInterceptor(chain -> {
            boolean connected = networkMonitor.isConnected();
            if (networkMonitor.isConnected()) {
                return chain.proceed(chain.request());
            } else {
                throw new NoNetworkException();
            }
        });*/
        okHttpClientBuilder.addNetworkInterceptor(new MyNetworkInterceptor());
        okHttpClientBuilder.connectTimeout(5, TimeUnit.SECONDS);

        mRetrofit = new Retrofit.Builder()
                .baseUrl(mContext.getResources().getString(R.string.serverhost))
                .client(okHttpClientBuilder.build())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                //.addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public RetrofitService getServer(){
        return mRetrofit.create(RetrofitService.class);
    }

    private class MyNetworkInterceptor implements Interceptor
    {
        @Override
        public Response intercept(Chain chain) throws IOException {
            if (networkMonitor.isConnected()) {
                return chain.proceed(chain.request());
            } else {
                ToastUtil.setToast(mContext,"无网络连接，请检查网络");
            }
            return null;
        }
    }
}
