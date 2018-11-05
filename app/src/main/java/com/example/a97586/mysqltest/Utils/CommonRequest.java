package com.example.a97586.mysqltest.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class CommonRequest {
    private String requestCode;

    private HashMap<String,String> requestParam;

    public CommonRequest(){
        requestCode = "";
        requestParam = new HashMap<>();
    }

    public void setRequestCode(String requestCode) {
        this.requestCode = requestCode;
    }

    public void addRequestParam(String paramkey,String paramvalue) {
        requestParam.put(paramkey,paramvalue);
    }

    public String getJsonStr(){
        JSONObject object = new JSONObject();
        JSONObject param = new JSONObject(requestParam);//参数列表
        try {
            object.put("ActionType",requestCode);
            object.put("params",param);//参数列表的name以及内容
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }
}

