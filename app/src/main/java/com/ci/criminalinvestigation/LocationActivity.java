package com.ci.criminalinvestigation;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.widget.TextView;

import com.ci.criminalinvestigation.base.BaseActivity;


/**
 * @author: wjl
 * @date:2018/5/27
 * @desc 该类现在废弃。待使用
 */

public class LocationActivity extends BaseActivity {
    private TextView tv_info;
    LocationManager locationManager;
    public Context context = this;

    @SuppressWarnings("static-access")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_location);
        tv_info = findViewById(R.id.tv_info);
        getGPSLocationMsg();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //授权通过
                getGPSLocationMsg();
            } else {
                alertToast("需要去设置开启定位权限");
            }
        }
    }


    /**
     * 获取定位信息
     */
    public void getGPSLocationMsg() {
        //初始化locationManager
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager != null) {
            //检查定位权限
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission
                    .ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat
                    .checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                //未授权进行动态授权
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission
                        .ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 123);
                return;
            }

            //授权成功进行定位信息的获取
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            updateView(location);
            //每3秒获取一次GPS的定位信息
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 8, new
                    LocationListener() {

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {
                            // 当GPS LocationProvider可用时，更新位置
                            if (ActivityCompat.checkSelfPermission(context, Manifest.permission
                                    .ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                                    ActivityCompat.checkSelfPermission(context, Manifest.permission
                                            .ACCESS_COARSE_LOCATION) != PackageManager
                                            .PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(LocationActivity.this, new
                                        String[]{Manifest.permission
                                        .ACCESS_FINE_LOCATION, Manifest.permission
                                        .ACCESS_COARSE_LOCATION}, 123);
                                return;
                            }
                            updateView(locationManager.getLastKnownLocation(provider));
                        }

                        @Override
                        public void onProviderDisabled(String provider) {
                            if (ActivityCompat.checkSelfPermission(context, Manifest.permission
                                    .ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                                    ActivityCompat.checkSelfPermission(context, Manifest.permission
                                            .ACCESS_COARSE_LOCATION) != PackageManager
                                            .PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(LocationActivity.this, new
                                        String[]{Manifest.permission
                                        .ACCESS_FINE_LOCATION, Manifest.permission
                                        .ACCESS_COARSE_LOCATION}, 123);
                                return;
                            }
                            updateView(locationManager.getLastKnownLocation(null));
                        }

                        @Override
                        public void onLocationChanged(Location location) {
                            //GPS定位信息发生改变时，更新位置
                            updateView(location);
                        }
                    });
        }
    }


    /**
     * 更新信息显示
     *
     * @param location
     */
    private void updateView(Location location) {
        if (location != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("实时的位置信息：\n");
            sb.append("经度：");
            sb.append(location.getLongitude());
            sb.append("\n纬度：");
            sb.append(location.getLatitude());
            sb.append("\n高度：");
            sb.append(location.getAltitude());
            sb.append("\n速度：");
            sb.append(location.getSpeed());
            sb.append("\n方向：");
            sb.append(location.getBearing());
            tv_info.setText(sb.toString());
        } else {
            tv_info.setText("");
        }
    }


}
