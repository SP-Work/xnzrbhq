package com.otitan.xnbhq.presenter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.esri.core.geodatabase.GeodatabaseFeature;
import com.esri.core.geometry.Envelope;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.CodedValueDomain;
import com.esri.core.map.Field;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.tasks.query.QueryParameters;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.adapter.LayerLableAdapter;
import com.otitan.xnbhq.entity.MyLayer;
import com.otitan.xnbhq.mview.ILayerView;
import com.otitan.xnbhq.util.BaseUtil;
import com.otitan.xnbhq.util.SymbolUtil;
import com.otitan.xnbhq.util.ToastUtil;
import com.titan.baselibrary.util.ProgressDialogUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by li on 2017/6/5.
 * 图层标注presenter
 */

public class LayerLablePresenter {

    private Context mContext;
    private ILayerView iLayerView;
    public MyLayer myLayer;
    private long[] arrays;
    private HashMap<Field,Boolean> checkboxs = new HashMap<>();

    public LayerLablePresenter(Context context, ILayerView layerView){
        this.mContext = context;
        this.iLayerView = layerView;
    }

    /**显示选择图层的对应字段*/
    public void showLayerAials(final View lableView, MyLayer myLayer){
        this.myLayer = myLayer;
        ImageView imageView = (ImageView) lableView.findViewById(R.id.attr_field_exit);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lableView.setVisibility(View.GONE);
            }
        });
        ListView listView =(ListView) lableView.findViewById(R.id.field_list);
        List<Field> fields = myLayer.getLayer().getFeatureTable().getFields();
        for(Field f:fields){
            checkboxs.put(f,false);
        }
        LayerLableAdapter lableAdapter = new LayerLableAdapter(mContext,iLayerView,fields,this,checkboxs);
        listView.setAdapter(lableAdapter);
        BaseUtil.setHeight(lableAdapter,listView);

    }

    /**查询当前区域内的对应图层的小班数据*/
    public void queryFeatures(final MyLayer myLayer,final boolean isChecked,final List<Field> fields,final int position){
        QueryParameters q = new QueryParameters();
        q.setWhere("1=1");
        q.setInSpatialReference(iLayerView.getSpatialReference());
        q.setReturnGeometry(true);
        q.setGeometry(iLayerView.getCurrentEnvelope());
        //myLayer.getLayer().setSelectionColor(0);
        myLayer.getLayer().getFeatureTable().queryIds(q, new CallbackListener<long[]>() {
            @Override
            public void onCallback(long[] longs) {
                if(longs == null){
                    ProgressDialogUtil.stopProgressDialog(mContext);
                    //Looper.prepare();
                    ToastUtil.setToast(mContext,"当前视图范围内没有图班");
                    //Looper.loop();
                    return;
                }
                if(longs.length > 0){
                    arrays = longs;
                    ProgressDialogUtil.stopProgressDialog(mContext);
                    addLableToLayer(isChecked,fields,position);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                ProgressDialogUtil.stopProgressDialog(mContext);
            }
        });
    }

    /**添加lable*/
    public void addLableToLayer(boolean isChecked, List<Field> fields, int position){
        if(isChecked){
            iLayerView.getGraphicLayer().removeAll();
            return;
        }
        if(arrays == null){
            return;
        }
        iLayerView.getGraphicLayer().removeAll();
        for(long id : arrays){
            GeodatabaseFeature feature = (GeodatabaseFeature) myLayer.getLayer().getFeature(id);
            Object obj = null;
            if(fields.get(position).getFieldType() == Field.esriFieldTypeDate){
                Object obj1 = feature.getAttributeValue(fields.get(position));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(Long.parseLong(obj1.toString()));
                obj = sdf.format(date);
            }else{
                Object objvalue = feature.getAttributeValue(fields.get(position));
                CodedValueDomain domain = (CodedValueDomain)fields.get(position).getDomain();
                if(domain != null){
                    Map<String, String> values = domain.getCodedValues();
                    for(String key : values.keySet()){
                        if(key.equals(objvalue)){
                            obj = values.get(key);
                        }
                    }
                }else{
                    obj = objvalue;
                }
            }

            if(obj != null){
                PictureMarkerSymbol textSymbol = SymbolUtil.TextPicSymobel(mContext,obj.toString(),Color.BLUE,16, SymbolUtil.MODE.TOP);
                //TextSymbol textSymbol = new TextSymbol(20,obj.toString(), Color.BLUE);
                //设定之后可以使标注显示中文
                Envelope env = new Envelope();
                feature.getGeometry().queryEnvelope(env);
                Graphic graphic = new Graphic(env.getCenter(),textSymbol);
                if(iLayerView.getGraphicLayer() != null){
                    iLayerView.getGraphicLayer().addGraphic(graphic);
                }else{
                    iLayerView.addGraphicLayer();
                    iLayerView.getGraphicLayer().addGraphic(graphic);
                }
            }
        }
        iLayerView.getMapView().invalidate();
    }
}
