package com.cctbn.toutiao.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.SeekBar;

/**
 * Created by zgjt on 2016/9/7.
 */
public class CustomSeekBar extends SeekBar{

    public CustomSeekBar(Context context) {
        super(context);
    }
    @Override
    public void setThumb(Drawable thumb) {
        super.setThumb(thumb);
    }
}
