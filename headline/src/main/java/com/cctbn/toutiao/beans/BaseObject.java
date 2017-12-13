package com.cctbn.toutiao.beans;

/**
 * Created by zgjt on 2016/8/31.
 */
public class BaseObject {
    private String resCode;
    private String resMsg;
    private String tag;

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getResMsg() {
        return resMsg;
    }

    public void setResMsg(String resMsg) {
        this.resMsg = resMsg;
    }

    public String getTag() {
        return tag;
    }

    public BaseObject setTag(String tag) {
        this.tag = tag;
        return this;
    }
}
