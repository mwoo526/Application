package org.androidtown.application;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
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
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import org.androidtown.application.helper.DateTimeHelper;
import org.androidtown.application.model.DeviceInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView imageView1;
    TextView textView1,textView2;

    Button button1,button2,button3,button4;
    ImageButton imageButton1;
    Fragment fr;
    private boolean isFragment=true;
    String storename,storetime,storeaddress,storetel;
    String storemenu1,storemenu2,storemenu3,storeprice1,storeprice2,storeprice3;
    String file;
    String baseurl;
    Bitmap bitmap;

    private java.util.Calendar EditCal;
    private int Year,Month,Day,Starthour,Startmin;
    int EditYear;
    int EditMonth;
    int EditDay;
    int YEAR =0, MONTH=0,DAY=0,HOUR=0,MINUTE=0;

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
        file=intent.getStringExtra("file");
        baseurl="http://13.124.233.188/"+file;;
        imageView1=(ImageView)findViewById(R.id.imageView1);
        textView1=(TextView)findViewById(R.id.textView1);
        textView2=(TextView)findViewById(R.id.textView2);
        button1=(Button)findViewById(R.id.button1);
        button2=(Button)findViewById(R.id.button2);
        button3=(Button)findViewById(R.id.button3);
        button4=(Button)findViewById(R.id.button4);
        imageButton1=(ImageButton)findViewById(R.id.imageButton1);

        EditCal = java.util.Calendar.getInstance(Locale.KOREA);
        EditYear = EditCal.get(java.util.Calendar.YEAR);
        EditMonth = EditCal.get(java.util.Calendar.MONTH);
        EditDay = EditCal.get(java.util.Calendar.DAY_OF_MONTH);

        int[] date = DateTimeHelper.getInstance().getDate();
        YEAR=date[0];
        MONTH=date[1];
        DAY=date[2];

        int[] time = DateTimeHelper.getInstance().getTime();
        HOUR=time[0];
        MINUTE=time[1];

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        imageButton1.setOnClickListener(this);

        textView2.setText(storename);

        //안드로이드에서 네트워크 관련 작업을 할때는 별도의 작업 스레드에서 작업해야한다.

        Thread mThread = new Thread(){
            @Override
            public void run() {
                try{
                    URL url = new URL(baseurl);
                    // URL 주소를 이용해서 URL 객체 생성

                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
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
            imageView1.setImageBitmap(bitmap);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        getSupportFragmentManager().beginTransaction().add(R.id.detailfragment, new DetailFragment()).commit();


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button1:
                Intent intent1=new Intent(this,ImageActivity.class);
                intent1.putExtra("storename",storename);
                startActivity(intent1);
                break;

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

            case R.id.imageButton1:
                dateAlarm();

                break;

        }
    }

    public class AlarmHATT { // Manifest 부분에 진동, 홀드상태 활성화 두개의 퍼미션 추가
        Context context;
        public AlarmHATT(Context context) {
            this.context=context;
        }
        public void Alarm() {
            AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(DetailActivity.this, Receiver.class);
            intent.putExtra("title",storename);
            intent.putExtra("address",storeaddress);
            intent.putExtra("opentime",Starthour+"시 "+Startmin+"분");
            PendingIntent sender = PendingIntent.getBroadcast(DetailActivity.this, 0, intent, 0 );
            Calendar cal;
            cal= Calendar.getInstance(Locale.KOREA);
            cal.set(Calendar.YEAR,Year);
            cal.set(Calendar.MONTH,Month);
            cal.set(Calendar.DAY_OF_MONTH,Day);
            cal.set(Calendar.HOUR_OF_DAY, Starthour);
            cal.set(Calendar.MINUTE, Startmin-30); //30분전 푸시알림
            cal.set(Calendar.SECOND,00);
            //알람 예약
            am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), sender);
            Log.e("alarm",Year+" / "+Month+1 +" / "+Day+" / "+Starthour+" / "+Startmin );
            if(Starthour<=12)
                Toast.makeText(getApplicationContext(), "오전  "+Starthour+"시 "+Startmin+"분"+"  예약 되엇습니다.",Toast.LENGTH_LONG).show();
            if(Starthour>12)
                Starthour-=12;
                Toast.makeText(getApplicationContext(), "오후  "+Starthour+"시 "+Startmin+"분"+"  예약 되엇습니다.",Toast.LENGTH_LONG).show();

        }
    }
    void dateAlarm(){
        final int temp_yy = YEAR;
        final int temp_mm = MONTH;
        final int temp_dd = DAY;

        DatePickerDialog dialog= new DatePickerDialog(this,new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayofMonth) {
                StartDate(year,monthOfYear,dayofMonth);
                timeAlarm();
            }
        },YEAR,MONTH,DAY);

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener(){

            @Override
            public void onCancel(DialogInterface dialogInterface) {
                YEAR=temp_yy;
                MONTH=temp_mm;
                DAY=temp_dd;
            }
        });
        dialog.setTitle("예약 알림설정");
        dialog.setIcon(R.drawable.alarm);
        dialog.setMessage("예약날짜을 선택하세요");
        dialog.show();

    }


    void timeAlarm()
    {

        final int temp_hh = HOUR;
        final int temp_mi = MINUTE;

        TimePickerDialog dialog = new TimePickerDialog(this,new TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                StartTime( hourOfDay,  minute);
                new AlarmHATT(getApplicationContext()).Alarm();

            }
        },HOUR,MINUTE,false);

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener(){

            @Override
            public void onCancel(DialogInterface dialogInterface) {
                HOUR=temp_hh;
                MINUTE=temp_mi;
            }
        });

        dialog.setTitle("예약 알림설정");
        dialog.setIcon(R.drawable.alarm);
        dialog.setMessage("예약시간을 선택하세요");
        dialog.show();
    }

    public void StartDate(int year, int monthOfYear, int dayofMonth){
        Year=year;
        Month=monthOfYear;
        Day=dayofMonth;
    }

    public void StartTime(int hourOfDay, int minute){
        Starthour=hourOfDay;
        Startmin=minute;
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