package com.l.example.demo1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.l.example.adapters_activity.AdaptersActivity;

import butterknife.OnClick;

/**
 * 圆形图片、广告
 */
public class MainActivity extends BaseActivity {

    private String TAG = "123";

    private boolean aBoolean = true;
//    @BindView(R.id.btn1)
//    Button btn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        ButterKnife.bind(MainActivity.this);

    }


    @OnClick({R.id.btn1, R.id.tv1})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                Log.i(TAG, "onclick: ");
                startActivity(new Intent(MainActivity.this, AdaptersActivity.class));
                break;
            case R.id.tv1:
                Toast.makeText(MainActivity.this, "666", Toast.LENGTH_SHORT).show();
                break;
            /**
             * 包括自定义view
             */
            case R.id.btn2:
                startActivity(new Intent(MainActivity.this, CustomViewActivity.class));
                break;
        }
    }


//    @OnClick(R.id.btn1)

}
