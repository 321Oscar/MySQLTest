package com.example.a97586.mysqltest;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.a97586.mysqltest.Utils.Constant;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HttpClientActivity extends AppCompatActivity {

    private TextView textView;

    //消息机制
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
        setContentView(R.layout.activity_http_client);

        textView = findViewById(R.id.tv_content);
        requestUsingHttpURLConnection();
    }

    private void requestUsingHttpURLConnection(){
        //
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient = new DefaultHttpClient();//HttpClient 是一个接口，无法实例化，所以通常会创建一个DefaultHttpClient实例
//                HttpGet get = new HttpGet("https://www.baidu.com");//发起GET请求就使用HttpGET,发起POST就用HttpPOST
                HttpPost post = new HttpPost(Constant.URL_Post);
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("account","Xu"));
                params.add(new BasicNameValuePair("password","Wei"));

                try {
                    UrlEncodedFormEntity reqEntity = new UrlEncodedFormEntity(params,"utf-8");
                    post.setEntity(reqEntity);
                    HttpResponse httpResponse = httpClient.execute(post);//调用HttpClient对象的execute()方法
                    //状态码200说明响应成功
                    if(httpResponse.getStatusLine().getStatusCode() == 200){
                        HttpEntity entity = httpResponse.getEntity();//取出报文的具体内容
                        String response = EntityUtils.toString(entity,"utf-8");//报文编码

                        //发送消息
                        Message msg = new Message();
                        msg.what =1;
                        msg.obj = response;
                        handler.sendMessage(msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


}
