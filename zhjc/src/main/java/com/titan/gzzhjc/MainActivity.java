package com.titan.gzzhjc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.titan.gzzhjc.activity.GhryActivity;
import com.titan.gzzhjc.activity.GylActivity;
import com.titan.gzzhjc.activity.GzlqActivity;
import com.titan.gzzhjc.activity.LmcfActivity;
import com.titan.gzzhjc.activity.LqggActivity;
import com.titan.gzzhjc.activity.LycyActivity;
import com.titan.gzzhjc.activity.LyjcActivity;
import com.titan.gzzhjc.activity.ShmhActivity;
import com.titan.gzzhjc.activity.SlfhActivity;
import com.titan.gzzhjc.activity.SthlyActivity;
import com.titan.gzzhjc.activity.TghlActivity;
import com.titan.gzzhjc.activity.YhswActivity;
import com.titan.gzzhjc.activity.YzlActivity;
import com.titan.gzzhjc.activity.ZmscActivity;
import com.titan.gzzhjc.activity.ZrbhqActivity;
import com.titan.gzzhjc.adapter.RecycleAdapter;
import com.titan.gzzhjc.entity.DividerGridItemDecoration;
import com.titan.gzzhjc.entity.LocalImageHolderView;

import java.util.ArrayList;
import java.util.List;

/**
 * 主界面
 */
public class MainActivity extends AppCompatActivity implements RecycleAdapter.OnItemRecycleListener{

    Context context;
    private ConvenientBanner convenientBanner;//顶部轮播
    List<Integer> topImgs = new ArrayList<>();
    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_zhjc);
        context = MainActivity.this;

        initConvenientBanner();
        initRecyclerView();

    }

    /**
     * 界面顶部轮播图
     */
    public void initConvenientBanner(){
        topImgs.add(R.drawable.banner_one);
        topImgs.add(R.drawable.banner_two);
        topImgs.add(R.drawable.banner_three);
        topImgs.add(R.drawable.banner_four);

        convenientBanner = (ConvenientBanner) findViewById(R.id.convenientBanner);
        convenientBanner.setPages(
                new CBViewHolderCreator<LocalImageHolderView>() {
                    @Override
                    public LocalImageHolderView createHolder() {
                        return new LocalImageHolderView();
                    }
                }, topImgs)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused});
    }

    public void initRecyclerView(){
        List<String> txts = new ArrayList<>();
        List<Integer> list = new ArrayList<>();
        txts.add(getResources().getString(R.string.gzlq));
        list.add(R.drawable.pic_index_gzlq);
        txts.add(getResources().getString(R.string.lyjc));
        list.add(R.drawable.pic_index_lyjc);
        txts.add(getResources().getString(R.string.lmcf));
        list.add(R.drawable.pic_index_lmcf);
        txts.add(getResources().getString(R.string.shmh));
        list.add(R.drawable.pic_index_shmh);
        txts.add(getResources().getString(R.string.tghl));
        list.add(R.drawable.pic_index_tghl);
        txts.add(getResources().getString(R.string.gyl));
        list.add(R.drawable.pic_index_gyl);
        txts.add(getResources().getString(R.string.slfh));
        list.add(R.drawable.pic_index_slfh);
        txts.add(getResources().getString(R.string.yzl));
        list.add(R.drawable.pic_index_yzl);
        txts.add(getResources().getString(R.string.sthly));
        list.add(R.drawable.pic_index_sthly);
        txts.add(getResources().getString(R.string.zmsc));
        list.add(R.drawable.pic_index_zmsc);
        txts.add(getResources().getString(R.string.yhsw));
        list.add(R.drawable.pic_index_yhsw);
        txts.add(getResources().getString(R.string.ghry));
        list.add(R.drawable.pic_index_ghry);
        txts.add(getResources().getString(R.string.lycy));
        list.add(R.drawable.pic_index_lycy);
        txts.add(getResources().getString(R.string.lqgg));
        list.add(R.drawable.pic_index_lqgg);
        txts.add(getResources().getString(R.string.zrbhq));
        list.add(R.drawable.pic_index_zrbhq);
        txts.add(getResources().getString(R.string.emporty));
        list.add(R.drawable.pic_index_white);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        gridLayoutManager = new GridLayoutManager(context,4);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        RecycleAdapter adapter = new RecycleAdapter(context,txts,list);
        recyclerView.setAdapter(adapter);
        DividerGridItemDecoration decoration = new DividerGridItemDecoration(context);
        recyclerView.addItemDecoration(decoration);
        adapter.setOnItemClickListener(this);

    }


    // 开始自动翻页
    @Override
    protected void onResume() {
        super.onResume();
        //开始自动翻页
        if (convenientBanner != null) {
            convenientBanner.startTurning(5000);
        }
    }

    // 停止自动翻页
    @Override
    protected void onPause() {
        super.onPause();
        //停止翻页
        if (convenientBanner != null) {
            convenientBanner.stopTurning();
        }
    }


    @Override
    public void onItemClick(View view, String str) {
        Intent intent = null;
        int i = view.getId();
        if (i == R.drawable.pic_index_gzlq) {
            intent = new Intent(MainActivity.this, GzlqActivity.class);
            startActivity(intent);

        } else if (i == R.drawable.pic_index_lyjc) {
            intent = new Intent(MainActivity.this, LyjcActivity.class);
            startActivity(intent);

        } else if (i == R.drawable.pic_index_lmcf) {
            intent = new Intent(MainActivity.this, LmcfActivity.class);
            startActivity(intent);

        } else if (i == R.drawable.pic_index_shmh) {
            intent = new Intent(MainActivity.this, ShmhActivity.class);
            startActivity(intent);

        } else if (i == R.drawable.pic_index_tghl) {
            intent = new Intent(MainActivity.this, TghlActivity.class);
            startActivity(intent);

        } else if (i == R.drawable.pic_index_gyl) {
            intent = new Intent(MainActivity.this, GylActivity.class);
            startActivity(intent);

        } else if (i == R.drawable.pic_index_slfh) {
            intent = new Intent(MainActivity.this, SlfhActivity.class);
            startActivity(intent);

        } else if (i == R.drawable.pic_index_yzl) {
            intent = new Intent(MainActivity.this, YzlActivity.class);
            startActivity(intent);

        } else if (i == R.drawable.pic_index_sthly) {
            intent = new Intent(MainActivity.this, SthlyActivity.class);
            startActivity(intent);

        } else if (i == R.drawable.pic_index_zmsc) {
            intent = new Intent(MainActivity.this, ZmscActivity.class);
            startActivity(intent);

        } else if (i == R.drawable.pic_index_yhsw) {
            intent = new Intent(MainActivity.this, YhswActivity.class);
            startActivity(intent);

        } else if (i == R.drawable.pic_index_ghry) {
            intent = new Intent(MainActivity.this, GhryActivity.class);
            startActivity(intent);

        } else if (i == R.drawable.pic_index_lycy) {
            intent = new Intent(MainActivity.this, LycyActivity.class);
            startActivity(intent);

        } else if (i == R.drawable.pic_index_lqgg) {
            intent = new Intent(MainActivity.this, LqggActivity.class);
            startActivity(intent);

        } else if (i == R.drawable.pic_index_zrbhq) {
            intent = new Intent(MainActivity.this, ZrbhqActivity.class);
            startActivity(intent);

        } else {
        }
    }
}
