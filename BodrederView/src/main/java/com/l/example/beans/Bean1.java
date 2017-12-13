package com.l.example.beans;

/**
 * Created by Administrator on 2017/1/11.
 */

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Bean1 {
    static String str = "{\"RESULT\":{\"userAddresses\":[{\"countyName\":\"新吴区\",\"contactPhone\":\"18762808688\",\"specifyAddress\":\"镇泽路88号\",\"isDefault\":\"Y\",\"cityName\":\"无锡市\",\"addressCode\":\"002\",\"streetName\":\"新安\",\"provinceName\":\"江苏省\",\"contactPerson\":\"张大大\"},{\"countyName\":\"新吴区\",\"contactPhone\":\"18762808688\",\"specifyAddress\":\"镇泽路88号\",\"isDefault\":\"N\",\"cityName\":\"无锡市\",\"addressCode\":\"ADR1482658782264S\",\"streetName\":\"新安\",\"provinceName\":\"江苏省\",\"contactPerson\":\"张大大\"},{\"countyName\":\"新区\",\"contactPhone\":\"18989898898\",\"specifyAddress\":\"震泽路18号\",\"isDefault\":\"N\",\"cityName\":\"无锡\",\"addressCode\":\"001\",\"streetName\":\"新安\",\"provinceName\":\"江苏\",\"contactPerson\":\"张大大\"}]}}";

    public String countyName;
    public String countactPerson;
    private static List<Bean1> dates = new ArrayList<Bean1>();

    public static List<Bean1> getBean1() {
        try {
            JSONObject obj = new JSONObject(str);

            JSONArray array = obj.getJSONObject("RESULT").getJSONArray("userAddresses");
            for (int i = 0; i < array.length(); i++) {
                Bean1 bean = new Bean1();
                bean.countyName = array.getJSONObject(i).get("countyName").toString();
                bean.countactPerson = array.getJSONObject(i).get("contactPhone").toString();
                dates.add(bean);
            }


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println(dates.toString());
        return dates;
    }


    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getCountactPerson() {
        return countactPerson;
    }

    public void setCountactPerson(String countactPerson) {
        this.countactPerson = countactPerson;
    }

    @Override
    public String toString() {
        return "Bean1{" +
                "countyName='" + countyName + '\'' +
                ", countactPerson='" + countactPerson + '\'' +
                '}';
    }


}

