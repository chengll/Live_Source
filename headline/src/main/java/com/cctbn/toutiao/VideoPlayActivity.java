package com.cctbn.toutiao;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.cctbn.toutiao.adapter.TopAdapter;
import com.cctbn.toutiao.beans.BannerVo;
import com.cctbn.toutiao.utils.CheckUtils;
import com.cctbn.toutiao.utils.Constans;
import com.cctbn.toutiao.widget.MediaController;
import com.cctbn.toutiao.widget.VideoView;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.ArrayList;;

/**
 * Created by zgjt on 2016/9/1.
 */
public class VideoPlayActivity extends FragmentActivity implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, View.OnTouchListener, MediaController.onClickIsFullScreenListener {

    /**
     * View播放
     */
    private VideoView videoView;

    /**
     * 加载预览进度条
     */
    private ProgressBar progressBar;

    /**
     * 标记当视频暂停时播放位置
     */
    private int intPositionWhenPause = -1;
    private TopAdapter mAdapter;

    private ListView listView;
    ArrayList<BannerVo.ListEntity> list = new ArrayList<>();
    private int screenWidth;
    private int screenHeight;

   TextView topTitle;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_play);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();
        initVideoView();
    }

    /**
     * 初始化videoview播放
     */
    public void initVideoView() {
        View view = LayoutInflater.from(VideoPlayActivity.this).inflate(R.layout.video_top, null);
        //初始化进度条
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        //初始化VideoView
        videoView = (VideoView) findViewById(R.id.videoView);
        ImageView top_back = (ImageView) findViewById(R.id.top_back);

//        layout = (LinearLayout)view.findViewById(R.id.layout);
        //初始化videoview控制条
        /**
         * 设置view播放控制条
         */
        MediaController mediaController = new MediaController(this);
        mediaController.setClickIsFullScreenListener(this);
        //设置videoview的控制条
        videoView.setMediaController(mediaController);
        //设置显示控制条
        mediaController.show(0);
        //设置播放完成以后监听
        videoView.setOnCompletionListener(this);
        //设置发生错误监听，如果不设置videoview会向用户提示发生错误
        videoView.setOnErrorListener(this);
        //设置在视频文件在加载完毕以后的回调函数
        videoView.setOnPreparedListener(this);
        //设置videoView的点击监听
        videoView.setOnTouchListener(this);
        //设置网络视频路径
        Uri uri = Uri.parse(getIntent().getStringExtra("url"));
        url = getIntent().getStringExtra("url");
        videoView.setVideoURI(uri);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 420);
        videoView.setLayoutParams(params);

        topTitle = (TextView) findViewById(R.id.titleTop);
        final TextView title = (TextView) view.findViewById(R.id.title);
        final TextView time = (TextView) view.findViewById(R.id.time);
        final TextView source = (TextView) view.findViewById(R.id.source);
        final TextView dec = (TextView) view.findViewById(R.id.dec);
        title.setText(getIntent().getStringExtra("title"));
        topTitle.setText(getIntent().getStringExtra("title"));
        time.setText(Html.fromHtml("上传时间:&nbsp;<font color='#333333'>" + getIntent().getStringExtra("time") + "<font/>"));
        source.setText(Html.fromHtml("来源:&nbsp;<font color='#333333'>" + getIntent().getStringExtra("source") + "<font/>"));
        String decStr = CheckUtils.removeHTMLTag(getIntent().getStringExtra("dec"), getIntent().getStringExtra("dec").length());
        dec.setText(Html.fromHtml("简介:&nbsp;<font color='#333333'>" + decStr + "<font/>"));

        listView = (ListView) findViewById(R.id.recommendList);
        listView.addHeaderView(view);

        mAdapter = new TopAdapter(VideoPlayActivity.this, 6);
        listView.setAdapter(mAdapter);
        getList();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    topTitle.setText(list.get(i - 1).getTitle());
                    title.setText(list.get(i - 1).getTitle());
                    time.setText(Html.fromHtml("上传时间:&nbsp;<font color='#333333'>" + list.get(i - 1).getTime() + "<font/>"));
                    source.setText(Html.fromHtml("来源:&nbsp;<font color='#333333'>" + list.get(i - 1).getSource() + "<font/>"));
                    String decStr = CheckUtils.removeHTMLTag(list.get(i - 1).getDescription(), list.get(i - 1).getDescription().length());
                    dec.setText(Html.fromHtml("简介:&nbsp;<font color='#333333'>" + decStr + "<font/>"));
                    progressBar.setVisibility(View.VISIBLE);
                    videoView.stopPlayback();
                    if (list.get(i - 1).getVideo() != null)
                        url = list.get(i - 1).getVideo();
                    videoView.setVideoURI(Uri.parse(list.get(i - 1).getVideo()));
                    videoView.start();
                }
            }
        });

        top_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        //启动视频播放
