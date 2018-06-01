package com.ci.criminalinvestigation.base;

/**
 * author: WuJinLi
 * desc  :
 */

public interface IBaseView {

    void showLoading(String msg);
    void cancleLoading();
    void alertToast(String msg);
}
