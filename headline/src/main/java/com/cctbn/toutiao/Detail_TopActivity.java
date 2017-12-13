/**
 * 
 */
package com.cctbn.toutiao;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 *详情页面 webview
 * @author Mry
 * 
 */
public class Detail_TopActivity extends Activity implements OnClickListener {
	private WebView webView_home;
	private String url;
	ProgressDialog myDialog;
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				url = (String) msg.obj;
				webView_home.getSettings().setBlockNetworkImage(false);
				webView_home.loadUrl(url);
				myDialog.dismiss();
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_top);
		findView();
		getDate();
		myDialog.show();
	}

	public void findView() {
		// mTitle.setText(getString(R.string.modle1));
		ImageView share = (ImageView) findViewById(R.id.share);
		webView_home = (WebView) findViewById(R.id.webView_home);
		ImageView back = (ImageView) findViewById(R.id.top_back);
		back.setOnClickListener(this);
		share.setOnClickListener(this);
		TextView titleText = (TextView) findViewById(R.id.top_title);
		titleText.setText(getIntent().getStringExtra("title"));
		WebSettings webSettings = webView_home.getSettings();// webView:
																// 类WebView的实例
		webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		myDialog = new ProgressDialog(this);

	}

	public void getDate() {
		Intent intent = getIntent();
		url = intent.getStringExtra("url");

		new Thread(new Runnable() {
			@Override
			public void run() {
				Message msg = handler.obtainMessage();
				msg.what = 0;
				msg.obj = url;
				handler.sendMessage(msg);
			}
		}).start();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.top_back:
			finish();
			break;

		case R.id.share:
//			SharePopUtils popUtils = new SharePopUtils(this, url,
//					titleText.getText() + "", "中国交通", "");
			break;
		}
	}
}
