package com.l.example.demo1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.l.example.demo1.img.ImgActivity;

import customview.GuaGuaKaActivity;

/**
 * Created by l on 2017/2/13.
 *
 * 功能：刮刮卡、圆形图片；
 */
public class CustomViewActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customview);

    }

    public void onClick(View view) {
        switch (view.getId()){
            /**
             * 呱呱卡效果
             */
            case R.id.btn1:
                startActivity(new Intent(CustomViewActivity.this, GuaGuaKaActivity.class));
                break;
            /**
             * 圆形图片效果
             */
            case R.id.btn2:
                startActivity(new Intent(CustomViewActivity.this, ImgActivity.class));
                break;
        }
    }
}
