package com.cctbn.toutiao;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;

import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.cctbn.toutiao.widget.MediaController;
import com.cctbn.toutiao.widget.VideoView;

/**
 * Created by zgjt on 2016/9/7.
 */
public class VideoFullScreenActivity extends FragmentActivity implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, View.OnTouchListener,MediaController.onClickIsFullScreenListener{

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_play);
        initVideoView();
    }

    /**
     * 初始化videoview播放
     */
    public void initVideoView() {
        //初始化进度条
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        //初始化VideoView
        videoView = (VideoView) findViewById(R.id.videoView);

//        layout = (LinearLayout)view.findViewById(R.id.layout);
        //初始化videoview控制条
        /**
         * 设置view播放控制条
         */
        MediaController mediaController = new MediaController(this);
        //设置videoview的控制条
        videoView.setMediaController(mediaController);
        //设置显示控制条
        mediaController.show(0);
        mediaController.setClickIsFullScreenListener(this);
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
        videoView.setVideoURI(uri);


        TextView topTitle = (TextView) findViewById(R.id.titleTop);
        topTitle.setText(getIntent().getStringExtra("title"));
        ImageView top_back = (ImageView) findViewById(R.id.top_back);
        top_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finishActivity();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //启动视频播放
        videoView.seekTo(getIntent().getIntExtra("position",0));
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
            LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
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
        setVideoViewLayoutParams(1);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
//        videoView.setLayoutParams(params);
        //跳转到暂停时保存的位置
        if (intPositionWhenPause >= 0){
            videoView.seekTo(intPositionWhenPause);
            intPositionWhenPause = -1;
        }

        videoView.start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != videoView) {
            videoView.stopPlayback();
            videoView = null;
        }
    }

    @Override
    public void setOnClickIsFullScreen() {
      finishActivity();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
            finishActivity();
        return super.onKeyDown(keyCode, event);
    }

    public void finishActivity(){
        intPositionWhenPause = videoView.getCurrentPosition();
        Intent intent = new Intent();
        intent.putExtra("position",intPositionWhenPause);
        intent.setClass(VideoFullScreenActivity.this,VideoPlayActivity.class);
        setResult(RESULT_OK,intent);
        finish();
    }
}
