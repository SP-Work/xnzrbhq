package com.otitan.xnbhq.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.esri.android.map.TiledLayer;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.core.geometry.Geometry;
import com.esri.core.renderer.Renderer;
import com.esri.core.renderer.SimpleRenderer;
import com.esri.core.renderer.UniqueValue;
import com.esri.core.renderer.UniqueValueRenderer;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.otitan.xnbhq.MyApplication;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.adapter.TcRenderAdapter;
import com.otitan.xnbhq.entity.MyLayer;
import com.otitan.xnbhq.mview.ILayerView;
import com.otitan.xnbhq.presenter.LayerControlPresenter;
import com.otitan.xnbhq.util.BussUtil;
import com.otitan.xnbhq.util.ToastUtil;
import com.otitan.xnbhq.util.Util;
import com.titan.baselibrary.listener.CancleListener;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by li on 2017/6/1.
 * 图层渲染窗口
 */

public class RenderSetDialog extends Dialog {

    private Context mContext;
    private ILayerView iLayerView;
    private LayerControlPresenter layerControlPresenter;

    public RenderSetDialog(@NonNull Context context, @StyleRes int themeResId,ILayerView layerView,LayerControlPresenter layerControlPresenter) {
        super(context, themeResId);
        this.mContext = context;
        this.iLayerView = layerView;
        this.layerControlPresenter = layerControlPresenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layer_render_system);
        setCanceledOnTouchOutside(true);

        TextView titleSys = (TextView) findViewById(R.id.tile_extent);
        titleSys.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (iLayerView.getBaseTitleLayer() == null || !iLayerView.getBaseTitleLayer().isInitialized()) {
                    ToastUtil.setToast(mContext, "图层未初始化完成");
                    return;
                }
                layerTmdSystem(iLayerView.getBaseTitleLayer());
            }
        });

        TextView imageSys = (TextView) findViewById(R.id.image_system);
        imageSys.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(iLayerView.getImgTitleLayer() != null){
                    layerTmdSystem(iLayerView.getImgTitleLayer());
                }else{
                    ToastUtil.setToast(mContext, "图层未加载,请在图层控制中加载数据");
                    return;
                }

