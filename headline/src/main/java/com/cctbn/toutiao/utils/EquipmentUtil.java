package com.cctbn.toutiao.utils;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.InputStreamReader;

/**
 * Created by zgjt on 2016/8/31.
 */
public class EquipmentUtil {
    private static String sUniquelyCode;
    /**
     * 获得imei
     *
     * @param context
     * @return
     */
    public static final String getUniquely(Context context) {
        if (TextUtils.isEmpty(sUniquelyCode)) {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            sUniquelyCode = tm.getDeviceId();
            if (TextUtils.isEmpty(sUniquelyCode)) {
                WifiManager wm = (WifiManager) context
                        .getSystemService(Context.WIFI_SERVICE);
                sUniquelyCode = wm.getConnectionInfo().getMacAddress();
            }

            if (TextUtils.isEmpty(sUniquelyCode)) {
                sUniquelyCode = getUniquelyCodeFromMacAddress(getLocalMacAddress());
            }
        }
        return sUniquelyCode;
    }

    private static String getUniquelyCodeFromMacAddress(String macAddress) {
        return macAddress.replaceAll(":", "");
    }

    private static String getLocalMacAddress() {
        String macAddr = null;
        char[] buf = new char[1024];
        InputStreamReader isr = null;

        try {
            Process pp = Runtime.getRuntime().exec("busybox ifconfig eth0");
            isr = new InputStreamReader(pp.getInputStream());
            isr.read(buf);
            macAddr = new String(buf);
            int start = macAddr.indexOf("HWaddr") + 7;
            int end = start + 18;
            macAddr = macAddr.substring(start, end);
        } catch (Exception e) {
            macAddr = "Read Exception";
        }
        return macAddr;
    }
}
