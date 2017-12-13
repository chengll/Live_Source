package com.l.example.demo1.img;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.l.example.demo1.R;
import com.l.example.demo1.img.banners1.BannerLayout;
import com.l.example.demo1.img.banners1.GlideImageLoader;
import com.l.example.demo1.img.banners1.PicassoImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/21.
 * 功能：圆形图片、
 *      广告轮播图:https://github.com/dongjunkun/BannerLayout
 *      其中：customview.imgs.CircleImageView 的图片类型不能设置为 fitXY （未定义此属性）
 *
 *
 */

public class ImgActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img);

        initBanner1();
    }

    private void initBanner1() {
        BannerLayout bannerLayout = (BannerLayout) findViewById(R.id.banner);
        BannerLayout bannerLayout2 = (BannerLayout) findViewById(R.id.banner2);

        final List<String> urls = new ArrayList<>();
        urls.add("http://img3.imgtn.bdimg.com/it/u=2674591031,2960331950&fm=23&gp=0.jpg");
        urls.add("http://img5.imgtn.bdimg.com/it/u=3639664762,1380171059&fm=23&gp=0.jpg");
        urls.add("http://img0.imgtn.bdimg.com/it/u=1095909580,3513610062&fm=23&gp=0.jpg");
        urls.add("http://img4.imgtn.bdimg.com/it/u=1030604573,1579640549&fm=23&gp=0.jpg");
        urls.add("http://img5.imgtn.bdimg.com/it/u=2583054979,2860372508&fm=23&gp=0.jpg");
        bannerLayout.setImageLoader(new GlideImageLoader());
        bannerLayout.setViewUrls(urls);

        //添加监听事件
        bannerLayout.setOnBannerItemClickListener(new BannerLayout.OnBannerItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(ImgActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
            }
        });

        //低于三张
        final List<String> urls2 = new ArrayList<>();
        urls2.add("http://img3.imgtn.bdimg.com/it/u=2674591031,2960331950&fm=23&gp=0.jpg");
        urls2.add("http://img5.imgtn.bdimg.com/it/u=3639664762,1380171059&fm=23&gp=0.jpg");
        bannerLayout2.setImageLoader(new PicassoImageLoader());
        bannerLayout2.setViewUrls(urls2);
    }
}
