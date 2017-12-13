package com.l.example.adapters;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.l.example.beans.Bean1;
import com.l.example.demo1.R;

public class MyAdapter1 extends CommonAdapter<Bean1>{

	public MyAdapter1(Context context, List<Bean1> datas, int layoutId) {
		super(context, datas, layoutId);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void convert(ViewHolder holder, final Bean1 bean) {
		// TODO Auto-generated method stub
		holder.setText(R.id.tv_title, bean.countyName)
				.setText(R.id.tv_desc, bean.countactPerson);
		//#通过此方法设置某一个 view 的点击事件
		holder.getView(R.id.tv_title).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(mContext,"点击了" + bean.countyName ,Toast.LENGTH_SHORT).show();
			}
		});
		/**
		 * #通过 getConterView() 方法可获取整个 item 布局文件，对此 item 设置点击事件
		 */
		holder.getConvertView().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(mContext,"item" + bean.countactPerson ,Toast.LENGTH_SHORT).show();
			}
		});
		
	}

}
