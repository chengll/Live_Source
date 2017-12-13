package com.cctbn.toutiao;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.cctbn.toutiao.adapter.FragmetViewPagerAdapter;
import com.cctbn.toutiao.beans.UpdateVo;
import com.cctbn.toutiao.fragment.TopFragment;
import com.cctbn.toutiao.newversion.VersionUpgradeActivity;
import com.cctbn.toutiao.utils.WindowsTools;
import com.cctbn.toutiao.widget.PagerSlidingTabStrip;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {
    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private FragmetViewPagerAdapter adapter;
    private List<Fragment> fragments;

    UpdateVo list = new UpdateVo();
    Dialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType. E_UM_NORMAL);
        setContentView(R.layout.activity_main);
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // 设置Tab是自动填充满屏幕的
         tabs.setShouldExpand(true);
        // 设置Tab的分割线是透明的
         tabs.setDividerColor(Color.TRANSPARENT);
        // 设置Tab Indicator的颜色
        tabs.setUnderlineColor(Color.TRANSPARENT);
        tabs.setIndicatorColor(Color.parseColor("#F8404A"));
//         tabs.setTextColor(getResources()
//         .getColor(R.color.tab_text_normal_color));
        // 设置选中Tab文字的颜色 (这是我自定义的一个方法)
//         tabs.setSelectedTextColor(getResources().getColor(R.color.daohang));
        // 取消点击Tab时的背景色
         tabs.setTabBackground(0);
        pager = (ViewPager) findViewById(R.id.pager);

        fragments = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            if (i == 0) {
                initFragment(0);
            } else if (i == 1) {
                initFragment(6);
            } else {
                initFragment(i - 1);
            }

        }
        adapter = new FragmetViewPagerAdapter(getSupportFragmentManager(), pager, fragments);

        pager.setAdapter(adapter);
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        pager.setPageMargin(pageMargin);

        tabs.setViewPager(pager);

        getCode();
    }

    /**
     * 判断是否需要更新
     */
    private void getCode() {
        // 网络请求
        final String url = "HTTP://www.cctbn.com/android/tops/version";
        FinalHttp fh = new FinalHttp();
        fh.get(url, new AjaxCallBack<Object>() {
            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                // TODO Auto-generated method stub
                super.onFailure(t, errorNo, strMsg);
            }

            @Override
            public void onLoading(long count, long current) {
                // TODO Auto-generated method stub
                super.onLoading(count, current);
            }

            @Override
            public void onStart() {
                // TODO Auto-generated method stub
                super.onStart();
            }

            //版本更新
            @Override
            public void onSuccess(Object t) {
                // TODO Auto-generated method stub
                String str1 = WindowsTools.getVerCode(getApplicationContext());
                String str = str1.replace(".", "");
                int verCode = Integer.parseInt(str);
                list = JSON.parseObject(t.toString(), UpdateVo.class);
//				String Code = list.getVersion().replace(".", "");
                if(Integer.parseInt(list.getVersion().replace(".", "")) > (verCode)){
                    mDialog = new AlertDialog.Builder(MainActivity.this).setTitle("软件更新").setMessage(list.getDes()).setPositiveButton("更新软件", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            Intent intent = new Intent();
                            intent.putExtra(VersionUpgradeActivity.DOWNLOAD,list.getDownload());
                            intent.setClass(MainActivity.this, VersionUpgradeActivity.class);
                            startActivity(intent);
                        }
                    }).setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            mDialog.dismiss();
                        }
                    }).create();
                    mDialog.show();
                }
                super.onSuccess(t);
            }
        });

    }
    private long mExitTime;
    //退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "在按一次退出", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        // 拦截MENU按钮点击事件，让他无任何操作
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void initFragment(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        TopFragment fragment = new TopFragment();
        fragment.setArguments(bundle);
        fragments.add(fragment);
    }

}
