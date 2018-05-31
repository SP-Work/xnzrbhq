package com.otitan.xnbhq.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.esri.core.geometry.Point;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.db.DataBaseHelper;
import com.otitan.xnbhq.db.DbHelperService;
import com.otitan.xnbhq.entity.Station;
import com.otitan.xnbhq.mview.IBaseView;
import com.otitan.xnbhq.util.ToastUtil;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by li on 2017/6/1.
 * 小地名添加
 */

public class AddAddressDialog extends Dialog {

    private Context mContext;
    private Point mappoint;
    private int id;
    private IBaseView iBaseView;
    private DecimalFormat decimalFormat = new DecimalFormat(".000000");

    public AddAddressDialog(@NonNull Context context, @StyleRes int themeResId, Point mappoint, int id, IBaseView baseView) {
        super(context, themeResId);
        this.mContext = context;
        this.mappoint = mappoint;
        this.id = id;
        this.iBaseView = baseView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_addpoint_view);

        final EditText txtViewX = (EditText) findViewById(R.id.xdm_add_x);
        String xx = decimalFormat.format(mappoint.getX()) + "";
        txtViewX.setText(xx);
        final EditText txtViewY = (EditText) findViewById(R.id.xdm_add_y);
        String yy = decimalFormat.format(mappoint.getY()) + "";
        txtViewY.setText(yy);
        final EditText txtname = (EditText) findViewById(R.id.xdm_add_name);
        final Spinner txtType = (Spinner) findViewById(R.id.xdm_add_type);

        final List<String> list = DataBaseHelper.getXdmType(mContext);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(R.layout.myspinner);
        txtType.setAdapter(adapter);

        Button button = (Button) findViewById(R.id.xdm_add_btn_sure);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (TextUtils.isEmpty(txtViewX.getText())) {
                    ToastUtil.setToast(mContext, "请输入地名经度");
                    return;
                }
                if (TextUtils.isEmpty(txtViewY.getText())) {
                    ToastUtil.setToast(mContext, "请输入地名纬度");
                    return;
                }
                if (TextUtils.isEmpty(txtname.getText())) {
                    ToastUtil.setToast(mContext, "请输入小地名名称");
                    return;
                }
                dismiss();

                String name = txtname.getText() == null ? "" : txtname.getText().toString();
                String type = "";
                if (list.size() > 0) {
                    long index = txtType.getSelectedItemId();
                    type = list.get((int) index);
                }
                String lon = txtViewX.getText().toString();
                String lat = txtViewY.getText().toString();
                addPointToSearchData(name, type, lon, lat);

                //actionMode = ActionMode.MODE_NULL;
                iBaseView.getGraphicLayer().removeGraphic(id);
            }
        });

        ImageView close = (ImageView) findViewById(R.id.xdm_add_close);
        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                iBaseView.getGraphicLayer().removeGraphic(id);
                dismiss();
            }
        });
    }

    /**
     * 添加小地名到本地数据库
     */
    private void addPointToSearchData(String name, String type, String lon, String lat) {
        // 添加到本地数据库
        DbHelperService<Station> service = new DbHelperService<Station>(mContext, Station.class);
        Station station = new Station(name, lon, lat, type);
        boolean flag = service.add(station);
        if (flag) {
            //添加成功
            ToastUtil.setToast(mContext, "添加成功");
        } else {
            ToastUtil.setToast(mContext, "添加失败");
        }
        //DataBaseHelper.addPointToSearchData(mContext, lon, lat, "db.sqlite",name, type);
        // 添加到后台服务器
//		Webservice webservice = new Webservice(mContext);
//		String result = webservice.addXdmData(name, type, lon, lat);
//		if (result.equals("true")) {
//			// 添加成功
//			ToastUtil.setToast(mContext, "添加成功");
//		} else if (result.equals(Webservice.netException)) {
//			// 添加失败
//			ToastUtil.setToast(mContext, "网络异常");
//		}
    }
}
