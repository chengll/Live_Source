package com.cctbn.toutiao.newversion;

import com.cctbn.toutiao.R;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


/**
 * 
 * @author czz
 * @createdate 2014-2-19 下午2:35:00
 * @Description: TODO(版本升级核心类)
 */
public class VersionUpgradeActivity extends Activity {

	private ProgressBar progressBar1;
	public static final String DOWNLOAD = "downloadUrl"; // Intent 传值所用key
	public String url = ""; // 升级下载apk所用地址 http://down.apk.hiapk.com/mc/d?i=451
	private TextView textView2;
	private Button button1;
	public static final String LOAD_ACTION = "com.version.upgrade";
	private LoadBroadCastReceiver loadBroadCastReceiver;
	private Intent version_intent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		//  隐藏标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newversion_view);
		init();
		// 注册一个广播接受者
		IntentFilter filter = new IntentFilter(LOAD_ACTION);
		loadBroadCastReceiver = new LoadBroadCastReceiver();
		registerReceiver(loadBroadCastReceiver, filter);
		// 开启一个服务
		VersionService.url = url;
		version_intent = new Intent(this, VersionService.class);
		startService(version_intent);
	}

	private void init() {
		progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
		textView2 = (TextView) findViewById(R.id.textView2);
		button1 = (Button) findViewById(R.id.button1);
		url = getIntent().getStringExtra(DOWNLOAD);
		progressBar1.setMax(100);
		button1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public void onDestroy() {
		unregisterReceiver(loadBroadCastReceiver);
		super.onDestroy();
	}

	// 屏蔽back键
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 
	 * @author czz
	 * @createdate 2014-3-7 下午2:23:23
	 * @Description: TODO(接受广播)
	 */
	public class LoadBroadCastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 处理广播
			if (intent.getAction().equals(LOAD_ACTION)) {
				if (intent.getIntExtra(VersionService.FLAG, 0) == 0) {
					int intExtra = intent.getIntExtra(
							VersionService.INTNEN_NAME, 0);
					progressBar1.setProgress(intExtra);
					textView2.setText(intExtra + "%");
				} else if (intent.getIntExtra(VersionService.FLAG, 1) == 1) {
					// 下载完成之后销毁服务
					finish();
					if (version_intent != null) {
						stopService(version_intent);
					}
				}

			}
		}

	}
}