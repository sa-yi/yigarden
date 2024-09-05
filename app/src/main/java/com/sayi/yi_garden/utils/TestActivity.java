package com.sayi.yi_garden.utils;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class TestActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textView=new TextView(this);
        setContentView(textView);
        new Thread(() -> networkRequest()).start();

    }
    String cookies="";
    TextView textView;
    private void networkRequest(){
        HttpURLConnection connection=null;
        try {
            URL url = new URL("https://bbs.66ccff.cc/wp-json/wp/v2/posts");
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);
            //设置请求方式 GET / POST 一定要大小
            connection.setRequestMethod("POST");

            cookies="PHPSESSID=gtnaj5vskgq48fqvth5nhdnr1j; __51huid__KPuawXIH0WWhVaO2=eb91a1dd-0f7e-5003-b0cf-814b3e2a2d94; mailpoet_subscriber=%7B%22subscriber_id%22%3A444%7D; _pk_id.1.375a=bf4e0bfb7a0abf99.1714147134.; cf_clearance=pIrBYZlNsZO0trvzRQPOxzuZJVBakELhciZhhNQaSRA-1714909470-1.0.1.1-v6RS71_jy6gSAgJRNMXt.eCfy7Qy.095nN.QBDDSRvcD0_kCZQLjkJanESs6i.aIzk.CT4TOMkW0dfXlh8hHjA; e9b891be4972a1756cdc8b851c663818=40abee17d6b196159549c88fd500d4a7; cf_clearance=T6hoZwJEomj5YR_ODOmQgKJDVtn_C.KASF_H6Ivl590-1715445937-1.0.1.1-JUjLXImVpiNWM6uhtom096KNHmxp7zAK2oMxce1n7Y0JFYPP2HjvBQOqdM.wxTktswjqrClcrMY_vUUP6Vzi7Q; X_CACHE_KEY=ed9e3fd64c65df83f924c0d9c340063f; theme_mode=white-theme; fps_accelerat=0; wordpress_test_cookie=WP+Cookie+check; wordpress_logged_in_bdf5fb41c0e0e935d2c6e30a9ee42a6c=%E9%A3%92%E7%BF%8A%7C1722308625%7C0UzZflfOMe2TNdGkq58yyGUn0FeMBFHfMaGPC2N8fpu%7C888e79ae1bd1b3a1ef0a71027954c8d87daeb3eae40d549bd03ba900f8b9efa6; agc_verified_18=1; mailpoet_page_view=%7B%22timestamp%22%3A1721574239%7D\n";

            connection.setRequestProperty("Cookie",cookies);
            connection.connect();
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            // 读取响应
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // 打印结果
            System.out.println(response.toString());
            textView.setText(response.toString());

            // 断开连接
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private String getStringByStream(InputStream inputStream){
        Reader reader;
        try {
            reader=new InputStreamReader(inputStream,"UTF-8");
            char[] rawBuffer=new char[512];
            StringBuffer buffer=new StringBuffer();
            int length;
            while ((length=reader.read(rawBuffer))!=-1){
                buffer.append(rawBuffer,0,length);
            }
            return buffer.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
