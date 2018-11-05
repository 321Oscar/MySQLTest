package com.example.a97586.mysqltest.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/*
* 处理服务器返回的数据
* */
public class CommonResponse {

    private String resCode="";

    private String resMsg="";

    private HashMap<String,String> propertyMap;

    private ArrayList<HashMap<String,String>> mapList;

    public CommonResponse(String responseString){
        propertyMap = new HashMap<>();
        mapList = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(responseString);

            resCode = root.getString("resCode");
            resMsg = root.getString("resMsg");

            JSONObject property = root.optJSONObject("property");
            if(property!=null){
                parseProperty(property,propertyMap);
            }

            JSONArray list = root.getJSONArray("list");
            if(list != null){
                parseList(list);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseProperty(JSONObject property,HashMap<String,String> targetMap){
        Iterator it = property.keys();
        while (it.hasNext()){
            String key = it.next().toString();
            Object value = property.opt(key);
            targetMap.put(key,value.toString());
        }
    }

    private void parseList(JSONArray list){
        int i = 0;
        while(i<list.length()){
            HashMap<String,String> map = new HashMap<>();
            try {
                parseProperty(list.getJSONObject(i++),map);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mapList.add(map);
        }
    }

    public String getResCode(){
        return resCode;
    }

    public String getResMsg(){
        return resMsg;
    }

    public HashMap<String,String> getPropertyMap(){
        return propertyMap;
    }

    public ArrayList<HashMap<String,String>> getDataList(){
        return mapList;
    }
}