//                List<File> fileList = MyApplication.resourcesManager.getImgTitlePath();
//                if(layerControlPresenter == null){
//                    return;
//                }
//                if(layerControlPresenter.imgCheckMap == null){
//                    if (iLayerView.getImgTitleLayer() != null && iLayerView.getImgTitleLayer().isInitialized()) {
//                        layerTmdSystem(iLayerView.getImgTitleLayer());
//                    } else {
//                        ToastUtil.setToast(mContext, "图层未加载,请在图层控制中加载数据");
//                        return;
//                    }
//                }else {
//                    if(layerControlPresenter.imgTileLayerMap.size() > 0){
//                        layerTmdSystemImge(fileList, layerControlPresenter.imgTileLayerMap);
//                    }else{
//                        ToastUtil.setToast(mContext, "图层未加载,请在图层控制中加载数据");
//                        return;
//                    }
//                }
            }
        });

        TextView dxtSys = (TextView) findViewById(R.id.dxt_system);
        dxtSys.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (iLayerView.getDxtTitleLayer() == null || !iLayerView.getDxtTitleLayer().isInitialized()) {
                    ToastUtil.setToast(mContext, "图层未初始化完成");
                    return;
                }
                layerTmdSystem(iLayerView.getDxtTitleLayer());
            }
        });

        ListView listView = (ListView) findViewById(R.id.layer_render_system);
        TcRenderAdapter adapter = new TcRenderAdapter(mContext, iLayerView.getLayerList(),this);
        listView.setAdapter(adapter);

        ImageView image = (ImageView) findViewById(R.id.close_render);
        image.setOnClickListener(new CancleListener(this));

    }

    /**
     * 影像图、地形图 图层渲染设置设置
     */
    public void layerTmdSystem(final TiledLayer layer) {
        final Dialog dialog = new Dialog(mContext, R.style.Dialog);
        // 展示图层透明度和图层颜色设置的dialog
        dialog.setContentView(R.layout.title_layer_tmdsystem);
        dialog.setCanceledOnTouchOutside(true);

        final String name = new File(layer.getUrl()).getName();

        final SeekBar seekBar = (SeekBar) dialog.findViewById(R.id.title_symbol_seekbar);
        int tmdtxt = MyApplication.sharedPreferences.getInt(name, 100);
        seekBar.setProgress(tmdtxt);

        final TextView tmd = (TextView) dialog.findViewById(R.id.title_toumingdu);
        tmd.setText((float) tmdtxt / 100 + "");

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar arg0) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0) {

            }

            @Override
            public void onProgressChanged(SeekBar arg0, final int arg1, boolean arg2) {
                MyApplication.sharedPreferences.edit().putInt(name, arg1).apply();
                float tt = (float) arg1 / 100;
                tmd.setText(tt + "");
                layer.setOpacity(tt);
            }
        });

        Button system = (Button) dialog.findViewById(R.id.title_symbo_reset);
        system.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                seekBar.setProgress(100);
                layer.setOpacity((float) 1.0);
                dialog.dismiss();
            }
        });

        ImageView close = (ImageView) dialog.findViewById(R.id.tmdsettings_close);
        close.setOnClickListener(new CancleListener(dialog));

        BussUtil.setDialogParams(mContext, dialog, 0.8, 1);
    }

    /**
     * 影像图、地形图图层透明度设置
     */
    public void layerTmdSystemImge(final List<File> fileList, final Map<String, ArcGISLocalTiledLayer> imgTileLayerMap) {
        final Dialog dialog = new Dialog(mContext, R.style.Dialog);
        // 展示图层透明度和图层颜色设置的dialog
        dialog.setContentView(R.layout.title_layer_tmdsystem);
        dialog.setCanceledOnTouchOutside(true);

        final SeekBar seekBar = (SeekBar) dialog.findViewById(R.id.title_symbol_seekbar);
        final TextView tmd = (TextView) dialog.findViewById(R.id.title_toumingdu);

        for (File f : fileList) {
            ArcGISLocalTiledLayer layer = imgTileLayerMap.get(f.getName());
            if (layer != null) {
                final String name = new File(layer.getUrl()).getName();
                int tmdtxt = MyApplication.sharedPreferences.getInt(name, 100);
                seekBar.setProgress(tmdtxt);
                tmd.setText(((float) tmdtxt / 100) + "");
            }
        }


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar arg0) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0) {

            }

            @Override
            public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
                for (File f : fileList) {
                    ArcGISLocalTiledLayer layer = imgTileLayerMap.get(f.getName());
                    if (layer != null) {
                        String name = new File(layer.getUrl()).getName();
                        MyApplication.sharedPreferences.edit().putInt(name, arg1).apply();
                        float tt = (float) arg1 / 100;
                        tmd.setText(tt + "");
                        layer.setOpacity(tt);
                    }
                }
            }
        });

        Button system = (Button) dialog.findViewById(R.id.title_symbo_reset);
        system.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                seekBar.setProgress(100);
                for (File f : fileList) {
                    ArcGISLocalTiledLayer layer = imgTileLayerMap.get(f.getName());
                    if (layer != null) {
                        layer.setOpacity((float) 1.0);
                    }
                }
                dialog.dismiss();
            }
        });

