package com.cctbn.toutiao.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @author zm
 * @Description: SharedPreference
 */
public class SharedPreferenceUtil {
	public static final String SHARED_PREFERENCE_NAME = "traffic"; // SharedPreference操作的文�??

	/**
	 * @Description: 保存int�?
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveInt(Context context, String key, int value) {
		Editor editor = context.getSharedPreferences(
				SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
		editor.putInt(key, value);
		editor.commit();
	}

	/**
	 * @Description: 获取保存的int�?
	 * @param context
	 * @param key
	 * @return
	 */
	public static int getInt(Context context, String key) {
		SharedPreferences shared = context.getSharedPreferences(
				SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		int value = shared.getInt(key, 0);
		return value;
	}

	/**
	 * @Description: 保存long�?
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveLong(Context context, String key, long value) {
		Editor editor = context.getSharedPreferences(
				SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
		editor.putLong(key, value);
		editor.commit();
	}

	/**
	 * @Description: 获取保存的long�?
	 * @param context
	 * @param key
	 * @return
	 */
	public static long getLong(Context context, String key) {
		SharedPreferences shared = context.getSharedPreferences(
				SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		long value = shared.getLong(key, 0L);
		return value;
	}

	/**
	 * @Description: 保存boolean
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveBoolean(Context context, String key, boolean value) {
		Editor editor = context.getSharedPreferences(
				SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	/**
	 * @Description: 获取boolean
	 * @param context
	 * @param key
	 * @return
	 */
	public static boolean getBoolean(Context context, String key) {
		SharedPreferences shared = context.getSharedPreferences(
				SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		boolean value = shared.getBoolean(key, false);
		return value;
	}

	/**
	 * @Description: 保存String�?
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveString(Context context, String key, String value) {
		Editor editor = context.getSharedPreferences(
				SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
		editor.putString(key, value);
		editor.commit();
	}

	/**
	 * @Description: 获取保存的String�?
	 * @param context
	 * @param key
	 * @return
	 */
	public static String getString(Context context, String key) {
		SharedPreferences shared = context.getSharedPreferences(
				SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		String value = shared.getString(key, "");
		return value;
	}

	/**
	 * 
	 * @Description 清空本地的缓�?
	 * @param context
	 * @param key
	 */
	public static void removeString(Context context, String key) {
		SharedPreferences shared = context.getSharedPreferences(
				SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		Editor editor = shared.edit();
		editor.remove(key);
		editor.commit();
	}

}
