package com.l.example.adapters_activity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.l.example.adapters.MyAdapterWithCommAdapter;
import com.l.example.adapters.MyAdapterWithCommViewHolder;
import com.l.example.beans.Bean;
import com.l.example.demo1.BaseActivity;
import com.l.example.demo1.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/11.
 *
 * 慕课网可复用listview、gridle适配器
 */
public class MukeWebAdapterActivity extends BaseActivity{
    private ListView mListView;
    private List<Bean> mDatas;
    private MyAdapterWithCommViewHolder mAdapterWithCommViewHolder;
    private MyAdapterWithCommAdapter mAdapterWithCommAdapter;


//    private List<Bean1> dates = new ArrayList<Bean1>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mukeweb);

        TextView tvBar = (TextView) findViewById(R.id.tv_topbar);
        tvBar.setText("慕课网");
        initDatas();
        initView();
    }

    /**
     * 初始化数据和适配器
     * @return void
     * @author Yann
     * @date 2015-8-5 下午10:15:03
     */
    private void initDatas()
    {
        mDatas = new ArrayList<Bean>();
        Bean bean = new Bean("Android新技能 Get 1", "Android-打造万能的ListView和GridView适配器", "2015-08-05", "10086");
        mDatas.add(bean);
        bean = new Bean("Android新技能 Get 2", "Android-打造万能的ListView和GridView适配器", "2015-08-05", "10086");
        mDatas.add(bean);
        bean = new Bean("Android新技能 Get 3", "Android-打造万能的ListView和GridView适配器", "2015-08-05", "10086");
        mDatas.add(bean);
        bean = new Bean("Android新技能 Get 4", "Android-打造万能的ListView和GridView适配器", "2015-08-05", "10086");
        mDatas.add(bean);
        bean = new Bean("Android新技能 Get 5", "Android-打造万能的ListView和GridView适配器", "2015-08-05", "10086");
        mDatas.add(bean);

        bean = new Bean("Android新技能 Get 6", "Android-打造万能的ListView和GridView适配器", "2015-08-05", "10086");
        mDatas.add(bean);
        bean = new Bean("Android新技能 Get 7", "Android-打造万能的ListView和GridView适配器", "2015-08-05", "10086");
        mDatas.add(bean);
        bean = new Bean("Android新技能 Get 8", "Android-打造万能的ListView和GridView适配器", "2015-08-05", "10086");
        mDatas.add(bean);
        bean = new Bean("Android新技能 Get 9", "Android-打造万能的ListView和GridView适配器", "2015-08-05", "10086");
        mDatas.add(bean);

        bean = new Bean("Android新技能 Get 10", "Android-打造万能的ListView和GridView适配器", "2015-08-05", "10086");
        mDatas.add(bean);
        bean = new Bean("Android新技能 Get 11", "Android-打造万能的ListView和GridView适配器", "2015-08-05", "10086");
        mDatas.add(bean);
        bean = new Bean("Android新技能 Get 12", "Android-打造万能的ListView和GridView适配器", "2015-08-05", "10086");
        mDatas.add(bean);
        bean = new Bean("Android新技能 Get 13", "Android-打造万能的ListView和GridView适配器", "2015-08-05", "10086");
        mDatas.add(bean);

        mAdapterWithCommViewHolder = new MyAdapterWithCommViewHolder(this,mDatas,R.layout.item);
        mAdapterWithCommAdapter = new MyAdapterWithCommAdapter(this, mDatas, R.layout.item);



//        dates = Bean1.getBean1();
//        adapter1 = new MyAdapter1(this, dates, R.layout.item_layout);
    }
//    MyAdapter1 adapter1;
    /**
     * 为列表设置适配器
     * @return void
     * @author Yann
     * @date 2015-8-5 下午10:15:04
     */
    private void initView()
    {
        mListView = (ListView) findViewById(R.id.listView);
//		mListView.setAdapter(mAdapterWithCommViewHolder);
		mListView.setAdapter(mAdapterWithCommAdapter);
//        mListView.setAdapter(adapter1);
    }
}
