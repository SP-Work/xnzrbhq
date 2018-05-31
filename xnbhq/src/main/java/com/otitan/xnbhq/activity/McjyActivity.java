package com.otitan.xnbhq.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.otitan.xnbhq.BaseActivity;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.dialog.AddQyxxDialog;
import com.otitan.xnbhq.dialog.JyxkzDialog;
import com.otitan.xnbhq.dialog.YszSearchDialog;
import com.otitan.xnbhq.util.BussUtil;
import com.titan.baselibrary.util.DialogParamsUtil;

/**
 * 木材经营
 */
public class McjyActivity extends BaseActivity implements View.OnClickListener{

    private View parentView,bootemView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        parentView = getLayoutInflater().inflate(R.layout.activity_mcjy,null);
        super.onCreate(savedInstanceState);
        setContentView(parentView);

        xbbjInclude.setVisibility(View.GONE);
        initview();

        activitytype = getIntent().getStringExtra("name");
        proData = BussUtil.getConfigXml(mContext,activitytype);

    }

    /*控件初始化*/
    private void initview(){
        bootemView = childview.findViewById(R.id.mcjy_bottomview);
        /*运输证查询*/
        TextView ysz_view =(TextView) bootemView.findViewById(R.id.mcjy_yszcx);
        ysz_view.setOnClickListener(this);
        /*企业信息*/
        TextView qyxx_view =(TextView) bootemView.findViewById(R.id.mcjy_qyxxcj);
        qyxx_view.setOnClickListener(this);
        /*许可证年审记录查询*/
        TextView xkz_view =(TextView) bootemView.findViewById(R.id.mcjy_xkzcx);
        xkz_view.setOnClickListener(this);

    }

    @Override
    public View getParentView() {
        return parentView;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.mcjy_yszcx:
                //运输证查询
                YszSearchDialog yszSearchDialog = new YszSearchDialog(mContext,R.style.Dialog,sjssLlist);
                DialogParamsUtil.setDialogParamsCenter(mContext,yszSearchDialog,0.7,0.7);
                break;
            case R.id.mcjy_qyxxcj:
                //企业信息采集
                AddQyxxDialog addQyxxDialog = new AddQyxxDialog(mContext,R.style.Dialog,sjssLlist);
                DialogParamsUtil.setDialogParamsCenter(mContext,addQyxxDialog,0.7,0.7);
                break;
            case R.id.mcjy_xkzcx:
                //许可证年审记录查询
                JyxkzDialog jyxkzDialog = new JyxkzDialog(mContext,R.style.Dialog);
                DialogParamsUtil.setDialogParamsCenter(mContext,jyxkzDialog,0.7,0.7);
                break;
            default:
                break;
        }

    }
}
