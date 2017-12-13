/**
 * 
 */
package com.cctbn.toutiao.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * @ClassName: WindowsTools
 * @Description: TODO(窗口工具类)
 * @author lewis
 * @date 
 * 
 */
public class WindowsTools {
	/** 获取屏幕的宽度 */
	public final static int getWindowsWidth(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}
    public static String getVerCode(Context context) {  
        String verCode = null;  
        try {  
            //注意："com.example.try_downloadfile_progress"对应AndroidManifest.xml里的package="……"部分  
            verCode = context.getPackageManager().getPackageInfo(  
                    "com.cctbn.toutiao", 0).versionName;
        } catch (NameNotFoundException e) {  
            Log.e("msg",e.getMessage());  
        }  
        return verCode;  
    }
}
