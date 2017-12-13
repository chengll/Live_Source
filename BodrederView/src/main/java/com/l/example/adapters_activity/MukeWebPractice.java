package com.l.example.adapters_activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.l.example.adapters.MyAdapter1;
import com.l.example.beans.Bean1;
import com.l.example.demo1.BaseActivity;
import com.l.example.demo1.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/11.
 */
public class MukeWebPractice extends BaseActivity{
    private List<Bean1> dates = new ArrayList<Bean1>();
    private ListView mListView;
    private MyAdapter1 adapter1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mukeweb);

        TextView tvBar = (TextView) findViewById(R.id.tv_topbar);
        tvBar.setText("Practice");

        dates = Bean1.getBean1();
        adapter1 = new MyAdapter1(this, dates, R.layout.item_layout);

        mListView = (ListView) findViewById(R.id.listView);
        mListView.setAdapter(adapter1);

    }
}






















