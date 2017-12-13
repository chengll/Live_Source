package com.cctbn.toutiao.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.util.Util;
import com.cctbn.toutiao.HeadLineApplication;

/**
 * Created by zgjt on 2016/8/31.
 */
public class Constans {
    public static final String URL = "http://appapi.cctbn.com:8080/sinot/";//


    public static String getParamsUrl(String queryName, String params) {
        StringBuffer sb = new StringBuffer();
        String url = sb.append(Constans.URL).append(queryName).append("/")
                .append("android").append("/")
                .append(HeadLineApplication.sUniquelyCode).append("/")
                .append(HeadLineApplication.VERSION).append("?").append(params)
                .toString();
        return url;
    }

}
