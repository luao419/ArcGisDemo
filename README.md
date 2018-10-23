# ArcGisDemo
arcgis加载天地图相关应用
### 当初开发的时候在网上搜索了很久，没有看到合适的arcgis相关的资料，很多资料都是之前旧版本的arcgis，大部分的代码也不能用，所以工作完成之后在这里稍微总结一下
 1. ## 天地图相关
    [天地图官网](http://www.tianditu.gov.cn)  
    [代码所用的服务地址](http://service.tianditu.gov.cn/)

1. ## arcigs相关
    1. ### TianDiMapUtils  
        #### TileInfo参数  

        | 参数类型 | 参数名 | 解释 | 备注 |
        | ------ | ------ | ------ |------ |
        | int | DPI | 分辨率 | 
        |  TileInfo.ImageFormat | imageFormat | 切片的加载方式？ | 不太确定
        |  List<LevelOfDetail> | levelOfDetails | 主要缩放等级 | 
        |  Point | origin | 起始点 | 这里在异步加载的时候，点有时候不可用，不知道什么原因 |
        |  SpatialReference | spatialReference | 坐标系类型 | 
        |  int | tileHeight | 切片高 | 
        | int | tileWidth | 切片宽 |   

        #### WebTiledLayer参数  

        | 参数类型 | 参数名 | 解释 | 备注 |
        | ------ | ------ | ------ |------ |
        | String | templateUri | 地图地址 | 
        |  Iterable<String> | subDomains | 不知道术语，见备注 | 天地图WMTS服务有八个域名可以用来访问，0~8中任何一个都可以进行切片
        |  TileInfo | tileInfo | 关键信息 | 见上文 |
        |  Envelope | fullExtent | 地图范围 | 
    1. ### ArcGisUtil  
        - 使用 Lifecycle 解决生命周期问题，把生命周期的控制抽出到工具类中  
        - arcgis中没有像百度、高德中那种marker点击事件，所以需要根据点击坐标自己实现  
        关键代码解释
        ``` java
         mapView.setOnTouchListener(DefaultMapViewOnTouchListener defaultMapViewOnTouchListener,MapView mapView){
            @Override
            public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
                //地图手势处理
                onClickMap(motionEvent);
                return true;
            }
         }
        ```
        处理手势事件
         ```java
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
       ```
        从一个图层里里 查找获得 Graphics对象. x,y是屏幕坐标,layer是GraphicsLayer目标图层（要查找的）。相差的距离是50像素。
        ```java
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
        ```  
        展示地图弹窗
        ```java
        public Callout showTextPop(String text, Point arcgisPoint) {
                TextView calloutContent = new TextView(mapView.getContext());
                calloutContent.setTextColor(Color.BLACK);
                calloutContent.setSingleLine();
                // format coordinates to 4 decimal places
                calloutContent.setText(text);
                return showPop(calloutContent, arcgisPoint);
            }
        ```
        
3.  ## 最后上代码   [代码地址](https://github.com/luao419/ArcGisDemo)  ，如果能帮助到各位希望能颗star


 
