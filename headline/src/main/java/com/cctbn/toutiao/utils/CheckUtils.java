package com.cctbn.toutiao.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * *
 * 
 * @author mayz
 * @date 创建时间：2016年4月6日 下午1:41:43
 */
public class CheckUtils {


	// 计算时间
	public static String days(String endtime) {
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String begin = df.format(new Date());
			Date star = df.parse(begin);
			Date end = df.parse(endtime);
			long between = (star.getTime() - end.getTime()) / 1000;// 除以1000是为了转换成秒
			long day = between / (24 * 3600);
			long hour = between % (24 * 3600) / 3600;
			long minute = between % 3600 / 60;
			long second = between % 60;
			if (between < 60) {
				return "刚刚";
			} else if (between >= 60 && hour < 1 && day < 1) {
				return minute + "分钟前";
			} else if (hour >= 1 && day < 1) {

				return hour + "小时前";
			} else if (day >= 1 && day < 30) {
				return day + "天前";
			} else {
				return endtime;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}

	}

	/**
	 * 替换html中的所有标签
	 * @param input 输入的html
	 * @param length 字符串的长度
     * @return
     */
	public static String removeHTMLTag(String input, int length) {
		if (input == null || input.trim().equals("")) {
			return "";
		}
		// 去掉所有html元素
		String str = input.replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll("<[^>]*>", "");
		str = str.replaceAll("[(/>)<]", "");
		int len = str.length();
		if (len <= length) {
			return str;
		} else {
			str = str.substring(0, length);
			str += "......";
		}
		return str;
	}


}
