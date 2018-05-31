package com.otitan.xnbhq.presenter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.esri.android.map.FeatureLayer;
import com.esri.core.geodatabase.GeodatabaseFeature;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Polygon;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.FeatureResult;
import com.esri.core.map.Field;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.tasks.SpatialRelationship;
import com.esri.core.tasks.query.QueryParameters;
import com.otitan.xnbhq.BaseActivity;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.adapter.SetAdapter;
import com.otitan.xnbhq.entity.MyLayer;
import com.otitan.xnbhq.entity.Row;
import com.otitan.xnbhq.mview.ILayerView;
import com.otitan.xnbhq.util.BaseUtil;
import com.otitan.xnbhq.util.BussUtil;
import com.otitan.xnbhq.util.CursorUtil;
import com.otitan.xnbhq.util.SytemUtil;
import com.otitan.xnbhq.util.ToastUtil;
import com.titan.baselibrary.customview.DropdownEdittext;
import com.titan.baselibrary.util.ProgressDialogUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by li on 2017/6/2.
 * 小班查询
 */

public class XbqueryPresenter {

    private Context mContext;
    private ILayerView iLayerView;
    private List<GeodatabaseFeature> list_xbsearch = new ArrayList<>();
    //自定义查询定义变量
    private boolean isAutoSelect = true;

    public XbqueryPresenter(Context context,ILayerView layerView){
        this.mContext = context;
        this.iLayerView = layerView;
    }

