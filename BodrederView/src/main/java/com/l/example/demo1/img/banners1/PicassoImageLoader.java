package com.l.example.demo1.img.banners1;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;


/**
 * Created by Administrator on 2016/12/21 0021.
 */

public class PicassoImageLoader implements BannerLayout.ImageLoader {
    @Override
    public void displayImage(Context context, String path, ImageView imageView) {
        Picasso.with(context).load(path).into(imageView);
    }
}
