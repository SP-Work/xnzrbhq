package com.otitan.xnbhq.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.esri.core.geodatabase.GeodatabaseFeature;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Polygon;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.LineSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.otitan.xnbhq.BaseActivity;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.adapter.FeatureResultAdapter;
import com.otitan.xnbhq.mview.IBaseView;
import com.otitan.xnbhq.util.BaseUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by li on 2017/6/1.
 * 小班删除
 */

public class FeatureDelDialog extends Dialog {

    private Context mContext;
    private IBaseView iBaseView;
    private List<GeodatabaseFeature> list;
    private Map<GeodatabaseFeature, String> selMap;

    public FeatureDelDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    public FeatureDelDialog(@NonNull Context context, @StyleRes int themeResId, List<GeodatabaseFeature> list,
                            IBaseView baseView, Map<GeodatabaseFeature, String> selMap) {
        super(context, themeResId);
        this.mContext = context;
        this.list = list;
        this.iBaseView = baseView;
        this.selMap = selMap;
    }

    protected FeatureDelDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.featureresult_view);
        setCanceledOnTouchOutside(true);
        final ListView listView = (ListView) findViewById(R.id.featureresult_listview);
        final FeatureResultAdapter adapter = new FeatureResultAdapter(mContext, list, selMap);
        listView.setAdapter(adapter);

        BaseUtil.getIntance(mContext).setHeight(adapter, listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                iBaseView.getGraphicLayer().removeAll();
                Geometry geometry = list.get(position).getGeometry();
                SimpleFillSymbol fillSymbol = new SimpleFillSymbol(Color.RED);
                LineSymbol lineSymbol = new SimpleLineSymbol(Color.RED, 4);// 248,116,14
                fillSymbol.setOutline(lineSymbol);
                Graphic graphic = new Graphic(geometry, fillSymbol);

                Polygon geo = GeometryEngine.buffer(geometry, iBaseView.getSpatialReference(), 200, null);
                iBaseView.getMapView().setExtent(geo);

                iBaseView.getGraphicLayer().addGraphic(graphic);
                long id = list.get(position).getId();
                ((BaseActivity)mContext).showDeleteFeatureDialog(id, position, adapter, FeatureDelDialog.this, listView);
            }
        });
        adapter.notifyDataSetChanged();

    }


}
