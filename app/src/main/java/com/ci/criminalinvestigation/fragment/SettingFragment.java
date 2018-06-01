package com.ci.criminalinvestigation.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.ci.criminalinvestigation.LoginActivity;
import com.ci.criminalinvestigation.R;
import com.ci.criminalinvestigation.base.BaseFragment;
import com.ci.criminalinvestigation.utils.AppSharePreferenceMgr;
import com.ci.criminalinvestigation.utils.NetWorkUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * @author wujinli
 */
public class SettingFragment extends BaseFragment implements View.OnClickListener {
    private LinearLayout ll_location, ll_alter, ll_auto_login, ll_login_out;
    private boolean isAutoLogin = false;
    public LocationClient locationClient;
    public MyLocationListener listener;
    List<String> list = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isAutoLogin = (Boolean) AppSharePreferenceMgr.get(getActivity(), "isAutoLogin", false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        //初始化控件
        ll_location = view.findViewById(R.id.ll_location);
        ll_alter = view.findViewById(R.id.ll_alter);
        ll_auto_login = view.findViewById(R.id.ll_auto_login);
        ll_login_out = view.findViewById(R.id.ll_login_out);

        //退出自动登陆布局的现示
        if (isAutoLogin) {
            ll_auto_login.setVisibility(View.VISIBLE);
        } else {
            ll_auto_login.setVisibility(View.GONE);
        }

        ll_auto_login.setOnClickListener(this);
        ll_login_out.setOnClickListener(this);
        ll_location.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_auto_login://取消自动登陆
                isAutoLogin = false;
                AppSharePreferenceMgr.put(getActivity(), "isAutoLogin", false);
                ll_auto_login.setVisibility(View.GONE);
                break;
            case R.id.ll_login_out://退出登录
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                break;

            case R.id.ll_location:
                if (!list.isEmpty()) {//没有权限就添加
                    String[] permissions = list.toArray(new String[list.size()]);
                    //如果list不为空，就调用ActivityCompat
                    // .requestPermissions添加权限
                    ActivityCompat.requestPermissions(getActivity(), permissions, 1);
                } else {//有相关权限则执行程序
                    requestLocation();
                }
//                startActivity(new Intent(getActivity(), LocationInfoActivity.class));
                break;
            default:
                break;
        }
    }


    /**
     * 初始化数据
     */
    private void initData() {
        locationClient = new LocationClient(getActivity());
        listener = new MyLocationListener();
        locationClient.registerLocationListener(listener);
        //运行时权限，没有注册就重新注册
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission
                .ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            list.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission
                .READ_PHONE_STATE) !=
                PackageManager.PERMISSION_GRANTED) {
            list.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission
                .WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            list.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    /**
     * 请求定位信息
     */
    private void requestLocation() {

        LocationClientOption option = new LocationClientOption();
        if (NetWorkUtils.isNetworkAvailable(getActivity())) {
            option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        } else {
            option.setOpenGps(true);
            option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
        }
        option.setLocationNotify(true);
        option.setIsNeedAddress(true);
        option.setScanSpan(1000); //设置发起定位请求的间隔时间为5000ms
        locationClient.setLocOption(option);
        showLoading("加载中");
        locationClient.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[]
                                                   grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(getActivity(), "用户取消权限，程序运行失败", Toast.LENGTH_SHORT)
                                    .show();
                            return;
                        }
                    }
                    requestLocation();//调用定位
                } else {
                    Toast.makeText(getActivity(), "发生未知错误", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }


    /**
     * 定位监听器
     */
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            {//定位信息获取
                StringBuilder currentPosition = new StringBuilder();
                currentPosition.append("纬度：").append(bdLocation.getLatitude()).append("\n");
                currentPosition.append("经线：").append(bdLocation.getLongitude()).append("\n");
                currentPosition.append("城市code：").append(bdLocation.getAdCode()).append("\n");
                currentPosition.append("城市：").append(bdLocation.getCity()).append("\n");
                currentPosition.append("城市编码：").append(bdLocation.getCityCode()).append("\n");
                currentPosition.append("定位方式：");
                if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {
                    currentPosition.append("GPS");
                } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                    currentPosition.append("网络");
                } else {
                    currentPosition.append("Error:" + bdLocation.getLocType());
                }
                alertToast(currentPosition.toString());
                locationClient.stop();
                cancleLoading();
            }
        }
    }

}
