package com.cctbn.toutiao.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cctbn.toutiao.R;

import net.tsz.afinal.FinalBitmap;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings("deprecation")
public class CustomGallery extends Gallery implements
		AdapterView.OnItemClickListener,
		AdapterView.OnItemSelectedListener, OnTouchListener {

	private Context mContext;
	private MyOnItemClickListener mMyOnItemClickListener;

	private int mSwitchTime;

	private Timer mTimer;

	// private LinearLayout mOvalLayout;

	private int curIndex = 0;

	private int oldIndex = 0;

	// private int mFocusedId;

	private TextView textView;

	private int[] mAdsId;

	private List<String> mUris;

	List<ImageView> listImgs;
	private List<String> title;

	public CustomGallery(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public CustomGallery(Context context) {
		super(context);
	}

	public CustomGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
	}


	public void start(Context context, List<String> mris, int[] adsId,
			int switchTime, TextView textView, List<String> title) {
		this.mContext = context;
		this.mUris = mris;
		this.mAdsId = adsId;
		this.mSwitchTime = switchTime;
		this.title = title;
		this.textView = textView;
		// this.mOvalLayout = ovalLayout;
		// this.mFocusedId = focusedId;
		// this.mNormalId = normalId;
		ininImages();
		setAdapter(new AdAdapter());
		this.setOnItemClickListener(this);
		this.setOnTouchListener(this);
		this.setOnItemSelectedListener(this);
		this.setSoundEffectsEnabled(false);
		this.setAnimationDuration(700);
		this.setUnselectedAlpha(1);
		setSpacing(0);
		setSelection((getCount() / 2 / listImgs.size()) * listImgs.size());
		setFocusableInTouchMode(true);
		initOvalLayout();
		startTimer();
	}

	private void ininImages() {
		listImgs = new ArrayList<ImageView>();
		int len = mUris != null ? mUris.size() : mAdsId.length;
		for (int i = 0; i < len; i++) {
			ImageView imageview = new ImageView(mContext); //
			imageview.setScaleType(ImageView.ScaleType.FIT_XY); //
			imageview.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT));
			if (mUris == null) {//
				imageview.setImageResource(mAdsId[i]); //
			} else { //
				FinalBitmap.create(mContext)
						.display(imageview, mUris.get(i), imageview.getWidth(),
								imageview.getHeight(), null, null);
			}
			listImgs.add(imageview);
		}

	}

	private void initOvalLayout() {


	}

	class AdAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			if (listImgs.size() == 1)//
				return listImgs.size();
			return Integer.MAX_VALUE;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return listImgs.get(position % listImgs.size()); //
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		int kEvent;
		if (isScrollingLeft(e1, e2)) { //
			kEvent = KeyEvent.KEYCODE_DPAD_LEFT;
		} else { //
			kEvent = KeyEvent.KEYCODE_DPAD_RIGHT;
		}
		onKeyDown(kEvent, null);
		return true;

	}

	private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2) {
		return e2.getX() > (e1.getX() + 50);
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return super.onScroll(e1, e2, distanceX, distanceY);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (MotionEvent.ACTION_UP == event.getAction()
				|| MotionEvent.ACTION_CANCEL == event.getAction()) {
			startTimer();// ��ʼ�Զ���������
		} else {
			stopTimer();// ֹͣ�Զ���������
		}
		return false;
	}

	/** ͼƬ�л��¼� */
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		curIndex = position % listImgs.size();
		textView.setText(title.get(curIndex));
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		if (mMyOnItemClickListener != null) {
			mMyOnItemClickListener.onItemClick(curIndex);
		}
	}

	public void setMyOnItemClickListener(MyOnItemClickListener listener) {
		mMyOnItemClickListener = listener;
	}

	public interface MyOnItemClickListener {

		void onItemClick(int curIndex);
	}

	public void stopTimer() {
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
	}

	public void startTimer() {
		if (mTimer == null && listImgs.size() > 1 && mSwitchTime > 0) {
			mTimer = new Timer();
			mTimer.schedule(new TimerTask() {
				public void run() {
					handler.sendMessage(handler.obtainMessage(1));
				}
			}, mSwitchTime, mSwitchTime);
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			onScroll(null, null, 1, 0);
			onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
		}
	};
}
