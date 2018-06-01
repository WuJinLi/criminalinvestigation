package com.ci.criminalinvestigation.net;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author: wjl
 * @date:2018/5/26
 */

public interface ApiService {

    //登陆操作
    @GET("mobiletelogin")
    Call<LoginBean> login(@Query("account") String account, @Query("password") String password);

}
