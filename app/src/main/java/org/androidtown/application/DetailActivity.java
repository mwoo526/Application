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
    String storename,storetime,storemenu1,storemenu2,storemenu3,storeprice1,storeprice2,storeprice3,storeaddress,storetel;
    String menu1,menu2,menu3,price1,price2,price3;
    JSONObject jObject;

    final String ip="http://13.124.233.188/process/liststore";

    String url="http://13.124.233.188/process/adddevice";
    String registerUrl="http://13.124.233.188/process/register";



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

        checkRequest();
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



    public void transformJson(String data){
        try{
            JSONArray jarray = new JSONArray(data);
            for(int i =0; i<jarray.length();i++) {
                jObject = jarray.getJSONObject(i);
                storename = jObject.getString("storename");
                storemenu1 = jObject.getString("storemenu1");
                storeprice1 = jObject.getString("storeprice1");
                storemenu2 = jObject.getString("storemenu2");
                storeprice2 = jObject.getString("storeprice2");
                storemenu3 = jObject.getString("storemenu3");
                storeprice3 = jObject.getString("storeprice3");
                if(storename.equals((getStorename()))){
                    menu1=storemenu1;
                    menu2=storemenu2;
                    menu3=storemenu3;
                    price1=storeprice1;
                    price2=storeprice2;
                    price3=storeprice3;

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button1:
                addDevice();
                String regId = FirebaseInstanceId.getInstance().getToken();
                sendToMobileServer(regId);
                break;

            case R.id.button2:
                Intent intent = new Intent(this,ReviewActivity.class);
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

    private void addDevice() {

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String>  params = new HashMap<>();

                DeviceInfo device = getDeviceInfo();
                params.put("mobile", device.getMobile());
                params.put("osVersion", device.getOsVersion());
                params.put("model", device.getModel());
                params.put("display", device.getDisplay());
                params.put("manufacturer", device.getManufacturer());
                params.put("macAddress", device.getMacAddress());

                return params;
            }
        };

        request.setShouldCache(false);
        Volley.newRequestQueue(this).add(request);
    }

    public DeviceInfo getDeviceInfo() {
        DeviceInfo device = null;

        // 1. mobile
        String mobile = null;
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if ( telephonyManager.getLine1Number() != null ) {
            mobile = telephonyManager.getLine1Number();
        }

        // 2. osVersion
        String osVersion = Build.VERSION.RELEASE;

        // 3. model
        String model = Build.MODEL;

        // 4. display
        String display = getDisplay(this);

        // 5. manufacturer
        String manufacturer = Build.MANUFACTURER;

        // 6. macAddress
        String macAddress = getMacAddress(this);

        device = new DeviceInfo(mobile, osVersion, model, display, manufacturer, macAddress);

        return device;
    }

    /**
     *  get display resolution
     */
    private static String getDisplay(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        int deviceWidth = displayMetrics.widthPixels;
        int deviceHeight = displayMetrics.heightPixels;

        return deviceWidth + "x" + deviceHeight;
    }

    /**
     * get WiFi MAC address
     */
    private static String getMacAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();

        return info.getMacAddress();
    }
    public void sendToMobileServer(final String regId) {
        StringRequest request = new StringRequest(Request.Method.POST, registerUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String>  params = new HashMap<>();

                params.put("mobile", getMobile());
                params.put("registrationId", regId);

                return params;
            }
        };

        request.setShouldCache(false);
        Volley.newRequestQueue(getApplicationContext()).add(request);

    }

    public String getMobile() {
        String mobile = null;
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if ( telephonyManager.getLine1Number() != null ) {
            mobile = telephonyManager.getLine1Number();
        }

        return mobile;
    }

    /**
     * 수신자로부터 전달받은 Intent 처리
     *
     * @param intent
     */

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent != null) {
            processIntent(intent);
        }

    }



    private void processIntent(Intent intent) {
        String data = intent.getStringExtra("data");
        if(data==null){
            DisabledAlert();
        }
        else{

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("예약알림")
                        .setIcon(R.drawable.alert)
                        .setMessage(data)
                        .setCancelable(false)
                        .setPositiveButton("확인",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }

    }
    private void DisabledAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("알림이 없습니다.")
                .setCancelable(false)
                .setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
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

    public String getStorename() {
        return storename;
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

    public String getMenu1() {
        return menu1;
    }

    public String getMenu2() {
        return menu2;
    }

    public String getMenu3() {
        return menu3;
    }

    public String getPrice1() {
        return price1;
    }

    public String getPrice2() {
        return price2;
    }

    public String getPrice3() {
        return price3;
    }
}