package com.ci.criminalinvestigation;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.ci.criminalinvestigation.base.BaseActivity;
import com.ci.criminalinvestigation.contact.AppContact;
import com.ci.criminalinvestigation.net.ApiService;
import com.ci.criminalinvestigation.net.LoginBean;
import com.ci.criminalinvestigation.utils.AppSharePreferenceMgr;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author wujinli
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_username, et_password;
    private Button btn_login, btn_auto_login;
    private String username;
    private String password;
    private boolean isAutoLogin = false;
    private boolean isFirstLogin = true;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initData();
        initView();
        setListener();
        setView();

    }

    private void initData() {
        isAutoLogin = (Boolean) AppSharePreferenceMgr.get(this, "isAutoLogin", false);
        isFirstLogin = (Boolean) AppSharePreferenceMgr.get(this, "isFirstLogin", true);
        username = (String) AppSharePreferenceMgr.get(this, "username", "");
        password = (String) AppSharePreferenceMgr.get(this, "password", "");
    }

    private void initView() {
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
        btn_auto_login = findViewById(R.id.btn_auto_login);
    }

    private void setListener() {
        btn_login.setOnClickListener(this);
        btn_auto_login.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void setView() {
        //第一次登录
        if (isFirstLogin) {
            isFirstLogin = false;
            AppSharePreferenceMgr.put(this, "isFirstLogin", isFirstLogin);
        }

        if (isAutoLogin) {
            //选择自动登陆以后，下次登陆则会进行登陆信息的提取和东路的操作
            username = AppSharePreferenceMgr.get(this, "username", "").toString();
            password = AppSharePreferenceMgr.get(this, "password", "").toString();
            login();
        }

        if (isAutoLogin) {
            btn_auto_login.setBackground(getResources().getDrawable(R.drawable.img_open));
        } else {
            btn_auto_login.setBackground(getResources().getDrawable(R.drawable.img_close));
        }


        if (!TextUtils.isEmpty(username)) {
            et_username.setText(username);
            et_username.setSelection(username.length());
        }
        if (!TextUtils.isEmpty(password)) {
            et_password.setText(password);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login://登陆
                if (checkInput()) {
//                    login();
                    AppSharePreferenceMgr.put(getApplicationContext(), "username", username);
                    AppSharePreferenceMgr.put(getApplicationContext(), "password", password);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra(AppContact.USERNAME_KEY, username);
                    intent.putExtra(AppContact.PASSWORD_KEY, password);
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.btn_auto_login://自动登录
                if (!isAutoLogin) {
                    isAutoLogin = true;
                    btn_auto_login.setBackground(getResources().getDrawable(R.drawable.img_open));
                } else {
                    btn_auto_login.setBackground(getResources().getDrawable(R.drawable.img_close));
                    isAutoLogin = false;
                }
                AppSharePreferenceMgr.put(this, "isAutoLogin", isAutoLogin);
                break;
            default:
                break;
        }
    }


    /**
     * 校验信息正确性
     *
     * @return
     */
    public boolean checkInput() {
        username = et_username.getText().toString().trim();
        password = et_password.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "用户密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    /**
     * 登陆
     */
    public void login() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppContact.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        Call<LoginBean> login = apiService.login(username, password);

        login.enqueue(new Callback<LoginBean>() {
            @Override
            public void onResponse(Call<LoginBean> call, Response<LoginBean> response) {
                LoginBean message = response.body();
                if ("0".equals(message.getMessage())) {
                    AppSharePreferenceMgr.put(getApplicationContext(), "username", username);
                    AppSharePreferenceMgr.put(getApplicationContext(), "password", password);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra(AppContact.USERNAME_KEY, username);
                    intent.putExtra(AppContact.PASSWORD_KEY, password);
                    startActivity(intent);
                    finish();
                } else if ("1".equals(message.getMessage())) {
                    Toast.makeText(getApplicationContext(), "登陆失败,请重新登录", Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<LoginBean> call, Throwable t) {

            }
        });
    }
}