//        videoView.start();
        //设置获取焦点
        videoView.setFocusable(true);

    }

    /**
     * 设置videiview的全屏和窗口模式
     *
     * @param paramsType 标识 1为全屏模式 2为窗口模式
     */
    public void setVideoViewLayoutParams(int paramsType) {

        if (1 == paramsType) {
            RelativeLayout.LayoutParams LayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
//            LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//            LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//            LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//            LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            videoView.setLayoutParams(LayoutParams);
        } else {
            //动态获取宽高
            DisplayMetrics DisplayMetrics = new DisplayMetrics();
            this.getWindowManager().getDefaultDisplay().getMetrics(DisplayMetrics);
            /**
             * 设置窗口模式下videoview的高度
             */
            int videoHeight = DisplayMetrics.heightPixels - 50;
            /**
             * 设置窗口模式下的videoview的宽度
             */
            int videoWidth = DisplayMetrics.widthPixels - 50;
            RelativeLayout.LayoutParams LayoutParams = new RelativeLayout.LayoutParams(videoWidth, videoHeight);
            LayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            videoView.setLayoutParams(LayoutParams);
        }

    }

    /**
     * 视频播放完成以后调用的回调函数
     */
    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    /**
     * 视频播放发生错误时调用的回调函数
     */
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                Log.e("text", "发生未知错误");

                break;
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                Log.e("text", "媒体服务器死机");
                break;
            default:
                Log.e("text", "onError+" + what);
                break;
        }
        switch (extra) {
            case MediaPlayer.MEDIA_ERROR_IO:
                //io读写错误
                Log.e("text", "文件或网络相关的IO操作错误");
                break;
            case MediaPlayer.MEDIA_ERROR_MALFORMED:
                //文件格式不支持
                Log.e("text", "比特流编码标准或文件不符合相关规范");
                break;
            case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                //一些操作需要太长时间来完成,通常超过3 - 5秒。
                Log.e("text", "操作超时");
                break;
            case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
                //比特流编码标准或文件符合相关规范,但媒体框架不支持该功能
                Log.e("text", "比特流编码标准或文件符合相关规范,但媒体框架不支持该功能");
                break;
            default:
                Log.e("text", "onError+" + extra);
                break;
        }
        //如果未指定回调函数， 或回调函数返回假，VideoView 会通知用户发生了错误。
        return false;
    }

    /**
     * 视频文件加载文成后调用的回调函数
     *
     * @param mp
     */
    @Override
    public void onPrepared(MediaPlayer mp) {
        //如果文件加载成功,隐藏加载进度条
        progressBar.setVisibility(View.GONE);

    }

    /**
     * 对videoView的触摸监听
     *
     * @param v
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }


    /**
     * 页面暂停效果处理
     */
    @Override
    protected void onPause() {
        super.onPause();
        //如果当前页面暂定则保存当前播放位置，并暂停
        intPositionWhenPause = videoView.getCurrentPosition();
//        //停止回放视频文件
//        videoView.stopPlayback();
        videoView.pause();
    }

    /**
     * 页面从暂停中恢复
     */
    @Override
    protected void onResume() {
        super.onResume();
        //跳转到暂停时保存的位置
        if (intPositionWhenPause >= 0) {
            videoView.seekTo(intPositionWhenPause);
            intPositionWhenPause = -1;
        }
        videoView.start();
//        if (intPositionWhenPause >= 0) {
//            videoView.seekTo(intPositionWhenPause);
//            //初始播放位置
//            intPositionWhenPause = -1;
////            videoView.start();
//        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            //切换成竖屏
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 420);
            videoView.setLayoutParams(params);
            listView.setVisibility(View.VISIBLE);

        } else {
            //切换成横屏
            //设置为全屏模式播放
//            listView.setVisibility(View.GONE);
//            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(screenHeight,screenWidth);
//            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//            params.addRule(RelativeLayout.CENTER_IN_PARENT);
//            videoView.setLayoutParams(params);

        }

        if (intPositionWhenPause >= 0) {
            videoView.seekTo(intPositionWhenPause);
            intPositionWhenPause = -1;
        }
        videoView.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != videoView) {
            videoView = null;
        }
    }

    @Override
    public void setOnClickIsFullScreen() {
        intPositionWhenPause = videoView.getCurrentPosition();
        videoView.pause();
//        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {//设置RelativeLayout的全屏模式
            Intent intent = new Intent();
            intent.putExtra("position", intPositionWhenPause);
            intent.putExtra("title",topTitle.getText().toString());
            intent.putExtra("url", url);
            intent.setClass(this, VideoFullScreenActivity.class);
            startActivityForResult(intent, 1);
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        } else {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            if (data != null)
                intPositionWhenPause = data.getIntExtra("position", 0);
        }
    }

    /**
     * 获取推荐列表
     */
    public void getList() {
        AjaxParams params = new AjaxParams();
        params.put("page", getIntent().getIntExtra("position", 1) + "");
        params.put("pagesize", "10");
        String url = Constans.getParamsUrl("queryVideos", params.getParamString());
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
                        mAdapter.addItems((ArrayList<BannerVo.ListEntity>) topline.getList());
                        list.addAll(topline.getList());
                    }
                }
            }
        });
    }
}