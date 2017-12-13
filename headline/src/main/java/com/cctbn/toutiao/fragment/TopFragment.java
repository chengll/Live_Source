package com.cctbn.toutiao.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.view.listener.AbOnListViewListener;
import com.ab.view.pullview.AbPullListView;
import com.alibaba.fastjson.JSON;
import com.cctbn.toutiao.Detail_TopActivity;
import com.cctbn.toutiao.R;
import com.cctbn.toutiao.VideoPlayActivity;
import com.cctbn.toutiao.adapter.TopAdapter;
import com.cctbn.toutiao.beans.BannerVo;
import com.cctbn.toutiao.utils.Constans;
import com.cctbn.toutiao.widget.CustomGallery;
import com.cctbn.toutiao.widget.CustomGallery.MyOnItemClickListener;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by zgjt on 2016/8/31.
 */
public class TopFragment extends Fragment implements AbOnListViewListener, AdapterView.OnItemClickListener , MyOnItemClickListener{
    private AbPullListView listView;
    private View banner;
    private CustomGallery gallery;
    private TextView bannerTitle;
    private TopAdapter mAdapter;
    int type = 0;
    private List<BannerVo.ListEntity> lunboList = new ArrayList<>();
    private List<String> mris = new ArrayList<>();
    private List<String> titleName = new ArrayList<>();
    private int[] imageId = new int[]{R.mipmap.icon_default};

    private int page = 1;
    private boolean flag = false;

    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_layout, null);
        initView(v);
        return v;
    }


    public void initView(View v) {
        listView = (AbPullListView) v.findViewById(R.id.home_listView);
        banner = LayoutInflater.from(getActivity()).inflate(R.layout.banner_layout, null);
        gallery = (CustomGallery) banner.findViewById(R.id.lunbo_Viewpager);
        bannerTitle = (TextView) banner.findViewById(R.id.lunbo_title);

        WindowManager wm = getActivity().getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, width * 7 / 15);
        gallery.setLayoutParams(params);
        listView.addHeaderView(banner);
        Bundle bundle = getArguments();
        if (bundle != null)
            type = bundle.getInt("type");
        mAdapter = new TopAdapter(getActivity(), type);
        listView.setAdapter(mAdapter);
        if (type != 6){
            getList(type, true);
        }else{
            gallery.setVisibility(View.GONE);
            bannerTitle.setVisibility(View.GONE);
        }
        getList(type, false);
        listView.setAbOnListViewListener(this);
        listView.setOnItemClickListener(this);
        gallery.setMyOnItemClickListener(this);
    }


    /**
     * @param type
     * @param isBanner 是否是轮播图
     */
    public void getList(int type, final boolean isBanner) {
        if (!isBanner) {
            listView.setPullLoadEnable(true);
            listView.setPullRefreshEnable(true);
        } else {
            listView.setPullLoadEnable(false);
            listView.setPullRefreshEnable(false);
        }
        AjaxParams params = new AjaxParams();
        params.put("page", page + "");
        params.put("pagesize", "25");
        String url;
        if (type == 6) {
            //请求视频
            url = Constans.getParamsUrl("queryVideos", params.getParamString());
        } else {
            if (isBanner) {
                //是轮播图
                params.put("istop", "0101");
            } else {
                params.put("istop", "0102");
            }
            if (type != 0)
                params.put("newstype", "060" + type);
            url = Constans.getParamsUrl("queryTops", params.getParamString());
        }

        FinalHttp finalHttp = new FinalHttp();
        finalHttp.get(url, new AjaxCallBack<String>() {
            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                BannerVo topline = JSON.parseObject(s, BannerVo.class);
                if (topline != null) {
                    if (topline.getCode().equals("0000")) {
                        if (isBanner) {
                            List<BannerVo.ListEntity> list = topline.getList();
                            lunboList.clear();
                            lunboList.addAll(list);
                            if (list == null || list.size() == 0) {
                                gallery.setVisibility(View.GONE);
                                bannerTitle.setVisibility(View.GONE);
                            } else {
                                gallery.setVisibility(View.VISIBLE);
                                banner.setVisibility(View.VISIBLE);
                                bannerTitle.setVisibility(View.VISIBLE);
                                mris.clear();
                                for (int i = 0; i < list.size(); i++) {
                                    mris.add(list.get(i).getLitpic().get(0));
                                    titleName.add(list.get(i).getTitle());
                                }
                                gallery.start(getActivity(), mris, imageId, 3000, bannerTitle, titleName);
                            }
                        } else {
                            if (flag){
                                listView.stopRefresh();
                                flag = true;
                            }
                            mAdapter.addItems((ArrayList<BannerVo.ListEntity>) topline.getList());
                        }

                    } else {
//                                showToast(topline.getResMsg());
                    }
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        this.page = 1;
        mAdapter.cleanItem();
        getList(type, false);
        flag = true;
    }

    @Override
    public void onLoadMore() {
        this.page++;
        getList(type, false);

    }

    // 轮播图点击事件
    @Override
    public void onItemClick(int curIndex) {
        BannerVo.ListEntity entity = lunboList.get(curIndex);
        String url = entity.getHtmlurl();
        Intent intent = new Intent(getActivity(), Detail_TopActivity.class);
        intent.putExtra("title", entity.getTitle());
        intent.putExtra("url", url);
        startActivity(intent);
    }

    // listview 点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            BannerVo.ListEntity topline = (BannerVo.ListEntity)mAdapter
                    .getItem(position - 2);
            if (type == 6){
                Intent intent = new Intent(getActivity(), VideoPlayActivity.class);
                intent.putExtra("url", topline.getVideo());
                intent.putExtra("title", topline.getTitle());
                intent.putExtra("time", topline.getTime());
                intent.putExtra("source", topline.getSource());
                intent.putExtra("dec", topline.getDescription());
                if (page > 1){
                    intent.putExtra("position",page-1);
                }else{
                    intent.putExtra("position",page);
                }

                startActivity(intent);
            }else{
                String url = topline.getHtmlurl();
                Intent intent = new Intent(getActivity(), Detail_TopActivity.class);
                intent.putExtra("url", url);
                intent.putExtra("title", topline.getTitle());
                startActivity(intent);
            }
        }else{
            Toast.makeText(getActivity(),"请求过于频繁，请稍后重试",Toast.LENGTH_SHORT);
        }

    }
}