//		RadioButton sure = (RadioButton) dialog.findViewById(R.id.layer_render_sure);
//		sure.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				for(File f : fileList){
//					ArcGISLocalTiledLayer layer = imgTileLayerMap.get(f.getName());
//					if(layer != null){
//						String name = new File(layer.getUrl()).getName();
//						int arg1 = sharedPreferences.getInt(name, 100);
//						float tt = (float)arg1/100;
//						tmd.setText(tt + "");
//						layer.setOpacity(tt);
//					}
//				}
//
//			}
//		});

        ImageView close = (ImageView) dialog.findViewById(R.id.tmdsettings_close);
        close.setOnClickListener(new CancleListener(dialog));

        BussUtil.setDialogParams(mContext, dialog, 0.8, 0.8);
    }

    /**
     * 展示图层渲染设置
     */
    public void showLayerRender(final MyLayer myLayer) {
        final Dialog dialog = new Dialog(mContext, R.style.Dialog);
        // 展示图层透明度和图层颜色设置的dialog
        dialog.setContentView(R.layout.dialog_color_show);
        dialog.setCanceledOnTouchOutside(true);

        final SeekBar seekBar = (SeekBar) dialog.findViewById(R.id.symbol_seekbar);
        seekBar.setProgress(MyApplication.sharedPreferences.getInt(myLayer.getLayer().getName() + "tmd", 100));

        final TextView textView = (TextView) dialog.findViewById(R.id.toumingdu);
        int sektxt = MyApplication.sharedPreferences.getInt(myLayer.getLayer().getName() + "tmd", 0);
        textView.setText(sektxt + "");

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar arg0) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0) {

            }

            @Override
            public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
                MyApplication.sharedPreferences.edit().putInt(myLayer.getLayer().getName() + "tmd", arg1).apply();
                textView.setText(arg0.getProgress() + "");
            }
        });
        //填充色设置
        final TextView tianchongse = (TextView) dialog.findViewById(R.id.tianchongse);
        tianchongse.setBackgroundColor(MyApplication.sharedPreferences.getInt(myLayer.getLayer().getName() + "tianchongse", R.color.nocolor));
        tianchongse.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ColorDialog colorDialog = new ColorDialog(mContext,R.style.Dialog,0, tianchongse, seekBar, myLayer);
                BussUtil.setDialogParam(mContext, colorDialog, 0.35, 0.35, 0.6, 0.6);
            }
        });
        //边界的设置
        final EditText outlinewidth = (EditText) dialog.findViewById(R.id.outlinewidth);
        outlinewidth.setText(MyApplication.sharedPreferences.getFloat(myLayer.getLayer().getName() + "owidth", 0) + "");
        final TextView bianjiese = (TextView) dialog.findViewById(R.id.bianjiese);
        bianjiese.setBackgroundColor(MyApplication.sharedPreferences.getInt(myLayer.getLayer().getName() + "bianjiese", R.color.nocolor));
        bianjiese.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ColorDialog colorDialog = new ColorDialog(mContext,R.style.Dialog,1, bianjiese, seekBar, myLayer);
                BussUtil.setDialogParam(mContext, colorDialog, 0.35, 0.35, 0.6, 0.6);
            }
        });

        //重置按钮
        Button button = (Button) dialog.findViewById(R.id.symbo_reset);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                seekBar.setProgress(0);
                Renderer renderer = myLayer.getRenderer();
                myLayer.getLayer().setRenderer(renderer);
                iLayerView.getMapView().invalidate();
                dialog.dismiss();
            }
        });
        //只设置透明度按钮
        RadioButton btntmd = (RadioButton) dialog.findViewById(R.id.layer_render_btn_opsure);
        btntmd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                float txt = seekBar.getProgress();
                float opacity = txt / 100;
                myLayer.getLayer().setOpacity(1 - opacity);
                iLayerView.getMapView().invalidate();
                dialog.dismiss();
            }
        });
        //确定按钮
        RadioButton radioSure = (RadioButton) dialog.findViewById(R.id.layer_render_btn_sure);
        radioSure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                int txt = seekBar.getProgress();
                setLayerRenderer(txt, outlinewidth, myLayer);
                dialog.dismiss();
            }
        });

        ImageView radioCancle = (ImageView) dialog.findViewById(R.id.tmdsettings_close1);
        radioCancle.setOnClickListener(new CancleListener(dialog));

        BussUtil.setDialogParams(mContext, dialog, 0.5, 0.7);
    }

    private void setLayerRenderer(int seekValue, EditText outlinewidth, MyLayer myLayer) {
        int txt = seekValue;
        //SimpleFillSymbol simpleFillSymbol = new SimpleFillSymbol(sharedPreferences.getInt("color", Color.GREEN));
        int tcs = MyApplication.sharedPreferences.getInt(myLayer.getLayer().getName() + "tianchongse", R.color.nocolor);
        int tcolor = Util.getColor(tcs, txt);//3158064  959459376
        SimpleFillSymbol simpleFillSymbol = new SimpleFillSymbol(tcolor);
        Object obj = outlinewidth.getText();
        float owidth = 0;
        if (obj != null) {
            String str = obj.toString();
            boolean flag = Util.CheckStrIsDouble(str);
            if (flag) {
                owidth = Float.parseFloat(str);
            }
        }
        int bjs = MyApplication.sharedPreferences.getInt(myLayer.getLayer().getName() + "bianjiese", R.color.nocolor);
        simpleFillSymbol.setOutline(new SimpleLineSymbol(bjs, owidth));

        SimpleMarkerSymbol markerSymbol = new SimpleMarkerSymbol(tcolor, (int) owidth, SimpleMarkerSymbol.STYLE.CIRCLE);
        markerSymbol.setColor(tcs);
        markerSymbol.setOutline(new SimpleLineSymbol(bjs, owidth));

        SimpleLineSymbol lineSymbol = new SimpleLineSymbol(tcolor, (int) owidth, com.esri.core.symbol.SimpleLineSymbol.STYLE.SOLID);
        lineSymbol.setWidth(owidth);

        MyApplication.sharedPreferences.edit().putFloat(myLayer.getLayer().getName() + "owidth", owidth).apply();

        Renderer renderer = null;
        if (myLayer.getLayer().getGeometryType().equals(Geometry.Type.POLYGON)) {
            boolean flag = false;
            List<com.esri.core.map.Field> fields = myLayer.getLayer().getFeatureTable().getFields();
            for (com.esri.core.map.Field ff : fields){
                if(ff.getName().equals("aaa")){
                    flag = true;
                    break;
                }
            }

            if(flag){
                UniqueValueRenderer uniqueValueRenderer = new UniqueValueRenderer();
                uniqueValueRenderer.setField1("aaa");
                UniqueValue uniqueValue = new UniqueValue();
                String[] uniqueAttribute1 = new String[]{"0"};
                uniqueValue.setDescription("0");
                uniqueValue.setValue(uniqueAttribute1);
                uniqueValue.setSymbol(simpleFillSymbol);
                uniqueValueRenderer.addUniqueValue(uniqueValue);

                UniqueValue uniqueValue2 = new UniqueValue();
                String[] uniqueAttribute2 = new String[]{"1"};
                uniqueValue2.setDescription("0");
                uniqueValue2.setValue(uniqueAttribute2);
                SimpleFillSymbol fillSymbol = new SimpleFillSymbol(Util.getColor(Color.rgb(3,104,48), txt));
                fillSymbol.setOutline(new SimpleLineSymbol(bjs, owidth));
                uniqueValue2.setSymbol(fillSymbol);
                uniqueValueRenderer.addUniqueValue(uniqueValue2);
                renderer = uniqueValueRenderer;
            }else{
                renderer = new SimpleRenderer(simpleFillSymbol);
            }
        } else if (myLayer.getLayer().getGeometryType().equals(Geometry.Type.POLYLINE)) {
            renderer = new SimpleRenderer(lineSymbol);
        } else {
            renderer = new SimpleRenderer(markerSymbol);
        }

        myLayer.getLayer().setRenderer(renderer);

        MyApplication.sharedPreferences.edit().putString(myLayer.getLayer().getName(), "").apply();
    }

