package com.ci.criminalinvestigation.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * author: WuJinLi
 * desc  :
 */

public class ActivityCollector {

    public static List<Activity> list = new ArrayList<>();

    public static void AddActivity(Activity activity) {
        list.add(activity);
    }

    public static void removeActivity(Activity activity) {
        list.remove(activity);
    }


    public static void finishAll() {
        for (Activity activity : list) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
