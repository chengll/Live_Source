package com.l.example.adapters_activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.l.example.demo1.BaseActivity;
import com.l.example.demo1.R;

import butterknife.OnClick;

/**
 * Created by Administrator on 2017/1/10.
 *
 * 万能适配器
 *
 */
public class AdaptersActivity extends BaseActivity{
    String str = "{\"RESULT\":{\"userAddresses\":[{\"countyName\":\"新吴区\",\"contactPhone\":\"18762808688\",\"specifyAddress\":\"镇泽路88号\",\"isDefault\":\"Y\",\"cityName\":\"无锡市\",\"addressCode\":\"002\",\"streetName\":\"新安\",\"provinceName\":\"江苏省\",\"contactPerson\":\"张大大\"},{\"countyName\":\"新吴区\",\"contactPhone\":\"18762808688\",\"specifyAddress\":\"镇泽路88号\",\"isDefault\":\"N\",\"cityName\":\"无锡市\",\"addressCode\":\"ADR1482658782264S\",\"streetName\":\"新安\",\"provinceName\":\"江苏省\",\"contactPerson\":\"张大大\"},{\"countyName\":\"新区\",\"contactPhone\":\"18989898898\",\"specifyAddress\":\"震泽路18号\",\"isDefault\":\"N\",\"cityName\":\"无锡\",\"addressCode\":\"001\",\"streetName\":\"新安\",\"provinceName\":\"江苏\",\"contactPerson\":\"张大大\"}]}}";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adapters);
    }


    @OnClick({R.id.btn1,R.id.btn2,R.id.btn3,R.id.btn4})
    public void onclick(View view) {
        switch (view.getId()){
            case R.id.btn1:
                startActivity(new Intent(AdaptersActivity.this,MukeWebAdapterActivity.class));
                break;
            case R.id.btn2:
                startActivity(new Intent(this,MukeWebPractice.class));
                break;
            case R.id.btn3:
                break;
            case R.id.btn4:
                break;

        }
    }
}
