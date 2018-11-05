package com.example.a97586.mysqltest;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a97586.mysqltest.Utils.CommonRequest;
import com.example.a97586.mysqltest.Utils.CommonResponse;
import com.example.a97586.mysqltest.Utils.Constant;
import com.example.a97586.mysqltest.Utils.HttpPostTask;
import com.example.a97586.mysqltest.Utils.ResponseHandle;
import com.mysql.jdbc.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginTomcatActivity extends AppCompatActivity {

    private EditText etAccount, etPassword;
    private Button btnLogin, btnRegister;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_tomcat);

        etAccount = findViewById(R.id.et_account);
        etPassword = findViewById(R.id.et_password);
        tvResult = findViewById(R.id.tv_result);

        btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!StringUtils.isEmptyOrWhitespaceOnly(etAccount.getText().toString())
                        && !StringUtils.isEmptyOrWhitespaceOnly(etPassword.getText().toString())) {
                    Log.e("WeiD", "都不空");
                    register(etAccount.getText().toString(), etPassword.getText().toString());
                } else {
                    Toast.makeText(LoginTomcatActivity.this,
                            "账号密码不能为空",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!StringUtils.isEmptyOrWhitespaceOnly(etAccount.getText().toString())
                        && !StringUtils.isEmptyOrWhitespaceOnly(etPassword.getText().toString())) {
                    Log.e("WeiD", "都不空");
                    login_2(etAccount.getText().toString(), etPassword.getText().toString());
                } else {
                    Toast.makeText(LoginTomcatActivity.this,
                            "账号密码不能为空",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void register(String account, String password) {
//        String registerUrlStr = Constant.URL_Register + "?account="+account+"&password="+password;
        try {
            JSONObject postjson = new JSONObject();
            postjson.put("account", account);
            postjson.put("password", password);
            new MyAsyncTask(tvResult, postjson).execute(Constant.URL_Register);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void login(String account, String password) {
        try {
            JSONObject postjson = new JSONObject();
            postjson.put("account", account);
            postjson.put("password", password);
            new MyAsyncTask(tvResult, postjson).execute(Constant.URL_Login);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void login_2(String account, String password) {
        CommonRequest request = new CommonRequest();
        request.addRequestParam("account", account);
        request.addRequestParam("password", password);
        request.setRequestCode("Login");
        sendHttpPostRequest(Constant.URL_Login, request, new ResponseHandle() {
            @Override
            public void success(CommonResponse response) {
                Toast.makeText(LoginTomcatActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(String failCode, String failMsg) {
                String Msg = "";
                //10,00,0
                switch (failCode) {
                    case "10":
                        Msg = "密码错误";
                        break;
                    case "00":
                        Msg = "账号不存在";
                        break;
                    case "0":
                        Msg = "大错误";
                        break;
                }
                Toast.makeText(LoginTomcatActivity.this, Msg, Toast.LENGTH_SHORT).show();
            }
        }, true);
    }

    protected void sendHttpPostRequest(String url, CommonRequest request, ResponseHandle responseHandle,
                                       boolean i) {
        new HttpPostTask(mHandler, responseHandle, request).execute(url);
        if (i) {
            // TODO: 2018/11/1
        }
    }

    @SuppressLint("HandlerLeak")
    protected Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 1) {
                Toast.makeText(LoginTomcatActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 12) {
                Toast.makeText(LoginTomcatActivity.this, "接收失败", Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * AsyncTask类的三个泛型参数：
     * （1）Param 在执行AsyncTask是需要传入的参数，可用于后台任务中使用
     * （2）后台任务执行过程中，如果需要在UI上先是当前任务进度，则使用这里指定的泛型作为进度单位
     * （3）任务执行完毕后，如果需要对结果进行返回，则这里指定返回的数据类型
     */
    public static class MyAsyncTask extends AsyncTask<String, Integer, String> {

        private TextView tv;
        private JSONObject postValue;

        private MyAsyncTask(TextView v, JSONObject postValue) {
            this.postValue = postValue;
            tv = v;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * @param strings 这里的strings是一个数组，即AsyncTask在激活运行是调用execute()方法传入的参数
         */
        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            StringBuilder response = new StringBuilder();
            try {
                URL url = new URL(strings[0]);//声明一个URL，注意如果用网址，请用https开头，否则获取不到报文
                connection = (HttpURLConnection) url.openConnection();//打开该URL链接
                connection.setRequestMethod("POST");//设置请求方法
                connection.setReadTimeout(8000);
                connection.setConnectTimeout(8000);
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);
                connection.connect();

                DataOutputStream dos = new DataOutputStream(connection.getOutputStream());

//                request.addRequestParam("account",account);
//                request.addRequestParam("password",password);
                JSONObject content = postValue;

                dos.write(content.toString().getBytes());
                dos.flush();
                dos.close();

                InputStream in = connection.getInputStream();//通过连接的输入流获取下发报文，然后就是Java的流处理
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response.toString();//这里返回的结果就是作为onPostExecute方法的入参
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            //如果在doInBackGround方法，那么就会立即执行本方法
            //本方法在UI线程中执行，可以更新UI元素，典型的就是更新进度条进度，一般是在下载时候使用
            super.onProgressUpdate(values);
        }

        /*
         * 运行在UI线程中，所以可以直接操纵UI元素
         * @param s
         * */
        @Override
        protected void onPostExecute(String s) {
            if (!"".equals(s)) {
                Log.e("原文", s);
                try {
                    JSONObject resultJson = new JSONObject(s);
                    tv.setText(resultJson.getString("resCode"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("WeiDong", "结果为空");
            }
            super.onPostExecute(s);
        }
    }
}
