/*
 * Copyright (C) 2013 Andreas Stuetz <andreas.stuetz@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cctbn.toutiao.widget;

import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cctbn.toutiao.R;

//继承HorizontalScrillView，拥有HorizontalScrillView的超出界面时可左右滑动的功能。
public class PagerSlidingTabStrip extends HorizontalScrollView {
	// 通过 viewpager 的 adapter 可以实现接口IconTabProvider
	public interface IconTabProvider {
		public int getPageIconResId(int position);
	}

	// @formatter:off
	private static final int[] ATTRS = new int[] { android.R.attr.textSize,
			android.R.attr.textColor };
	// @formatter:on

	private LinearLayout.LayoutParams defaultTabLayoutParams;
	private LinearLayout.LayoutParams expandedTabLayoutParams;

	private final PageListener pageListener = new PageListener();
	public OnPageChangeListener delegatePageListener;

	private LinearLayout tabsContainer;
	private ViewPager pager;

	private int tabCount;

	private int currentPosition = 0;
	private float currentPositionOffset = 0f;

	private Paint rectPaint;
	private Paint dividerPaint;

	private int indicatorColor = 0xFF666666;
	private int underlineColor = 0x1A000000;
	private int dividerColor = 0x1A000000;

	private boolean shouldExpand = false;
	private boolean textAllCaps = true;

	private int scrollOffset = 52;
	private int indicatorHeight = 2;
	private int underlineHeight = 2;
	private int dividerPadding = 12;
	private int tabPadding = 24;
	private int dividerWidth = 1;

	private int tabTextSize = 12;
	private int tabTextColor = 0xFF666666;
	private Typeface tabTypeface = null;
	private int tabTypefaceStyle = Typeface.BOLD;

	private int lastScrollX = 0;

	private int tabBackgroundResId = R.drawable.background_tab;

	private Locale locale;

	public PagerSlidingTabStrip(Context context) {
		this(context, null);
	}

	public PagerSlidingTabStrip(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	// 构造方法
	public PagerSlidingTabStrip(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);

		setFillViewport(true);
		setWillNotDraw(false);
		// 动态生成横向的LinearLayout，并添加
		tabsContainer = new LinearLayout(context);
		tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
		tabsContainer.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		addView(tabsContainer);

		DisplayMetrics dm = getResources().getDisplayMetrics();
		// 初始化样式值（指示器，下划线，分隔线和TAB文字等样式）
		scrollOffset = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, scrollOffset, dm);
		indicatorHeight = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, indicatorHeight, dm);
		underlineHeight = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, underlineHeight, dm);
		dividerPadding = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, dividerPadding, dm);
		tabPadding = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, tabPadding, dm);
		dividerWidth = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, dividerWidth, dm);
		tabTextSize = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_SP, tabTextSize, dm);

		// get system attrs (android:textSize and android:textColor)

		TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);

		tabTextSize = a.getDimensionPixelSize(0, tabTextSize);
		tabTextColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsDividerColor, tabTextColor);

		a.recycle();

		// get custom attrs

		a = context.obtainStyledAttributes(attrs,
				R.styleable.PagerSlidingTabStrip);
		// 获取布局文件的配置样式值
		indicatorColor = a.getColor(
				R.styleable.PagerSlidingTabStrip_pstsIndicatorColor,
				indicatorColor);
		underlineColor = a.getColor(
				R.styleable.PagerSlidingTabStrip_pstsUnderlineColor,
				underlineColor);
		dividerColor = a
				.getColor(R.styleable.PagerSlidingTabStrip_pstsDividerColor,
						dividerColor);
		indicatorHeight = a.getDimensionPixelSize(
				R.styleable.PagerSlidingTabStrip_pstsIndicatorHeight,
				indicatorHeight);
		underlineHeight = a.getDimensionPixelSize(
				R.styleable.PagerSlidingTabStrip_pstsUnderlineHeight,
				underlineHeight);
		dividerPadding = a.getDimensionPixelSize(
				R.styleable.PagerSlidingTabStrip_pstsDividerPadding,
				dividerPadding);
		tabPadding = a.getDimensionPixelSize(
				R.styleable.PagerSlidingTabStrip_pstsTabPaddingLeftRight,
				tabPadding);
		tabBackgroundResId = a.getResourceId(
				R.styleable.PagerSlidingTabStrip_pstsTabBackground,
				tabBackgroundResId);
		shouldExpand = a
				.getBoolean(R.styleable.PagerSlidingTabStrip_pstsShouldExpand,
						shouldExpand);
		scrollOffset = a
				.getDimensionPixelSize(
						R.styleable.PagerSlidingTabStrip_pstsScrollOffset,
						scrollOffset);
		textAllCaps = a.getBoolean(
				R.styleable.PagerSlidingTabStrip_pstsTextAllCaps, textAllCaps);

		a.recycle();
		// 指示器画笔
		rectPaint = new Paint();
		rectPaint.setAntiAlias(true);
		rectPaint.setStyle(Style.FILL);
		// 分割线画笔
		dividerPaint = new Paint();
		dividerPaint.setAntiAlias(true);
		dividerPaint.setStrokeWidth(dividerWidth);
		// 创建两个LinearLayout.LayoutParams，一个为适应内容长度
		defaultTabLayoutParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		// 一个为长度比例相同
		expandedTabLayoutParams = new LinearLayout.LayoutParams(0,
				LayoutParams.MATCH_PARENT, 1.0f);

		if (locale == null) {
			locale = getResources().getConfiguration().locale;
		}
	}

	// 绑定ViewPager
	public void setViewPager(ViewPager pager) {
		this.pager = pager;
		// viewpager的adapter不能为空否则会异常，
		if (pager.getAdapter() == null) {
			throw new IllegalStateException(
					"ViewPager does not have adapter instance.");
		}
		// 设置了viewpager的OnPageChangeListener,注意是pagerSlidingTabStrip
		// 内部类PageListener
		pager.setOnPageChangeListener(pageListener);

		notifyDataSetChanged();
	}

	public void setOnPageChangeListener(OnPageChangeListener listener) {
		this.delegatePageListener = listener;
	}

	// 创建TAB导航栏标题，初始化默认第一个标题内容
	public void notifyDataSetChanged() {
		// 移除全部子视图，根据adapter内容addTab
		tabsContainer.removeAllViews();

		tabCount = pager.getAdapter().getCount();

		for (int i = 0; i < tabCount; i++) {
			// 如果是 icon 的话，通过 viewpager 的 adapter 实现接口IconTabProvider 否则是 text
			if (pager.getAdapter() instanceof IconTabProvider) {
				addIconTab(i,
						((IconTabProvider) pager.getAdapter())
								.getPageIconResId(i));
			} else {
				addTextTab(i, pager.getAdapter().getPageTitle(i).toString());
			}

		}

		updateTabStyles();

		getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@SuppressWarnings("deprecation")
					@SuppressLint("NewApi")
					@Override
					public void onGlobalLayout() {

						if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
							getViewTreeObserver().removeGlobalOnLayoutListener(
									this);
						} else {
							getViewTreeObserver().removeOnGlobalLayoutListener(
									this);
						}

						currentPosition = pager.getCurrentItem();
						scrollToChild(currentPosition, 0);
					}
				});

	}

	// 添加文字TAB导航栏内容
	private void addTextTab(final int position, String title) {

		TextView tab = new TextView(getContext());
		tab.setText(title);
		tab.setGravity(Gravity.CENTER);
		tab.setSingleLine();

		addTab(position, tab);
	}

	// 添加图片TAB导航栏内容
	private void addIconTab(final int position, int resId) {

		ImageButton tab = new ImageButton(getContext());
		tab.setImageResource(resId);

		addTab(position, tab);

	}

	private void addTab(final int position, View tab) {
		tab.setFocusable(true);
		tab.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pager.setCurrentItem(position);
			}
		});
		// 设置每个tab左右的padding
		tab.setPadding(tabPadding, 0, tabPadding, 0);
		// 往container里面动态添加tab,哪个TabLayoutParams注意看前文
		tabsContainer
				.addView(tab, position, shouldExpand ? expandedTabLayoutParams
						: defaultTabLayoutParams);
	}

	// 设置每个tab的样式
	@SuppressLint("NewApi")
	private void updateTabStyles() {

		for (int i = 0; i < tabCount; i++) {

			View v = tabsContainer.getChildAt(i);

			v.setBackgroundResource(tabBackgroundResId);
			// 背景、文字大小、颜色、字体、是否大写
			if (v instanceof TextView) {

				TextView tab = (TextView) v;
				tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSize);
				tab.setTypeface(tabTypeface, tabTypefaceStyle);
				tab.setTextColor(tabTextColor);
				// API控制
				// setAllCaps() is only available from API 14, so the upper case
				// is made manually if we are on a
				// pre-ICS-build
				if (textAllCaps) {
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
						tab.setAllCaps(true);
					} else {
						tab.setText(tab.getText().toString()
								.toUpperCase(locale));
					}
				}
			}
		}

	}

	// 滚动到特定位置,调用父类scrollTo，布局超出边界才会起作用
	private void scrollToChild(int position, int offset) {

		if (tabCount == 0) {
			return;
		}

		int newScrollX = tabsContainer.getChildAt(position).getLeft() + offset;

		if (position > 0 || offset > 0) {
			newScrollX -= scrollOffset;
		}
		// 滑动到的位置。
		if (newScrollX != lastScrollX) {
			lastScrollX = newScrollX;
			scrollTo(newScrollX, 0);
		}

	}

	// onDraw方法绘制分隔线，绘制指示器。
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (isInEditMode() || tabCount == 0) {
			return;
		}
		final int height = getHeight();

		// draw indicator line
		// 画笔颜色
		rectPaint.setColor(indicatorColor);

		// default: line below current tab
		View currentTab = tabsContainer.getChildAt(currentPosition);
		float lineLeft = currentTab.getLeft();
		float lineRight = currentTab.getRight();

		// if there is an offset, start interpolating left and right coordinates
		// between current and next tab
		if (currentPositionOffset > 0f && currentPosition < tabCount - 1) {

			View nextTab = tabsContainer.getChildAt(currentPosition + 1);
			final float nextTabLeft = nextTab.getLeft();
			final float nextTabRight = nextTab.getRight();

			lineLeft = (currentPositionOffset * nextTabLeft + (1f - currentPositionOffset)
					* lineLeft);
			lineRight = (currentPositionOffset * nextTabRight + (1f - currentPositionOffset)
					* lineRight);
		}
		// 根据当前tab坐标和下一个tab坐标和currentPositionOffset计算指示器坐标并绘制指示器线
		canvas.drawRect(lineLeft, height - indicatorHeight, lineRight, height,
				rectPaint);

		// draw underline
		// 绘制指示器矩形长条
		rectPaint.setColor(underlineColor);
		canvas.drawRect(0, height - underlineHeight, tabsContainer.getWidth(),
				height, rectPaint);

		// draw divider
		// 绘制各个tab之间的分隔线长度为dividerPadding
		dividerPaint.setColor(dividerColor);
		for (int i = 0; i < tabCount - 1; i++) {
			View tab = tabsContainer.getChildAt(i);
			canvas.drawLine(tab.getRight(), dividerPadding, tab.getRight(),
					height - dividerPadding, dividerPaint);
		}
	}

	// 内部PageListener类，用于监听滑动事件得到当前滑动偏移量和当前tab
	private class PageListener implements OnPageChangeListener {
		// 在此就实现了导航栏会随着ViewPager滑动而滑动
		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
			// 当前 view 的位置也即 tab 的位置
			currentPosition = position;
			// 当前 view 滑动的距离。其中 currentPositionOffset 为 float，介于 0~1 代表相对于 tab
			// 宽偏移的比例
			currentPositionOffset = positionOffset;
			// 根据上面得到的 view 的位置和偏移位置，来同步 tab 的位置和偏移距离。
			scrollToChild(position, (int) (positionOffset * tabsContainer
					.getChildAt(position).getWidth()));
			// 重绘 view，实现 tab 滑动的效果。
			invalidate();
			// 给pagerslidingtab加滑动监听delegatePageListener
			// 下面的 delegatePageListener 就是我们设置的
			// viewpager.setOnPageChangeListener.而现在把它封装在整个 pagerSlidingTabStrip
			// 中，实现 viewpager 滑动的效果。
			if (delegatePageListener != null) {
				delegatePageListener.onPageScrolled(position, positionOffset,
						positionOffsetPixels);
			}
		}

		// 重写onPageScrollStateChanged
		@Override
		public void onPageScrollStateChanged(int state) {
			// 滑动结束。positionOffset 归零
			if (state == ViewPager.SCROLL_STATE_IDLE) {
				scrollToChild(pager.getCurrentItem(), 0);
			}
			// 调用 viewpager.setOnPageChangeListener
			if (delegatePageListener != null) {
				delegatePageListener.onPageScrollStateChanged(state);
			}
		}

		// 重写onPageSelected
		@Override
		public void onPageSelected(int position) {
			// 调用 viewpager.setOnPageChangeListener
			if (delegatePageListener != null) {
				delegatePageListener.onPageSelected(position);
			}
		}

	}

	public void setIndicatorColor(int indicatorColor) {
		this.indicatorColor = indicatorColor;
		invalidate();
	}

	public void setIndicatorColorResource(int resId) {
		this.indicatorColor = getResources().getColor(resId);
		invalidate();
	}

	public int getIndicatorColor() {
		return this.indicatorColor;
	}

	public void setIndicatorHeight(int indicatorLineHeightPx) {
		this.indicatorHeight = indicatorLineHeightPx;
		invalidate();
	}

	public int getIndicatorHeight() {
		return indicatorHeight;
	}

	public void setUnderlineColor(int underlineColor) {
		this.underlineColor = underlineColor;
		invalidate();
	}

	public void setUnderlineColorResource(int resId) {
		this.underlineColor = getResources().getColor(resId);
		invalidate();
	}

	public int getUnderlineColor() {
		return underlineColor;
	}

	public void setDividerColor(int dividerColor) {
		this.dividerColor = dividerColor;
		invalidate();
	}

	public void setDividerColorResource(int resId) {
		this.dividerColor = getResources().getColor(resId);
		invalidate();
	}

	public int getDividerColor() {
		return dividerColor;
	}

	public void setUnderlineHeight(int underlineHeightPx) {
		this.underlineHeight = underlineHeightPx;
		invalidate();
	}

	public int getUnderlineHeight() {
		return underlineHeight;
	}

	public void setDividerPadding(int dividerPaddingPx) {
		this.dividerPadding = dividerPaddingPx;
		invalidate();
	}

	public int getDividerPadding() {
		return dividerPadding;
	}

	public void setScrollOffset(int scrollOffsetPx) {
		this.scrollOffset = scrollOffsetPx;
		invalidate();
	}

	public int getScrollOffset() {
		return scrollOffset;
	}

	public void setShouldExpand(boolean shouldExpand) {
		this.shouldExpand = shouldExpand;
		requestLayout();
	}

	public boolean getShouldExpand() {
		return shouldExpand;
	}

	public boolean isTextAllCaps() {
		return textAllCaps;
	}

	public void setAllCaps(boolean textAllCaps) {
		this.textAllCaps = textAllCaps;
	}

	public void setTextSize(int textSizePx) {
		this.tabTextSize = textSizePx;
		updateTabStyles();
	}

	public int getTextSize() {
		return tabTextSize;
	}

	public void setTextColor(int textColor) {
		this.tabTextColor = textColor;
		updateTabStyles();
	}

	public void setTextColorResource(int resId) {
		this.tabTextColor = getResources().getColor(resId);
		updateTabStyles();
	}

	public int getTextColor() {
		return tabTextColor;
	}

	public void setTypeface(Typeface typeface, int style) {
		this.tabTypeface = typeface;
		this.tabTypefaceStyle = style;
		updateTabStyles();
	}

	public void setTabBackground(int resId) {
		this.tabBackgroundResId = resId;
	}

	public int getTabBackground() {
		return tabBackgroundResId;
	}

	public void setTabPaddingLeftRight(int paddingPx) {
		this.tabPadding = paddingPx;
		updateTabStyles();
	}

	public int getTabPaddingLeftRight() {
		return tabPadding;
	}

	// activity销毁时在onSaveInstanceState记录当前页数，
	// 当onRestoreInstanceState获得当前页数并重新
	// requestLayout进行测量、布局和绘制
	@Override
	public void onRestoreInstanceState(Parcelable state) {
		SavedState savedState = (SavedState) state;
		super.onRestoreInstanceState(savedState.getSuperState());
		currentPosition = savedState.currentPosition;
		requestLayout();
	}

	@Override
	public Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		SavedState savedState = new SavedState(superState);
		savedState.currentPosition = currentPosition;
		return savedState;
	}

	static class SavedState extends BaseSavedState {
		int currentPosition;

		public SavedState(Parcelable superState) {
			super(superState);
		}

		private SavedState(Parcel in) {
			super(in);
			currentPosition = in.readInt();
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			super.writeToParcel(dest, flags);
			dest.writeInt(currentPosition);
		}

		public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
			@Override
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			@Override
			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}

}