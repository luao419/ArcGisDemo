package com.luao.arcgisdemo.gisutils;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;

import java.util.List;

/**
 * @author luao
 * arcgis工具类
 */
public class ArcGisUtil implements LifecycleObserver {

    private MapView mapView = null;

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private void onContextDestroyed() {
        mapView.destroyDrawingCache();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onContextResume() {
        mapView.resume();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onContextPause() {
        mapView.pause();
    }

    public interface OnClickMapListener {
        /**
         * 点击地图
         *
         * @param graphicsFromLayer 点击的覆盖物
         * @param point             arcgis点
         */
        void onClickMap(Graphic graphicsFromLayer, Point point);
    }

    private OnClickMapListener onClickMapListener = null;

    public void setOnClickMapListener(OnClickMapListener onClickMapListener) {
        this.onClickMapListener = onClickMapListener;
    }

    /**
     * 构造函数
     *
     * @param activity activity
     * @param mapView  arcigsMapView
     */
    public ArcGisUtil(AppCompatActivity activity, MapView mapView) {
        this.mapView = mapView;
        activity.getLifecycle().addObserver(this);
        initLisenter();
    }

    public ArcGisUtil(Fragment fragment, MapView mapView) {
        this.mapView = mapView;
        fragment.getLifecycle().addObserver(this);
        initLisenter();
    }

    private void initLisenter() {
        mapView.setOnTouchListener(new DefaultMapViewOnTouchListener(mapView.getContext(), mapView) {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
//                Log.d(sTag, "onSingleTapConfirmed: " + motionEvent.toString());
                // get the point that was clicked and convert it to a point in map coordinates
                onClickMap(motionEvent);
                return true;
            }
        });
    }

    /**
     * 处理地图的点击事件
     *
     * @param motionEvent 点击事件
     */
    private void onClickMap(MotionEvent motionEvent) {
        android.graphics.Point screenPoint = new android.graphics.Point(Math.round(motionEvent.getX()),
                Math.round(motionEvent.getY()));

        // create a map point from screen point
        Point mapPoint = mapView.screenToLocation(screenPoint);
        // convert to WGS84 for lat/lon format
        Point wgs84Point = (Point) GeometryEngine.project(mapPoint, SpatialReferences.getWgs84());
        Graphic graphicsFromLayer = getGraphicsFromLayer1(mapView.getGraphicsOverlays(), Math.round(motionEvent.getX()), Math.round(motionEvent.getY()));

        if (graphicsFromLayer != null) {
            Point point = (Point) graphicsFromLayer.getGeometry();
            onClickMapListener.onClickMap(graphicsFromLayer, point);
        }
    }


    /**
     * 移动中心点
     *
     * @param point
     */
    @Deprecated
    public static void moveMapCenter(Point point) {

    }

    /**
     * 从一个图层里里 查找获得 Graphics对象. x,y是屏幕坐标,layer
     * 是GraphicsLayer目标图层（要查找的）。相差的距离是50像素。
     *
     * @param xScreen x屏幕坐标
     * @param yScreen y屏幕坐标
     * @return 选中的图层
     */
    public Graphic getGraphicsFromLayer1(List<GraphicsOverlay> graphicsOverlay, double xScreen, double yScreen) {
        //遍历arcgis地图，
        for (GraphicsOverlay g : graphicsOverlay) {
            for (Graphic graphic : g.getGraphics()) {
                Point geometry = (Point) graphic.getGeometry();
                android.graphics.Point point = mapView.locationToScreen(geometry);
                double x1 = point.x;
                double y1 = point.y;
                if (Math.sqrt((xScreen - x1) * (xScreen - x1) + (yScreen - y1) * (yScreen - y1)) < 50) {
                    //触发了点击
                    return graphic;
                }
            }
        }
        return null;
    }

    /**
     * 从一个图层里里 查找获得 Graphics对象. x,y是屏幕坐标,layer
     * 是GraphicsLayer目标图层（要查找的）。相差的距离是50像素。
     *
     * @param xScreen x屏幕坐标
     * @param yScreen y屏幕坐标
     * @return 选中的图层
     */
    public Graphic getGraphicsFromLayer(GraphicsOverlay graphicsOverlay, double xScreen, double yScreen) {
        //遍历arcgis地图，
        for (Graphic graphic :
                graphicsOverlay.getGraphics()) {
            Point geometry = (Point) graphic.getGeometry();
            android.graphics.Point point = mapView.locationToScreen(geometry);
            double x1 = point.x;
            double y1 = point.y;
            if (Math.sqrt((xScreen - x1) * (xScreen - x1) + (yScreen - y1) * (yScreen - y1)) < 50) {
                return graphic;
            }
        }
        return null;
    }

    /**
     * 从一个图层里里 查找获得 Graphics对象. x,y是屏幕坐标,layer
     * 是GraphicsLayer目标图层（要查找的）。相差的距离是50像素。
     *
     * @param xScreen x屏幕坐标
     * @param yScreen y屏幕坐标
     * @return 选中的图层
     */
    public Graphic getGraphicsFromLayer(List<Graphic> graphics, double xScreen, double yScreen) {
        //遍历arcgis地图，
        for (Graphic graphic :
                graphics) {
            Point geometry = (Point) graphic.getGeometry();
            android.graphics.Point point = mapView.locationToScreen(geometry);
            double x1 = point.x;
            double y1 = point.y;
            if (Math.sqrt((xScreen - x1) * (xScreen - x1) + (yScreen - y1) * (yScreen - y1)) < 50) {
                //触发了点击
//                break;
                return graphic;
            }
        }
        return null;
    }


    /**
     * 添加graphics到mapview（新建图层对象）
     *
     * @return 添加后的图层
     */
    public GraphicsOverlay addGraphicsOverlay() {
        //create the graphics overlay
        GraphicsOverlay graphicsOverlay = new GraphicsOverlay();
        graphicsOverlay.setVisible(true);
        //add the overlay to the map view
        mapView.getGraphicsOverlays().add(graphicsOverlay);
        return graphicsOverlay;
    }


    /**
     * 展示地图弹窗
     *
     * @param view        需要弹出的view
     * @param arcgisPoint arcgis坐标点
     * @return 返回的arcgis的callout 弹窗
     */
    public Callout showPop(View view, Point arcgisPoint) {
        Callout mCallout = mapView.getCallout();
        mCallout.setLocation(arcgisPoint);
        mCallout.setContent(view);
//        mCallout.setStyle(Callout.Style);
        mCallout.show();
        //显示完聚焦
        mapView.setViewpointCenterAsync(arcgisPoint);
        return mCallout;
    }

    /**
     * 展示地图弹窗
     *
     * @param text        展示的字符串
     * @param arcgisPoint arcgis坐标点
     * @return 返回的arcgis的callout 弹窗
     */
    public Callout showTextPop(String text, Point arcgisPoint) {
        TextView calloutContent = new TextView(mapView.getContext());
        calloutContent.setTextColor(Color.BLACK);
        calloutContent.setSingleLine();
        // format coordinates to 4 decimal places
        calloutContent.setText(text);
        return showPop(calloutContent, arcgisPoint);
    }
}
