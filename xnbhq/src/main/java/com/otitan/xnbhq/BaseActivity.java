package com.otitan.xnbhq;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Xml;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.esri.android.map.Callout;
import com.esri.android.map.CalloutPopupWindow;
import com.esri.android.map.CalloutStyle;
import com.esri.android.map.FeatureLayer;
import com.esri.android.map.FeatureLayer.SelectionMode;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.GraphicsLayer.RenderingMode;
import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.android.map.TiledLayer;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.map.event.OnZoomListener;
import com.esri.android.runtime.ArcGISRuntime;
import com.esri.core.geodatabase.GeodatabaseFeature;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.FeatureResult;
import com.esri.core.map.FeatureTemplate;
import com.esri.core.map.Field;
import com.esri.core.map.Graphic;
import com.esri.core.renderer.Renderer;
import com.esri.core.symbol.FillSymbol;
import com.esri.core.symbol.LineSymbol;
import com.esri.core.symbol.MarkerSymbol;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.Symbol;
import com.esri.core.table.FeatureTable;
import com.esri.core.table.TableException;
import com.esri.core.tasks.SpatialRelationship;
import com.esri.core.tasks.na.NAFeaturesAsFeature;
import com.esri.core.tasks.na.RouteTask;
import com.esri.core.tasks.na.StopGraphic;
import com.esri.core.tasks.query.Order;
import com.esri.core.tasks.query.QueryParameters;
import com.github.abel533.echarts.Geo;
import com.lling.photopicker.PhotoPickerActivity;
import com.otitan.xnbhq.adapter.FeatureArraysAdapter;
import com.otitan.xnbhq.adapter.FeatureResultAdapter;
import com.otitan.xnbhq.adapter.SetAdapter;
import com.otitan.xnbhq.daoImpl.LocationDaoImpl;
import com.otitan.xnbhq.db.DataBaseHelper;
import com.otitan.xnbhq.dialog.AddAddressDialog;
import com.otitan.xnbhq.dialog.AddnewLayerDialog;
import com.otitan.xnbhq.dialog.CoordinateDialog;
import com.otitan.xnbhq.dialog.EditPhotoDialog;
import com.otitan.xnbhq.dialog.FeatureDelDialog;
import com.otitan.xnbhq.dialog.GpsSetDialog;
import com.otitan.xnbhq.dialog.JjxxsbDialog;
import com.otitan.xnbhq.dialog.LayerSelectDialog;
import com.otitan.xnbhq.dialog.MergeFeatureDialog;
import com.otitan.xnbhq.dialog.RenderSetDialog;
import com.otitan.xnbhq.dialog.ResultAreaDialog;
import com.otitan.xnbhq.dialog.SettingDialog;
import com.otitan.xnbhq.dialog.ShouCangDialog;
import com.otitan.xnbhq.dialog.SpaceStatisticsDialog;
import com.otitan.xnbhq.dialog.UnreportedDialog;
import com.otitan.xnbhq.dialog.UpdataMobileInfoDialog;
import com.otitan.xnbhq.drawTool.DrawEvent;
import com.otitan.xnbhq.drawTool.DrawEventListener;
import com.otitan.xnbhq.drawTool.DrawTool;
import com.otitan.xnbhq.edite.activity.LineEditActivity;
import com.otitan.xnbhq.edite.activity.PointEditActivity;
import com.otitan.xnbhq.edite.activity.XbEditActivity;
import com.otitan.xnbhq.entity.ActionMode;
import com.otitan.xnbhq.entity.Emergency;
import com.otitan.xnbhq.entity.MyFeture;
import com.otitan.xnbhq.entity.MyLayer;
import com.otitan.xnbhq.entity.Row;
import com.otitan.xnbhq.mview.ILayerControlView;
import com.otitan.xnbhq.mview.ILayerView;
import com.otitan.xnbhq.mview.INavigatView;
import com.otitan.xnbhq.mview.IPicView;
import com.otitan.xnbhq.mview.IYzlView;
import com.otitan.xnbhq.presenter.BasePresenter;
import com.otitan.xnbhq.presenter.GpsCollectPresenter;
import com.otitan.xnbhq.presenter.LayerControlPresenter;
import com.otitan.xnbhq.presenter.LayerLablePresenter;
import com.otitan.xnbhq.presenter.NavigationPresenter;
import com.otitan.xnbhq.presenter.RepairPresenter;
import com.otitan.xnbhq.presenter.TrajectoryPresenter;
import com.otitan.xnbhq.presenter.XbqueryPresenter;
import com.otitan.xnbhq.util.ArcGISUtils;
import com.otitan.xnbhq.util.BaseUtil;
import com.otitan.xnbhq.util.BussUtil;
import com.otitan.xnbhq.util.ConverterUtil;
import com.otitan.xnbhq.util.GeometryEngineUtil;
import com.otitan.xnbhq.util.PadUtil;
import com.otitan.xnbhq.util.SpatialUtil;
import com.otitan.xnbhq.util.SymbolUtil;
import com.otitan.xnbhq.util.SytemUtil;
import com.otitan.xnbhq.util.ToastUtil;
import com.otitan.xnbhq.util.Util;
import com.otitan.xnbhq.util.ZoomControlView;
import com.titan.baselibrary.customview.MorePopWindow;
import com.titan.baselibrary.permission.PermissionsActivity;
import com.titan.baselibrary.permission.PermissionsChecker;
import com.titan.baselibrary.util.DialogParamsUtil;
import com.titan.baselibrary.util.ProgressDialogUtil;

import org.osgeo.proj4j.ProjCoordinate;
import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by li on 2016/5/26.
 * activity基类
 */
