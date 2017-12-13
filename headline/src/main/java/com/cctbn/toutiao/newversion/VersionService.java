package com.cctbn.toutiao.newversion;

import java.io.File;
import com.cctbn.toutiao.R;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.widget.RemoteViews;
import android.widget.Toast;

public class VersionService extends Service {
	public static String url;
	private String saveFileStr = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/"; // apk下载保存地址
	private File f;
	private int id = 0;
	public static String INTNEN_NAME = "num";
	public static String FLAG = "flag";
	private Handler handler;  
	private Runnable runnable;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		flags = START_REDELIVER_INTENT;
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		// 下载apk
		FinalHttp fh = new FinalHttp();
		InitNotification();
		fh.configRequestExecutionRetryCount(10);
		fh.configTimeout(Integer.MAX_VALUE);
		handler = new Handler();

		handler.postDelayed(runnable, 1000);
		if (!VersionUitls.ExistSDCard()) { // 判断sd卡是否存在
			saveFileStr = getFilesDir().getParent() + File.separator + "files"
					+ File.separator;
		}
		f = new File(saveFileStr);
		if (!f.exists()) {
			f.mkdirs();
		}
			fh.download(url, saveFileStr + "versionupgrade.apk", callback);
	}

	private void InitNotification() {
		nfm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		nfn = new Notification(R.mipmap.app, "正在下载", System.currentTimeMillis() + 20);
		rv = new RemoteViews(getPackageName(), R.layout.newversion_view_1);
		rv.setTextViewText(R.id.textView1, getString(R.string.app_name)
				+ "正在升级");
		// 第五步：使用NotificationManager类的notify方法显示Notification消息。在这一步需要指定标识Notification的唯一Id。这个Id必须相对于同一个NotificationManager对象是唯一的，否则就会覆盖相同Id的Notification
		nfn.flags = Notification.FLAG_AUTO_CANCEL; // 点击完之后自动消失
		nfn.contentView = rv;
		nfm.notify(id, nfn);// 处理通知
	}

	private void LoadingSet_Notification(int num) {
		rv.setTextViewText(R.id.textView2, num + "%");
		rv.setProgressBar(R.id.progressBar1, 100, num, false);
		nfn.contentView = rv;
		nfm.notify(id, nfn);// 处理通知
	}

	@SuppressWarnings("deprecation")
	private void Loading_Success_Set_Notification(File t) {
		rv = new RemoteViews(getPackageName(), R.layout.update_success_view_1);
		nfn = new Notification(R.mipmap.app,
				getString(R.string.app_name) + "下载完成",
				System.currentTimeMillis() + 20);
		rv.setTextViewText(R.id.textView1, getString(R.string.app_name)
				+ "下载完成！");
		nfn.contentView = rv;
		nfn.flags = Notification.FLAG_AUTO_CANCEL; // 点击完之后自动消失
		PendingIntent PendingIntentintent = PendingIntent.getActivity(
				VersionService.this, 0, VersionUitls.installApk(t), 0);
		nfn.contentIntent = PendingIntentintent;
		nfm.notify(id, nfn);// 处理通知
		// 安装应用之前判断 该apk是保存位置
	}

	AjaxCallBack<File> callback = new AjaxCallBack<File>() {
		@Override
		public void onLoading(long count, long current) {
			super.onLoading(count, current);
			int num = (int) (current * 100 / count);
			LoadingSet_Notification(num);

			// 更新界面
			Intent intent = new Intent();
			intent.putExtra(FLAG, 0);
			intent.setAction(VersionUpgradeActivity.LOAD_ACTION);
			intent.putExtra(INTNEN_NAME, num);
			sendBroadcast(intent);
		}

		public void onSuccess(File t) {
			if (t.exists() && t.isFile()) {
				handler.removeCallbacks(runnable);
				Loading_Success_Set_Notification(t);
				// 1、sd卡 正常安装 2、 手机内存 注：下载到手机内存里面的apk文件 ，需要手动的修改该文件的权限
				if (!VersionUitls.ExistSDCard()) {
					VersionUitls.exec(t.toString());
				}
				stopSelf();
				Intent installApk = VersionUitls.installApk(t);
				installApk.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(installApk);
				FinishActivity();
			}
		}

		private void FinishActivity() {
			Intent intent = new Intent();
			intent.putExtra(FLAG, 1);
			intent.setAction(VersionUpgradeActivity.LOAD_ACTION);
			sendBroadcast(intent);
		}

		@Override
		public void onFailure(Throwable t, int errorNo, String strMsg) {
			super.onFailure(t, errorNo, strMsg);
			handler.removeCallbacks(runnable);
			FinishActivity();
			nfm.cancel(id);
			stopSelf();
			Toast.makeText(VersionService.this, "下载失败"+errorNo+"--"+strMsg, Toast.LENGTH_SHORT)
					.show();
		};
	};
	private RemoteViews rv;
	private Notification nfn;
	private NotificationManager nfm;
}
