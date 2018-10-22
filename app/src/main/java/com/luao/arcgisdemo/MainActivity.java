package com.luao.arcgisdemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.layers.ArcGISTiledLayer;
import com.esri.arcgisruntime.layers.WebTiledLayer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.luao.arcgisdemo.entity.SysUnitInfo;
import com.luao.arcgisdemo.gisutils.ArcGisUtil;
import com.luao.arcgisdemo.gisutils.SecondActivity;
import com.luao.arcgisdemo.gisutils.TianDiMapUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MapView mMapView;


    private ArcGISMap map;

    private WebTiledLayer imageBaseMap;
    private WebTiledLayer imageBaseMapAnno;

    private WebTiledLayer vectorBaseMap;
    private WebTiledLayer vectorBaseMapAnno;

    private WebTiledLayer terrainBaseMap;
    private WebTiledLayer terrainBaseMapAnno;

    private Point center;
    /**
     * 坐标系
     */
    final SpatialReference wgs84 = SpatialReference.create(4326);
    /**
     * 加载的图层
     */
    private ArcGISTiledLayer tiledLayerBaseMap;
    /**
     * 图层切换按钮
     */
    private TextView layerChangeBtn;

    /**
     * 当前图层
     */
    private int currentLayer;
    /**
     * 泵站信息list
     */
    private List<Graphic> graphicListPump;
    /**
     * 水厂图层list
     */
    private List<Graphic> graphicListWaterStation;
    /**
     * 设备列表
     */
    private List<SysUnitInfo> sysUnitInfos;
    /**
     * 泵站图层
     */
    private GraphicsOverlay graphicsOverlayPump;
    /**
     * 水清图层
     */
    private GraphicsOverlay graphicsOverlayWaterStation;
    /**
     * 功能根据
     */
    private ArcGisUtil arcGisUtil;
    /**
     * 点击弹窗
     */
    private TextView calloutContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMapView = findViewById(R.id.gis_map);
        layerChangeBtn = findViewById(R.id.layerChangeBtn);


        arcGisUtil = new ArcGisUtil(this, mMapView);

        final SpatialReference SRID_MERCATOR = SpatialReference.create(102100);
        map = new ArcGISMap(SRID_MERCATOR);
        center = new Point(106.4360552458, 38.2487400748, SpatialReference.create(4326));

        imageBaseMap = TianDiMapUtils.getTianDiMap(TianDiMapUtils.MapType.URL_CN_IMAGEBASEMAP);
        imageBaseMapAnno = TianDiMapUtils.getTianDiMap(TianDiMapUtils.MapType.URL_CN_IMAGEBASEMAP_ANNOTATION);
        ;

        terrainBaseMap = TianDiMapUtils.getTianDiMap(TianDiMapUtils.MapType.URL_CN_TERRAINBASEMAP);
        terrainBaseMapAnno = TianDiMapUtils.getTianDiMap(TianDiMapUtils.MapType.URL_CN_TERRAINBASEMAP_ANNOTATION);

        vectorBaseMap = TianDiMapUtils.getTianDiMap(TianDiMapUtils.MapType.URL_CN_VECTORBASEMAP);
        vectorBaseMapAnno = TianDiMapUtils.getTianDiMap(TianDiMapUtils.MapType.URL_CN_VECTORBASEMAP_ANNOTATION);

        final List<WebTiledLayer> imageBaseMapLayerList = new ArrayList<>();
        imageBaseMapLayerList.add(imageBaseMap);
        imageBaseMapLayerList.add(imageBaseMapAnno);
        final List<WebTiledLayer> terrainBaseMapLayerList = new ArrayList<>();
        terrainBaseMapLayerList.add(terrainBaseMap);
        terrainBaseMapLayerList.add(terrainBaseMapAnno);
        final List<WebTiledLayer> vectorBaseMapLayerList = new ArrayList<>();
        vectorBaseMapLayerList.add(vectorBaseMap);
        vectorBaseMapLayerList.add(vectorBaseMapAnno);

        map.setMaxScale(TianDiMapUtils.getSCALES()[TianDiMapUtils.getSCALES().length - 1]);

        mMapView.setMap(map);

        map.getOperationalLayers().addAll(imageBaseMapLayerList);

        imageBaseMap.loadAsync();
        imageBaseMapAnno.loadAsync();

        terrainBaseMap.loadAsync();
        terrainBaseMapAnno.loadAsync();

        vectorBaseMap.loadAsync();
        vectorBaseMapAnno.loadAsync();

        layerChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.getOperationalLayers().clear();
                currentLayer++;
                switch (currentLayer % 3) {
                    case 0:
                        map.getOperationalLayers().addAll(imageBaseMapLayerList);
                        break;
                    case 1:
                        map.getOperationalLayers().addAll(terrainBaseMapLayerList);
                        break;
                    case 2:
                        map.getOperationalLayers().addAll(vectorBaseMapLayerList);
                        break;
                    default:
                        map.getOperationalLayers().addAll(imageBaseMapLayerList);
                        break;
                }

            }
        });

        calloutContent = new TextView(this);
        calloutContent.setTextColor(Color.BLACK);
        calloutContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });

        arcGisUtil.setOnClickMapListener(new ArcGisUtil.OnClickMapListener() {
            @Override
            public void onClickMap(Graphic graphicsFromLayer, Point point) {
                Object name = graphicsFromLayer.getAttributes().get("name");
                if (name != null) {
                    calloutContent.setText(name.toString());
                    arcGisUtil.showPop(calloutContent, point);
                }
            }
        });
        loadUnitMarkerData();
    }

    /**
     * 加载地图marker元素数据
     */
    private void loadUnitMarkerData() {
//        ApiManager.getInstance().loadGisData()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new ApiObserver<List<SysUnitInfo>>() {
//                    @Override
//                    public void onSuccess(List<SysUnitInfo> sysUnitInfoList) {
//                        sysUnitInfos = sysUnitInfoList;
//                        //添加点
//                        graphicsOverlayPump = arcGisUtil.addGraphicsOverlay();
//                        graphicsOverlayWaterStation = arcGisUtil.addGraphicsOverlay();
//                        PictureMarkerSymbol iconPump = new PictureMarkerSymbol(new BitmapDrawable(changeSize(R.drawable.gis_ic_bengzhan)));
//                        PictureMarkerSymbol iconWaterStation = new PictureMarkerSymbol(new BitmapDrawable(changeSize(R.drawable.gis_ic_shuichang)));
//
//                        graphicListPump = new ArrayList<>();
//                        graphicListWaterStation = new ArrayList<>();
//
//                        for (SysUnitInfo sysUnitInfo : sysUnitInfoList) {
//                            Point point = new Point(sysUnitInfo.getLgtd(), sysUnitInfo.getLttd(), wgs84);
//                            HashMap<String, Object> stringStringHashMap = new HashMap<>();
//                            stringStringHashMap.put("name", sysUnitInfo.getName());
//                            if (sysUnitInfo.getKey1().equals("1")) {
//                                graphicListPump.add(new Graphic(point, stringStringHashMap, iconPump));
//                            } else if (sysUnitInfo.getKey1().equals("5")) {
//                                graphicListWaterStation.add(new Graphic(point, stringStringHashMap, iconWaterStation));
//                            }
//                        }
//                        graphicsOverlayPump.getGraphics().addAll(graphicListPump);
//                        graphicsOverlayWaterStation.getGraphics().addAll(graphicListWaterStation);
//                    }
//
//                    @Override
//                    public void onFailure() {
//
//                    }
//                });
    }
    /**
     * 移动地图中心
     *
     * @param point 中心点
     */
    private void moveMapCenter(Point point) {
        mMapView.setViewpointCenterAsync(point, TianDiMapUtils.getSCALES()[11]);
    }


    /**
     * 图片资源大小不契合，需要缩放
     *
     * @param drawable 图片资源
     * @return 新的图片资源
     */
    private Bitmap changeSize(@DrawableRes int drawable) {
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), drawable);
        int originalWidth = originalBitmap.getWidth();
        int originalHeight = originalBitmap.getHeight();
        int newWidth = 30;
        int newHeight = 70; // 自定义 高度 暂时没用

        float scale = ((float) newHeight) / originalHeight;
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        return Bitmap.createBitmap(originalBitmap, 0, 0,
                originalWidth, originalHeight, matrix, true);//你的 ImageView
    }
}