//@RuntimePermissions //@RuntimePermissions 注释此活动或碎片
public abstract class BaseActivity extends AppCompatActivity implements LayerSelectDialog.SetOnItemClickListener,
        View.OnClickListener, DrawEventListener, IYzlView,IPicView, ILayerControlView, ILayerView, INavigatView {


    public ArrayList<String> picList = new ArrayList<>();
    public JjxxsbDialog getJjxxsbDialog() {
        return jjxxsbDialog;
    }

    public void setJjxxsbDialog(JjxxsbDialog jjxxsbDialog) {
        this.jjxxsbDialog = jjxxsbDialog;
    }

    /*现场事件上报，在图片回调中使用*/
    private JjxxsbDialog jjxxsbDialog = null;
    private Spinner mapScaleSpinner;
    /*系统投影坐标系*/
    public static SpatialReference spatialReference = SpatialUtil.defalutSpatialReference();
    public Context mContext;
    public WifiManager wifiMgr;
    public View childview;
    public static MapView mapView;
    /* 地图服务及地址 */
    public TiledLayer tiledLayer, imgTiledLayer, dxtTiledLayer;
    /* GPS采集工具拦*/
    private Button gpstart, gpspend, gpsstop;
    private boolean gpsstart = false;//是否是首次点击开始按钮
    private boolean gps_start_flag = false;//开始按钮的状态
    private boolean gps_spend_flag = false;//暂停按钮的状态
    private boolean gps_stop_flag = false;//结束按钮的状态
    /*MorePopWindow选择*/
    private MorePopWindow guijiPopwindow, dataPopWindow, dimingMangerPopWindow;
    private MorePopWindow personCenterPopup, kjtjPopup, xbSearchPopup;
    /*轨迹时间选择 */
    //private TimePopupWindow pwTime;
    /* MyTouchListener */
    private MyTouchListener myTouchListener;
    /* 地图服务及地址 */
    public GraphicsLayer graphicsLayer;
    /* 是否首次定位 */
    boolean isFirstLoc = true;
    /* 当前位置经纬度 */
    public static double currentLon = 0.0;// 经度
    public static double currentLat = 0.0;// 纬度
    public double altitude = 0.0;// 海拔
    /* 上一个定位点的经纬度 */
    public double bfPointLon = 0.0;
    public double bfPointLat = 0.0;
    /* 当前位置点 */
    public static Point currentPoint = new Point(0, 0);
    /*当前位置gps点*/
    public Point gpspoint = new Point(0,0);
    /* 导航终点*/
    private Point navstopoint = new Point(0, 0);
    /* 自动定位绘制误差圆及中心点 */
    public static Graphic locationGraphic, circleGraphic;
    public int locationID, circleID;
    public Polygon circlePolygon;
    /* 记录轨迹 */
    public Polyline gjPolyline;
    public Graphic gjGraphic;
    public int gjGraphicID;
    /* 定位中心点样式 */
    public MarkerSymbol locationMarkerSymbol = null;
    /* 样式 */
    public MarkerSymbol markerSymbol;
    public LineSymbol lineSymbol;
    public FillSymbol fillSymbol;
    public FillSymbol selectfillSymbol;
    /* callout */
    public Callout callout;
    public List<CalloutPopupWindow> calloutPopupWindows = new ArrayList<>();
    public DecimalFormat decimalFormat = new DecimalFormat(".000000");
    /* 标绘方式及geometry类型 */
    public static final int EMPTY = 0;
    public static final int POINT = 1;
    public static final int ENVELOPE = 2;
    public static final int POLYLINE = 3;
    public static final int POLYGON = 4;
    public static final int CIRCLE = 5;
    public static final int ELLIPSE = 6;
    public static final int FREEHAND_POLYGON = 7;
    public static final int FREEHAND_POLYLINE = 8;
    public static final int FREEHAND_POLYLINEGPS = 9;
    /* featureLayer图形几何类型 */
    public com.esri.core.geometry.Geometry.Type layerType;
    /* 标识进入系统名称 */
    public String activitytype;
    /*测量面积 */
    public Point startPoint;
    public ActionMode actionMode = ActionMode.MODE_NULL;
    public int graphicID = 0;
    public Graphic drawGraphic;
    public int drawType;
    public boolean active;
    public Point point_all;
    public Envelope envelope_all;
    public Polyline polyline_all;
    public Polygon polygon_all;
    /* 导航 */
    public RouteTask mRouteTask = null;
    public NAFeaturesAsFeature mStops = new NAFeaturesAsFeature();
    public Polyline polyline_nav;
    public EditText nav_start, nav_stop;
    public PictureMarkerSymbol pictureMarkerSymbol;
    /* 图层控制 加载小班数据 */
    public HashMap<String, Boolean> childCheckBox = new HashMap<>();
    public List<String> childKeyList = new ArrayList<>();
    /* 保存加载图层的父级名称 自己目录的图层名称 */
    public static List<MyLayer> layerNameList = new ArrayList<>();
    public static List<MyLayer> polygonLayerList = new ArrayList<>();
    /*选择图层所在MyLayer*/
    public static MyLayer myLayer;
    /* 时间格式化 */
    @SuppressLint("SimpleDateFormat")
    public SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static final int PlAYBACK = 5;
    /* 轨迹回放 */
    public Runnable hfrunnable = null;
    public Handler timer = new Handler();// 定时器
    /*数据所属 */
    public List<HashMap<String, String>> sjssLlist = new ArrayList<>();
    /* 行政区域 */
    public List<HashMap<String, String>> xzqyLlist = new ArrayList<>();
    /* 基地性质 */
    public List<HashMap<String, String>> jdxzLlist = new ArrayList<>();
    /* 选择小班的GeodatabaseFeature */
    public static GeodatabaseFeature selGeoFeature = null;
    /* 选择小班 */
    public static Geometry selectGeometry = null;
    /* 选择图斑的属性信息 */
    public static Map<String, Object> selectFeatureAts = null;
    /*选择图斑的属性字段 */
    public static List<Field> selectfiledList = null;
    /* 选择图斑的集合 */
    public static long[] featureIds = null;
    public static List<GeodatabaseFeature> selGeoFeaturesList = new ArrayList<>();
    public static List<GeodatabaseFeature> getFeaturesList = new ArrayList<>();
    public static List<GeodatabaseFeature> allFeaturesList = new ArrayList<>();
    public static Map<GeodatabaseFeature, String> selMap = new HashMap<>();
    /* 记录操作的小班及操作类型 */
    public ArrayList<Map<String, Object>> undonList = new ArrayList<>();
    /* FeatureLayer图层 */
    public static FeatureTemplate layerTemplate;
    public static Symbol layerSymbol;
    public Renderer layerRenderer;
    public Map<String, Object> layerFeatureAts;
    /*小班查询 */
    //private List<GeodatabaseFeature> list_xbsearch = new ArrayList<>();
    /*back键监听 */
    private long firstTime = 0;
    private static final int ALBUM = 0x000002;
    public static final int TAKE_PHOTO = 0x000001;
    public static final int PICK_PHOTO = 0x000003;
    private String mCurrentPhotoPath = "";// 图片路径
    public static Point touchpoint;
    /*GPS位置监听 */
    private LocationManager locationManager;
    /*显示当前位置控件 */
    private TextView mylocationValue;
    private TextView mapscaleValue;
    /*otms文件夹下各个系统的配置数据*/
    public List<Row> proData = new ArrayList<Row>();
    /*空间统计用到的变量*/
    public DrawTool drawTool;
    /*定位许可*/
    private final int SDK_PERMISSION_REQUEST = 127;
    private String permissionInfo;
    /*百度定位client*/
    public LocationClient mLocClient;
    public MyLocationListenner locationListenner = new MyLocationListenner();
    /*presenter*/
    public BasePresenter basePresenter;
    public RepairPresenter repairPresenter;
    public NavigationPresenter navigationPresenter;
    public GpsCollectPresenter gpsCollectPresenter;
    public XbqueryPresenter xbqueryPresenter;
    public TrajectoryPresenter trajectoryPresenter;
    public LayerControlPresenter layerControlPresenter;
    public LayerLablePresenter lablePresenter;
    /*baseActivity页面include*/
    public View xbqdInclude, tckzInclude, tckzImgInclude, minegjsearchInclude, otherGjSearchInclude, layerLableInclude,
            xbbjInclude, xbSearchZdyInclude, xbSearchJdInclude, gpsCaijiInclude, xdmSearchInclude;
    /*在include内的view但是需要做全局变量的*/
    public TextView sjtbImgview, gjsearchImgview, dmMangerImgview, personCenter, xbbjImgview, tcxrImgview, xbsearchImgview;
    /*小班编辑RadioButton*/
    public RadioButton addFeatureBtn, addFeatureGbBtn, selectButton, qiegeButton, hebingButton, xiubanButton,
            repealButton, attributButton, deleteButton, copyButton;
    /*小班搜索结果展示头部*/
    public View toplayou;
    /*Myapplication*/
    public MyApplication app = new MyApplication();
    /*设置地图最大范围*/
    public static Envelope extent = new Envelope(-20037507.229594, -19971868.880409, 20037507.229594, 19971868.880409);

    // 所需的定位权限，sd读写权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //setContentView(R.unreportdata.activity_base);
        //去除arcgis文字
        //ArcGISRuntimeEnvironment.setLicense("runtimelite,1000,rud8065403504,none,RP5X0H4AH7CLJ9HSX018");
         ArcGISRuntime.setClientId("qwvvlkN4jCDmbEAO");//去除水印的

        /*初始化数据*/
        initData();
        /* 定位设置 */
        initLocation();
		/* 地图控件初始化 */
        initView();
        /*初始化presenter*/
        initPresenter();
		/* 添加地图 */
        addLayer();
        mapviewStatusChange();
		/* 定义样式 */
        initSymbol();
        /*设置五参数*/
        //initGpsSettings();
        /**/
        layerNameList.clear();
        PadUtil.isPad(mContext);
        //featureLayerList.clear();
        //featureLayer = null;


    }

    public abstract View getParentView();

    /**
     * 初始化presenter
     */
    private void initPresenter() {
        basePresenter = new BasePresenter(BaseActivity.this, this);
        xbqueryPresenter = new XbqueryPresenter(mContext, this);
        trajectoryPresenter = new TrajectoryPresenter(BaseActivity.this, this);
        gpsCollectPresenter = new GpsCollectPresenter(mContext, this);
        navigationPresenter = new NavigationPresenter(mContext, this);
        repairPresenter = new RepairPresenter(mContext, this, basePresenter);
        lablePresenter = new LayerLablePresenter(mContext, this);
        layerControlPresenter = new LayerControlPresenter(mContext, this, this);
    }

    /**
     * 初始化数据
     */
    public void initData() {
        mContext = BaseActivity.this;
        System.out.println("JVM内存信息：" + Util.toMemoryInfo());
        ProgressDialogUtil.stopProgressDialog(mContext);
        /* 屏蔽自动弹出输入法 */
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        wifiMgr = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    /**
     * 地图控件初始化
     */
    private void initView() {
        childview = getParentView();
        mapView = (MapView) childview.findViewById(R.id.map);
        /*放大缩小工具*/
        ZoomControlView zoomControlView = (ZoomControlView) childview.findViewById(R.id.zoomcontrolview);
        if (mapView != null && zoomControlView != null) {
            zoomControlView.setMapView(mapView);
        }
        /**include======================================================view*/
        /*小班清单*/
        xbqdInclude = childview.findViewById(R.id.dialog_xbqd_include);
        /*图层控制*/
        tckzInclude = childview.findViewById(R.id.tckz_include);
        /*影像数据图层控制*/
        tckzImgInclude = childview.findViewById(R.id.imgtuceng_include);
        /*自身轨迹查询显示窗口*/
        minegjsearchInclude = childview.findViewById(R.id.guijisearch_include);
        /*其他轨迹查询显示窗口*/
        otherGjSearchInclude = childview.findViewById(R.id.gjserchother_include);
        /*小班编辑工具*/
        xbbjInclude = childview.findViewById(R.id.xbbj_include);
        /*小班自定义查询*/
        xbSearchZdyInclude = childview.findViewById(R.id.xbsearchzdy_include);
        /*小班自简单查询*/
        xbSearchJdInclude = childview.findViewById(R.id.xbsearchjd_include);
        /*小地名搜索*/
        xdmSearchInclude = childview.findViewById(R.id.xdmsearch_include);
        /*GPS采集工具栏*/
        gpsCaijiInclude = childview.findViewById(R.id.gpscaiji_include);
        /*图层标注view*/
        layerLableInclude = childview.findViewById(R.id.shuxinglable_include);
        /*新建图层*/
        TextView addlayer = (TextView) childview.findViewById(R.id.top_addnewlayer);
        addlayer.setOnClickListener(this);
        /**========================================================================*/
        /*图层控制按钮*/
        TextView tckzImgView = (TextView) childview.findViewById(R.id.tckz_imageview);
        tckzImgView.setOnClickListener(this);
        /*数据同步*/
        sjtbImgview = (TextView) childview.findViewById(R.id.shuju_sjtb);
        sjtbImgview.setOnClickListener(this);
        /*小班清单按钮*/
        TextView xbqdImgview = (TextView) childview.findViewById(R.id.share_xbqd);
        xbqdImgview.setOnClickListener(this);
        /*轨迹查询*/
        gjsearchImgview = (TextView) childview.findViewById(R.id.share_gjcx);
        gjsearchImgview.setOnClickListener(this);
        /*坐标定位*/
        TextView zbdwImgview = (TextView) childview.findViewById(R.id.share_zbdw);
        zbdwImgview.setOnClickListener(this);
        /*上报事件*/
        TextView sbsjImgview = childview.findViewById(R.id.share_sbsj);
        sbsjImgview.setOnClickListener(this);
        /*系统设置*/
        TextView settingImgview = (TextView) childview.findViewById(R.id.share_xtsz);
        settingImgview.setOnClickListener(this);
        /*地名管理*/
        dmMangerImgview = (TextView) childview.findViewById(R.id.share_dmgl);
        dmMangerImgview.setOnClickListener(this);
        /*个人中心*/
        personCenter = (TextView) childview.findViewById(R.id.share_grzx);
        personCenter.setOnClickListener(this);
        /*小班编辑*/
        xbbjImgview = (TextView) childview.findViewById(R.id.share_xbbj);
        xbbjImgview.setOnClickListener(this);
        /*小班查询 */
        xbsearchImgview = (TextView) childview.findViewById(R.id.share_xbcx);
        xbsearchImgview.setOnClickListener(this);
        /*小班编辑子按钮*/
        selectButton = (RadioButton) childview.findViewById(R.id.selectButton);
        qiegeButton = (RadioButton) childview.findViewById(R.id.qiegeButton);
        hebingButton = (RadioButton) childview.findViewById(R.id.hebingButton);
        xiubanButton = (RadioButton) childview.findViewById(R.id.xiubanButton);
        repealButton = (RadioButton) childview.findViewById(R.id.repealButton);
        attributButton = (RadioButton) childview.findViewById(R.id.attributButton);
        deleteButton = (RadioButton) childview.findViewById(R.id.deleteButton);
        copyButton = (RadioButton) childview.findViewById(R.id.copyButton);
        addFeatureBtn = (RadioButton) childview.findViewById(R.id.addfeature);
        addFeatureGbBtn = (RadioButton) childview.findViewById(R.id.addfeaturegb);
        /**=====================================================================*/
        /*显示当前坐标view*/
        mylocationValue = (TextView) childview.findViewById(R.id.mylocationValue);
        /*显示当前比例尺view*/
        mapscaleValue = (TextView) childview.findViewById(R.id.mapScaleValue);
        /*空间统计*/
        tcxrImgview = (TextView) childview.findViewById(R.id.share_tcxr);
        tcxrImgview.setOnClickListener(this);
        TextView shuxingLable = (TextView) childview.findViewById(R.id.shuxing_lable);
        shuxingLable.setOnClickListener(this);
        /*gps采集工具栏*/
        gpstart = (Button) childview.findViewById(R.id.share_gps_start);
        gpspend = (Button) childview.findViewById(R.id.share_gps_suspend);
        gpsstop = (Button) childview.findViewById(R.id.share_gps_stop);

        mapScaleSpinner = (Spinner) childview.findViewById(R.id.map_scale);
        initSpinner();
    }

    /**
     * 添加地图
     */
    @SuppressLint("ClickableViewAccessibility")
    public void addLayer() {
		/* 基础底图 */
        String titlePath = MyApplication.resourcesManager.getTitlePath();
        tiledLayer = basePresenter.addTitleLayer("vector");
        /**/
        mapView.setMaxExtent(extent);
		/* 地形图 */
//        String dxtLayerPath = MyApplication.resourcesManager.getLayerPath();
//        dxtTiledLayer = basePresenter.addTitleLayer(dxtLayerPath);
		/* 县乡村边界图 */
//        String xzqhLayerPath = MyApplication.resourcesManager.getArcGISLocalyyLayerPath();
//        basePresenter.addTitleLayer(xzqhLayerPath);
        /*触摸事件*/
        myTouchListener = new MyTouchListener(mContext, mapView);
        mapView.setOnTouchListener(myTouchListener);
		/* 设置地图背景 */
        mapView.setMapBackground(0xffffff, 0xffffff, 3, 3);
        /*添加graphicLayer*/
        graphicsLayer = basePresenter.addGraphicLayer();
        /*地图缩放*/
        mapView.setOnZoomListener(new OnZoomListener() {
            private static final long serialVersionUID = 6225276681409635976L;

            @Override
            public void preAction(float arg0, float arg1, double arg2) {
            }

            @Override
            public void postAction(float arg0, float arg1, double arg2) {
                double scale = mapView.getScale();
                DecimalFormat df = new DecimalFormat("0");
                String txt = "当前比例尺   1:" + df.format(scale);
                mapscaleValue.setText(txt);
            }
        });
    }

    /**
     * mapview 状态变化监听
     */
    public void mapviewStatusChange() {
        mapView.setOnStatusChangedListener(new OnStatusChangedListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void onStatusChanged(Object source, STATUS status) {
                if (source instanceof FeatureLayer) {
                    if (STATUS.LAYER_LOADED == status) {
                        ProgressDialogUtil.stopProgressDialog(mContext);
                        ToastUtil.setToast(mContext, "数据加载成功");
                    } else {
                        ProgressDialogUtil.stopProgressDialog(mContext);
                        ToastUtil.setToast(mContext, "数据加载错误");
                    }
                }
            }
        });
    }

    /**
     * 初始化symbol
     */
    public void initSymbol() {
        pictureMarkerSymbol = new PictureMarkerSymbol(getResources().getDrawable(R.drawable.icon_gcoding));
        pictureMarkerSymbol.setOffsetY(20);
        markerSymbol = new SimpleMarkerSymbol(Color.BLACK, 16, SimpleMarkerSymbol.STYLE.CIRCLE);
        //lineSymbol = new SimpleLineSymbol(Color.argb(100, 248, 116, 14), 5);// 248,116,14
        lineSymbol = new SimpleLineSymbol(Color.RED, 4);// 248,116,14
        fillSymbol = new SimpleFillSymbol(Color.BLACK);
        fillSymbol.setOutline(lineSymbol);
        fillSymbol.setAlpha(50);

        selectfillSymbol = new SimpleFillSymbol(Color.TRANSPARENT);
        selectfillSymbol.setOutline(lineSymbol);
        selectfillSymbol.setAlpha(10);

        LocationDisplayManager ls = mapView.getLocationDisplayManager();
        try {
            locationMarkerSymbol = ls.getDefaultSymbol();
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
    }

    /* 比例尺初始化*/
    public void initSpinner() {
        final String[] arrays = mContext.getResources().getStringArray(R.array.mapscale);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, arrays);
        adapter.setDropDownViewResource(R.layout.myspinner);
        mapScaleSpinner.setAdapter(adapter);
        mapScaleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String scale = arrays[position];
                mapView.setScale(Double.valueOf(scale.split(":")[1]));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    /**实时显示坐标*/
    public void setLocationView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (currentPoint == null || !currentPoint.isValid()) {
                    return;
                }
                String txt = "当前坐标:"
                        + decimalFormat.format(gpspoint.getX()) + ","
                        + decimalFormat.format(gpspoint.getY());
//                        + "\n        经度:"
//                        + decimalFormat.format(gpspoint.getX()) + " 纬度:"
//                        + decimalFormat.format(gpspoint.getY());
                mylocationValue.setText(txt);
            }
        });
    }

    /**
     * 定位的初始化
     */
    public void initLocation() {
        LocationDaoImpl locationDao = new LocationDaoImpl();
        locationDao.initLocation(mContext, mLocClient, locationListenner);
    }

    /**
     * 点击地图获取点坐标
     */
    public void pointInMap(View view) {
        ToastUtil.setToast(mContext, R.string.getmappoint);
        actionMode = ActionMode.MODE_ADD_CALLOUT;
    }

    /**
     * 当前位置手动定位
     */
    public void mylocation(@NonNull View view) {
        initTouch();
        ToastUtil.setToatLeft(this, "自身定位");
        if (currentPoint != null && currentPoint.isValid()) {
            mapRemove(new View(mContext));
            isFirstLoc = true;
            callout.setStyle(R.xml.calloutstyle);
            callout.show(currentPoint, basePresenter.loadCalloutView(currentPoint));
            updataLocation(currentPoint);
        } else {
            ToastUtil.setToast(mContext, "无法获取位置信息");
        }
    }

    /**
     * 清除graphic
     */
    public void clean(View view) {
        initTouch();
        ToastUtil.setToatLeft(mContext, "清除标绘");
        clearGraphic();
        graphicsLayer.removeAll();
        if (graphicsLayer != null && graphicsLayer.isInitialized()) {
            mapView.removeLayer(graphicsLayer);
        }
        mapView.addLayer(graphicsLayer);
        for (int i = 0; i < calloutPopupWindows.size(); i++) {
            calloutPopupWindows.get(i).hide();
        }
        mStops.clearFeatures();
    }

    /**
     * 清除mapview中的graphicLayer图层
     */
    public void clearGraphic() {
        locationGraphic = null;
        circleGraphic = null;
        restory();
        mapView.invalidate();
    }

    /**
     * 测量距离
     */
    public void ceju(View view) {
        initTouch();
        graphicsLayer.removeAll();
        polyline_all = null;
        ToastUtil.setToatLeft(mContext, "测量距离");
        actionMode = ActionMode.MODE_CEJU;
        drawType = POLYLINE;
        activate(drawType);
    }

    /**
     * 测量面积
     */
    public void cemian(View view) {
        graphicsLayer.removeAll();
        initTouch();
        actionMode = ActionMode.MODE_CEMIAN;
        startPoint = null;
        ToastUtil.setToatLeft(mContext, "测量面积");
        basePresenter.initCmPopuwindow();
    }

    /**
     * i键查询
     */
    public void isearche(View view) {
        initTouch();
        ToastUtil.setToatLeft(mContext, "属性查询");
        actionMode = ActionMode.MODE_ISEARCHE;
        int size = layerNameList.size();
        if (size > 0) {
            if (size == 1) {
                basePresenter.getMylayer();
            } else {
                showFeatureLayer(actionMode, layerNameList);
            }
        } else {
            ToastUtil.setToast(mContext, "请加载空间图层");
        }
    }

    /**
     * 小班路径导航
     */
    public void xbnavigation(View view) {
        initTouch();
        mRouteTask = navigationPresenter.initRoutAndGeocoding();
        ToastUtil.setToatLeft(mContext, "小班路径导航");
        actionMode = ActionMode.MODE_XBNAVIGATION;
        polyline_nav = new Polyline();
        deactivate();
        mStops.clearFeatures();
    }

    /**
     * 导航按钮
     */
    public void navigation(View view) {
        initTouch();
        mRouteTask = navigationPresenter.initRoutAndGeocoding();
        ToastUtil.setToatLeft(mContext, "路径导航");
        actionMode = ActionMode.MODE_NAVIGATION;
        polyline_nav = new Polyline();
        deactivate();
        mStops.clearFeatures();

        final View naview = childview.findViewById(R.id.navigation_include);
        nav_start = (EditText) naview.findViewById(R.id.navigation_start);
        if (currentPoint != null && currentPoint.isValid()) {
            nav_start.setText("我的位置");
            polyline_nav.startPath(currentPoint);
            PictureMarkerSymbol picSymbol = new PictureMarkerSymbol(getResources().getDrawable(R.drawable.nav_route_result_start_point));
            picSymbol.setOffsetY(18);
            Graphic graphic = new Graphic(currentPoint, picSymbol);
            graphicsLayer.addGraphic(graphic);
            StopGraphic stop = new StopGraphic(graphic);
            mStops.addFeature(stop);
        }
        nav_stop = (EditText) naview.findViewById(R.id.navigation_stop);

        ImageView close = (ImageView) naview.findViewById(R.id.navigation_close);
        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                naview.setVisibility(View.GONE);
                graphicsLayer.removeAll();
            }
        });

    }

    /**
     * gpstart gps采集工具栏开始按钮
     */
    public void gpstart(View view) {
        int type = gpsCollectPresenter.getCollection_type();
        if (type == 0) {
            if ((!gps_start_flag && !gps_stop_flag) || (gps_start_flag && gps_stop_flag)) {
                gpsCollectPresenter.copyGjDb();
            }
            gps_start_flag = true;
            gps_spend_flag = false;
            if (gps_stop_flag) {
                gps_stop_flag = false;
            }
            gpstart.setEnabled(false);
            gpspend.setEnabled(true);
            gpsstop.setEnabled(true);
            return;
        }
        int size = layerNameList.size();
        if (size == 0) {
            ToastUtil.setToast(mContext, "未加载空间数据,请在图层控制中加载数据");
            return;
        }

        gps_start_flag = true;
        if (gps_spend_flag) {
            //不需要选择图层
            gpstart.setEnabled(false);
            gpspend.setEnabled(true);
            gps_spend_flag = false;
        } else if (gps_stop_flag) {
            //点击结束按钮后
            gpsstart = false;
            gps_stop_flag = false;
            actionMode = ActionMode.MODE_EDIT_ADD;
            getFeatureLayer(actionMode);

        } else {
            actionMode = ActionMode.MODE_EDIT_ADD;
            getFeatureLayer(actionMode);
        }
//        if ((!gps_start_flag && !gps_stop_flag) || (gps_start_flag && gps_stop_flag)) {
//            actionMode = ActionMode.MODE_EDIT_ADD;
//            getFeatureLayer(actionMode);
//        }

    }

    /**
     * gpsuspend gps采集工具栏暂停按钮
     */
    public void gpsuspend(View view) {
        if (gpsCollectPresenter != null) {
            if (gpsCollectPresenter.collection_type == 0) {
                gpstart.setEnabled(true);
                gpspend.setEnabled(false);
                gpsstop.setEnabled(true);
                gps_spend_flag = true;
                return;
            }
        }

        ToastUtil.setToast(mContext, "暂停记录轨迹");
        gpstart.setEnabled(true);
        gpstart.setVisibility(View.VISIBLE);
        gpspend.setEnabled(false);
        gpsstop.setEnabled(true);
        gps_spend_flag = true;
    }

    /**
     * gpstop gps采集工具栏结束按钮
     */
    public void gpstop(View view) {
        if (gpsCollectPresenter != null) {
            if (gpsCollectPresenter.collection_type == 0) {
                ToastUtil.setToast(mContext, "结束记录轨迹");
                gpstart.setEnabled(true);
                gpspend.setEnabled(false);
                gpsstop.setEnabled(false);
                gps_stop_flag = true;
                gps_start_flag = false;
                gpsstart = false;
                return;
            }
        }
        ToastUtil.setToast(mContext, "结束记录轨迹,并保存图斑");
        gpstart.setEnabled(true);
        gpstart.setVisibility(View.VISIBLE);
        gpspend.setEnabled(false);
        gpsstop.setEnabled(false);
        gps_stop_flag = true;
        gps_start_flag = false;
        gpsstart = false;

        if (polyline_all == null) {
            ToastUtil.setToast(mContext, "轨迹数据不存在");
            return;
        }
        int size = polyline_all.getPointCount();
        if (size > 2) {
            addGPSFeature();
        } else {
            ToastUtil.setToast(mContext, "轨迹数据不符合构建面规则");
            return;
        }
    }

    /**
     * 异步类
     */
    class MyAsyncTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(final String... params) {
            if (params[0].equals("uplonlat")) {
                /* 保存GPS坐标数据 */
                basePresenter.addGuijiPoint(currentPoint);

            } else if (params[0].equals("sjss")) {

                basePresenter.uploadsjssData();

            } else if (params[0].equals("xzqy")) {

                basePresenter.uploadxzqyData();

            } else if (params[0].equals("jdxz")) {

                basePresenter.uploadjdxzData();
            }
            return null;
        }
    }

    /**
     * 当前位置刷新
     */
    public void updataLocation(Point currentPoint) {
        if (currentPoint == null || !currentPoint.isValid()) {
            return;
        }

        if (currentPoint.isValid() && graphicsLayer != null) {
            graphicsLayer.updateGraphic(locationID, currentPoint);
            graphicsLayer.updateGraphic(circleID, circlePolygon);
        }

        if (isFirstLoc) {
            double scale = mapView.getScale();
            if (3000 < scale) {
                mapView.setScale(3000);
            }
            mapView.centerAt(currentPoint, true);
            mapView.invalidate();
            isFirstLoc = false;
        }
    }

    /**
     * 定位监听
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (dxtTiledLayer != null && dxtTiledLayer.isInitialized() && wifiMgr.isWifiEnabled()) {
                //NetUtil.openOrcloseWifi(BaseActivity.this, wifiMgr);
            }
            if (location == null)
                return;
            if (location.getLongitude() == 4.9E-324 && location.getLatitude() == 4.9E-324)
                return;
            if (location.getLongitude() == 1.0 && location.getLatitude() == 1.0)
                return;

            if (graphicsLayer == null) {
                graphicsLayer = new GraphicsLayer(RenderingMode.STATIC);
                mapView.addLayer(graphicsLayer);
            }

            currentPoint = getGPSpoint(location);
            //currentPoint = new Point(667279.435, 2956758.767);

            if (gps_start_flag && currentPoint != null && currentPoint.isValid()) {
                if (gpsCollectPresenter.collection_type == 0) {
                    if (gps_stop_flag) {
                        return;
                    }
                    if (gps_spend_flag) {
                        return;
                    }
                    gpsCollectPresenter.addGjdateTodb(currentPoint);
                } else if (gpsCollectPresenter.collection_type == 1) {
                    ////采集路线形成图斑
                    addPointToLine(currentPoint);
                }
            }

            initGraphicLocation();
            //更新位置
            updataLocation(currentPoint);
            //展示行进路线
            showTravelLine(currentPoint);

            //addPointToLine(currentPoint);

            if (bfPointLon != currentLon || bfPointLat != currentLat) {
                double distance = BussUtil.distance(bfPointLat, bfPointLon, currentLat, currentLon);
                if (distance > 1) {
					/* 上传坐标到后台 */
                    new MyAsyncTask().execute("uplonlat");
                }
                setLocationView();
                bfPointLat = currentLat;
                bfPointLon = currentLon;
            }
        }
    }

    /**
     * 获取位置坐标 GPS定位 网络定位 百度定位
     */
    private Point getGPSpoint(BDLocation blocation) {
        Location location = null;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }else{
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        if (location == null) {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        /* 判别是取的gps坐标还是百度坐标 true 为百度坐标 */
        boolean dwflag = false;
        double lon = 0.0,lat = 0.0;// 经度
        if (location != null) {
            //GPS坐标
            lat = location.getLatitude(); // 纬度 26.567741305680546
            lon = location.getLongitude(); // 经度 106.68937683886078
            altitude = location.getAltitude();// 1040.8563754250627
            dwflag = false;
        } else {
            //百度坐标
            lat = blocation.getLatitude(); // 纬度
            lon = blocation.getLongitude(); // 经度
            altitude = blocation.getAltitude();
            dwflag = true;
        }

        Point point = getPoint(lon, lat, altitude,dwflag);
        if ((point != null || point.isValid()) && !point.isEmpty()) {
            currentLon = point.getX();
            currentLat = point.getY();
        }
        return point;
    }

    /**
     * gps采集路线
     */
    public void addPointToLine(Point point) {
        if (polyline_all == null) {
            return;
        }
        if (gps_stop_flag) {
            return;
        }
        if (gps_spend_flag) {
            return;
        }

        if (!gpsstart) {
            gpsstart = true;
            polyline_all.startPath(point);
        } else {
            polyline_all.lineTo(point);
        }

        if (graphicsLayer != null) {
            graphicsLayer.updateGraphic(graphicID, polyline_all);
            mapView.invalidate();
        }

    }

    /**
     * 百度点坐标转换为西安80坐标
     */
    public Point getPoint(double longitude, double latitude, double altitude,boolean dwflag) {
        if (dwflag) {
            Point point = ConverterUtil.gps2gCoordinate(longitude, latitude);
            Point gpsPoint = ConverterUtil.g2bCoordinate(point.getX(), point.getY());
            // wgs1984的坐标
            longitude = 2 * longitude - gpsPoint.getX();
            latitude = 2 * latitude - gpsPoint.getY();
        }

        gpspoint = new Point(longitude,latitude);

        Point point;
        if(!spatialReference.isWGS84()){
            point = GeometryEngineUtil.getPoint3857(gpspoint);
        }else{
            point = gpspoint;
        }ProjCoordinate coordinate = basePresenter.meth(longitude, latitude, altitude);
        //return new Point(coordinate.x, coordinate.y);
        return point;
    }

    /**
     * 生成定位的graphic
     */
    public void initGraphicLocation() {

        Graphic locationGraphic = graphicsLayer.getGraphic(locationID);
        if (locationGraphic == null) {
            if (locationMarkerSymbol == null) {
                locationMarkerSymbol = new PictureMarkerSymbol(getResources().getDrawable(R.drawable.mylocation_dw));
            }
            locationGraphic = new Graphic(currentPoint, locationMarkerSymbol);
            locationID = graphicsLayer.addGraphic(locationGraphic);
        } else {
            graphicsLayer.updateGraphic(locationID, currentPoint);
        }
        Graphic circleGraphic = graphicsLayer.getGraphic(circleID);
        Point point = (Point)GeometryEngine.project(currentPoint,SpatialUtil.defalutSpatialReference(),SpatialUtil.wgs_2343());
        Polygon circle = GeometryEngine.buffer(point, SpatialUtil.wgs_2343(), 20, null);
        circlePolygon = GeometryEngineUtil.getPolygon2343(circle);
        if (circleGraphic == null) {
            FillSymbol symbol = SymbolUtil.DroolCircle(Color.BLUE);
            circleGraphic = new Graphic(circlePolygon, symbol);
            circleID = graphicsLayer.addGraphic(circleGraphic);
        } else {
            graphicsLayer.updateGraphic(circleID, circlePolygon);
        }

    }

    private class MyTouchListener extends MapOnTouchListener {
        MapView map;

        private MyTouchListener(Context context, MapView view) {
            super(context, view);
            map = view;
            callout = map.getCallout();
            callout.setStyle(R.xml.call);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Point point = map.toMapPoint(event.getX(), event.getY());
            if (point == null || point.isEmpty() || !point.isValid()) {
                return false;
            }

            if (active
                    && (drawType == POINT || drawType == ENVELOPE
                    || drawType == CIRCLE
                    || drawType == FREEHAND_POLYLINE || drawType == FREEHAND_POLYGON)
                    && event.getAction() == MotionEvent.ACTION_DOWN) {

                switch (drawType) {
                    case POINT:
                        point.setXY(point.getX(), point.getY());
                        break;
                    case ENVELOPE:
                        startPoint = point;
                        envelope_all.setCoords(point.getX(), point.getY(),point.getX(), point.getY());
                        break;
                    case CIRCLE:
                        startPoint = point;
                        break;
                    case FREEHAND_POLYGON:
                        polygon_all.startPath(point);
                        break;
                    case FREEHAND_POLYLINE:
                        if (polyline_all == null) {
                            return false;
                        }
                        int size = polyline_all.getPointCount();
                        if (size == 0) {
                            polyline_all.startPath(point);
                        } else {
                            Polyline polyline = polyline_all;
                            double d1 = polyline.calculateLength2D();
                            polyline.lineTo(point);
                            double d2 = polyline.calculateLength2D();
                            double d = d2 - d1;
                            if (d > 6) {
                                polyline_all.lineTo(point);
                            }
                        }
                        break;
                }

                if (actionMode == ActionMode.MODE_SELECT) {
                    return true;
                } else if (actionMode == ActionMode.MODE_XIUBAN) {
                    return true;
                } else if (actionMode == ActionMode.MODE_QIEGE) {
                    return true;
                } else if (actionMode == ActionMode.MODE_EDIT_ADD_GB) {
                    return true;
                } else if (actionMode == ActionMode.MODE_EDIT_ADD && drawType == FREEHAND_POLYLINE) {
                    return true;
                }

            } else if (event.getAction() == MotionEvent.ACTION_UP) {

                if (layerType != null && actionMode == ActionMode.MODE_EDIT_ADD
                        && drawType == FREEHAND_POLYLINE && layerType.equals(Geometry.Type.POLYGON)) {
                    if (polyline_all == null) {
                        return false;
                    }

                    int size = polyline_all.getPointCount();
                    if (size >= 3) {
                        polygon_all = ArcGISUtils.LineToPolygon(mContext, polyline_all, mapView);
                        graphicsLayer.updateGraphic(graphicID, polygon_all);
                        mapView.invalidate();
                        int size1 = polygon_all.getPointCount();
                        if (size1 >= 3) {
                            saveFeature(polygon_all);
                        } else {
                            ToastUtil.setToast(mContext, "勾绘有问题，请重新画");
                        }

                    } else {
                        polyline_all = null;
                    }
                    return true;
                }

                if (layerType != null && actionMode == ActionMode.MODE_EDIT_ADD
                        && drawType == FREEHAND_POLYLINE && layerType.equals(Geometry.Type.POLYLINE)) {
                    graphicsLayer.updateGraphic(graphicID, polygon_all);
                    mapView.invalidate();
                    saveFeature(polygon_all);
                    return true;
                }

                if (layerType != null && actionMode == ActionMode.MODE_EDIT_ADD_GB
                        && drawType == FREEHAND_POLYGON && layerType.equals(Geometry.Type.POLYGON)) {
                    graphicsLayer.updateGraphic(graphicID, polygon_all);
                    mapView.invalidate();
                    saveFeature(polygon_all);
                    return true;
                }

                if (actionMode == ActionMode.MODE_SELECT) {
                    if (layerNameList.size() > 0) {
                        getGeometryInfo(envelope_all);
                    }
                    return true;
                }
                if (actionMode == ActionMode.MODE_CEMIAN && drawType == FREEHAND_POLYLINE) {
                    int size = polyline_all.getPointCount();
                    if (size >= 3) {
                        graphicsLayer.removeGraphic(graphicID);
                        Polygon polygon = ArcGISUtils.LineToPolygon(mContext, polyline_all, mapView);
                        Graphic graphic = new Graphic(polygon, fillSymbol);
                        graphicsLayer.addGraphic(graphic);
                        double area = Math.abs(polygon.calculateArea2D());
                        DroolCeMian(polygon, area);
                    }
                    ToastUtil.setToast(mContext, "测量结束");
                    restory();
                    return true;
                }

                if (actionMode == ActionMode.MODE_CEMIAN && drawType == FREEHAND_POLYGON) {
                    double area = Math.abs(polygon_all.calculateArea2D());
                    if (area > 0) {
                        DroolCeMian(polygon_all, area);
                        graphicsLayer.updateGraphic(graphicID, polygon_all);
                    }
                    ToastUtil.setToast(mContext, "测量结束");
                    restory();
                    return true;
                }

                if (drawType == FREEHAND_POLYLINE) {
                    if (actionMode == ActionMode.MODE_XIUBAN || actionMode == ActionMode.MODE_QIEGE) {
                        saveFeature(polygon_all);
                        return true;
                    }
                }
            }
            return super.onTouch(v, event);
        }

        @Override
        public boolean onDragPointerMove(MotionEvent from, MotionEvent to) {

            Point point = map.toMapPoint(to.getX(), to.getY());
            if (point == null || point.isEmpty()) {
                return false;
            }

            if (active
                    && (drawType == ENVELOPE || drawType == FREEHAND_POLYGON
                    || drawType == POLYLINE
                    || drawType == FREEHAND_POLYLINE || drawType == CIRCLE)) {
                switch (drawType) {
                    case ENVELOPE:
                        if (startPoint == null || startPoint.isEmpty()) {
                            return false;
                        }
                        envelope_all
                                .setXMin(startPoint.getX() > point.getX() ? point
                                        .getX() : startPoint.getX());
                        envelope_all
                                .setYMin(startPoint.getY() > point.getY() ? point
                                        .getY() : startPoint.getY());
                        envelope_all
                                .setXMax(startPoint.getX() < point.getX() ? point
                                        .getX() : startPoint.getX());
                        envelope_all
                                .setYMax(startPoint.getY() < point.getY() ? point
                                        .getY() : startPoint.getY());
                        if (graphicsLayer != null) {
                            graphicsLayer.updateGraphic(graphicID,
                                    envelope_all.copy());
                        }
                        break;
                    case FREEHAND_POLYGON:
                        polygon_all.lineTo(point);
                        if (graphicsLayer != null) {
                            graphicsLayer.updateGraphic(graphicID, polygon_all);
                        }
                        break;
                    case FREEHAND_POLYLINE:
                        Polyline polyline = polyline_all;
                        double d1 = polyline.calculateLength2D();
                        polyline.lineTo(point);
                        double d2 = polyline.calculateLength2D();
                        double d = d2 - d1;
                        if (d < 6) {
                            polyline_all.removePoint(polyline_all.getPointCount() - 1);
                        }
                        if (graphicsLayer != null) {
                            graphicsLayer.updateGraphic(graphicID, polyline_all);
                        }
                        break;
                }

                return true;
            }
            return super.onDragPointerMove(from, to);
        }

        @Override
        public boolean onDragPointerUp(MotionEvent from, MotionEvent to) {

            Point point = map.toMapPoint(to.getX(), to.getY());
            if (point == null || point.isEmpty()) {
                return false;
            }

            if (active
                    && (drawType == ENVELOPE || drawType == FREEHAND_POLYGON
                    || drawType == POLYLINE
                    || drawType == FREEHAND_POLYLINE || drawType == CIRCLE)) {
                switch (drawType) {
                    case ENVELOPE:
                        if (startPoint == null || startPoint.isEmpty()) {
                            return false;
                        }
                        envelope_all.setXMin(startPoint.getX() > point.getX() ? point.getX() : startPoint.getX());
                        envelope_all.setYMin(startPoint.getY() > point.getY() ? point.getY() : startPoint.getY());
                        envelope_all.setXMax(startPoint.getX() < point.getX() ? point.getX() : startPoint.getX());
                        envelope_all.setYMax(startPoint.getY() < point.getY() ? point.getY() : startPoint.getY());
                        if (graphicsLayer != null) {
                            graphicsLayer.updateGraphic(graphicID,envelope_all.copy());
                        }
                        break;
                    case FREEHAND_POLYGON:
                        polygon_all.lineTo(point);
                        if (graphicsLayer != null) {
                            graphicsLayer.updateGraphic(graphicID, polygon_all);
                        }
                        break;
                    case FREEHAND_POLYLINE:
                        Polyline polyline = polyline_all;
                        if (polyline == null || !polyline.isValid()) {
                            return false;
                        }
                        double d1 = polyline.calculateLength2D();
                        polyline.lineTo(point);
                        double d2 = polyline.calculateLength2D();
                        double d = d2 - d1;
                        if (d > 6) {
                            polyline_all.lineTo(point);
                            if (graphicsLayer != null) {
                                graphicsLayer.updateGraphic(graphicID, polyline_all);
                            }
                        }

                        break;
                }
                active = false;
            }
            return super.onDragPointerUp(from, to);
        }

        @Override
        public boolean onSingleTap(MotionEvent e) {
            Point mapPoint = map.toMapPoint(e.getX(), e.getY());
            if (mapPoint == null || mapPoint.isEmpty()) {
                return false;
            }
            if (actionMode == ActionMode.MODE_QIEGE && drawType == POLYLINE) {
                // 切割小班
                if (startPoint == null) {
                    startPoint = mapPoint;
                    polyline_all.startPath(mapPoint);
                } else {
                    polyline_all.lineTo(mapPoint);
                }
                graphicsLayer.updateGraphic(graphicID, drawGraphic);
            } else if (actionMode == ActionMode.MODE_CEMIAN && drawType == POLYGON) {
                // 面积测量
                if (startPoint == null) {
                    startPoint = mapPoint;
                    polygon_all.startPath(mapPoint);
                } else
                    polygon_all.lineTo(mapPoint);
                DroolCeMian(mapPoint, polygon_all);
                graphicsLayer.updateGraphic(graphicID, polygon_all);
            } else if (actionMode == ActionMode.MODE_CEJU && drawType == POLYLINE) {
                // 距离测量
                if (startPoint == null) {
                    startPoint = mapPoint;
                    polyline_all.startPath(mapPoint);
                } else {
                    polyline_all.lineTo(mapPoint);
                }
                DroolCeJu(mapPoint, polyline_all);
                graphicsLayer.updateGraphic(graphicID, polyline_all);


            } else if (actionMode == ActionMode.MODE_NAVIGATION) {
                // 路径导航
                callout.setStyle(R.xml.calloutstyle);
                callout.show(mapPoint, loadNavigationView(mapPoint));
            } else if (actionMode == ActionMode.MODE_XBNAVIGATION) {
                getGeometryInfo(mapPoint);
            } else if (actionMode == ActionMode.MODE_ISEARCHE) {
                // i建查詢
                if (myLayer != null && layerType != null) {
                    xbSearchZdyInclude.setVisibility(View.GONE);
                    xbSearchJdInclude.setVisibility(View.GONE);
                    getFeatureInfo(mapPoint, myLayer.getLayer());
                    //getGeometryInfo(mapPoint);
                }
            } else if (actionMode == ActionMode.MODE_ADD_ADDRESS) {
                // 小地名添加
                Graphic graphic = new Graphic(mapPoint, pictureMarkerSymbol);
                int id = graphicsLayer.addGraphic(graphic);
                showAddXdmView(mapPoint, id);
            } else if (actionMode == ActionMode.MODE_EDIT_ADD && layerType != null) {
				/* 古树名木添加 */
                if (layerType.equals(Geometry.Type.POINT)) {
                    point_all = mapPoint;
                    saveFeature(new Polygon());
                }
            } else if (actionMode == ActionMode.MODE_ADD_CALLOUT) {
                //点击地图获取地图点坐标数据
                addCalloutPoint(map, mapPoint);
            }
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Point mapPoint = map.toMapPoint(e.getX(), e.getY());
            if (mapPoint == null || mapPoint.isEmpty()) {
                return false;
            }
            //路径导航
//			if (actionMode == ActionMode.MODE_NAVIGATION) {
//				drawLineToMap(mapPoint, polyline_nav);
//				actionMode = ActionMode.MODE_NULL;
//				polyline_nav = null;
//				return true;
//			}

            if (actionMode == ActionMode.MODE_CEJU) {
                polyline_all.lineTo(mapPoint);
                DroolCeJu(mapPoint, polyline_all);
                graphicsLayer.updateGraphic(graphicID, polyline_all);
                ToastUtil.setToast(mContext, "测量结束");
                restory();
                return false;
            }

            if (actionMode == ActionMode.MODE_CEMIAN) {
                if (drawType == POLYGON) {
                    polygon_all.lineTo(mapPoint);
                    DroolCeMian(mapPoint, polygon_all);
                    graphicsLayer.updateGraphic(graphicID, polygon_all);
                    ToastUtil.setToast(mContext, "测量结束");
                    restory();
                }
                return false;
            }
            return super.onDoubleTap(e);
        }

//		@Override
//		public void onLongPress(MotionEvent e) {
//			Point mappoint = map.toMapPoint(e.getX(), e.getY());
//			if (mappoint == null || mappoint.isEmpty()) {
//				return;
//			}
//			// Point point = (Point)
//			// GeometryEngine.project(mappoint,spatialReference,SpatialReference.create(4326));
//			XmlPullParser parser = getResources().getXml(
//					R.xml.calloutpopuwindowstyle);
//
//			CalloutStyle style = new CalloutStyle(mContext,
//					Xml.asAttributeSet(parser));
//			View view = loadCalloutPopuwindow(mappoint);
//			CalloutPopupWindow calloutP = new CalloutPopupWindow(view,
//					CalloutPopupWindow.MODE.OVERFLOW, style);
//			calloutP.showCallout(map, mappoint, 0, 0);
//			calloutPopupWindows.add(calloutP);
//			BussUtil.closeCalloutPopu(view, calloutP);
//		}

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share_sbsj:
                //事件上报
                showSbsjDialog();
                break;
            case R.id.top_addnewlayer:
                addNewLayers();
                break;
            case R.id.share_xbqd:
                /*小班清单*/
                getXbList();
                break;
            case R.id.share_tcxr:
                /*图层渲染*/
                showLayerRenderSystem();
                //showPopupKjtj();
                break;
            case R.id.share_xbcx:
                /*小班查询*/
                xbsearchSelect();
                break;
            case R.id.share_xbbj:
                /* 空间数据编辑 */
                if (xbbjInclude.getVisibility() == View.VISIBLE) {
                    xbbjInclude.setVisibility(View.GONE);
                } else {
                    xbbjInclude.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.share_grzx:
                /*个人中心 */
                takephotoAndedit();
                break;
            case R.id.popup_mobile_info:
                /* 个人中心 下拉 设备信息 */
                personCenterPopup.dismiss();
                initMobileInfoView();
                break;
            case R.id.share_dmgl:
                /* 地名搜素 */
                dimingMangerPopWindow = new MorePopWindow(this, R.layout.popup_share_addressmanger);
                dimingMangerPopWindow.showPopupWindow(dimingMangerPopWindow,dmMangerImgview);

                View dmsearch = dimingMangerPopWindow.getContentView().findViewById(R.id.xiaodimingsearch);
                dmsearch.setOnClickListener(this);
                View dmAdd = dimingMangerPopWindow.getContentView().findViewById(R.id.xiaodimingadd);
                dmAdd.setOnClickListener(this);

                break;
            case R.id.xiaodimingsearch:
                /* 地名搜素 - 小地名查询 */
                dimingMangerPopWindow.dismiss();
                xdmSearchInclude.setVisibility(View.VISIBLE);
                basePresenter.initXdmView(xdmSearchInclude);
                break;
            case R.id.xiaodimingadd:
                /* 地名搜素--小地名添加 */
                dimingMangerPopWindow.dismiss();
                actionMode = ActionMode.MODE_ADD_ADDRESS;
                break;
            case R.id.share_xtsz:
                /*系统设置*/
                showDialogSettings();
                //gpsCollectPresenter.showGPSCollectType();
                break;
            case R.id.tckz_imageview:
                /* 图层控制*/
                tckzInclude.setVisibility(View.VISIBLE);
                layerControlPresenter.initOtmsData();
                break;
            case R.id.share_gjcx:
                /* 轨迹查询 */
                //guijiPopwindow = new MorePopWindow(this, R.unreportdata.popup_share_gujichaxun);
                //guijiPopwindow.showPopupWindow(gjsearchImgview);
                //guijiPopwindow.getContentView().findViewById(R.id.mine_guiji).setOnClickListener(this);
                //guijiPopwindow.getContentView().findViewById(R.id.show_guijizs).setOnClickListener(this);
                if(minegjsearchInclude.getVisibility() == View.VISIBLE){
                    minegjsearchInclude.setVisibility(View.GONE);
                }else{
                    minegjsearchInclude.setVisibility(View.VISIBLE);
                }
                trajectoryPresenter.initMyTrajectorySearch(minegjsearchInclude);

                break;
            case R.id.mine_guiji:
                /* 轨迹查询--自身轨迹查询 */
                //guijiPopwindow.dismiss();
               // minegjsearchInclude.setVisibility(View.VISIBLE);
                //trajectoryPresenter.initMyTrajectorySearch(minegjsearchInclude);
                break;
            case R.id.show_guijizs:
                /* 轨迹查询--轨迹展示 */
                //guijiPopwindow.dismiss();
                //initGuijiSearchView();
                break;
            case R.id.tile_extent:
                /* 定位至基础地图所在范围 */
                zoomTitleLayer();
                break;
            case R.id.image_extent:
                /* 定位至影像图所在范围 */
                layerControlPresenter.zoomImageLayer();
                break;
            case R.id.dxt_extent:
                /* 定位至地形图所在范围 */
                zoomDxtTitleLayer();
                break;
            case R.id.shuju_sjtb:
                /* 数据同步 */
                dataPopWindow = new MorePopWindow(this, R.layout.popup_shujutongbu);
                dataPopWindow.showPopupWindow(dataPopWindow,sjtbImgview);
                break;
            case R.id.share_zbdw:
			    /* 坐标定位 */
                showZbdwDialog();
                break;
            case R.id.pop_photo_takephoto:
			    /* 拍照 */
                mCurrentPhotoPath = basePresenter.takephoto();
                break;
            case R.id.pop_photo_fromalum:
			    /* 从相册选取 */
                basePresenter.fromAlum();
                break;
            case R.id.pop_mycollect:
			    /* 我的收藏 */
                myCollect();
                break;
            case R.id.pop_xbsear_jiandan:
			    /*简单查询*/
                xbSearchPopup.dismiss();
                if (layerNameList.size() == 0) {//featureLayerList.size() == 0
                    ToastUtil.setToast(mContext, "请先加载空间数据");
                } else if (layerNameList.size() > 0) {
                    if (layerNameList.size() == 1) {
                        basePresenter.getMylayer();
                        xbqueryPresenter.showSearchXBjd(xbSearchJdInclude);
                    } else {
                        showFeatureLayer(ActionMode.MODE_XBSEARCHJD, layerNameList);
                    }
                }
                break;
            case R.id.pop_xbsear_zidy:
			    /*自定义查询*/
                xbSearchPopup.dismiss();
                if (layerNameList.size() == 0) {//featureLayerList.size() == 0
                    ToastUtil.setToast(mContext, "请先加载空间数据");
                } else if (layerNameList.size() > 0) {
                    if (layerNameList.size() == 1) {
                        basePresenter.getMylayer();
                        xbqueryPresenter.showSearchXiaoZDY(xbSearchZdyInclude);
                    } else {
                        showFeatureLayer(ActionMode.MODE_XBSEARCHZDY, layerNameList);
                    }
                }
                break;
            case R.id.shuxing_lable:
                /*属性标注*/
                showFeatureLayer(ActionMode.MODE_ADD_LABLE,layerNameList);
                break;
            default:
                break;
        }

    }

    /**
     * 显示我的收藏点
     */
    private void myCollect() {
        personCenterPopup.dismiss();
        ShouCangDialog dialog = new ShouCangDialog(mContext, graphicsLayer,
                pictureMarkerSymbol, mapView, ShouCangDialog.SqlType.SELECT);
        BussUtil.setDialogParams(mContext, dialog, 0.7, 1);
    }

    /**
     * 空间统计
     */
    public void showDialogKjtj() {
        if (myLayer == null) {
            ToastUtil.setToast(mContext, "未加载小班数据,请在图层控制中加载数据");
            return;
        }
        SpaceStatisticsDialog statisticsDialog = new SpaceStatisticsDialog(mContext, R.style.Dialog, drawTool);
        BussUtil.setDialogParams(mContext, statisticsDialog, 0.45, 0.6);
    }

    /**上报事件*/
    public void showSbsjDialog(){
        List<Emergency> list = DataBaseHelper.getUnResportData(mContext);
        if(list.size() > 0){
            UnreportedDialog unreportedDialog = new UnreportedDialog(mContext);
            BussUtil.setDialogParams(mContext, unreportedDialog, 0.5, 0.8);
        }else{
            JjxxsbDialog jjxxsbDialog = new JjxxsbDialog(BaseActivity.this,R.style.Dialog,this);
            WindowManager.LayoutParams params = BussUtil.setDialogParams(mContext, jjxxsbDialog, 0.5, 0.8);
            setJjxxsbDialog(jjxxsbDialog);
            jjxxsbDialog.setDialogParams(params);
        }
    }

    /**
     * 拍照及编辑
     */
    private void takephotoAndedit() {
        personCenterPopup = new MorePopWindow(this, R.layout.popup_takephoto);
        personCenterPopup.showPopupWindow(personCenterPopup,personCenter);

        View takephoto = personCenterPopup.getContentView().findViewById(R.id.pop_photo_takephoto);
        takephoto.setOnClickListener(this);

        View fromalum = personCenterPopup.getContentView().findViewById(R.id.pop_photo_fromalum);
        fromalum.setOnClickListener(this);

        View mycollect = personCenterPopup.getContentView().findViewById(R.id.pop_mycollect);
        mycollect.setOnClickListener(this);

        View mInfo = personCenterPopup.getContentView().findViewById(R.id.popup_mobile_info);
        mInfo.setOnClickListener(this);
    }

    /**
     * 小班查询选择  简单查询和自定义查询
     */
    public void xbsearchSelect() {
        xbSearchPopup = new MorePopWindow(this, R.layout.popup_xbsearch_select);
        xbSearchPopup.showPopupWindow(xbSearchPopup,xbsearchImgview);

        //简单查询
        View jdsearch = xbSearchPopup.getContentView().findViewById(R.id.pop_xbsear_jiandan);
        jdsearch.setOnClickListener(this);
        //自定义查询
        View zdysearch = xbSearchPopup.getContentView().findViewById(R.id.pop_xbsear_zidy);
        zdysearch.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO:// 拍照
                if (resultCode == RESULT_OK) {
                    EditPhotoDialog dialog = new EditPhotoDialog(mContext,
                            R.style.Dialog, mCurrentPhotoPath, currentPoint);
                    dialog.show();
                    personCenterPopup.dismiss();
                }
                break;

            case PICK_PHOTO://现场信息上报选择图片后
                if(data != null){
                    //图片选择成功
                    picList.clear();
                    picList = data.getStringArrayListExtra(PhotoPickerActivity.KEY_RESULT);
                    jjxxsbDialog.loadPhoto();
                }

                break;
            case ALBUM:// 从相册选取
                if (resultCode == RESULT_OK && null != data) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumns = {MediaStore.Images.Media.DATA};
                    Cursor c = this.getContentResolver().query(selectedImage,
                            filePathColumns, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePathColumns[0]);
                    String picturePath = c.getString(columnIndex);
                    c.close();
                    EditPhotoDialog dialog = new EditPhotoDialog(mContext,
                            R.style.Dialog, picturePath, currentPoint);
                    dialog.show();
                    personCenterPopup.dismiss();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 选择小班
     */
    public void selectFeature(View view) {
        initTouch();
        if (selGeoFeaturesList.size() > 0) {
            selGeoFeaturesList.clear();
        }
        for (final MyLayer layer : layerNameList) {
            if (layer != null && layer.getLayer().isInitialized()) {
                layer.getLayer().clearSelection();
            }
        }
        actionMode = ActionMode.MODE_SELECT;
        drawType = ENVELOPE;
        activate(drawType);
    }

    /**
     * 新增小班
     */
    public void addFeature(View view) {
        initTouch();
        if (polyline_all != null) {
            graphicsLayer.updateGraphic(graphicID, polyline_all);
            mapView.invalidate();
        }
        drawType = FREEHAND_POLYLINE;
        actionMode = ActionMode.MODE_EDIT_ADD;
        getFeatureLayer(actionMode);
        if (layerNameList.size() == 0) {
            addFeatureBtn.setChecked(false);
        }
    }

    /**
     * 新增共边小班
     */
    public void addFeatureGb(View view) {
        initTouch();
        int size = selGeoFeaturesList.size();
        if (size == 0) {
            ToastUtil.setToast(mContext, "请先选中有公共边小班");
            addFeatureGbBtn.setChecked(false);
            return;
        } else if (size > 1) {
            boolean flag = BaseUtil.getIntance(mContext).checkGeoFeature(selGeoFeaturesList);
            if (!flag) {
                ToastUtil.setToast(mContext, "选中的小班不属于同一个图层");
                addFeatureGbBtn.setChecked(false);
                return;
            }
        }
        actionMode = ActionMode.MODE_EDIT_ADD_GB;
        getFeatureLayer(actionMode);
        if (layerNameList.size() == 0) {
            addFeatureGbBtn.setChecked(false);
        }
    }

    /**
     * 复制小班
     */
    public void copyFeature(View view) {
        initTouch();
        int size = selGeoFeaturesList.size();
        if (size == 0) {
            copyButton.setChecked(false);
            ToastUtil.setToast(mContext, "请先选中某个图斑");
            return;
        }
        actionMode = ActionMode.MODE_EDIT_COPY;
        getFeatureLayer(actionMode);
    }

    /**
     * 选择featureLayer
     */
    public void getFeatureLayer(ActionMode mode) {
        //clear();
        int size = layerNameList.size();
        if (size == 0) {
            ToastUtil.setToast(mContext, "未加载空间数据,请在图层控制中加载数据");
            return;
        }
        if (size > 0) {
            showFeatureLayer(mode, layerNameList);
            return;
        }
    }

    /**
     * 删除小班
     */
    public void deleteFeature(View view) {
        initTouch();
        int size = selGeoFeaturesList.size();
        if (size == 1) {
            selGeoFeature = selGeoFeaturesList.get(0);
            selectGeometry = selGeoFeature.getGeometry();
            selectFeatureAts = selGeoFeature.getAttributes();
            long id = selGeoFeature.getId();
            showDeleteFeatureDialog(id, 0, null, null, null);
        } else if (size > 1) {
            showDelFeatureResult(selGeoFeaturesList);
        } else {
            deleteButton.setChecked(false);
            ToastUtil.setToast(mContext, "未选中小班");
        }
    }

    /**
     * 撤销
     */
    public void repealFeature(View view) {
        initTouch();
        clear();
        actionMode = ActionMode.MODE_NULL;
        int size = undonList.size();
        if (size == 0) {
            repealButton.setChecked(false);
            ToastUtil.setToast(mContext, "无法回退");
            return;
        }
        for (int i = size - 1; i >= 0; i--) {
            Map<String, Object> map = undonList.get(i);
            if (map.get("type").equals("add")) {
                FeatureLayer layer = (FeatureLayer) map.get("layer");
                long id = Long.parseLong(map.get("id").toString());
                try {
                    layer.getFeatureTable().deleteFeature(id);
                    undonList.remove(undonList.get(i));
                    break;
                } catch (TableException e) {
                    e.printStackTrace();
                }
            } else if (map.get("type").equals("delete")) {
                FeatureLayer layer = (FeatureLayer) map.get("layer");
                int id = Integer.parseInt(map.get("id").toString());
                Geometry geometry = (Geometry) map.get("geometry");
                Symbol symbol = (Symbol) map.get("symbol");
                Map<String, Object> attributes = (Map<String, Object>) map.get("attribute");
                attributes.put("OBJECTID",id);
                Graphic graphic = new Graphic(geometry, symbol, attributes);
                try {
                    GeodatabaseFeatureTable table = (GeodatabaseFeatureTable) layer.getFeatureTable();
                    GeodatabaseFeature feature = new GeodatabaseFeature(attributes,geometry,table);
                    long fid = table.addFeature(feature);
                } catch (TableException e) {
                    e.printStackTrace();
                }
                undonList.remove(undonList.get(i));
                break;
            } else if (map.get("type").equals("update")) {
                FeatureLayer layer = (FeatureLayer) map.get("layer");
                long id = Long.parseLong(map.get("id").toString());
                Geometry geometry = (Geometry) map.get("geometry");
                Symbol symbol = (Symbol) map.get("symbol");
                Graphic graphic = new Graphic(geometry, symbol, (int) id);
                try {
                    layer.getFeatureTable().updateFeature(id, graphic);
                    undonList.remove(undonList.get(i));
                    break;
                } catch (TableException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 地图复位
     */
    public void mapRemove(View view) {
        initTouch();
        clearGraphic();
        actionMode = ActionMode.MODE_NULL;
        drawType = EMPTY;
        activate(drawType);
        mapView.invalidate();
    }

    /**
     * 分割小班
     */
    public void qiegeFeature(View view) {
        initTouch();
        int size = selGeoFeaturesList.size();
        if (size == 0) {
            qiegeButton.setChecked(false);
            ToastUtil.setToast(mContext, "请选中一个图斑进行分割操作");
            return;
        }
        if (size > 1) {
            selectButton.setChecked(true);
            ToastUtil.setToast(mContext, "只能对一个小班进行分割操作");
            return;
        }
        GeodatabaseFeature feature = selGeoFeaturesList.get(0);
        if (feature.getGeometry().getType() != Geometry.Type.POLYGON) {
            ToastUtil.setToast(mContext, "所选图斑不是面图形");
            return;
        }
        actionMode = ActionMode.MODE_QIEGE;
        getSelParams(selGeoFeaturesList, 0,selMap);

        if (selectGeometry != null && selectGeometry.getType().equals(Geometry.Type.POLYGON)) {
            drawType = FREEHAND_POLYLINE;
            activate(drawType);
        } else {
            if (selectGeometry == null) {
                ToastUtil.setToast(mContext, "未选择切割图形");
                return;
            }
            if (!selectGeometry.getType().equals(Geometry.Type.POLYGON)) {
                ToastUtil.setToast(mContext, "所选图形不符合切割规则");
            }
        }

    }

    /**
     * 合并小班
     */
    public void mergeFeature(View view) {
        initTouch();
        if (selGeoFeaturesList == null || selGeoFeaturesList.size() < 2) {
            if (selGeoFeaturesList.size() == 0) {
                hebingButton.setChecked(false);
            } else {
                selectButton.setChecked(true);
            }
            ToastUtil.setToast(mContext, "请选择两个及以上图斑进行合并");
            return;
        }

        showHeBing(selGeoFeaturesList, view);

    }

    /**
     * 修斑
     */
    public void xiubFeature(View view) {
        initTouch();
        int size = selGeoFeaturesList.size();
        if (size == 0) {
            xiubanButton.setChecked(false);
            ToastUtil.setToast(mContext, "请选中一个小班面进行修斑操作");
            return;
        }
        if (size > 1) {
            selectButton.setChecked(true);
            ToastUtil.setToast(mContext, "只能对一个小班面进行修斑操作");
            return;
        }
        GeodatabaseFeature feature = selGeoFeaturesList.get(0);
        if (feature.getGeometry().getType() != Geometry.Type.POLYGON) {
            ToastUtil.setToast(mContext, "所选图斑不是面图形");
            return;
        }
        actionMode = ActionMode.MODE_XIUBAN;
        getSelParams(selGeoFeaturesList, 0,selMap);
        drawType = FREEHAND_POLYLINE;
        activate(drawType);
    }

    /**
     * 属性查询
     */
    public void attributeFeture(View view) {
        initTouch();
        List<GeodatabaseFeature> data = new ArrayList<GeodatabaseFeature>();
        data.addAll(selGeoFeaturesList);
        int size = data.size();
        if (size > 0) {
            if (actionMode == ActionMode.MODE_XBQD && getFeaturesList.size() > 0) {
                toFeatureResult(getFeaturesList);
            } else if (size == 1) {
                toFeatureResult(data);
            } else {
                basePresenter.showListFeatureResult(data);
            }
        } else {
            attributButton.setChecked(false);
            ToastUtil.setToast(mContext, "未任何选中图斑");
        }
    }

    /**
     * 小班保存
     */
    public void saveFeature(Polygon polygon_all) {
        if (actionMode == ActionMode.MODE_EDIT_ADD) {
            // 新增分点线面
            if (drawType == POINT) {
                Map<String, Object> map = basePresenter.addFeaturePoint(point_all);
                undonList.add(map);
            } else if (drawType == FREEHAND_POLYGON) {//
                Map<String, Object> map = basePresenter.addFeaturePolygon(polygon_all);
                undonList.add(map);
            } else if (drawType == FREEHAND_POLYLINE && myLayer.getLayer().getGeometryType().equals(Geometry.Type.POLYGON)) {
                Map<String, Object> map = basePresenter.addFeaturePolygon(polygon_all);
                undonList.add(map);
            } else if (drawType == FREEHAND_POLYLINE && myLayer.getLayer().getGeometryType().equals(Geometry.Type.POLYLINE)) {
                Map<String, Object> map = basePresenter.addFeatureLine(polyline_all);
                undonList.add(map);
            }
        } else if (actionMode == ActionMode.MODE_QIEGE) {
            saveQgFeature(polyline_all, selectGeometry);
            basePresenter.recordXb(selGeoFeature.getId(), "update", selGeoFeature.getAttributes(), selGeoFeature.getGeometry(), myLayer.getLayer());
        } else if (actionMode == ActionMode.MODE_XIUBAN) {
            saveXbFeature(polyline_all);
            basePresenter.recordXb(selGeoFeature.getId(), "update", selGeoFeature.getAttributes(), selGeoFeature.getGeometry(), myLayer.getLayer());
        } else if (actionMode == ActionMode.MODE_EDIT_ADD_GB) {
            Map<String, Object> map = addFeatureGbMian();
            undonList.add(map);
        }

        polygon_all = null;
        polyline_all = null;
        point_all = null;
        clear();
        mapView.invalidate();
        if (actionMode == ActionMode.MODE_XIUBAN) {
            xiubFeature(new View(mContext));
        }

        if (actionMode == ActionMode.MODE_QIEGE) {
            qiegeFeature(new View(mContext));
        }

    }

    /**
     * 退出小班编辑
     */
    public void exitedit(View view) {
    }

    /**
     * 切割面保存方法
     */
    public void saveQgFeature(Polyline line, Geometry selgeom) {
        if (line == null || !line.isValid())
            return;
		/* 判断两个图形是否有相交部分 */
        boolean flag = GeometryEngine.intersects(selgeom, line, spatialReference);
		/* flag为true时线段与所选小班有交互 反之无交互 */
        if (!flag) {
            ToastUtil.setToast(mContext, "两个图斑无交集");
            return;
        }
        clear();
		/* 获取到线与面相交的部分 */
        Polyline cutGeom = (Polyline) GeometryEngine.intersect(selgeom, line, spatialReference);
        if (cutGeom == null || cutGeom.isEmpty() || !cutGeom.isValid()) {
            return;
        }
		/* 判断交线是否自相交 */
        int line_size = cutGeom.getPointCount();
        Point startCut = cutGeom.getPoint(0);
        Point endCut = cutGeom.getPoint(line_size - 1);
        boolean sflag = GeometryEngine.touches(startCut, selgeom, spatialReference);
        boolean eflag = GeometryEngine.touches(endCut, selgeom, spatialReference);
        boolean zxjflag = ArcGISUtils.isIntersect(cutGeom, mapView);

        if (zxjflag) {
			/* 线段自交 */
			/* 判断首位交点是否都是在选择geometry面内,如果有一个不在面内则不符合切割规则 */
            ToastUtil.setToast(mContext, "线段自交,封闭切割");

            Polygon polygon = ArcGISUtils.LineToPolygon(mContext, line, mapView);
            Geometry difGeometry = GeometryEngine.difference(selgeom, polygon, spatialReference);

            Geometry geometry = GeometryEngine.difference(selgeom, difGeometry, spatialReference);

            basePresenter.updateFeature(difGeometry, selGeoFeature, myLayer);

            basePresenter.addFeatureOnLayer(geometry, selectFeatureAts);

        } else {
			/* 线段不自交 */
			/* 判断首位交点是否都是在选择geometry面内,如果有一个不在面内则不符合切割规则 */
            if (!(sflag && eflag)) {
                ToastUtil.setToast(mContext, "不满足图斑切割规则");
                return;
            }
            Polygon curpolygon = (Polygon) selgeom;
            int psize = curpolygon.getPointCount();
            int s_s_index = 0, s_e_index = 0, e_e_index = 0, e_s_index = 0;
            for (int i = 0; i < psize - 1; i++) {
                Polyline lineTemp = new Polyline();
                Point point1 = curpolygon.getPoint(i);
                lineTemp.startPath(point1);
                Point point2 = null;
                if (i == psize) {
                    point2 = curpolygon.getPoint(0);
                } else {
                    point2 = curpolygon.getPoint(i + 1);
                }
                lineTemp.lineTo(point2);

                boolean intersect_s = GeometryEngine.intersects(startCut,lineTemp, spatialReference);
                if (intersect_s) {
                    s_s_index = i;
                    s_e_index = i + 1;
                    break;
                }
            }

            for (int i = 0; i < psize - 1; i++) {
                Polyline lineTemp = new Polyline();
                Point point1 = curpolygon.getPoint(i);
                lineTemp.startPath(point1);
                Point point2 = null;
                if (i == psize) {
                    point2 = curpolygon.getPoint(0);
                } else {
                    point2 = curpolygon.getPoint(i + 1);
                }
                lineTemp.lineTo(point2);

                boolean intersect_s = GeometryEngine.intersects(endCut,lineTemp, spatialReference);
                if (intersect_s) {
                    e_s_index = i;
                    e_e_index = i + 1;
                    break;
                }
            }
			/* 以线的起点为新Geometry的起点 获取线的右侧的面 */
            Polygon yPolygon = new Polygon();
            for (int i = 0; i < line_size; i++) {
                if (i == 0) {
                    yPolygon.startPath(cutGeom.getPoint(i));
                } else {
                    yPolygon.lineTo(cutGeom.getPoint(i));
                }
            }

            if (e_s_index > s_e_index) {
                for (int i = e_s_index; i >= s_e_index; i--) {
                    yPolygon.lineTo(curpolygon.getPoint(i));
                }
            } else if (s_s_index > e_e_index) {
                for (int i = e_e_index; i <= s_s_index; i++) {
                    yPolygon.lineTo(curpolygon.getPoint(i));
                }
            }

            Geometry diff = GeometryEngine.difference(selgeom, yPolygon, spatialReference);
            Geometry geometry = GeometryEngine.difference(selgeom, diff, spatialReference);
            basePresenter.updateFeature(diff, selGeoFeature, myLayer);

            basePresenter.addFeatureOnLayer(geometry, selectFeatureAts);
        }
    }

    /**
     * 新增共边面
     */
    public Map<String, Object> addFeatureGbMian() {
        if (polygon_all == null) {
            ToastUtil.setToast(mContext, "请先勾绘线条");
            return new HashMap<>();
        }
        Geometry geom = GeometryEngine.simplify(polygon_all, spatialReference);
        Map<String, Object> map = basePresenter.addFeatureGbmOnLayer(geom, selGeoFeaturesList.get(0).getAttributes());
        mapRemove(new View(mContext));
        return map;
    }

    /**
     * 添加GPS跑动轨迹形成的面或者线
     */
    public void addGPSFeature() {
        if (myLayer == null) {
            ToastUtil.setToast(mContext, "未选择图层");
            return;
        }
        if (layerType == null) {
            ToastUtil.setToast(mContext, "未选择图层");
            return;
        }
        if (polyline_all == null) {
            return;
        }

        if (layerType.equals(Geometry.Type.POLYLINE) || layerType.equals(Geometry.Type.LINE)) {
            basePresenter.addFeatureLine(polyline_all);
        } else if (layerType.equals(Geometry.Type.POLYGON)) {
            Polygon polygon = new Polygon();
            int size = polyline_all.getPointCount();
            polygon.startPath(polyline_all.getPoint(0));
            for (int i = 1; i < size; i++) {
                polygon.lineTo(polyline_all.getPoint(i));
            }
            polygon_all = polygon;
            if (polygon.isEmpty() || !polygon.isValid()) {
                ToastUtil.setToast(mContext, "GPS轨迹数据不符合构建面图形规则");
                return;
            }
            basePresenter.addFeaturePolygon(polygon);
            polyline_all = null;
        }
    }

    /**
     * 初始化设备修改信息窗口
     */
    public void initMobileInfoView() {
        UpdataMobileInfoDialog infoDialog = new UpdataMobileInfoDialog(mContext, R.style.Dialog);
        BussUtil.setDialogParams(mContext, infoDialog, 0.5, 0.8);
    }

    /**
     * 初始化系统设置dialog窗口
     */
    public void showDialogSettings() {
        SettingDialog settingDialog = new SettingDialog(BaseActivity.this, R.style.Dialog, this,gpsCollectPresenter);
        BussUtil.setDialogParams(mContext, settingDialog, 0.5, 0.8);
    }

    /**
     * GPS五参数设置
     */
    private void initGpsSettings() {
        GpsSetDialog setDialog = new GpsSetDialog(mContext, R.style.Dialog);
        BussUtil.setDialogParams(mContext, setDialog, 0.5, 0.5);
    }

    /**
     * 其他设备轨迹查看
     */
    public void initGuijiSearchView() {
        // 还未完成
        otherGjSearchInclude.setVisibility(View.VISIBLE);
        ImageView imageView = (ImageView) otherGjSearchInclude.findViewById(R.id.viewguji_close);
        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                otherGjSearchInclude.setVisibility(View.GONE);
            }
        });
        //ExpandableListView expandableListView = (ExpandableListView) other_guiji.findViewById(R.id.viewguiji_expand);
    }

    /**
     * 创建一个半径为5m的圆
     */
    public Polygon creatPolygon(Point point) {
        return GeometryEngine.buffer(point, spatialReference, 5, null);
    }

    /**
     * 坐标定位弹出窗口
     */
    public void showZbdwDialog() {
        CoordinateDialog coordinateDialog = new CoordinateDialog(mContext, R.style.Dialog, this,basePresenter);
        BussUtil.setDialogParams(mContext, coordinateDialog, 0.6, 1);
    }

    /**
     * 缩放至基础图层所在位置
     */
    public void zoomTitleLayer() {
        runOnUiThread(new Runnable() {
            public void run() {
                if (tiledLayer != null && tiledLayer.isVisible()) {
                    mapView.setExtent(tiledLayer.getFullExtent());
                    mapView.invalidate();
                } else {
                    ToastUtil.setToast(mContext, "基础图未加载,请检查地图文件是否存在");
                }
            }
        });
    }

    /**
     * 缩放至地形图层所在位置
     */
    public void zoomDxtTitleLayer() {
        runOnUiThread(new Runnable() {
            public void run() {
                if (dxtTiledLayer != null && dxtTiledLayer.isVisible()) {
                    mapView.setExtent(dxtTiledLayer.getFullExtent());
                    mapView.invalidate();
                } else {
                    ToastUtil.setToast(mContext, "地形图图未加载,请在图层控制中加载数据");
                }
            }
        });
    }

    /**加载otms文件夹下数据*/
    public void initOtmsChild(List<File> groups, List<Map<String, List<File>>> childs,int gPosition, int cPosition) {
        String parentName = groups.get(gPosition).getName();
        String childName = childs.get(gPosition).get(parentName).get(cPosition).getName().split("\\.")[0];
        String path = childs.get(gPosition).get(parentName).get(cPosition).getPath();

        boolean ischeck = childCheckBox.get(path);
        layerControlPresenter.changeCBoxStatus(ischeck, path, parentName, childName);
    }

    /**缩放至图元所在位置*/
    public void ZoomToGeom(Geometry geometry) {
        if(geometry.getType() == Geometry.Type.POINT){
            return;
        }
        mapView.setExtent(geometry);
        mapView.invalidate();
    }

    /**
     * 路径导航时点击地图显示选择项
     */
    public View loadNavigationView(final Point mapPoint) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.navigation_callout, null);

        TextView startv = (TextView) view.findViewById(R.id.navigation_startp);
        startv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                callout.hide();
                graphicsLayer.removeAll();
                mStops.clearFeatures();
                polyline_nav = new Polyline();
                PictureMarkerSymbol picSymbol = new PictureMarkerSymbol(getResources().getDrawable(R.drawable.nav_route_result_start_point));
                picSymbol.setOffsetY(18);
                Graphic graphic = new Graphic(mapPoint, picSymbol);
                graphicsLayer.addGraphic(graphic);
                nav_start.setText("起点");
                polyline_nav.startPath(mapPoint);
                StopGraphic stop = new StopGraphic(graphic);
                mStops.addFeature(stop);
            }
        });
        TextView halfway = (TextView) view.findViewById(R.id.navigation_hpway);
        halfway.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                callout.hide();
                if (mStops.getFeatures().size() == 0 && currentPoint != null && currentPoint.isValid()) {
                    graphicsLayer.removeAll();
                    polyline_nav.startPath(currentPoint);
                    nav_start.setText("我的位置");
                    Drawable drawable = getResources().getDrawable(R.drawable.nav_route_result_start_point);
                    PictureMarkerSymbol picSymbol = new PictureMarkerSymbol(drawable);
                    picSymbol.setOffsetY(18);
                    Graphic graphic = new Graphic(currentPoint, picSymbol);
                    graphicsLayer.addGraphic(graphic);
                }
                Graphic graphic = new Graphic(mapPoint, pictureMarkerSymbol);
                graphicsLayer.addGraphic(graphic);
                StopGraphic stop = new StopGraphic(graphic);
                mStops.addFeature(stop);
            }
        });
        TextView stopv = (TextView) view.findViewById(R.id.navigation_stopp);
        stopv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                callout.hide();
                if (mStops.getFeatures().size() == 0 && currentPoint != null && currentPoint.isValid()) {
                    graphicsLayer.removeAll();
                    Drawable drawable = getResources().getDrawable(R.drawable.nav_route_result_start_point);
                    PictureMarkerSymbol picSymbol = new PictureMarkerSymbol(drawable);
                    picSymbol.setOffsetY(18);
                    Graphic graphic1 = new Graphic(currentPoint, picSymbol);
                    graphicsLayer.addGraphic(graphic1);
                    polyline_nav.startPath(currentPoint);
                    nav_start.setText("我的位置");
                } else {
                    nav_stop.setText("终点");
                }

                navigationPresenter.drawLineToMap(mapPoint, polyline_nav);
                actionMode = ActionMode.MODE_NULL;
                polyline_nav = null;
            }
        });

        return view;
    }

    /**
     * 测距点击地图时显示距离和关闭按钮
     */
    public View loadCalloutPoint(Polyline polyline) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.callout, null);

        ImageView close = (ImageView) view.findViewById(R.id.img_delete);
        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                drawType = POLYLINE;
                activate(drawType);
                callout.hide();
            }
        });

        DecimalFormat df = new DecimalFormat("0.00");
        double dd = polyline.calculateLength2D();

        TextView txt = (TextView) view.findViewById(R.id.showDistence);
        String value = df.format(dd) + "米";
        txt.setText(value);
        return view;
    }

    /**
     * 侧面时进行绘制操作
     */
    public void DroolCeMian(final Point point, final Polygon polygon) {
        runOnUiThread(new Runnable() {
            public void run() {
                ArcGISUtils.DroolMian(mContext, MyApplication.screen, graphicsLayer, point, polygon);
            }
        });
    }

    /**
     * 侧面时进行绘制操作
     */
    public void DroolCeMian(final Polygon polygon, final double area) {
        runOnUiThread(new Runnable() {
            public void run() {
                ArcGISUtils.DroolMian(mContext, MyApplication.screen, graphicsLayer, polygon, area);
            }
        });
    }

    /**
     * 测距时绘制
     */
    public void DroolCeJu(final Point point, final Polyline polyline) {
        runOnUiThread(new Runnable() {
            public void run() {
                int size1 = polyline.getPointCount();
                ArcGISUtils.DroolLine(mContext, MyApplication.screen, graphicsLayer, point, polyline);
                int size = polyline.getPointCount();
                if (size > 1) {
                    callout.show(point, loadCalloutPoint(polyline));
                }
            }
        });
    }

    /**数据初始化*/
    public void restory() {
        this.active = false;
        this.drawType = -1;
        this.point_all = null;
        this.envelope_all = null;
        this.polygon_all = null;
        this.polyline_all = null;
        this.drawGraphic = null;
        startPoint = null;
        actionMode = ActionMode.MODE_NULL;
    }

    /**清除标绘*/
    public void clear() {
        if (graphicsLayer != null) {
            //graphicsLayer.removeAll();
            locationGraphic = null;
            circleGraphic = null;
        }
    }

    /**勾绘数据清空*/
    public void deactivate() {
        clear();
        this.active = false;
        this.drawType = -1;
        this.point_all = null;
        this.envelope_all = null;
        this.polygon_all = null;
        this.polyline_all = null;
        this.drawGraphic = null;
        this.startPoint = null;
    }

    /**初始化勾绘数据*/
    public void activate(int drawType) {
        if (mapView == null)
            return;
        if (graphicsLayer != null && graphicID != 0) {
            graphicsLayer.removeAll();
            graphicID = 0;
        }
        this.deactivate();

        this.drawType = drawType;
        this.active = true;
        switch (this.drawType) {
            case POINT:
                this.point_all = new Point();
                drawGraphic = new Graphic(this.point_all, this.markerSymbol);
                break;
            case ENVELOPE:
                this.envelope_all = new Envelope();
                if (actionMode == ActionMode.MODE_SELECT) {
                    drawGraphic = new Graphic(this.envelope_all, this.selectfillSymbol);
                } else {
                    drawGraphic = new Graphic(this.envelope_all, this.fillSymbol);
                }
                break;
            case POLYGON:
            case CIRCLE:
            case FREEHAND_POLYGON:
                this.polygon_all = new Polygon();
                drawGraphic = new Graphic(this.polygon_all, this.fillSymbol);
                break;
            case FREEHAND_POLYLINEGPS:
                this.polyline_all = new Polyline();
                drawGraphic = new Graphic(this.polyline_all, this.lineSymbol);
                break;
            case POLYLINE:
            case FREEHAND_POLYLINE:
                this.polyline_all = new Polyline();
                drawGraphic = new Graphic(this.polyline_all, this.lineSymbol);
                break;
        }

        if (graphicsLayer != null) {
            graphicID = this.graphicsLayer.addGraphic(drawGraphic);
        }

    }

    /**
     * 小班数据查询
     */
    public void getFeatureInfo(final Geometry geometry, FeatureLayer layer) {

        QueryParameters queryParams = new QueryParameters();
        queryParams.setOutFields(new String[]{"*"});
        queryParams.setSpatialRelationship(SpatialRelationship.INTERSECTS);
        queryParams.setGeometry(geometry);
        queryParams.setReturnGeometry(true);
        queryParams.setOutSpatialReference(spatialReference);
        Map<String, Order> orderByFields = new HashMap<>();
        orderByFields.put("OBJECTID",Order.ASC);
        queryParams.setOrderByFields(orderByFields);
        layer.selectFeatures(queryParams, SelectionMode.NEW,
                new CallbackListener<FeatureResult>() {
                    @Override
                    public void onError(Throwable arg0) {
                        ToastUtil.setToast(mContext, "查询出错");
                    }

                    @Override
                    public void onCallback(final FeatureResult featureResult) {

                        new Thread(new Runnable() {
                            public void run() {
                                if (featureResult.featureCount() > 0) {
                                    showFeaturInfo(geometry, featureResult);
                                }
                            }
                        }).start();
                    }
                });

    }

    /**显示小班查询数据*/
    private void showFeaturInfo(Geometry geometry, FeatureResult result) {
        Iterator<Object> iterator = result.iterator();
        GeodatabaseFeature feature = null;
        while (iterator.hasNext()) {
            feature = (GeodatabaseFeature) iterator.next();
            Map<String, Object> attributes = feature.getAttributes();
            final List<Field> fieldList = feature.getTable().getFields();
            View views = basePresenter.loadAttributeView(fieldList, attributes);
            Point point = (Point) geometry;
            callout.setStyle(R.xml.calloutstyle);
            callout.show(point, views);
        }
    }

    /**
     * 编辑图层选择窗口， 选择要编辑的图层
     */
    public void showFeatureLayer(ActionMode mode, List<MyLayer> layerList) {//
        if(layerList.size() == 0){
            ToastUtil.setToast(mContext,"请先加载图层");
            return;
        }else if(layerList.size() == 1){
            actionMode = mode;
            myLayer = layerNameList.get(0);
            layerType = myLayer.getLayer().getGeometryType();
            String layername = myLayer.getLname();

            if (layername.contains("设计") && (mode != ActionMode.MODE_XBSEARCHZDY && mode != ActionMode.MODE_XBSEARCHJD)) {
                ToastUtil.setToast(mContext, "你选择的小班为设计小班,不可编辑请重新选择编辑图层");
                return;
            }
            SytemUtil.getEditSymbo(BaseActivity.this, myLayer.getLayer());
            selectMethed(mode);
        }else{
            actionMode = mode;
            LayerSelectDialog selectDialog = new LayerSelectDialog(mContext,R.style.Dialog,layerList,mode);
            selectDialog.setLayerOnItemClickListener(this);
            BussUtil.setDialogParams(mContext, selectDialog, 0.5, 0.5);
        }
    }

    /**
     * 添加点数据时 提示是否使用当前点坐标
     */
    public void showAddCurrentPoint(final int drawType, final Point point) {
        AlertDialog.Builder builder = new Builder(mContext);
        builder.setMessage("使用当前点!");
        builder.setTitle("信息提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                point_all = point;
                saveFeature(new Polygon());
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                ToastUtil.setToast(mContext, "请手动点击地图添加点");
                activate(drawType);
            }
        });
        builder.create().show();
    }

    /**
     * 导航添加导航点
     */
    public void addFeatureNavi(Point point) {
        Graphic graphic = new Graphic(point, null);
        StopGraphic stop = new StopGraphic(graphic);
        mStops.addFeature(stop);
    }

    /**
     * 弹出添加小地名窗口
     */
    public void showAddXdmView(Point mappoint, int id) {
        AddAddressDialog addressDialog = new AddAddressDialog(mContext,R.style.Dialog,mappoint,id,this);
        BussUtil.setDialogParams(mContext, addressDialog, 0.5, 0.6);
    }

    /**
     * 选择小班查询方法
     */
    public void showGeometryInfo(Geometry geometry, FeatureLayer layer, final ActionMode mode) {
        QueryParameters queryParams = new QueryParameters();
        queryParams.setOutFields(new String[]{"*"});
        queryParams.setSpatialRelationship(SpatialRelationship.INTERSECTS);
        queryParams.setGeometry(geometry);
        queryParams.setReturnGeometry(true);
        queryParams.setOutSpatialReference(spatialReference);
        Map<String, Order> orderByFields = new HashMap<>();
        orderByFields.put("OBJECTID",Order.ASC);
        queryParams.setOrderByFields(orderByFields);
        layer.selectFeatures(queryParams, SelectionMode.NEW,
                new CallbackListener<FeatureResult>() {

                    @Override
                    public void onError(Throwable arg0) {
                        ToastUtil.setToast(mContext, "查询出错");
                    }

                    @Override
                    public void onCallback(FeatureResult featureResult) {
                        if (featureResult.featureCount() == 0) {
                            ToastUtil.setToast(mContext, "没有选中任何图斑");
                            return;
                        }
                        final List<GeodatabaseFeature> list = new ArrayList<GeodatabaseFeature>();
                        Iterator<Object> iterator = featureResult.iterator();
                        GeodatabaseFeature geodatabaseFeature = null;
                        while (iterator.hasNext()) {
                            geodatabaseFeature = (GeodatabaseFeature) iterator.next();
                            list.add(geodatabaseFeature);
                        }
                        graphicsLayer.removeAll();
                        runOnUiThread(new Runnable() {
                            public void run() {
                                if (mode == ActionMode.MODE_DBCEMJ) {
                                    showResultMj(list);
                                }
                            }
                        });
                    }
                });
    }

    /**
     * 当查询结果只有一个时直接跳转到属性页面
     */
    public void toFeatureResult(List<GeodatabaseFeature> list) {
        if (graphicsLayer != null) {
            graphicsLayer.removeAll();
        }
        getSelParams(list, 0,selMap);

        Intent intent = null;
        if (selectGeometry.getType().equals(Geometry.Type.POINT)) {
            intent = new Intent(mContext, PointEditActivity.class);
        } else if (selectGeometry.getType().equals(Geometry.Type.POLYLINE)) {
            intent = new Intent(mContext, LineEditActivity.class);
        } else if (selectGeometry.getType().equals(Geometry.Type.POLYGON)) {
            intent = new Intent(mContext, XbEditActivity.class);
        } else {
            intent = new Intent(mContext, XbEditActivity.class);
        }

        String pname = myLayer.getPname();// 工程名称
        String path = myLayer.getPath();
        String cname = myLayer.getCname();
        MyFeture feture = new MyFeture(pname, path, cname, selGeoFeature, myLayer);
        Bundle bundle = new Bundle();
        bundle.putSerializable("myfeture", feture);
        bundle.putSerializable("parent", "Base");
        bundle.putSerializable("id", selGeoFeature.getId() + "");
        intent.putExtras(bundle);
        startActivity(intent);
        feture = null;
    }

    /**
     * 提示是否刪除小班
     */
    public void showDeleteFeatureDialog(final long id, final int position,
                                        final FeatureResultAdapter adapter, final Dialog dddialog, final ListView listView) {
        AlertDialog.Builder builder = new Builder(mContext);
        builder.setMessage("确认删除吗!");
        builder.setTitle("信息提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    GeodatabaseFeature feature = selGeoFeaturesList.get(position);
                    MyLayer myLayer = BaseUtil.getIntance(mContext).getFeatureInLayer(feature,layerNameList,selMap);
                    FeatureTable featureTable = myLayer.getTable();

                    featureTable.deleteFeature(id);

                    basePresenter.recordXb(id, "delete", feature.getAttributes(), feature.getGeometry(), myLayer.getLayer());
                    if (adapter != null) {
                        selGeoFeaturesList.remove(position);
                        adapter.notifyDataSetChanged();
                    } else {
                        selGeoFeaturesList.remove(position);
                    }
                    int size = selGeoFeaturesList.size();
                    if (size == 0 && dddialog != null) {
                        dddialog.dismiss();
                    }
                    mapRemove(new View(mContext));
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                        BaseUtil.getIntance(mContext).setHeight(adapter, listView);
                    }
                    dialog.dismiss();
                } catch (TableException e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (selGeoFeaturesList.size() == 1) {
                    selectButton.setChecked(true);
                }
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        if (mLocClient != null) {
            mLocClient.stop();
        }
        mapView.destroyDrawingCache();
        super.onDestroy();
    }


    /**
     * 合并小班选择保留哪个小班属性
     */
    public void showHeBing(final List<GeodatabaseFeature> list, final View view) {
        MergeFeatureDialog mergeFeatureDialog = new MergeFeatureDialog(mContext,R.style.Dialog,list,this,basePresenter);
        DialogParamsUtil.setDialogParamsCenter(mContext,mergeFeatureDialog,0.4,0.5);
    }

    /**
     * 添加feature在图层上
     */
    @SuppressLint("SimpleDateFormat")
    public void addFeatureToOtherLayer(GeodatabaseFeature feature) {
        try {
            if (!feature.getGeometry().isValid() || feature.getGeometry().isEmpty()) {
                return;
            }
            FeatureLayer layer = myLayer.getLayer();
            GeodatabaseFeatureTable table = myLayer.getTable();
            long id = table.addFeature(feature);

			/* 添加小班后 记录添加小班的id 备撤销时删除 */
            //basePresenter.recordXb(id, "add", feature.getAttributes(), feature.getGeometry(), layer);
        } catch (TableException e) {
            e.printStackTrace();
        }

        mapView.invalidate();
    }

    /**
     * 小班 修斑保存方法
     */
    public void saveXbFeature(Polyline drawline) {
        if (selectGeometry == null) {
            ToastUtil.setToast(mContext, "请选择小班");
        }
        // 所画草图线的点个数
        int drawSize = drawline.getPointCount();
        if (drawSize < 2) {
            ToastUtil.setToast(mContext, "草图线段点数少于2,无法修整面");
            return;
        }
        Polygon selectPpolygon = (Polygon) selectGeometry;
        int pathSize = selectPpolygon.getPathCount();//2
        if (pathSize == 1) {
            repairPresenter.saveXBoPathFeature(drawline, selectPpolygon);
        } else if (pathSize > 1) {
            //多条path时
            saveXbAllPath(drawline);
        }
    }

    /**多条path时小班修改保存*/
    public void saveXbAllPath(Polyline drawline) {
        Polygon selectPpolygon = (Polygon) selectGeometry;
        int pathSize = selectPpolygon.getPathCount();
        List<Polygon> polygons = new ArrayList<>();
        Geometry first = new Polygon();
        for (int i = 0; i<pathSize; i++) {
            int start = selectPpolygon.getPathStart(i);//0 63
            int end = selectPpolygon.getPathEnd(i);//63 98
            Polygon polygon = new Polygon();
            polygon.startPath(selectPpolygon.getPoint(start));
            for (int k = start + 1; k < end; k++) {
                polygon.lineTo(selectPpolygon.getPoint(k));
            }
            polygons.add(polygon);
            if(i==0){
                first = polygon;
            }
        }
        boolean flag1 = GeometryEngine.within(drawline, first, spatialReference);

        if(flag1){
            for(int i=0;i<polygons.size();i++){
                boolean flag = GeometryEngine.intersects(drawline, polygons.get(i), spatialReference);
                if (flag) {
                    Graphic graphic = repairPresenter.saveXBoPathFeature(drawline, polygons.get(i));
                    if(graphic != null ) {
                        Geometry geom = GeometryEngine.difference(first,graphic.getGeometry(), spatialReference);
                        first = basePresenter.updateFeature(geom, selGeoFeature, myLayer).getGeometry();
                    }else{
                        first = polygons.get(i);
                    }
                } else{
                    if(first != null){
                        Geometry geom = GeometryEngine.difference(first,polygons.get(i), spatialReference);
                        Graphic graphic = basePresenter.updateFeature(geom, selGeoFeature, myLayer);
                        if(graphic != null){
                            first = graphic.getGeometry();
                        }
                    }
                }

            }
        }else{
            for(int i=0;i<polygons.size();i++){
                boolean flag = GeometryEngine.intersects(drawline, polygons.get(i), spatialReference);
                if (flag) {
                    Graphic graphic = repairPresenter.saveXBoPathFeature(drawline, polygons.get(i));
                    if(graphic != null ) {
                        first = graphic.getGeometry();
                    }
                } else{
                    if(first != null){
                        Geometry geom = GeometryEngine.difference(first,polygons.get(i), spatialReference);
                        Graphic graphic = basePresenter.updateFeature(geom, selGeoFeature, myLayer);
                        if(graphic != null){
                            first = graphic.getGeometry();
                        }
                    }
                }

            }
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        resetJiami();
    }

    /**
     * 监听键盘
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            resetJiami();
            ProgressDialogUtil.stopProgressDialog(mContext);
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 对加载的加密数据解密后重新加密
     */
    public void resetJiami() {
        int size = layerNameList.size();
        for (int i = 0; i < size; i++) {
            if (layerNameList.get(i).isFlag()) {
                String path = layerNameList.get(i).getPath();
                childCheckBox.put(path, false);
                SytemUtil.jiamicript(path);
                if(layerNameList.get(i).getLayer() != null && layerNameList.get(i).getLayer().isInitialized()){
                    mapView.removeLayer(layerNameList.get(i).getLayer());
                }
            }
        }
    }

    /**
     * 小班查询方法
     */
    public void getGeometryInfo(Geometry geometry) {
        selGeoFeaturesList.clear();
        for (final MyLayer layer : layerNameList) {
            getGeometryInfo(geometry, layer);
        }
    }
    /**根据勾选的区域查询图层小班的ids*/
    public void getGeometryIds(final Geometry geometry, final MyLayer layer){
        selGeoFeaturesList.clear();
        QueryParameters queryParams = new QueryParameters();
        queryParams.setOutFields(new String[]{"*"});
        queryParams.setSpatialRelationship(SpatialRelationship.INTERSECTS);
        queryParams.setGeometry(geometry);
        queryParams.setReturnGeometry(true);
        queryParams.setWhere("1=1");
        queryParams.setOutSpatialReference(spatialReference);
        Map<String, Order> orderByFields = new HashMap<>();
        orderByFields.put("OBJECTID",Order.ASC);
        queryParams.setOrderByFields(orderByFields);
        layer.getLayer().getFeatureTable().queryIds(queryParams, new CallbackListener<long[]>() {
            @Override
            public void onCallback(long[] longs) {
                ProgressDialogUtil.stopProgressDialog(mContext);
                if(longs.length > 0){
                    featureIds = longs;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initXbdqData(layer);
                        }
                    });
                }
            }

            @Override
            public void onError(Throwable throwable) {
                ProgressDialogUtil.stopProgressDialog(mContext);
            }
        });
    }

    /**绑定小班清单数据*/
    private void initXbdqData(MyLayer layer){
        xbqdInclude.setVisibility(View.VISIBLE);
        final View view = xbqdInclude.findViewById(R.id.share_xbqd_top);
        if (featureIds.length > 0) {
            view.setVisibility(View.VISIBLE);
        }
        ListView listView = (ListView) xbqdInclude.findViewById(R.id.xb_data_listview);
        FeatureArraysAdapter adapter = new FeatureArraysAdapter(featureIds, mContext, layer);
        BaseUtil.getIntance(mContext).setHeight(adapter, listView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                GeodatabaseFeature feature = (GeodatabaseFeature) myLayer.getLayer().getFeature(featureIds[position]);
                getFeaturesList.clear();
                getFeaturesList.add(feature);

                if (graphicsLayer != null) {
                    graphicsLayer.removeAll();
                    circleGraphic = null;
                    locationGraphic = null;
                }

                SimpleFillSymbol symbol = new SimpleFillSymbol(Color.RED);
                symbol.setAlpha(80);
                Graphic graphic = new Graphic(feature.getGeometry(), symbol);
                graphicsLayer.addGraphic(graphic);

                //建立一个1000米缓冲区
                Polygon polygon = GeometryEngine.buffer(feature.getGeometry(), spatialReference, 400, null);
                mapView.setExtent(polygon);
            }
        });


        ImageView close = (ImageView) xbqdInclude.findViewById(R.id.share_xbqd_close);
        close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                xbqdInclude.setVisibility(View.GONE);
                view.setVisibility(View.GONE);
            }
        });
    }

    /**根据勾绘区域查询图层小班*/
    public void getGeometryInfo(final Geometry geometry, final MyLayer layer) {
        selGeoFeaturesList.clear();
        QueryParameters queryParams = new QueryParameters();
        queryParams.setOutFields(new String[]{"*"});
        queryParams.setSpatialRelationship(SpatialRelationship.INTERSECTS);
        queryParams.setGeometry(geometry);
        queryParams.setReturnGeometry(true);
        queryParams.setWhere("1=1");
        queryParams.setOutSpatialReference(spatialReference);
        Map<String, Order> orderByFields = new HashMap<>();
        String obj = layer.getTable().getFields().get(0).getName();
        orderByFields.put(obj,Order.ASC);
        queryParams.setOrderByFields(orderByFields);
        layer.getLayer().selectFeatures(queryParams, SelectionMode.NEW,
                new CallbackListener<FeatureResult>() {

                    @Override
                    public void onError(Throwable arg0) {
                        ToastUtil.setToast(mContext, "查询出错");
                        ProgressDialogUtil.stopProgressDialog(mContext);
                    }

                    @Override
                    public void onCallback(FeatureResult featureResult) {

                        long size = featureResult.featureCount();
                        if (size > 0) {
                            Iterator<Object> iterator = featureResult.iterator();
                            GeodatabaseFeature geodatabaseFeature = null;
                            while (iterator.hasNext()) {
                                geodatabaseFeature = (GeodatabaseFeature) iterator.next();
                                selGeoFeaturesList.add(geodatabaseFeature);
                                selMap.put(geodatabaseFeature, layer.getCname());
                            }
                            //小班路径导航
                            if (size == 1 && actionMode == ActionMode.MODE_XBNAVIGATION) {
                                // 小班路径导航
                                if (currentPoint == null || !currentPoint.isValid()) {
                                    ToastUtil.setToast(mContext, "未获取到当前位置坐标");
                                    return;
                                }
                                Graphic graphic = new Graphic(currentPoint, pictureMarkerSymbol);
                                graphicsLayer.addGraphic(graphic);
                                mStops.clearFeatures();
                                polyline_nav = new Polyline();
                                if (mStops.getFeatures().size() == 0) {
                                    polyline_nav.startPath(currentPoint);
                                }
                                StopGraphic stop = new StopGraphic(graphic);
                                mStops.addFeature(stop);

                                navigationPresenter.drawLineToMap((Point) geometry, polyline_nav);
                                mapView.setExtent(polyline_nav);
                                mapView.invalidate();
                                actionMode = ActionMode.MODE_NULL;
                                polyline_nav = null;
                            } else if (actionMode == ActionMode.MODE_XBQD) {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        ProgressDialogUtil.stopProgressDialog(mContext);
                                        allFeaturesList.clear();
                                        allFeaturesList.addAll(selGeoFeaturesList);
                                        showXbListData(allFeaturesList, layer);
                                    }
                                });
                            }
                        }
                    }
                });
    }

    /**
     * 删除选中时如果选中了多个小班弹出选择删除哪个小班
     */
    public void showDelFeatureResult(final List<GeodatabaseFeature> list) {
        FeatureDelDialog delDialog = new FeatureDelDialog(mContext,R.style.Dialog,list,this,selMap);
        BussUtil.setDialogParams(mContext, delDialog, 0.55, 0.55);
    }

    /**
     * 获取选中小班的数据
     */
    public GeodatabaseFeature getSelParams(List<GeodatabaseFeature> list, int position,Map<GeodatabaseFeature, String> selMap) {

        GeodatabaseFeature feature = list.get(position);
        myLayer = BaseUtil.getIntance(mContext).getFeatureInLayer(feature,layerNameList,selMap);
        if (myLayer == null) {
            ToastUtil.setToast(mContext, "数据图层已经移除，请重新选择小班");
            return null;
        }
        long id = feature.getId();
        try {
            selGeoFeature = (GeodatabaseFeature) myLayer.getTable().getFeature(id);
            selectGeometry = selGeoFeature.getGeometry();
            selectFeatureAts = selGeoFeature.getAttributes();
            selectfiledList = selGeoFeature.getTable().getFields();
        } catch (TableException e) {
            e.printStackTrace();
        }
        return selGeoFeature;
    }

    /**
     * 弹出空间统计popupwindow
     */
    public void showPopupKjtj() {

        drawTool = new DrawTool(mapView);
        drawTool.addEventListener(this);

        kjtjPopup = new MorePopWindow(this, R.layout.popup_kjtj);
        kjtjPopup.showPopupWindow(kjtjPopup,tcxrImgview);

        View dbcemj = kjtjPopup.getContentView().findViewById(R.id.popup_kjtj_dbcemj);
        dbcemj.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                kjtjPopup.dismiss();
                showSymboSelect();
                actionMode = ActionMode.MODE_DBCEMJ;
            }
        });

    }

    /**
     * 选择 园 矩形 多边形  三种之中的任意一种勾绘方式
     */
    public void showSymboSelect() {
        if (layerNameList.size() == 0) {
            ToastUtil.setToast(mContext, "请先加载空间数据");
            return;
        }
        if (layerNameList.size() > 0) {
            actionMode = ActionMode.MODE_TONGJI;
            showFeatureLayer(actionMode, layerNameList);
        }
    }

    @Override
    public void handleDrawEvent(DrawEvent event) {
        // 将画好的图形（已经实例化了Graphic），添加到drawLayer中并刷新显示
        Graphic graphic = event.getDrawGraphic();
        graphicsLayer.addGraphic(graphic);
        if (actionMode == ActionMode.MODE_DBCEMJ) {
            showGeometryInfo(graphic.getGeometry(), myLayer.getLayer(), ActionMode.MODE_DBCEMJ);
        }
    }

    /**
     * 重新setTouch   myTouchListener
     */
    public void initTouch() {
        if (drawTool != null) {
            drawTool.activate(0);
            mapView.setOnTouchListener(myTouchListener);
        }
    }

    /**
     * 展示图斑面积总和
     */
    private void showResultMj(List<GeodatabaseFeature> list) {
        ResultAreaDialog areaDialog = new ResultAreaDialog(mContext,R.style.Dialog,list);
        BussUtil.setDialogParams(mContext, areaDialog, 0.4, 0.4);
    }

    /**
     * 显示图层渲染设置页面
     */
    public void showLayerRenderSystem() {
        RenderSetDialog setDialog = new RenderSetDialog(mContext,R.style.Dialog,this,layerControlPresenter);
        BussUtil.setDialogParams(mContext, setDialog, 0.55, 0.8);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // 缺少权限时, 进入权限配置页面
        if (new PermissionsChecker(this).lacksPermissions(PERMISSIONS)) {
            PermissionsActivity.startActivityForResult(this, PermissionsActivity.PERMISSIONS_REQUEST_CODE, PERMISSIONS);
        }

        /* 获取 数据所属 数据 */
        if(sjssLlist.size() == 0){
            new MyAsyncTask().execute("sjss");
        }
		/* 获取 行政区域 数据 */
		if(xzqyLlist.size() == 0){
            new MyAsyncTask().execute("xzqy");
        }
		/* 获取 基地性质 数据 */
		if(jdxzLlist.size() == 0){
            new MyAsyncTask().execute("jdxz");
        }
    }

    /**添加图层*/
    public void addNewLayers(){
        AlertDialog alertDialog = new AlertDialog.Builder(mContext, R.style.Dialog)
                .setTitle("请选择")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setSingleChoiceItems(new String[]{"二调数据模板", "林地变更模板","通用模板"}, 0,
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                AddnewLayerDialog addnewLayerDialog = new AddnewLayerDialog(mContext, R.style.Dialog, which);
                                BussUtil.setDialogParams(mContext, addnewLayerDialog, 0.5, 0.5);
                            }
                        }
                )
                .setNegativeButton("取消", null)
                .show();
    }

    /**
     * 获取加小班清单数据
     */
    public void getXbList() {
        actionMode = ActionMode.MODE_XBQD;
        polygonLayerList.clear();
        for (final MyLayer layer : layerNameList) {
            if (layer.getLayer().getGeometryType().equals(Geometry.Type.POLYGON)) {
                polygonLayerList.add(layer);
            }
        }

        int size = polygonLayerList.size();
        if (size > 1) {
            showFeatureLayer(actionMode, polygonLayerList);
        } else if (size == 1) {
            MyLayer layer = polygonLayerList.get(0);
            Envelope env = tiledLayer.getFullExtent();
            ProgressDialogUtil.startProgressDialog(mContext);
            getGeometryInfo(env, layer);
        } else {
            ToastUtil.setToast(mContext, "请在图层控制中加载小班数据");
        }
    }

    /**
     * 展示获取的小班列表
     */
    public void showXbListData(final List<GeodatabaseFeature> datalist, MyLayer layer) {
        xbqdInclude.setVisibility(View.VISIBLE);
        final View view = xbqdInclude.findViewById(R.id.share_xbqd_top);
        if (datalist.size() > 0) {
            view.setVisibility(View.VISIBLE);
        }
        ListView listView = (ListView) xbqdInclude.findViewById(R.id.xb_data_listview);
        SetAdapter adapter = new SetAdapter(datalist, mContext, layer);
        //BaseUtil.getIntance(mContext).setHeight(adapter, listView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                GeodatabaseFeature feature = datalist.get(position);
                getFeaturesList.clear();
                getFeaturesList.add(feature);

                if (graphicsLayer != null) {
                    graphicsLayer.removeAll();
                    circleGraphic = null;
                    locationGraphic = null;
                }

                SimpleFillSymbol symbol = new SimpleFillSymbol(Color.RED);
                symbol.setAlpha(80);
                Graphic graphic = new Graphic(feature.getGeometry(), symbol);
                graphicsLayer.addGraphic(graphic);

                //建立一个1000米缓冲区
                Polygon polygon = GeometryEngine.buffer(feature.getGeometry(), spatialReference, 400, null);
                mapView.setExtent(polygon);
            }
        });


        ImageView close = (ImageView) xbqdInclude.findViewById(R.id.share_xbqd_close);
        close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                xbqdInclude.setVisibility(View.GONE);
                view.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 点击地图获取地图点坐标数据并展示
     */
    public void addCalloutPoint(MapView map, Point mappoint) {
        XmlPullParser parser = getResources().getXml(R.xml.calloutpopuwindowstyle);

        CalloutStyle style = new CalloutStyle(mContext, Xml.asAttributeSet(parser));
        View view = basePresenter.loadCalloutPopuwindow(mappoint);
        //View view = loadCalloutView(mappoint);
        final CalloutPopupWindow calloutP = new CalloutPopupWindow(view, CalloutPopupWindow.MODE.OVERFLOW, style);
        calloutP.showCallout(map, mappoint, 0, 0);
        calloutPopupWindows.add(calloutP);

        ImageView close = (ImageView) view.findViewById(R.id.calloutpopuwindow_close);
        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                calloutP.hide();
                calloutPopupWindows.remove(calloutP);
            }
        });

        BussUtil.closeCalloutPopu(view, calloutP);
    }

    @Override
    public View getGpsCollectionView() {
        return gpsCaijiInclude;
    }

    @Override
    public View getParChildView() {
        return childview;
    }

    @Override
    public MapView getMapView() {
        return mapView;
    }

    @Override
    public GraphicsLayer getGraphicLayer() {
        return graphicsLayer;
    }

    @Override
    public List<MyLayer> getLayerNameList() {
        return layerNameList;
    }

    @Override
    public View getImgeLayerView() {
        return tckzImgInclude;
    }

    @Override
    public View getTckzView() {
        return tckzInclude;
    }

    @Override
    public HashMap<String, Boolean> getLayerCheckBox() {
        return childCheckBox;
    }

    @Override
    public List<Row> getSysLayerData() {
        return proData;
    }

    @Override
    public List<String> getLayerKeyList() {
        return childKeyList;
    }

    @Override
    public SharedPreferences getSharedPreferences() {
        return MyApplication.sharedPreferences;
    }

    @Override
    public Callout getCallout() {
        return callout;
    }

    @Override
    public double getCurrenLat() {
        return currentLat;
    }

    @Override
    public double getCurrentLon() {
        return currentLon;
    }

    @Override
    public NAFeaturesAsFeature getmStops() {
        return mStops;
    }

    @Override
    public SpatialReference getSpatialReference() {
        return spatialReference;
    }

    @Override
    public RouteTask getRouteTask() {
        return mRouteTask;
    }

    @Override
    public void removeGraphicLayer() {
        if (graphicsLayer != null && graphicsLayer.isInitialized()) {
            mapView.removeLayer(graphicsLayer);
        }
    }

    @Override
    public Envelope getCurrentEnvelope() {
        Polygon polygon = mapView.getExtent();
        Envelope envelope = new Envelope();
        polygon.queryEnvelope(envelope);
        return envelope;
    }

    @Override
    public void addGraphicLayer() {
        if(graphicsLayer != null){
            mapView.removeLayer(graphicsLayer);
        }
        if (graphicsLayer != null) {
            mapView.addLayer(graphicsLayer);
        } else {
            graphicsLayer = new GraphicsLayer(GraphicsLayer.RenderingMode.STATIC);
            mapView.addLayer(graphicsLayer);
        }
    }

    @Override
    public View getGpsCaijiInclude() {
        return gpsCaijiInclude;
    }

    @Override
    public TiledLayer getDxtTitleLayer() {
        return dxtTiledLayer;
    }

    @Override
    public TiledLayer getBaseTitleLayer() {
        return tiledLayer;
    }

    @Override
    public TiledLayer getImgTitleLayer() {
        return imgTiledLayer;
    }

    @Override
    public List<MyLayer> getLayerList() {
        return layerNameList;
    }

    @Override
    public Point getCurrentPoint() {
        return currentPoint;
    }

    @Override
    public Point getGpsPoint() {
        return gpspoint;
    }

    @Override
    public void addImageLayer(TiledLayer layer) {
        imgTiledLayer = layer;
        mapView.addLayer(layer);
        addGraphicLayer();
    }

    @Override
    public ArrayList<String> getPicList() {
        return picList;
    }

    @Override
    public void setLayerOnItemClickListener(AdapterView<?> parent, View view, int position, long id,ActionMode mode,List<MyLayer> layerList,LayerSelectDialog dialog) {
        myLayer = layerList.get(position);
        layerType = myLayer.getLayer().getGeometryType();
        String layername = myLayer.getLname();

        if (layername.contains("设计") && (mode != ActionMode.MODE_XBSEARCHZDY && mode != ActionMode.MODE_XBSEARCHJD)) {
            ToastUtil.setToast(mContext, "你选择的小班为设计小班,不可编辑请重新选择编辑图层");
            return;
        }
        SytemUtil.getEditSymbo(BaseActivity.this, myLayer.getLayer());
        dialog.dismiss();
        selectMethed(mode);
    }

    private void selectMethed(ActionMode mode){
        if (mode == ActionMode.MODE_XBQD) {
            //小班清单
            ProgressDialogUtil.startProgressDialog(mContext);
            getGeometryInfo(tiledLayer.getFullExtent(), myLayer);
        } else if (mode == ActionMode.MODE_TONGJI) {
            showDialogKjtj();
        } else if (mode == ActionMode.MODE_XBSEARCHZDY) {
					/* 小班自定义查询 */
            xbqueryPresenter.showSearchXiaoZDY(xbSearchZdyInclude);
        } else if (mode == ActionMode.MODE_XBSEARCHJD) {
					/* 小班简单查询 */
            xbqueryPresenter.showSearchXBjd(xbSearchJdInclude);
        } else if (mode == ActionMode.MODE_EDIT_ADD) {
					/* 新增图斑 */
            if (layerType.equals(Geometry.Type.POINT)) {
                drawType = POINT;
                if (currentPoint != null && currentPoint.isValid()) {
                    showAddCurrentPoint(drawType, currentPoint);
                } else {
                    ToastUtil.setToast(mContext, "未取得当前位置坐标,手动添加点");
                    activate(drawType);
                }

            } else if (layerType.equals(Geometry.Type.POLYLINE)) {
                if (gps_start_flag) {
                    gpstart.setVisibility(View.GONE);
                    gpspend.setVisibility(View.VISIBLE);
                    drawType = FREEHAND_POLYLINEGPS;
                    ToastUtil.setToast(mContext, "开始记录轨迹");
                } else {
                    drawType = FREEHAND_POLYLINE;
                }
                activate(drawType);
            } else if (layerType.equals(Geometry.Type.POLYGON)) {
                // drawType = FREEHAND_POLYGON;
                if (gps_start_flag) {
                    gpstart.setEnabled(false);
                    gpspend.setEnabled(true);
                    gpsstop.setEnabled(true);
                    gpstart.setVisibility(View.GONE);
                    gpspend.setVisibility(View.VISIBLE);
                    drawType = FREEHAND_POLYLINEGPS;
                    ToastUtil.setToast(mContext, "开始记录轨迹");
                } else {
                    drawType = FREEHAND_POLYLINE;
                }
                activate(drawType);
            }

        } else if (mode == ActionMode.MODE_EDIT_ADD_GB) {
            String tbname1 = myLayer.getTable().getTableName();
            String tbname2 = selGeoFeaturesList.get(0).getTable().getTableName();
            if (!tbname1.equals(tbname2)) {
                ToastUtil.setToast(mContext, "选择添加数据图层与选中小班不在一个数据图层");
                return;
            }
            if (layerType.equals(Geometry.Type.POLYGON)) {
                // drawType = FREEHAND_POLYGON;
                drawType = FREEHAND_POLYGON;
                activate(drawType);
            } else {
                ToastUtil.setToast(mContext, "请选择面图层数据");
                return;
            }
        } else if (mode == ActionMode.MODE_EDIT_COPY) {
            for (GeodatabaseFeature feature : selGeoFeaturesList) {
                if (layerType == feature.getGeometry().getType()) {
                    addFeatureToOtherLayer(feature);
                }
            }
        }else if(mode == ActionMode.MODE_ADD_LABLE){
            //显示标注view
            actionMode = mode;
            layerLableInclude.setVisibility(View.VISIBLE);
            lablePresenter.showLayerAials(layerLableInclude,myLayer);
        }
    }

    /*是否显示行进路线*/
    int lineGraphicID = -1;
    Polyline travelLine = null;
    boolean isTravelfirst = true;
    public void showTravelLine(Point point){
        boolean flag = gpsCollectPresenter.isTravel();
        if(flag){
            if(travelLine == null){
                travelLine = new Polyline();
            }
            if(isTravelfirst){
                travelLine.startPath(point);
                isTravelfirst = false;
            }else{
                travelLine.lineTo(point);
            }

            if(lineGraphicID == -1){
                SimpleLineSymbol lineSymbol = new SimpleLineSymbol(Color.RED,4);
                Graphic graphic = new Graphic(travelLine,lineSymbol);
                lineGraphicID = graphicsLayer.addGraphic(graphic);
            }else{
                graphicsLayer.updateGraphic(lineGraphicID,travelLine);
            }

        }else{
            travelLine = null;
            isTravelfirst = true;
        }
    }

    long timeMillis = 0;
    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis() - timeMillis < 2000){
            super.onBackPressed();
        }else{
            timeMillis = System.currentTimeMillis();
            ToastUtil.setToast(this,"再按一次退出");
        }
    }
}
