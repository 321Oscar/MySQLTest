package com.example.a97586.mysqltest;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.a97586.mysqltest.Utils.Constant;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpURLConnectionActivity extends AppCompatActivity {

    private TextView textView;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                textView.setText(msg.obj.toString());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_url_connection);

        textView = findViewById(R.id.tv_content);
        requestUsingHttpURLConnection();
    }

    private void requestUsingHttpURLConnection(){
        //
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(Constant.URL_Post);//声明一个URL
                    connection = (HttpURLConnection) url.openConnection();//打开该链接
//                    connection.setRequestMethod("GET");//设置请求方法，“GET或POST”

                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(8000);//设置连接建立的超时时间
                    connection.setReadTimeout(8000);//设置网络报文收发超时时间

                    //POST more do
                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                    String param = "account=xu&password=wei";
                    out.writeBytes(param);

                    InputStream in = connection.getInputStream();//通过连接的输入流去下发报文，然后就是Java的流处理
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while((line = reader.readLine()) != null ){
                        response.append(line);
                    }

//                    textView.setText(response.toString());
                    Message msg = new Message();
                    msg.what =1;
                    msg.obj = response.toString();
                    Log.e("WeiDong",response.toString());
                    handler.sendMessage(msg);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


}
