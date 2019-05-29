package com.example.administrator.sunxiaohong;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btn_obtain,btn_clear,btn_revisefood,btn_revisedrink,btn_reviseac,btn_reviselight;
    private TextView tv_param1,tv_data1;
    private TextView tv_food1,tv_revisefood;
    private TextView tv_drink1,tv_revisedrink;
    private TextView tv_ac1,tv_reviseac,tv_acnum;
    private TextView tv_light1,tv_reviselight,tv_lightnum;
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private final int ERRORCODE = 2;
    private Map<String,String>listItem = new HashMap<>();

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    /**
                     * 获取信息成功后，对该信息进行JSON解析，得到所需要的信息，然后在textView上展示出来。
                     */
                    Map<String,String>listMessage =(Map<String, String>)msg.obj;
                    setMessage(listMessage);

                    Toast.makeText(MainActivity.this, "获取数据成功", Toast.LENGTH_SHORT)
                            .show();
                    break;

                case FAILURE:
                    Toast.makeText(MainActivity.this, "获取数据失败", Toast.LENGTH_SHORT)
                            .show();
                    break;

                case ERRORCODE:
                    Toast.makeText(MainActivity.this, "获取的CODE码不为200！",
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

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
        btn_revisedrink = (Button) findViewById(R.id.btn_revisedrink);
        btn_revisedrink.setOnClickListener(this);
        btn_reviseac = (Button) findViewById(R.id.btn_reviseac);
        btn_reviseac.setOnClickListener(this);
        btn_reviselight = (Button) findViewById(R.id.btn_reviselight);
        btn_reviselight.setOnClickListener(this);

        tv_param1 = (TextView) findViewById(R.id.tv_param1);
        tv_data1 = (TextView) findViewById(R.id.tv_data1);

        tv_food1 = (TextView) findViewById(R.id.tv_food1);
        tv_revisefood = (TextView) findViewById(R.id.tv_revisefood);
        tv_drink1 = (TextView) findViewById(R.id.tv_drink1);
        tv_revisedrink = (TextView) findViewById(R.id.tv_revisedrink);
        tv_ac1 = (TextView) findViewById(R.id.tv_ac1);
        tv_reviseac = (TextView) findViewById(R.id.tv_reviseac);
        tv_acnum = (TextView) findViewById(R.id.tv_acnum);
        tv_light1 = (TextView) findViewById(R.id.tv_light1);
        tv_reviselight = (TextView) findViewById(R.id.tv_reviselight);
        tv_lightnum = (TextView) findViewById(R.id.tv_lightnum);
    }

    public void setMessage(Map<String,String> message){

        tv_param1.setText(message.get("param"));
        tv_data1.setText(message.get("data"));

        tv_food1.setText(message.get("food"));
        tv_revisefood.setText(message.get("foodalter"));

        tv_drink1.setText(message.get("drink"));
        tv_revisedrink.setText(message.get("drinkalter"));

        tv_ac1.setText(message.get("ac"));
        tv_reviseac.setText(message.get("acalter"));
        tv_acnum.setText(message.get("acnum"));

        tv_light1.setText(message.get("light"));
        tv_reviselight.setText(message.get("lightalter"));
        tv_lightnum.setText(message.get("lightnum"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_obtain:
                /**
                 * 点击按钮事件，在主线程中开启一个子线程进行网络请求
                 * （因为在4.0只有不支持主线程进行网络请求，所以一般情况下，建议另开启子线程进行网络请求等耗时操作）。
                 */
                new Thread() {
                    public void run() {
                        int code;
                        try {
                            String path = "http://10.20.4.225:8080/client/api?command=getTimeUser&apikey=fPe2s6FbR5VeBQJuGBWIDHGxoHjo4IBBbfVwopVDctf1E9lyLemrGtFrmYAXWNxcTV-u2Choh2YQYV2RGhPdpQ&signature=YKR50hbdGz5aAbwCI5kaX%2BU8RBQ%3D";
                            URL url = new URL(path);
                            /**
                             * 这里网络请求使用的是类HttpURLConnection，另外一种可以选择使用类HttpClient。
                             */
                            HttpURLConnection conn = (HttpURLConnection) url
                                    .openConnection();
                            conn.setRequestMethod("GET");//使用GET方法获取
                            conn.setConnectTimeout(5000);
                            code = conn.getResponseCode();
                            if (code == 200) {
                                /**
                                 * 如果获取的code为200，则证明数据获取是正确的。
                                 */
                                InputStream is = conn.getInputStream();
                                XmlPullParser xmlPullParser = Xml.newPullParser();
                                xmlPullParser.setInput(is,"UTF-8");
                                //获取解析的标签的类型
                                int type = xmlPullParser.getEventType();
                                while(type!=XmlPullParser.END_DOCUMENT){
                                    switch(type){
                                        case XmlPullParser.START_TAG:
                                            String startname = xmlPullParser.getName();
                                            if ("gettimeuserresponse".equals(startname)){
                                                String version = xmlPullParser.getAttributeValue(0);
                                                listItem.put("version",version);
                                            } else if ("param".equals(startname)){
                                                String param = xmlPullParser.nextText();
                                                listItem.put("param",param);
                                            } else if ("data".equals(startname)){
                                                String data = xmlPullParser.nextText();
                                                listItem.put("data",data);
                                            } else if ("food".equals(startname)){
                                                String food = xmlPullParser.nextText();
                                                listItem.put("food",food);
                                            } else if ("foodalter".equals(startname)){
                                                String foodalter = xmlPullParser.nextText();
                                                listItem.put("foodalter",foodalter);
                                            } else if ("drink".equals(startname)){
                                                String drink = xmlPullParser.nextText();
                                                listItem.put("drink",drink);
                                            } else if ("drinkalter".equals(startname)){
                                                String drinkalter = xmlPullParser.nextText();
                                                listItem.put("drinkalter",drinkalter);
                                            } else if ("acnum".equals(startname)){
                                                String acnum = xmlPullParser.nextText();
                                                listItem.put("acnum",acnum);
                                            } else if ("ac".equals(startname)){
                                                String ac = xmlPullParser.nextText();
                                                listItem.put("ac",ac);
                                            } else if ("acalter".equals(startname)){
                                                String acalter = xmlPullParser.nextText();
                                                listItem.put("acalter",acalter);
                                            } else if ("lightnum".equals(startname)){
                                                String lightnum = xmlPullParser.nextText();
                                                listItem.put("lightnum",lightnum);
                                            } else if ("light".equals(startname)){
                                                String light = xmlPullParser.nextText();
                                                listItem.put("light",light);
                                            } else if ("lightalter".equals(startname)){
                                                String lightalter = xmlPullParser.nextText();
                                                listItem.put("lightalter",lightalter);
                                            }
                                            break;
                                        case XmlPullParser.END_TAG:
                                            break;
                                    }
                                    type=xmlPullParser.next();
                                }

                                /**
                                 * 子线程发送消息到主线程，并将获取的结果带到主线程，让主线程来更新UI。
                                 */
                                Message msg = new Message();
                                msg.obj = listItem;
                                msg.what = SUCCESS;
                                handler.sendMessage(msg);

                            } else {

                                Message msg = new Message();
                                msg.what = ERRORCODE;
                                handler.sendMessage(msg);
                            }
                        } catch (MalformedURLException e) {

                            e.printStackTrace();
                            /**
                             * 如果获取失败，或出现异常，那么子线程发送失败的消息（FAILURE）到主线程，主线程显示Toast，来告诉使用者，数据获取是失败。
                             */
                            Message msg = new Message();
                            msg.what = FAILURE;
                            handler.sendMessage(msg);
                        }catch (IOException e){
                            e.printStackTrace();
                        } catch (XmlPullParserException e){
                            e.printStackTrace();
                        }
                    }
                }.start();
                break;
            case R.id.btn_clear:
                tv_param1.setText("");
                tv_data1.setText("");

                tv_food1.setText("");
                tv_revisefood.setText("");

                tv_drink1.setText("");
                tv_revisedrink.setText("");

                tv_ac1.setText("");
                tv_reviseac.setText("");
                tv_acnum.setText("");

                tv_light1.setText("");
                tv_reviselight.setText("");
                tv_lightnum.setText("");
                break;

            case R.id.btn_revisefood:
                Intent intent = new Intent(this, FoodActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_reviseac:
                Intent intent1 = new Intent(this, AcActivity.class);
                startActivity(intent1);
                break;
            case R.id.btn_reviselight:
                Intent intent2 = new Intent(this, LightActivity.class);
                startActivity(intent2);
                break;
            default:
                break;

        }
    }
}
