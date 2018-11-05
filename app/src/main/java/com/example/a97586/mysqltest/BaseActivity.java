package com.example.a97586.mysqltest;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.a97586.mysqltest.Utils.CommonRequest;
import com.example.a97586.mysqltest.Utils.HttpPostTask;
import com.example.a97586.mysqltest.Utils.ResponseHandle;


public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void sendHttpPostRequest(String url, CommonRequest request,
                                       ResponseHandle responseHandle,
                                       boolean showLoadingDialog){
        new HttpPostTask(mHandle,responseHandle,request).execute(url);
        if (showLoadingDialog){
            // TODO: 2018/11/1
        }
    }

    @SuppressLint("HandlerLeak")
    protected Handler mHandle = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(msg.what == 1){
                Toast.makeText(BaseActivity.this,"请求发送失败",Toast.LENGTH_SHORT).show();
            }else if(msg.what == 0){
                Toast.makeText(BaseActivity.this,"请求接受失败",Toast.LENGTH_SHORT).show();
            }
        }
    };
}
