/*
 * @Title MyAdapterWithCommViewHolder.java
 * @Copyright Copyright 2010-2015 Yann Software Co,.Ltd All Rights Reserved.
 * @Description��
 * @author Yann
 * @date 2015-8-5 ����10:03:45
 * @version 1.0
 */
package com.l.example.adapters;

import java.util.List;

import com.l.example.beans.Bean;
import com.l.example.demo1.R;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.Toast;

/**
 * @author
 * @date
 */
public class MyAdapterWithCommAdapter extends CommonAdapter<Bean> {
    //#
//    private Context mContext;
    //#
    private int position;
    public MyAdapterWithCommAdapter(Context context, List<Bean> datas, int layoutId) {
        super(context, datas, layoutId);
//        this.mContext = context;
        this.position = layoutId;
    }

    /**
     * ʵ��public abstract View getView(int position, View convertView, ViewGroup parent);
     * @see android.widget.Adapter#getView(int, View, android.view.ViewGroup)
     */
/*	@Override
    public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder = ViewHolder.get(mContext, convertView, parent, R.layout.item, position);
		Bean bean = mDatas.get(position);
		
		((TextView)holder.getView(R.id.tv_title)).setText(bean.getTitle());
		((TextView)holder.getView(R.id.tv_desc)).setText(bean.getDesc());
		((TextView)holder.getView(R.id.tv_time)).setText(bean.getTime());
		((TextView)holder.getView(R.id.tv_phone)).setText(bean.getPhone());
		
		return holder.getConvertView();
	}
*/

    /**
     * ʵ��public abstract void convert(ViewHolder holder, T t);
     *
//     * @see com.imooc.baseadapter.utils.CommonAdapter#convert(com.imooc.baseadapter.utils.ViewHolder, Object)
     */
    @Override
    public void convert(ViewHolder holder, final Bean bean) {
/*
		((TextView)holder.getView(R.id.tv_title)).setText(bean.getTitle());
		((TextView)holder.getView(R.id.tv_desc)).setText(bean.getDesc());
		((TextView)holder.getView(R.id.tv_time)).setText(bean.getTime());
		((TextView)holder.getView(R.id.tv_phone)).setText(bean.getPhone());
*/
        holder.setText(R.id.tv_title, bean.getTitle())
                .setText(R.id.tv_desc, bean.getDesc())
                .setText(R.id.tv_time, bean.getTime())
                .setText(R.id.tv_phone, bean.getPhone());

        //#通过此方法设置某一个 view 的点击事件
        holder.getView(R.id.tv_title).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"点击了" + bean.getTitle() ,Toast.LENGTH_SHORT).show();
            }
        });
        /**
         * #通过 getConterView() 方法可获取整个 item 布局文件，对此 item 设置点击事件
         */
        holder.getConvertView().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(mContext,"item" + bean.getTitle() ,Toast.LENGTH_SHORT).show();
            }
        });

        final CheckBox cBox = (CheckBox) (holder.getView(R.id.cb));
        if (cBox != null) {
            cBox.setChecked(bean.isChecked());
            cBox.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    bean.setChecked(cBox.isChecked());
                }
            });
        }
    }
}
