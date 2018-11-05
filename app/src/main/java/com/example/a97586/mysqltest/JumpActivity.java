package com.example.a97586.mysqltest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class JumpActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnMain,btnLogin,btnURL,btnClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jump);
        btnMain = findViewById(R.id.btn_main);
        btnLogin = findViewById(R.id.btn_tomcat);
        btnURL = findViewById(R.id.btn_url);
        btnClient = findViewById(R.id.btn_client);

        btnClient.setOnClickListener(this);
        btnMain.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnURL.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.btn_main:
                intent = new Intent(JumpActivity.this,MainActivity.class);
                break;
            case R.id.btn_client:
                intent = new Intent(JumpActivity.this,HttpClientActivity.class);
                break;
            case R.id.btn_tomcat:
                intent = new Intent(JumpActivity.this,LoginTomcatActivity.class);
                break;
            case R.id.btn_url:
                intent = new Intent(JumpActivity.this,HttpURLConnectionActivity.class);
                break;
        }
        startActivity(intent);
    }
}
