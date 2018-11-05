package com.example.a97586.mysqltest;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a97586.mysqltest.Utils.DBUtils;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static final String TAG ="MainActivity";
    Handler handler =new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            ((TextView)findViewById(R.id.tv_result)).setText((String)msg.obj);
            String str = "查询不存在";
            if(msg.what == 1) str="查询成功";
            Toast.makeText(MainActivity.this,str,Toast.LENGTH_SHORT).show();
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText et_name = findViewById(R.id.et_name);
        (findViewById(R.id.btn_01)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = et_name.getText().toString().trim();
                Log.e(TAG,name);
                if(name == null || name.equals("")){
                    Toast.makeText(MainActivity.this,"输入不能为空",Toast.LENGTH_SHORT).show();
                }else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            TextView tv_result = findViewById(R.id.tv_result);
                            HashMap<String,String> mp =
                                    DBUtils.getUserInfoByName(name);
                            Message msg = new Message();
                            if(mp == null){
                                msg.what = 0;
                                msg.obj ="无结果";
                                //非UI线程不要试着操控界面
                            }else {
                                String ss =new String();
                                for(String key : mp.keySet()){
                                    ss = ss+key+":"+mp.get(key)+";";
                                }
                                msg.what =1;
                                msg.obj =ss;
                            }
                            handler.sendMessage(msg);
                        }
                    }).start();
                }
            }
        });
    }
}
