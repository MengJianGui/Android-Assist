package com.example.administrator.sunxiaohong;

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
import java.util.HashMap;
import java.util.Map;

public class LightActivity extends AppCompatActivity{

    private TextView tv_method1,tv_result1,tv_service1,tv_revise1,tv_reviseresult1;
    private Map<String,String> listItem = new HashMap<>();
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private final int ERRORCODE = 2;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    /**
                     * 获取信息成功后，对该信息进行JSON解析，得到所需要的信息，然后在textView上展示出来。
                     */
                    Map<String,String>listMessage =(Map<String, String>)msg.obj;
                    setMessage(listMessage);
                    Toast.makeText(LightActivity.this, "获取数据成功", Toast.LENGTH_SHORT)
                            .show();
                    break;

                case FAILURE:
                    Toast.makeText(LightActivity.this, "获取数据失败", Toast.LENGTH_SHORT)
                            .show();
                    break;

                case ERRORCODE:
                    Toast.makeText(LightActivity.this, "获取的CODE码不为200！",
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
        setContentView(R.layout.activity_ac);
        init();
        new PlayThread().start();
    }

    protected void init(){
        tv_method1 = (TextView) findViewById(R.id.tv_method1);
        tv_result1 = (TextView) findViewById(R.id.tv_result1);
        tv_service1 = (TextView) findViewById(R.id.tv_service1);
        tv_revise1 = (TextView) findViewById(R.id.tv_revise1);
        tv_reviseresult1 = (TextView) findViewById(R.id.tv_reviseresult1);
        android.support.v7.widget.Toolbar tl_head = (android.support.v7.widget.Toolbar) findViewById(R.id.tl_head);
        tl_head.setTitle("灯具服务页面");
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

        tv_method1.setText(message.get("method"));
        tv_result1.setText(message.get("result"));

        tv_service1.setText(message.get("standard"));

        tv_revise1.setText(message.get("alter"));
        tv_reviseresult1.setText(message.get("resultofalter"));
    }

    private class PlayThread extends Thread{
        @Override
        public void run(){
            int code;
            try {
                String path = "http://10.20.4.225:8080/client/api?command=LightInference&apikey=fPe2s6FbR5VeBQJuGBWIDHGxoHjo4IBBbfVwopVDctf1E9lyLemrGtFrmYAXWNxcTV-u2Choh2YQYV2RGhPdpQ&signature=T7TdkLVQjHpIOqCEECnv2aSjZtc%3D";
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
                                if ("LightAndInferenceresponse".equals(startname)){
                                    String version = xmlPullParser.getAttributeValue(0);
                                    listItem.put("version",version);
                                }else if ("method".equals(startname)){
                                    String method = xmlPullParser.nextText();
                                    listItem.put("method",method);
                                }else if ("result".equals(startname)){
                                    String result = xmlPullParser.nextText();
                                    listItem.put("result",result);
                                }else if ("standard".equals(startname)){
                                    String standard = xmlPullParser.nextText();
                                    listItem.put("standard",standard);
                                }else if ("alter".equals(startname)){
                                    String alter = xmlPullParser.nextText();
                                    listItem.put("alter",alter);
                                }else if ("resultofalter".equals(startname)){
                                    String resultofalter = xmlPullParser.nextText();
                                    listItem.put("resultofalter",resultofalter);
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
