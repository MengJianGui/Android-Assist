package com.example.administrator.sunxiaohong2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btn_obtain,btn_clear,btn_revisefood,btn_reviseac,btn_reviselight;
    private TextView tv_param1,tv_data1;
    private TextView tv_phyparam1,tv_phydata1;
    private TextView tv_food1,tv_revisefood;
    private TextView tv_ac1,tv_reviseac,tv_acnum;
    private TextView tv_light1,tv_reviselight,tv_lightnum;
    Map<String,String> listItem = new HashMap<>();
    private static int mk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    //初始化控件
    protected void init(){
        btn_obtain = (Button) findViewById(R.id.btn_obtain);
        btn_obtain.setOnClickListener(this);
        btn_clear = (Button) findViewById(R.id.btn_clear);
        btn_clear.setOnClickListener(this);
        btn_revisefood = (Button) findViewById(R.id.btn_revisefood);
        btn_revisefood.setOnClickListener(this);

        btn_reviseac = (Button) findViewById(R.id.btn_reviseac);
        btn_reviseac.setOnClickListener(this);
        btn_reviselight = (Button) findViewById(R.id.btn_reviselight);
        btn_reviselight.setOnClickListener(this);

        tv_param1 = (TextView) findViewById(R.id.tv_param1);
        tv_data1 = (TextView) findViewById(R.id.tv_data1);

        tv_phyparam1 = (TextView) findViewById(R.id.tv_phyparam1);
        tv_phydata1 = (TextView) findViewById(R.id.tv_phydata1);

        tv_food1 = (TextView) findViewById(R.id.tv_food1);
        tv_revisefood = (TextView) findViewById(R.id.tv_revisefood);
        tv_ac1 = (TextView) findViewById(R.id.tv_ac1);
        tv_reviseac = (TextView) findViewById(R.id.tv_reviseac);
        tv_acnum = (TextView) findViewById(R.id.tv_acnum);
        tv_light1 = (TextView) findViewById(R.id.tv_light1);
        tv_reviselight = (TextView) findViewById(R.id.tv_reviselight);
        tv_lightnum = (TextView) findViewById(R.id.tv_lightnum);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_obtain:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Connect conn = new Connect();
                        mk = 1;
                        conn.execute();
                        try{
                            Thread.sleep(15000);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        Connect1 connect1 = new Connect1();
                        mk = 2;
                        connect1.execute();
                        try{
                            Thread.sleep(15000);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        Connect2 connect2 = new Connect2();
                        mk = 3;
                        connect2.execute();
                        try{
                            Thread.sleep(15000);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        Connect3 connect3 = new Connect3();
                        mk = 4;
                        connect3.execute();
                    }
                }).start();
                break;
            case R.id.btn_clear:
                tv_param1.setText("");
                tv_data1.setText("");

                tv_phyparam1.setText("");
                tv_phydata1.setText("");

                tv_food1.setText("");
                tv_revisefood.setText("");


                tv_ac1.setText("");
                tv_reviseac.setText("");
                tv_acnum.setText("");

                tv_light1.setText("");
                tv_reviselight.setText("");
                tv_lightnum.setText("");
                break;

            case R.id.btn_revisefood:
                Intent intent = new Intent(this, FoodActivity.class);
                intent.putExtra("mk", mk);
                startActivity(intent);
                break;
            case R.id.btn_reviseac:
                Intent intent1 = new Intent(this, AcActivity.class);
                intent1.putExtra("mk", mk);
                startActivity(intent1);
                break;
            case R.id.btn_reviselight:
                Intent intent2 = new Intent(this, LightActivity.class);
                intent2.putExtra("mk", mk);
                startActivity(intent2);
                break;
            default:
                break;

        }
    }

    private class Connect extends AsyncTask<Void,Void,Map<String,String>> {
        String user="root";
        String password = "mk143741";
        String url = "jdbc:mysql://10.20.4.164/mk1";
        String sql = "select * from sunxiaohong_alldata where id=1";
        @Override
        protected Map<String,String> doInBackground(Void... voids){
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn= DriverManager.getConnection(url,user,password);
                Statement stmt= conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                listItem = new HashMap<>();
                while(rs.next()){//按数据库表的行获取数据
                    listItem.put("参数",rs.getString("参数"));
                    listItem.put("数据",rs.getString("数据"));
                    listItem.put("生理参数",rs.getString("生理参数"));
                    listItem.put("生理数据",rs.getString("生理数据"));
                    listItem.put("食物",rs.getString("食物"));
                    listItem.put("食物(修正)",rs.getString("食物(修正)"));
                    listItem.put("空调(位置)",rs.getString("空调(位置)"));
                    listItem.put("空调(温度)",rs.getString("空调(温度)"));
                    listItem.put("空调(修正)",rs.getString("空调(修正)"));
                    listItem.put("灯具(位置)",rs.getString("灯具(位置)"));
                    listItem.put("灯具(亮度)",rs.getString("灯具(亮度)"));
                    listItem.put("灯具(修正)",rs.getString("灯具(修正)"));
                }
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
                    conn.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
                //tv_text.setText(result);这句话是错误的，在子线程中不能操作主线程中的控件，即布局中的控件，切记切记！！！而且这种情况下安装apk的时候有可能会出现闪退
                return listItem;//return result是把子线程的结果传递给onPostExecute函数
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Map<String,String> result){
            tv_param1.setText(result.get("参数"));
            tv_data1.setText(result.get("数据"));

            tv_phyparam1.setText(result.get("生理参数"));
            tv_phydata1.setText(result.get("生理数据"));

            tv_food1.setText(result.get("食物"));
            tv_revisefood.setText(result.get("食物(修正)"));

            tv_ac1.setText(result.get("空调(位置)"));
            tv_reviseac.setText(result.get("空调(修正)"));
            tv_acnum.setText(result.get("空调(温度)"));

            tv_light1.setText(result.get("灯具(位置)"));
            tv_reviselight.setText(result.get("灯具(修正)"));
            tv_lightnum.setText(result.get("灯具(亮度)"));
        }
    }

    private class Connect1 extends AsyncTask<Void,Void,Map<String,String>> {
        String user="root";
        String password = "mk143741";
        String url = "jdbc:mysql://10.20.4.164/mk1";
        String sql = "select * from sunxiaohong_alldata where id=2";
        @Override
        protected Map<String,String> doInBackground(Void... voids){
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn= DriverManager.getConnection(url,user,password);
                Statement stmt= conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                listItem = new HashMap<>();
                while(rs.next()){//按数据库表的行获取数据
                    listItem.put("参数",rs.getString("参数"));
                    listItem.put("数据",rs.getString("数据"));
                    listItem.put("生理参数",rs.getString("生理参数"));
                    listItem.put("生理数据",rs.getString("生理数据"));
                    listItem.put("食物",rs.getString("食物"));
                    listItem.put("食物(修正)",rs.getString("食物(修正)"));
                    listItem.put("空调(位置)",rs.getString("空调(位置)"));
                    listItem.put("空调(温度)",rs.getString("空调(温度)"));
                    listItem.put("空调(修正)",rs.getString("空调(修正)"));
                    listItem.put("灯具(位置)",rs.getString("灯具(位置)"));
                    listItem.put("灯具(亮度)",rs.getString("灯具(亮度)"));
                    listItem.put("灯具(修正)",rs.getString("灯具(修正)"));
                }
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
                    conn.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
                //tv_text.setText(result);这句话是错误的，在子线程中不能操作主线程中的控件，即布局中的控件，切记切记！！！而且这种情况下安装apk的时候有可能会出现闪退
                return listItem;//return result是把子线程的结果传递给onPostExecute函数
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Map<String,String> result){
            tv_param1.setText(result.get("参数"));
            tv_data1.setText(result.get("数据"));

            tv_phyparam1.setText(result.get("生理参数"));
            tv_phydata1.setText(result.get("生理数据"));

            tv_food1.setText(result.get("食物"));
            tv_revisefood.setText(result.get("食物(修正)"));

            tv_ac1.setText(result.get("空调(位置)"));
            tv_reviseac.setText(result.get("空调(修正)"));
            tv_acnum.setText(result.get("空调(温度)"));

            tv_light1.setText(result.get("灯具(位置)"));
            tv_reviselight.setText(result.get("灯具(修正)"));
            tv_lightnum.setText(result.get("灯具(亮度)"));
        }
    }

    private class Connect2 extends AsyncTask<Void,Void,Map<String,String>> {
        String user="root";
        String password = "mk143741";
        String url = "jdbc:mysql://10.20.4.164/mk1";
        String sql = "select * from sunxiaohong_alldata where id=3";
        @Override
        protected Map<String,String> doInBackground(Void... voids){
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn= DriverManager.getConnection(url,user,password);
                Statement stmt= conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                listItem = new HashMap<>();
                while(rs.next()){//按数据库表的行获取数据
                    listItem.put("参数",rs.getString("参数"));
                    listItem.put("数据",rs.getString("数据"));
                    listItem.put("生理参数",rs.getString("生理参数"));
                    listItem.put("生理数据",rs.getString("生理数据"));
                    listItem.put("食物",rs.getString("食物"));
                    listItem.put("食物(修正)",rs.getString("食物(修正)"));
                    listItem.put("空调(位置)",rs.getString("空调(位置)"));
                    listItem.put("空调(温度)",rs.getString("空调(温度)"));
                    listItem.put("空调(修正)",rs.getString("空调(修正)"));
                    listItem.put("灯具(位置)",rs.getString("灯具(位置)"));
                    listItem.put("灯具(亮度)",rs.getString("灯具(亮度)"));
                    listItem.put("灯具(修正)",rs.getString("灯具(修正)"));
                }
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
                    conn.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
                //tv_text.setText(result);这句话是错误的，在子线程中不能操作主线程中的控件，即布局中的控件，切记切记！！！而且这种情况下安装apk的时候有可能会出现闪退
                return listItem;//return result是把子线程的结果传递给onPostExecute函数
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Map<String,String> result){
            tv_param1.setText(result.get("参数"));
            tv_data1.setText(result.get("数据"));

            tv_phyparam1.setText(result.get("生理参数"));
            tv_phydata1.setText(result.get("生理数据"));

            tv_food1.setText(result.get("食物"));
            tv_revisefood.setText(result.get("食物(修正)"));

            tv_ac1.setText(result.get("空调(位置)"));
            tv_reviseac.setText(result.get("空调(修正)"));
            tv_acnum.setText(result.get("空调(温度)"));

            tv_light1.setText(result.get("灯具(位置)"));
            tv_reviselight.setText(result.get("灯具(修正)"));
            tv_lightnum.setText(result.get("灯具(亮度)"));
        }
    }

    private class Connect3 extends AsyncTask<Void,Void,Map<String,String>> {
        String user="root";
        String password = "mk143741";
        String url = "jdbc:mysql://10.20.4.164/mk1";
        String sql = "select * from sunxiaohong_alldata where id=4";
        @Override
        protected Map<String,String> doInBackground(Void... voids){
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn= DriverManager.getConnection(url,user,password);
                Statement stmt= conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                listItem = new HashMap<>();
                while(rs.next()){//按数据库表的行获取数据
                    listItem.put("参数",rs.getString("参数"));
                    listItem.put("数据",rs.getString("数据"));
                    listItem.put("生理参数",rs.getString("生理参数"));
                    listItem.put("生理数据",rs.getString("生理数据"));
                    listItem.put("食物",rs.getString("食物"));
                    listItem.put("食物(修正)",rs.getString("食物(修正)"));
                    listItem.put("空调(位置)",rs.getString("空调(位置)"));
                    listItem.put("空调(温度)",rs.getString("空调(温度)"));
                    listItem.put("空调(修正)",rs.getString("空调(修正)"));
                    listItem.put("灯具(位置)",rs.getString("灯具(位置)"));
                    listItem.put("灯具(亮度)",rs.getString("灯具(亮度)"));
                    listItem.put("灯具(修正)",rs.getString("灯具(修正)"));
                }
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
                    conn.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
                //tv_text.setText(result);这句话是错误的，在子线程中不能操作主线程中的控件，即布局中的控件，切记切记！！！而且这种情况下安装apk的时候有可能会出现闪退
                return listItem;//return result是把子线程的结果传递给onPostExecute函数
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Map<String,String> result){
            tv_param1.setText(result.get("参数"));
            tv_data1.setText(result.get("数据"));

            tv_phyparam1.setText(result.get("生理参数"));
            tv_phydata1.setText(result.get("生理数据"));

            tv_food1.setText(result.get("食物"));
            tv_revisefood.setText(result.get("食物(修正)"));

            tv_ac1.setText(result.get("空调(位置)"));
            tv_reviseac.setText(result.get("空调(修正)"));
            tv_acnum.setText(result.get("空调(温度)"));

            tv_light1.setText(result.get("灯具(位置)"));
            tv_reviselight.setText(result.get("灯具(修正)"));
            tv_lightnum.setText(result.get("灯具(亮度)"));
        }
    }
}
