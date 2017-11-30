package org.androidtown.application;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import org.androidtown.application.model.DeviceInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView imageView1;
    TextView textView1,textView2;
    Button button1,button2,button3,button4;
    Fragment fr;
    private boolean isFragment=true;
    String storename,storetime,storeaddress,storetel;
    String storemenu1,storemenu2,storemenu3,storeprice1,storeprice2,storeprice3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);
        ActionBar actionBar= getSupportActionBar();
        actionBar.hide();
        Intent intent = getIntent();
        storename = intent.getStringExtra("storename");
        storetime = intent.getStringExtra("storetime");
        storeaddress = intent.getStringExtra("storeaddress");
        storetel = intent.getStringExtra("storetel");
        storemenu1=intent.getStringExtra("storemenu1");
        storemenu2=intent.getStringExtra("storemenu2");
        storemenu3=intent.getStringExtra("storemenu3");
        storeprice1=intent.getStringExtra("storeprice1");
        storeprice2=intent.getStringExtra("storeprice2");
        storeprice3=intent.getStringExtra("storeprice3");

        imageView1=(ImageView)findViewById(R.id.imageButton1);

        textView1=(TextView)findViewById(R.id.textView1);
        textView2=(TextView)findViewById(R.id.textView2);
        textView2.setText(storename);

        button1=(Button)findViewById(R.id.button1);
        button2=(Button)findViewById(R.id.button2);
        button3=(Button)findViewById(R.id.button3);
        button4=(Button)findViewById(R.id.button4);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);

        getSupportFragmentManager().beginTransaction().add(R.id.detailfragment, new DetailFragment()).commit();


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button1:


            case R.id.button2:
                Intent intent = new Intent(this,ReviewActivity.class);
                intent.putExtra("storename",storename);
                startActivity(intent);
                break;

            case R.id.button3:
                switchFragment();
                button3.setVisibility(View.INVISIBLE);
                button4.setVisibility(View.VISIBLE);
                break;

            case R.id.button4:
                switchFragment();
                button4.setVisibility(View.INVISIBLE);
                button3.setVisibility(View.VISIBLE);
                break;
        }
    }

    // 상세정보 프레그먼트와 메뉴 프레그먼트를 교체
    private void switchFragment() {

        if(isFragment){
            fr=new MenuFragment();
        }else{
            fr=new DetailFragment();
        }
        isFragment=(isFragment) ? false : true;

        getSupportFragmentManager().beginTransaction().replace(R.id.detailfragment,fr).commit();
    }

    public String getStoretime() {
        return storetime;
    }

    public String getStoreaddress() {
        return storeaddress;
    }

    public String getStoretel() {
        return storetel;
    }

    public String getStoremenu1() {
        return storemenu1;
    }

    public String getStoremenu2() {
        return storemenu2;
    }

    public String getStoreprice1() {
        return storeprice1;
    }

    public String getStoreprice2() {
        return storeprice2;
    }

    public String getStoremenu3() {
        return storemenu3;
    }

    public String getStoreprice3() {
        return storeprice3;
    }
}