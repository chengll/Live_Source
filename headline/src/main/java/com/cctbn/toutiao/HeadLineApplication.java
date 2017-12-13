package com.cctbn.toutiao;

import android.app.Application;

import com.cctbn.toutiao.utils.EquipmentUtil;

/**
 * Created by zgjt on 2016/8/31.
 */
public class HeadLineApplication extends Application{
    public static HeadLineApplication context;
    public static String sUniquelyCode;// imei
    public static String VERSION = "1.1.8";
    public static int widthPixels;// 屏幕宽度
    public static int hightPixels;// 屏幕高度
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        sUniquelyCode = EquipmentUtil.getUniquely(this);
        widthPixels = context.getResources()
                .getDisplayMetrics().widthPixels;
        hightPixels = context.getResources()
                .getDisplayMetrics().heightPixels;
    }

}