    /**
     * 小班简单查询界面
     */
    public void showSearchXBjd(final View xbSearchJdInclude) {
        xbSearchJdInclude.setVisibility(View.VISIBLE);

        final RadioButton rb_xbh = (RadioButton) xbSearchJdInclude.findViewById(R.id.jdsearch_xbh);
        final RadioButton rb_xmmc = (RadioButton) xbSearchJdInclude.findViewById(R.id.jdsearch_xmmc);
        final RadioButton rb_gclb = (RadioButton) xbSearchJdInclude.findViewById(R.id.jdsearch_gclb);
        final RadioButton rb_nd = (RadioButton) xbSearchJdInclude.findViewById(R.id.jdsearch_nd);

        final EditText editText = (EditText) xbSearchJdInclude.findViewById(R.id.jdquery_edittext);
        final ListView listView = (ListView) xbSearchJdInclude.findViewById(R.id.listView_result_xbsearch_jd);
        list_xbsearch.clear();
        listView.setAdapter(new SetAdapter(list_xbsearch, mContext, BaseActivity.myLayer));
        listView.setVisibility(View.GONE);
        final View toplayou = xbSearchJdInclude.findViewById(R.id.toplayou);
        if (toplayou.getVisibility() == View.VISIBLE) {
            toplayou.setVisibility(View.GONE);
        }
        rb_xbh.setChecked(true);
        rb_xbh.setTextColor(mContext.getResources().getColor(R.color.blue));

        rb_xbh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (arg1) {
                    rb_xbh.setTextColor(mContext.getResources().getColor(R.color.blue));
                    rb_xmmc.setTextColor(mContext.getResources().getColor(R.color.balck));
                    rb_gclb.setTextColor(mContext.getResources().getColor(R.color.balck));
                    rb_nd.setTextColor(mContext.getResources().getColor(R.color.balck));
                }
            }
        });

        rb_xmmc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (arg1) {
                    rb_xbh.setTextColor(mContext.getResources().getColor(R.color.balck));
                    rb_xmmc.setTextColor(mContext.getResources().getColor(R.color.blue));
                    rb_gclb.setTextColor(mContext.getResources().getColor(R.color.balck));
                    rb_nd.setTextColor(mContext.getResources().getColor(R.color.balck));
                }
            }
        });
        rb_gclb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (arg1) {
                    rb_xbh.setTextColor(mContext.getResources().getColor(R.color.balck));
                    rb_xmmc.setTextColor(mContext.getResources().getColor(R.color.balck));
                    rb_gclb.setTextColor(mContext.getResources().getColor(R.color.blue));
                    rb_nd.setTextColor(mContext.getResources().getColor(R.color.balck));
                }
            }
        });

        rb_nd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (arg1) {
                    rb_xbh.setTextColor(mContext.getResources().getColor(R.color.balck));
                    rb_xmmc.setTextColor(mContext.getResources().getColor(R.color.balck));
                    rb_gclb.setTextColor(mContext.getResources().getColor(R.color.balck));
                    rb_nd.setTextColor(mContext.getResources().getColor(R.color.blue));
                }
            }
        });

        ImageView close = (ImageView) xbSearchJdInclude.findViewById(R.id.close_xbsearch_jd);
        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                xbSearchJdInclude.setVisibility(View.GONE);
            }
        });

        TextView sure = (TextView) xbSearchJdInclude.findViewById(R.id.btn_sure_jd);
        sure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                List<Field> list = BaseActivity.myLayer.getLayer().getFeatureTable().getFields();
                String queryTxt = "";
                String eTxt = editText.getText().toString().trim();
                if (rb_xbh.isChecked()) {
                    for (Field field : list) {
                        if (field.getAlias().contains("小班号") ||
                                field.getAlias().contains("XBH") ||
                                field.getAlias().contains("xbh")) {
                            queryTxt = field.getName() + " LIKE " + eTxt + "";
                            break;
                        }
                    }
                } else if (rb_xmmc.isChecked()) {
                    for (Field field : list) {
                        if (field.getAlias().contains("项目名称")) {
                            queryTxt = field.getName() + " LIKE " + eTxt + "";
                            break;
                        }
                    }
                } else if (rb_gclb.isChecked()) {
                    for (Field field : list) {
                        if (field.getAlias().contains("工程类别")) {
                            queryTxt = field.getName() + " LIKE " + eTxt + "";
                            break;
                        }
                    }
                } else if (rb_nd.isChecked()) {
                    for (Field field : list) {
                        if (field.getAlias().contains("年度")) {
                            queryTxt = field.getName() + " LIKE " + eTxt + "";
                            break;
                        }
                    }
                }
                ProgressDialogUtil.startProgressDialog(mContext);
                getFieldAndValue(BaseActivity.myLayer, queryTxt, listView,toplayou);
                if (list_xbsearch.size() == 0) {
                    ProgressDialogUtil.stopProgressDialog(mContext);
                    ToastUtil.setToast(mContext, "没有数据");
                }

            }
        });
    }

    /**
     * 获取所选图层的字段及字段值,小班简单查询
     */
    private void getFieldAndValue(final MyLayer layer, String queryStr, final ListView listView,final View toplayou) {
        if (queryStr.contains("LIKE")) {//"%" + searchTxt + "%"
            queryStr = queryStr.split("LIKE")[0] + "LIKE '%" + queryStr.split("LIKE")[1].trim() + "%'";
        }//SJXBH LIKE '%6%'  XBH LIKE '%4%'

        QueryParameters queryParams = new QueryParameters();//XIANG like 520118
        queryParams.setOutFields(new String[]{"*"});
        queryParams.setSpatialRelationship(SpatialRelationship.INTERSECTS);
        queryParams.setGeometry(iLayerView.getBaseTitleLayer().getFullExtent());
        queryParams.setReturnGeometry(true);
        queryParams.setInSpatialReference(iLayerView.getSpatialReference());
        queryParams.setOutSpatialReference(iLayerView.getSpatialReference());
        queryParams.setWhere(queryStr);

        layer.getLayer().selectFeatures(queryParams, FeatureLayer.SelectionMode.NEW, new CallbackListener<FeatureResult>() {

            @Override
            public void onError(Throwable arg0) {
                ToastUtil.setToast(mContext, "查询出现错误");
                ProgressDialogUtil.stopProgressDialog(mContext);
            }

            @Override
            public void onCallback(final FeatureResult result) {
                if (result != null) {
                    long count = result.featureCount();
                    if (count == 0) {
                        ToastUtil.setToast(mContext, "没有查询到符合条件的小班");
                        ProgressDialogUtil.stopProgressDialog(mContext);
                        return;
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            getSearchResult(layer, result, listView,toplayou);
                        }
                    }).start();
                } else {
                    ProgressDialogUtil.stopProgressDialog(mContext);
                }
            }
        });
    }

    /**展示小班简单查询结果*/
    private void getSearchResult(final MyLayer layer, final FeatureResult featureResult, final ListView listView,final View toplayou) {

        list_xbsearch.clear();
        if (featureResult.featureCount() > 0) {
            Iterator<Object> iterator = featureResult.iterator();
            GeodatabaseFeature geodatabaseFeature = null;
            while (iterator.hasNext()) {
                geodatabaseFeature = (GeodatabaseFeature) iterator.next();
                list_xbsearch.add(geodatabaseFeature);
            }

            ((Activity)mContext).runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (list_xbsearch.size() > 0) {
                        toplayou.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.VISIBLE);
                    }
                    bindingAdapter(listView,list_xbsearch);
                }
            });
            layer.getLayer().clearSelection();
        } else {
            ToastUtil.setToast(mContext, "无符合条件的小班");
        }
        ProgressDialogUtil.stopProgressDialog(mContext);
    }

    /**显示查询结果并高亮显示*/
    private void bindingAdapter(ListView listView,final List<GeodatabaseFeature> list_xbsearch) {
        SetAdapter adapter = new SetAdapter(list_xbsearch, mContext, BaseActivity.myLayer);
        listView.setAdapter(adapter);

        BaseUtil.getIntance(mContext).setHeight(adapter, listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                if (iLayerView.getGraphicLayer() != null) {
                    iLayerView.getGraphicLayer().removeAll();
                    BaseActivity.circleGraphic = null;
                    BaseActivity.locationGraphic = null;
                }
                Geometry geometry = list_xbsearch.get(position).getGeometry();
                if (geometry.getType() == Geometry.Type.POLYGON) {
                    SimpleFillSymbol symbol = new SimpleFillSymbol(Color.RED);
                    symbol.setAlpha(80);
                    Graphic graphic = new Graphic(geometry, symbol);
                    iLayerView.getGraphicLayer().addGraphic(graphic);
                } else if (geometry.getType() == Geometry.Type.POINT) {
                    SimpleMarkerSymbol markerSymbol = new SimpleMarkerSymbol(Color.RED, 20, SimpleMarkerSymbol.STYLE.CIRCLE);
                    Graphic graphic = new Graphic(geometry, markerSymbol);
                    iLayerView.getGraphicLayer().addGraphic(graphic);
                }

                //建立一个1000米缓冲区
                Polygon polygon = GeometryEngine.buffer(geometry, iLayerView.getSpatialReference(), 200, null);
                iLayerView.getMapView().setExtent(polygon);
            }
        });
    }

    private String selectField = "";// 获取所选图层的所选中字段
    private String selectValueKey = "";// 所选图层的所选中字段选择值对应的key
    private List<String> reductionList = new ArrayList<String>();

    /**
     * 小班自定义查询窗口
     */
    public void showSearchXiaoZDY(final View xbSearchZdyInclude) {
        selectField = "";
        selectValueKey = "";
        reductionList = new ArrayList<String>();

        final List<Field> fieldList = BaseActivity.myLayer.getTable().getFields();
        final List<String> list = new ArrayList<String>();
        int size = fieldList.size();
        for (int i = 0; i < size; i++) {
            String alias = fieldList.get(i).getAlias();
            list.add(alias);
        }
        list_xbsearch.clear();
        xbSearchZdyInclude.setVisibility(View.VISIBLE);
        final ListView listView_xbsearch = (ListView) xbSearchZdyInclude.findViewById(R.id.listView_result_xbsearch);
        SetAdapter adapter1 = new SetAdapter(list_xbsearch, mContext, BaseActivity.myLayer);
        listView_xbsearch.setAdapter(adapter1);
        final DropdownEdittext txt = (DropdownEdittext) xbSearchZdyInclude.findViewById(R.id.field_edittext);
        txt.tv.setText("");
        final Spinner yinzi = (Spinner) xbSearchZdyInclude.findViewById(R.id.select_spinner);
        final EditText queryTextView = (EditText) xbSearchZdyInclude.findViewById(R.id.query_edittext);

        queryTextView.setText("");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yinzi.setAdapter(adapter);
        yinzi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {
                if (isAutoSelect && position == 0) {
                    isAutoSelect = false;
                } else {
                    reductionList.clear();
                    Field field = fieldList.get(position);
                    selectField = field.getName();
                    txt.tv.setText("");
                    selectValueKey = "";
                    reductionList.add(selectField);
                    setTxt(queryTextView, reductionList);
                    String gname = BaseActivity.myLayer.getPname();
                    List<Row> list = getFieldValueByKey(gname, field);
                    showFieldValue(list, txt, queryTextView);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        TextView textView = (TextView) xbSearchZdyInclude.findViewById(R.id.select_char);
        textView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                showSelectChar(queryTextView);
            }
        });

        ImageView reduction = (ImageView) xbSearchZdyInclude.findViewById(R.id.char_reduction);
        reduction.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                List<String> list = reductionList;
                for (int m = list.size() - 1; m >= 0; m--) {
                    reductionList.remove(reductionList.get(m));
                    setTxt(queryTextView, reductionList);
                    break;
                }
            }
        });

        TextView btn = (TextView) xbSearchZdyInclude.findViewById(R.id.btn_sure);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (selectField.equals("")) {
                    ToastUtil.setToast(mContext, "未选择查询字段");
                    return;
                }
                if (selectValueKey.equals("")) {
                    ToastUtil.setToast(mContext, "未输入查询字段值");
                    return;
                }
                if (!selectField.equals("") && !selectValueKey.equals("")) {
                    ProgressDialogUtil.startProgressDialog(mContext);
                    String where = queryTextView.getText().toString();
                    getFieldAndValue(BaseActivity.myLayer, where, listView_xbsearch,new View(mContext));
                }
            }
        });

        ImageView close_xbsearch = (ImageView) xbSearchZdyInclude.findViewById(R.id.close_xbsearch);
        close_xbsearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                xbSearchZdyInclude.setVisibility(View.INVISIBLE);
                isAutoSelect = true;
                list_xbsearch.clear();
            }
        });
    }

    private String charSelect = "所选字符";

    public String showSelectChar(final EditText queryEditText) {
        final Dialog dialog = new Dialog(mContext, R.style.Dialog);
        dialog.setContentView(R.layout.charselect);

        final Button button1 = (Button) dialog.findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
                charSelect = "=";
                reductionList.add(charSelect);
                setTxt(queryEditText, reductionList);
            }
        });
        final Button button2 = (Button) dialog.findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
                charSelect = "<>";
                reductionList.add(charSelect);
                setTxt(queryEditText, reductionList);
            }
        });
        final Button button3 = (Button) dialog.findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
                charSelect = " LIKE ";
                reductionList.add(charSelect);
                setTxt(queryEditText, reductionList);
            }
        });
        final Button button4 = (Button) dialog.findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
                charSelect = ">";
                reductionList.add(charSelect);
                setTxt(queryEditText, reductionList);
            }
        });
        final Button button5 = (Button) dialog.findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
                charSelect = ">=";
                reductionList.add(charSelect);
                setTxt(queryEditText, reductionList);
            }
        });
        final Button button6 = (Button) dialog.findViewById(R.id.button6);
        button6.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
                charSelect = " and ";
                reductionList.add(charSelect);
                setTxt(queryEditText, reductionList);
            }
        });
        final Button button7 = (Button) dialog.findViewById(R.id.button7);
        button7.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
                charSelect = "<";
                reductionList.add(charSelect);
                setTxt(queryEditText, reductionList);
            }
        });
        final Button button8 = (Button) dialog.findViewById(R.id.button8);
        button8.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
                charSelect = "<=";
                reductionList.add(charSelect);
                setTxt(queryEditText, reductionList);
            }
        });

        final Button button9 = (Button) dialog.findViewById(R.id.button9);
        button9.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
                charSelect = " or ";
                reductionList.add(charSelect);
                setTxt(queryEditText, reductionList);
            }
        });

        final Button button10 = (Button) dialog.findViewById(R.id.button10);
        button10.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
                charSelect = " is ";
                reductionList.add(charSelect);
                setTxt(queryEditText, reductionList);
            }
        });

        final Button button11 = (Button) dialog.findViewById(R.id.button11);
        button11.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
                charSelect = " in ";
                reductionList.add(charSelect);
                setTxt(queryEditText, reductionList);
            }
        });

        final Button button12 = (Button) dialog.findViewById(R.id.button12);
        button12.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
                charSelect = " null ";
                reductionList.add(charSelect);
                setTxt(queryEditText, reductionList);
            }
        });

        dialog.show();
        BussUtil.setDialogParams(mContext, dialog, 0.45, 0.5);
        return charSelect;
    }

    /**调整光标位置*/
    private void setTxt(EditText txt, List<String> reductionList) {
        String txtStr = "";
        for (String str : reductionList) {
            txtStr = txtStr + str;
        }
        txt.setText(txtStr);
        CursorUtil.setEditTextLocation(txt);
    }

    /**
     * 获取对应图层对应字段的代码及代码值
     */
    private List<Row> getFieldValueByKey(String pname, Field field) {
        List<Row> list = new ArrayList<Row>();
        if (pname.contains("林地落界")) {
            list = SytemUtil.getAttributeList(mContext,
                    "ld_" + field.getName(), "attribute_ldlj.xml");
            return list;
        } else if (pname.contains("二调")) {
            list = SytemUtil.getAttributeList(mContext,
                    "ed_" + field.getName(), "attribute_ldlj.xml");
            return list;
        } else if (pname.contains("公益林")) {
            list = SytemUtil.getAttributeList(mContext,
                    "gyl_" + field.getName(), "attribute_ldlj.xml");
            return list;
        } else {
            list = BussUtil.getConfigXml(mContext, field.getName(), pname);
        }

        return list;
    }

    private int length = 0;// //////记录字符串被删除字符之前，字符串的长度
    private String tbf = "";
    private String taf = "";
    private String tcf = "";

    /**
     * 绑定并展示查询出所选字段的代码值
     */
    private void showFieldValue(final List<Row> list,final DropdownEdittext txt, final EditText queryTextView) {
        final String[] droplist = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            String dm = list.get(i).getId();
            String name = list.get(i).getName();
            droplist[i] = dm + "-" + name;
        }
        txt.setAdapter(mContext, droplist);

        txt.tv.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                tcf = s.toString();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                tbf = s.toString();
                length = s.length();
            }

            @Override
            public void afterTextChanged(Editable ea) {
                taf = ea.toString();
                if (!taf.equals("") && !taf.contains("-")) {
                    int aa = reductionList.size() % 2;
                    if (aa == 1) {
                        reductionList.remove(reductionList.get(reductionList.size() - 1));
                    }
                    selectValueKey = ea.toString();
                    reductionList.add(selectValueKey);
                    setTxt(queryTextView, reductionList);
                }
            }
        });

        txt.tv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                selectValueKey = list.get(arg2).getId();
                int aa = reductionList.size() % 2;
                if (aa == 1) {
                    reductionList.remove(reductionList.get(reductionList.size() - 1));
                }
                reductionList.add(selectValueKey);
                setTxt(queryTextView, reductionList);
            }
        });
    }

}
