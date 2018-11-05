package com.example.a97586.mysqltest.Utils;

public interface ResponseHandle {
    void success(CommonResponse response);

    void failure(String failCode,String failMsg);
}
