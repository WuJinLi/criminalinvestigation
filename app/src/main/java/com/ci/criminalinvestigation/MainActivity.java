package com.ci.criminalinvestigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.ci.criminalinvestigation.base.BaseActivity;
import com.ci.criminalinvestigation.contact.AppContact;
import com.ci.criminalinvestigation.fragment.LinkManFragment;
import com.ci.criminalinvestigation.fragment.MessageFragment;
import com.ci.criminalinvestigation.fragment.SettingFragment;
import com.ci.criminalinvestigation.fragment.WisdomFragment;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private WisdomFragment wisdomFragment;
    private MessageFragment messageFragment;
    private LinkManFragment linkManFragment;
    private SettingFragment settingFragment;
    private FragmentManager manager;
    private TextView tv_linkman, tv_sms, tv_wisdom, tv_setting;
    private String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        if (intent == null) {
            finish();
        } else {
            username = intent.getStringExtra(AppContact.USERNAME_KEY);
            password = intent.getStringExtra(AppContact.PASSWORD_KEY);
        }
        initView();
        initData();
    }


    private void initData() {
        tv_linkman.setSelected(true);
        setSelection(0);

        tv_linkman.setOnClickListener(this);
        tv_sms.setOnClickListener(this);
        tv_wisdom.setOnClickListener(this);
        tv_setting.setOnClickListener(this);

    }

    private void initView() {
        tv_linkman = findViewById(R.id.tv_linkman);
        tv_sms = findViewById(R.id.tv_sms);
        tv_wisdom = findViewById(R.id.tv_wisdom);
        tv_setting = findViewById(R.id.tv_setting);
        manager = getSupportFragmentManager();
    }

    //自定义方法用于根据对应的按钮下角标，进行响应碎片的展示
    private void setSelection(int tabIndex) {
        //初始化事务，用于处理碎片的显示隐藏
        FragmentTransaction ft = manager.beginTransaction();
        //调用隐藏所有碎片的方法
        hindFragment(ft);
        switch (tabIndex) {
            case 0:
                //判断碎片是否为空，为空进行初始化，创建，不为空，直接显示
                if (linkManFragment == null) {
                    linkManFragment = new LinkManFragment();
                    ft.add(R.id.framelayout_main_container, linkManFragment);
                } else {
                    ft.show(linkManFragment);
                }
                break;
            case 1:
                if (messageFragment == null) {
                    messageFragment = new MessageFragment();
                    ft.add(R.id.framelayout_main_container, messageFragment);
                } else {
                    ft.show(messageFragment);
                }
                Bundle bundle = new Bundle();
                String url = AppContact.MESSAGE_URL_BASE + "account=" + username + "&password=" +
                        password;
                if (TextUtils.isEmpty(url)) {
                    alertToast("访问链接不能为空");
                } else {
                    bundle.putString(AppContact.MESSAGE_KEY, url);
                    messageFragment.setArguments(bundle);
                }
                break;
            case 2:
                if (wisdomFragment == null) {
                    wisdomFragment = new WisdomFragment();
                    ft.add(R.id.framelayout_main_container, wisdomFragment);
                } else {
                    ft.show(wisdomFragment);
                }
                break;
            case 3:
                if (settingFragment == null) {
                    settingFragment = new SettingFragment();
                    ft.add(R.id.framelayout_main_container, settingFragment);
                } else {
                    ft.show(settingFragment);
                }
                break;
        }
        //执行事务
        ft.commit();
    }

    private void hindFragment(FragmentTransaction ft) {
        if (linkManFragment != null) {
            ft.hide(linkManFragment);
        }
        if (messageFragment != null) {
            ft.hide(messageFragment);
        }
        if (wisdomFragment != null) {
            ft.hide(wisdomFragment);
        }
        if (settingFragment != null) {
            ft.hide(settingFragment);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_linkman:
                setSelection(0);
                tv_linkman.setSelected(true);
                tv_sms.setSelected(false);
                tv_wisdom.setSelected(false);
                tv_setting.setSelected(false);
                break;
            case R.id.tv_sms:
                setSelection(1);
                tv_linkman.setSelected(false);
                tv_sms.setSelected(true);
                tv_wisdom.setSelected(false);
                tv_setting.setSelected(false);
                break;
            case R.id.tv_wisdom:
                setSelection(2);
                tv_linkman.setSelected(false);
                tv_sms.setSelected(false);
                tv_wisdom.setSelected(true);
                tv_setting.setSelected(false);
                break;
            case R.id.tv_setting:
                setSelection(3);
                tv_linkman.setSelected(false);
                tv_sms.setSelected(false);
                tv_wisdom.setSelected(false);
                tv_setting.setSelected(true);
                break;
        }
    }
}
