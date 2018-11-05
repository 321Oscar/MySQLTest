package com.example.a97586.mysqltest.Utils;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpPostTask extends AsyncTask<String,String,String> {

    private Handler mhandle;//
    private ResponseHandle rHandle;//返回信息处理回调接口
    private CommonRequest request;//请求类对象

    public HttpPostTask(Handler mhandle, ResponseHandle rHandle, CommonRequest request) {
        this.mhandle = mhandle;
        this.rHandle = rHandle;
        this.request = request;
    }


    @Override
    protected String doInBackground(String... strings) {
        StringBuilder resultBuf = new StringBuilder();
        try {
            URL url = new URL(strings[0]);

            //step1:使用URL打开一个HttpConnection连接
            HttpURLConnection connection =(HttpURLConnection) url.openConnection();

            //step2：设置HTTP Connection连接相关属性
            connection.setRequestProperty("Content-type","application/json;charset=utf-8");
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            connection.setDoOutput(true);
            connection.setDoInput(true);

            //如果是POST方法，需要在获取输入流之前向连接写入POST参数
            DataOutputStream out =new DataOutputStream(connection.getOutputStream());
            out.writeBytes(request.getJsonStr());
            out.flush();

            //step3：打开连接输入流读取返回报文 （在此步骤才真正开始网络请求）
            int responseCode = connection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                //通过连接的输入流获取下发报文，然后就是Java的流处理
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while((line = reader.readLine())!=null){
                    resultBuf.append(line);
                }
                return resultBuf.toString();
            }else {
//                mhandle.obtainMessage(Constant)
                // TODO: 2018/11/1
            }
        } catch (IOException e) {
            //网络请求过程中发生IO异常 mhandle
            // TODO: 2018/11/1
            e.printStackTrace();
        }
        return resultBuf.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        if(rHandle != null){
            if(!"".equals(s)){
                CommonResponse response = new CommonResponse(s);

                //这里的业务完成的判断Code也是与服务器约定好的
                if("11".equals(response.getResCode())){//正确
                    rHandle.success(response);
                }else {
                    rHandle.failure(response.getResCode(),response.getResMsg());
                }
            }
        }
        super.onPostExecute(s);
    }
}