//    /**
//     * 展示图层渲染设置
//     */
//    public void showLayerRender(final int groupPosition, final List<File> groups, final List<Map<String, List<File>>> childs) {
//        final Dialog dialog = new Dialog(mContext, R.style.Dialog);
//        // 展示图层透明度和图层颜色设置的dialog
//        dialog.setContentView(R.unreportdata.dialog_color_show);
//        dialog.setCanceledOnTouchOutside(true);
//
//        final SeekBar seekBar = (SeekBar) dialog.findViewById(R.id.symbol_seekbar);
//        seekBar.setProgress(MyApplication.sharedPreferences.getInt("tmd", 100));
//
//        final TextView textView = (TextView) dialog.findViewById(R.id.toumingdu);
//        int sektxt = MyApplication.sharedPreferences.getInt("tmd", 0);
//        textView.setText(sektxt + "");
//
//        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//
//            @Override
//            public void onStopTrackingTouch(SeekBar arg0) {
//
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar arg0) {
//
//            }
//
//            @Override
//            public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
//                MyApplication.sharedPreferences.edit().putInt("tmd", arg1).apply();
//                textView.setText(arg0.getProgress() + "");
//            }
//        });
//        //填充色设置
//        final TextView tianchongse = (TextView) dialog.findViewById(R.id.tianchongse);
//        tianchongse.setBackgroundColor(MyApplication.sharedPreferences.getInt("tianchongse", R.color.nocolor));
//        tianchongse.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                ColorDialog colorDialog = new ColorDialog(mContext,R.style.Dialog,0, tianchongse, seekBar, myLayer);
//                BussUtil.setDialogParam(mContext, colorDialog, 0.35, 0.35, 0.6, 0.6);
//            }
//        });
//        //边界的设置
//        final EditText outlinewidth = (EditText) dialog.findViewById(R.id.outlinewidth);
//        outlinewidth.setText(MyApplication.sharedPreferences.getFloat("owidth", 0) + "");
//        final TextView bianjiese = (TextView) dialog.findViewById(R.id.bianjiese);
//        bianjiese.setBackgroundColor(MyApplication.sharedPreferences.getInt("bianjiese", R.color.nocolor));
//        bianjiese.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                ColorDialog colorDialog = new ColorDialog(mContext,R.style.Dialog,1, bianjiese, seekBar, myLayer);
//                BussUtil.setDialogParam(mContext, colorDialog, 0.35, 0.35, 0.6, 0.6);
//            }
//        });
//
//        //重置按钮
//        Button button = (Button) dialog.findViewById(R.id.symbo_reset);
//        button.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                seekBar.setProgress(0);
//                String gName = groups.get(groupPosition).getName();
//                for (MyLayer layer : layerNameList) {
//                    String pname = layer.getPname();
//                    if (pname.equals(gName)) {
//                        layer.getLayer().setOpacity(1);
//                        layer.getLayer().setRenderer(layer.getRenderer());
//                    }
//                }
//                dialog.dismiss();
//            }
//        });
//        //只设置透明度按钮
//        RadioButton btntmd = (RadioButton) dialog.findViewById(R.id.layer_render_btn_opsure);
//        btntmd.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                float txt = seekBar.getProgress();
//                String gName = groups.get(groupPosition).getName();
//                float opacity = txt / 100;
//                for (MyLayer layer : layerNameList) {
//                    String pname = layer.getPname();
//                    if (pname.equals(gName)) {
//                        layer.getLayer().setOpacity(1 - opacity);
//                        mapView.invalidate();
//                    }
//                }
//                dialog.dismiss();
//            }
//        });
//        //确定按钮
//        RadioButton radioSure = (RadioButton) dialog.findViewById(R.id.layer_render_btn_sure);
//        radioSure.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                int txt = Integer.parseInt(textView.getText().toString());
//                //SimpleFillSymbol simpleFillSymbol = new SimpleFillSymbol(sharedPreferences.getInt("color", Color.GREEN));
//                int tcs = MyApplication.sharedPreferences.getInt("tianchongse", R.color.nocolor);
//                int tcolor = Util.getColor(tcs, txt);//3158064  959459376
//                SimpleFillSymbol simpleFillSymbol = new SimpleFillSymbol(tcolor);
//                Object obj = outlinewidth.getText();
//                float owidth = 0;
//                if (obj != null) {
//                    String str = obj.toString();
//                    boolean flag = Util.CheckStrIsDouble(str);
//                    if (flag) {
//                        owidth = Float.parseFloat(str);
//                    }
//                }
//                int bjs = MyApplication.sharedPreferences.getInt("bianjiese", R.color.nocolor);
//                simpleFillSymbol.setOutline(new SimpleLineSymbol(bjs, owidth));
//                MyApplication.sharedPreferences.edit().putFloat("owidth", owidth).apply();
//                Renderer renderer = new SimpleRenderer(simpleFillSymbol);
//
//                String gName = groups.get(groupPosition).getName();
//                for (int m = 0; m < layerNameList.size(); m++) {
//                    String keyname = layerNameList.get(m).getPname();
//                    if (keyname.equals(gName)) {
//                        //featureLayerList.get(m).setRenderer(renderer);
//                        layerNameList.get(m).getLayer().setRenderer(renderer);
//                    }
//                }
//
//                dialog.dismiss();
//            }
//        });
//
//        RadioButton radioCancle = (RadioButton) dialog.findViewById(R.id.layer_render_btn_cancle);
//        radioCancle.setOnClickListener(new BaseActivity.CancleLisence(dialog));
//
//        ImageView close = (ImageView) dialog.findViewById(R.id.tmdsettings_close);
//        close.setOnClickListener(new BaseActivity.CancleLisence(dialog));
//
//        BussUtil.setDialogParam(mContext, dialog, 0.75, 0.7, 0.5, 0.5);
//    }


}
