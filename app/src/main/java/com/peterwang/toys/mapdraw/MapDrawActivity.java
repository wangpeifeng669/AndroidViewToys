package com.peterwang.toys.mapdraw;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.Projection;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.PolygonOptions;
import com.peterwang.toys.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 地图电子围栏
 *
 * @author peter_wang
 * @create-time 15/11/4 08:23
 */
public class MapDrawActivity extends Activity implements MapPathDrawView.PathPointSelectListener {
    /**
     * 上海市经纬度
     */
    private static final LatLng SHANGHAI = new LatLng(31.238068, 121.501654);

    private AMap mAMap;
    private MapView mMapView;
    /**
     * 手势画电子围栏View
     */
    private MapPathDrawView mMapPathDrawView;

    private List<ST_DPoint> mPathList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_draw);
        setupViews(savedInstanceState);
        init();
    }

    private void setupViews(Bundle savedInstanceState) {
        mMapView = (MapView) this.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);// 此方法必须重写
        mMapPathDrawView = (MapPathDrawView) this.findViewById(R.id.path_draw_view);
    }

    private void init() {
        if (mAMap == null) {
            mAMap = mMapView.getMap();
            setUpMap();
        }

        mMapPathDrawView.setPathPointSelectListener(this);
    }

    private void setUpMap() {
        mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SHANGHAI, 15));// 设置指定的可视区域地图
        mAMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (mPathList.size() == 0) {
                    Toast.makeText(MapDrawActivity.this, "请先在面板上画围栏", Toast.LENGTH_SHORT).show();
                } else {
                    if (MapUtils.IsPointInPolygon(latLng.latitude, latLng.longitude, mPathList)) {
                        Toast.makeText(MapDrawActivity.this, "在安全范围内", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MapDrawActivity.this, "不在安全范围内", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void showPathDrawView(View view) {
        mMapPathDrawView.setVisibility(View.VISIBLE);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onPathSelect(List<MapPathDrawView.PathPoint> pathPointList) {
        //是否绘制成功
        boolean isSuccess = true;
        Projection projection = mAMap.getProjection();
        PolygonOptions options = new PolygonOptions();
        mPathList.clear();
        for (int i = 0; i < pathPointList.size(); i++) {
            int x = (int) (pathPointList.get(i).x - (mMapPathDrawView.getLeft() - mMapView.getLeft()));
            int y = (int) (pathPointList.get(i).y - (mMapPathDrawView.getTop() - mMapView.getTop()));
            if (x < 0 || y < 0) {
                isSuccess = false;
            } else {
                LatLng latLng = projection.fromScreenLocation(new Point(x, y));
                ST_DPoint point = new ST_DPoint();
                point.SetX(latLng.latitude);
                point.SetY(latLng.longitude);
                mPathList.add(point);
                options.add(latLng);
            }
        }
        if (isSuccess) {
            mAMap.clear();
            // 绘制路径
            mAMap.addPolygon(options.strokeWidth(15)
                    .strokeColor(Color.argb(50, 1, 1, 1))
                    .fillColor(Color.argb(50, 1, 1, 1)));
        } else {
            Toast.makeText(this, "绘制路径超过地图范围", Toast.LENGTH_SHORT).show();
        }

        mMapPathDrawView.setVisibility(View.GONE);
    }
}
