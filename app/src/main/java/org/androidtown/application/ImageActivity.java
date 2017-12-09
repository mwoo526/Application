package org.androidtown.application;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ToggleButton;
import android.widget.ViewFlipper;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ImageActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{

    String storename;
    String reviewstore,file;
    final String ip="http://13.124.233.188/process/listreview";
    JSONObject jObject;
    ArrayList<String> fileList = new ArrayList<>();
    ViewFlipper flipper;
    ArrayList<Bitmap> bitmap = new ArrayList<>();
    ToggleButton toggleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        Intent intent=getIntent();
        storename=intent.getStringExtra("storename");
        flipper=(ViewFlipper)findViewById(R.id.fipper);
        toggleButton=(ToggleButton)findViewById(R.id.togglebutton);
        checkRequest();

        toggleButton.setOnCheckedChangeListener(this);
    }
    public void checkRequest() {
        StringRequest request = new StringRequest(
                Request.Method.POST, ip,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        transformJson(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );
        request.setShouldCache(false);
        Volley.newRequestQueue(this).add(request);
    }

    public void transformJson(String data) {
        try {
            JSONArray jarray = new JSONArray(data);
            for (int i = 0; i < jarray.length(); i++) {
                jObject = jarray.getJSONObject(i);
                reviewstore=jObject.getString("reviewstore");
                file=jObject.getString("file");

                if(storename.equals(reviewstore)){
                    Log.e("image",file);
                    fileList.add(file);

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

            Thread mThread = new Thread(){
                @Override
                public void run() {
                    try{
                        for(int j=0;j<fileList.size();j++) {
                            URL url = new URL("http://13.124.233.188/" + fileList.get(j));
                            // URL 주소를 이용해서 URL 객체 생성

                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setDoInput(true);
                            conn.connect();

                            InputStream is = conn.getInputStream();
                            bitmap.add(BitmapFactory.decodeStream(is));
                        }
                        //웹에서 이미지를 가져온뒤 이미지뷰에 지정할 Bitmap을 생성하는 과정
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            mThread.start(); //웹에서 이미지를 가져오는 작업 스레드 실행
            try{
                mThread.join();
                // 메인 스레드는 작업 스레드가 이미지 작업을 가져올때까지 대기해야 하므로
                // 작업 스레드 의 join() 메소드를 호출해서
                // 메인 스레드가 작업 스레드가 종료될때까지 기다린다.
                for(int i=0;i<bitmap.size();i++) {
                    ImageView img = new ImageView(this);
                    img.setImageBitmap(bitmap.get(i));
                    flipper.addView(img);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if(b){
            //1초의 간격으로 ViewFlipper의 View 자동 교체
            flipper.setFlipInterval(3000);//플리핑 간격(1000ms)
            flipper.startFlipping();//자동 Flipping 시작

        }else{
            flipper.stopFlipping();//Flipping 정지
        }
    }
}
