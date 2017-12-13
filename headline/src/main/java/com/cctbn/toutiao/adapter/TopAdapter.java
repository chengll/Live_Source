package com.cctbn.toutiao.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cctbn.toutiao.R;
import com.cctbn.toutiao.beans.BannerVo;
import com.cctbn.toutiao.utils.CheckUtils;

import net.tsz.afinal.FinalBitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zgjt on 2016/8/31.
 */
public class TopAdapter extends BaseAdapter {
    private Context context;
    private int type;
    private List<BannerVo.ListEntity> list = new ArrayList<>();

    public TopAdapter(Context context, int type) {
        super();
        this.context = context;
        this.type = type;
    }

    /**
     * 在原有的数据上添加新数据
     *
     * @param itemList
     */

    public void addItems(ArrayList<BannerVo.ListEntity> itemList) {
        this.list.addAll(itemList);
        notifyDataSetChanged();
    }

    /**
     * 设置为新的数据，旧数据会被清空
     *
     * @param itemList
     */
    public void setItems(ArrayList<BannerVo.ListEntity> itemList) {
        this.list.clear();
        this.list = itemList;
        notifyDataSetChanged();
    }

    /**
     * 清除数据
     */
    public void cleanItem() {
        this.list.clear();
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.home_list_item, null);
            holder.icon = (ImageView) view.findViewById(R.id.home_list_item_icon);
            holder.videoIcon = (ImageView) view.findViewById(R.id.home_list_item_icon_video);
            holder.title = (TextView) view.findViewById(R.id.home_list_item_title);
            holder.name = (TextView) view.findViewById(R.id.home_list_item_name);
            holder.time = (TextView) view.findViewById(R.id.home_list_item_time);
            holder.layout = (FrameLayout) view.findViewById(R.id.framelayout);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        BannerVo.ListEntity topline = (BannerVo.ListEntity) getItem(i);
//		String litpic = topline.getLitpic().toString()
//				.substring(1, topline.getLitpic().toString().length() - 1);

        if (type == 6) {
            holder.videoIcon.setVisibility(View.VISIBLE);

        } else {
            holder.videoIcon.setVisibility(View.GONE);
//            holder.time.setText(CheckUtils.days(topline.getTime()));
        }

        if (topline.getLitpic() == null || topline.getLitpic().size() == 0 || TextUtils.isEmpty(topline.getLitpic().get(0))) {
            holder.layout.setVisibility(View.GONE);
            holder.icon.setVisibility(View.GONE);
        } else {
            holder.layout.setVisibility(View.VISIBLE);
            holder.icon.setVisibility(View.VISIBLE);
            Glide.with(context).load(topline.getLitpic().get(0)).into(holder.icon);
//            FinalBitmap.create(context).display(holder.icon, topline.getLitpic().get(0));
        }


        if (!TextUtils.isEmpty(topline.getTitle())) {
            holder.title.setText(topline.getTitle());
        }

        if (!TextUtils.isEmpty(topline.getSource())) {
            holder.name.setText(topline.getSource());
        }
        if (!TextUtils.isEmpty(topline.getTime()))
            holder.time.setText(CheckUtils.days(topline.getTime()));
//        holder.time.setText(topline.getTime());
        return view;
    }

    class ViewHolder {
        private TextView title;
        private TextView time;
        private TextView name;
        private FrameLayout layout;
        private ImageView icon;
        private ImageView videoIcon;
    }
}
