package com.example.administrator.sunxiaohong2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Xml;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class AcActivity extends AppCompatActivity {

    private TextView tv_method1,tv_result1,tv_service1,tv_revise1,tv_reviseresult1;
    private static int mk;
    static String sql;
    private Map<String,String> listItem = new HashMap<>();

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Map<String,String>listMessage =(Map<String, String>)msg.obj;
            setMessage(listMessage);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ac);
        Intent intent = getIntent();
        mk = intent.getIntExtra("mk", 0);
        if (mk ==0 || mk ==1){
            sql = "select * from sunxiaohong_ac where id=1";
        }else if (mk == 2){
            sql = "select * from sunxiaohong_ac where id=2";
        }else if (mk == 3){
            sql = "select * from sunxiaohong_ac where id=3";
        }else if (mk == 4){
            sql = "select * from sunxiaohong_ac where id=4";
        }
        init();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection connection = ConnectMysql.getConn();
                try {
                    Statement stmt= connection.createStatement();
                    ResultSet rs = stmt.executeQuery(sql);
                    while(rs.next()){//按数据库表的行获取数据
                        listItem.put("推理方法",rs.getString("推理方法"));
                        listItem.put("推理结果",rs.getString("推理结果"));
                        listItem.put("标准服务",rs.getString("标准服务"));
                        listItem.put("是否修正",rs.getString("是否修正"));
                        listItem.put("修正结果",rs.getString("修正结果"));
                    }
                    Message msg = new Message();
                    msg.obj = listItem;
                    handler.sendMessage(msg);
                    try{
                        rs.close();
                    }catch(SQLException e){
                        e.printStackTrace();
                    }
                    try{
                        stmt.close();
                    }catch(SQLException e){
                        e.printStackTrace();
                    }
                    try{
                        connection.close();
                    }catch(SQLException e){
                        e.printStackTrace();
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                }finally {
                    ConnectMysql.closeConn();
                }
            }
        }).start();
    }

    protected void init(){
        tv_method1 = (TextView) findViewById(R.id.tv_method1);
        tv_result1 = (TextView) findViewById(R.id.tv_result1);
        tv_service1 = (TextView) findViewById(R.id.tv_service1);
        tv_revise1 = (TextView) findViewById(R.id.tv_revise1);
        tv_reviseresult1 = (TextView) findViewById(R.id.tv_reviseresult1);
        android.support.v7.widget.Toolbar tl_head = (android.support.v7.widget.Toolbar) findViewById(R.id.tl_head);
        tl_head.setTitle("空调服务页面");
        //调用setSupportActionBar方法设置当前的Toolbar对象
        setSupportActionBar(tl_head);
        tl_head.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void setMessage(Map<String,String> message){

        tv_method1.setText(message.get("推理方法"));
        tv_result1.setText(message.get("推理结果"));

        tv_service1.setText(message.get("标准服务"));

        tv_revise1.setText(message.get("是否修正"));
        tv_reviseresult1.setText(message.get("修正结果"));
    }

    //这两个override是为了监听手机back按键而触发的
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
