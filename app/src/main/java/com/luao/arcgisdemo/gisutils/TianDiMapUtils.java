package com.luao.arcgisdemo.gisutils;

import com.esri.arcgisruntime.arcgisservices.LevelOfDetail;
import com.esri.arcgisruntime.arcgisservices.TileInfo;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.layers.WebTiledLayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 天地图工机
 */
public class TianDiMapUtils {
    /**
     * 分辨率
     */
    private static final int DPI = 96;

    /**
     * 比例尺  两种坐标系下的分辨率一致
     */
    private static final double[] SCALES = {591657527.591555,
            2.958293554545656E8, 1.479146777272828E8,
            7.39573388636414E7, 3.69786694318207E7,
            1.848933471591035E7, 9244667.357955175,
            4622333.678977588, 2311166.839488794,
            1155583.419744397, 577791.7098721985,
            288895.85493609926, 144447.92746804963,
            72223.96373402482, 36111.98186701241,
            18055.990933506204, 9027.995466753102,
            4513.997733376551, 2256.998866688275,
            1128.4994333441375
    };

    /**
     * 墨卡托坐标系下的分辨率
     */
    private static final double[] RESOLUTIONS_MERCATOR = {156543.03392800014,
            78271.51696402048, 39135.75848201024,
            19567.87924100512, 9783.93962050256,
            4891.96981025128, 2445.98490512564,
            1222.99245256282, 611.49622628141,
            305.748113140705, 152.8740565703525,
            76.43702828517625, 38.21851414258813,
            19.109257071294063, 9.554628535647032,
            4.777314267823516, 2.388657133911758,
            1.194328566955879, 0.5971642834779395,
            0.298582141738970};

    /**
     *
     */
    private static final SpatialReference SRID_MERCATOR = SpatialReference.create(102100);

    /**
     * 起始点
     */
    private static final Point ORIGIN_MERCATOR1 = new Point(106.4360552458, 38.2487400748, SpatialReference.create(4326));
    private static final Point ORIGIN_MERCATOR = new Point(-20037508.342787, 20037508.342787, SRID_MERCATOR);

    /**
     * 切片宽高
     */
    private static final int tileWidth = 256;
    private static final int tileHeight = 256;

    /**
     * 天地图WMTS服务有八个域名可以用来访问，0~8中任何一个都可以进行切片
     */
    private static final List<String> SubDomain = Arrays.asList("t0", "t1", "t2", "t3", "t4", "t5", "t6", "t7");

    /**
     * 天地图key，注册地址  https://console.tianditu.gov.cn/api/key
     */
    private static String TIANDI_MAP_KEY = "94aa433732f999e8f30b6a7b0141cbfd";


    /**
     * 中文矢量图
     */
    private static String URL_CN_VECTORBASEMAP = "https://{subDomain}.tianditu.gov.cn/DataServer?T=vec_w&X={col}&Y={row}&L={level}&tk="+TIANDI_MAP_KEY;

    /**
     * 中文矢量图标注
     */
    private static String URL_CN_VECTORBASEMAP_ANNOTATION = "http://{subDomain}.tianditu.gov.cn/DataServer?T=cva_w&X={col}&Y={row}&L={level}&tk="+TIANDI_MAP_KEY;

    /**
     * 中文地形图
     */
    private static String URL_CN_TERRAINBASEMAP = "http://{subDomain}.tianditu.gov.cn/DataServer?T=ter_w&X={col}&Y={row}&L={level}&tk="+TIANDI_MAP_KEY;

    /**
     * 中文地形图标注
     */
    private static String URL_CN_TERRAINBASEMAP_ANNOTATION = "http://{subDomain}.tianditu.gov.cn/DataServer?T=cta_w&X={col}&Y={row}&L={level}&tk="+TIANDI_MAP_KEY;

    /**
     * 中文影像图
     */
    private static String URL_CN_IMAGEBASEMAP = "http://{subDomain}.tianditu.gov.cn/DataServer?T=img_w&X={col}&Y={row}&L={level}&tk="+TIANDI_MAP_KEY;

    /**
     * 中文影像图标注
     */
    private static String URL_CN_IMAGEBASEMAP_ANNOTATION = "http://{subDomain}.tianditu.gov.cn/DataServer?T=cia_w&X={col}&Y={row}&L={level}&tk="+TIANDI_MAP_KEY;

    /**
     *
     */
    public enum MapType {
        /**
         * 中文矢量图
         */
        URL_CN_VECTORBASEMAP,
        /**
         * 中文矢量图标注
         */
        URL_CN_VECTORBASEMAP_ANNOTATION,
        /**
         * 中文地形图
         */
        URL_CN_TERRAINBASEMAP,
        /**
         * 中文地形图标注
         */
        URL_CN_TERRAINBASEMAP_ANNOTATION,
        /**
         * 中文影像图
         */
        URL_CN_IMAGEBASEMAP,
        /**
         * 中文影像图标注
         */
        URL_CN_IMAGEBASEMAP_ANNOTATION
    }

    public static double[] getSCALES() {
        return SCALES;
    }

    public static WebTiledLayer getTianDiMap(MapType mapType) {
        Envelope env = new Envelope(-20037508.3427892, -20037508.3427892, 20037508.3427892, 20037508.3427892, SRID_MERCATOR);//范围

        List<LevelOfDetail> mainLevelOfDetail = new ArrayList<>();
        for (int i = 0; i < RESOLUTIONS_MERCATOR.length; i++) {
            LevelOfDetail item = new LevelOfDetail(i, RESOLUTIONS_MERCATOR[i], SCALES[i]);
            mainLevelOfDetail.add(item);
        }
        TileInfo mainTileInfo = new TileInfo(
                DPI,
                TileInfo.ImageFormat.PNG,
                mainLevelOfDetail,
                ORIGIN_MERCATOR,
                ORIGIN_MERCATOR.getSpatialReference(),
                tileHeight,
                tileWidth
        );

        switch (mapType) {
            case URL_CN_VECTORBASEMAP:
                return new WebTiledLayer(URL_CN_VECTORBASEMAP, SubDomain, mainTileInfo, env);
            case URL_CN_VECTORBASEMAP_ANNOTATION:
                return new WebTiledLayer(URL_CN_VECTORBASEMAP_ANNOTATION, SubDomain, mainTileInfo, env);
            case URL_CN_TERRAINBASEMAP:
                return new WebTiledLayer(URL_CN_TERRAINBASEMAP, SubDomain, mainTileInfo, env);
            case URL_CN_TERRAINBASEMAP_ANNOTATION:
                return new WebTiledLayer(URL_CN_TERRAINBASEMAP_ANNOTATION, SubDomain, mainTileInfo, env);
            case URL_CN_IMAGEBASEMAP:
                return new WebTiledLayer(URL_CN_IMAGEBASEMAP, SubDomain, mainTileInfo, env);
            case URL_CN_IMAGEBASEMAP_ANNOTATION:
                return new WebTiledLayer(URL_CN_IMAGEBASEMAP_ANNOTATION, SubDomain, mainTileInfo, env);
            default:
                return null;
        }
    }
}
