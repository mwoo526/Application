package org.androidtown.application;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.iid.FirebaseInstanceId;

import org.androidtown.application.helper.BackHelper;
import org.androidtown.application.model.DeviceInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import noman.googleplaces.NRPlaces;
import noman.googleplaces.Place;
import noman.googleplaces.PlaceType;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener ,PlacesListener {

    //final String ip="http://13.124.233.188/process/liststore";
    final String ip="http://192.168.0.20:3000/process/liststore";
    //String url="http://13.124.233.188/process/adddevice";
    String url="http://192.168.0.20:3000/process/adddevice";
    //String registerUrl="http://13.124.233.188/process/register";
    String registerUrl="http://192.168.0.20:3000/process/register";

    private BackHelper backHelper;

    TextView textView;
    ImageButton imageButton1,imageButton2,imageButton3,imageButton4,imageButton5;
    EditText editText;
    String storename,storetime,storeaddress,storetel;
    String storemenu1,storemenu2,storemenu3,storeprice1,storeprice2,storeprice3;
    JSONObject jObject;
    SupportMapFragment mapfragment;
    GoogleMap map;
    MarkerOptions marker;
    LocationManager locationManager;

    //Geocoder
    final Geocoder geocoder = new Geocoder(this);

    //Places API Web Service
    LatLng currentPosition;
    List<Marker> previous_marker = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar= getSupportActionBar();
        actionBar.hide();

        Intent intent = getIntent();
        String name = intent.getStringExtra("nickname");
        textView=(TextView)findViewById(R.id.textView);
        textView.setText(name+" 님 ");

        imageButton1 = (ImageButton)findViewById(R.id.imageButton1);
        imageButton2 = (ImageButton)findViewById(R.id.imageButton2);
        imageButton3 = (ImageButton)findViewById(R.id.imageButton3);
        imageButton4 = (ImageButton)findViewById(R.id.imageButton4);
        imageButton5 = (ImageButton)findViewById(R.id.imageButton5);
        editText=(EditText) findViewById(R.id.editText);
        mapfragment=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.mapfragment);

        imageButton1.setOnClickListener(this);
        imageButton2.setOnClickListener(this);
        imageButton3.setOnClickListener(this);
        imageButton3.setOnClickListener(this);
        imageButton4.setOnClickListener(this);
        imageButton5.setOnClickListener(this);


        mapfragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map=googleMap;
                map.setMyLocationEnabled(true);
                // 현재 나의 위치를 추적
            }
        });

        try{
            MapsInitializer.initialize(this);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        backHelper=new BackHelper(this);


        //Permission();

        //Places API Web Service
        previous_marker = new ArrayList<Marker>();

        //단말 등록
        addDevice();
        String regId = FirebaseInstanceId.getInstance().getToken();
        //서버로 등록아이디 전송
        sendToMobileServer(regId);

    }

    // 권한 설정 (targetSdkVersion 이 23 이상일때
    private void Permission() {
       if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
               !=PackageManager.PERMISSION_GRANTED){
           if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION))
           {}
           else{
               ActivityCompat.requestPermissions(this,new String[]{
                       Manifest.permission.ACCESS_FINE_LOCATION,
                       Manifest.permission.ACCESS_COARSE_LOCATION
               },100);
           }
       }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageButton1:
                checkRequest();
                break;

            case R.id.imageButton2:
                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    createGpsDisabledAlert();
                    imageButton2.setVisibility(View.INVISIBLE);
                    imageButton3.setVisibility(View.VISIBLE);
                }
                else{
                    imageButton2.setVisibility(View.INVISIBLE);
                    imageButton3.setVisibility(View.VISIBLE);
                }

                break;

            case R.id.imageButton3:
                if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    createGpsDisabledAlert();
                }
                    requestMyLocation();

                break;
            case R.id.imageButton4:
                //
                try {
                showPlaceInformation(currentPosition);
                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(),"위치설정을 한후 건물밖으로 나오세요.",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.imageButton5:
                Intent intent = new Intent(getApplicationContext(),ReserveActivity.class);
                startActivity(intent);
                break;


        }
    }

    // 맛집 정보 요청
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

    // 맛집 정보(Json 형식) -> String 으로 반환 및 검색
    public void transformJson(String data){
        try{
            JSONArray jarray = new JSONArray(data);
            for(int i =0; i<jarray.length();i++) {
                jObject = jarray.getJSONObject(i);
                storename = jObject.getString("storename");
                storetime = jObject.getString("storetime");
                storeaddress = jObject.getString("storeaddress");
                storetel = jObject.getString("storetel");
                storemenu1 = jObject.getString("storemenu1");
                storemenu2 = jObject.getString("storemenu2");
                storemenu3 = jObject.getString("storemenu3");
                storeprice1 = jObject.getString("storeprice1");
                storeprice2 = jObject.getString("storeprice2");
                storeprice3 = jObject.getString("storeprice3");

                String checkName=editText.getText().toString();

                if (storename.equals(checkName)){
                    Intent intent = new Intent(this,DetailActivity.class)
                            .putExtra("Json",jObject.toString())
                            .putExtra("storename",storename)
                            .putExtra("storetime",storetime)
                            .putExtra("storeaddress",storeaddress)
                            .putExtra("storetel",storetel)
                            .putExtra("storemenu1",storemenu1)
                            .putExtra("storemenu2",storemenu2)
                            .putExtra("storemenu3",storemenu3)
                            .putExtra("storeprice1",storeprice1)
                            .putExtra("storeprice2",storeprice2)
                            .putExtra("storeprice3",storeprice3);
                    startActivity(intent);
                    break;
                }
                else if(!storename.equals(checkName)) {
                    showAlertDialog();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //알림창 구현
    public void showAlertDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("경고");
            builder.setIcon(R.drawable.alert);
            builder.setMessage("맛집명을 확인해주세요.");
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.create();
            builder.show();
    }

    // 위치설정 다이얼로그
    private void createGpsDisabledAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("\n"+"위치 정보 사용 옵션이 꺼져있습니다. " +"\n"+
                "정확하고 편리한 탐색을 위해 위치 정보 사용옵션을 켜주세요."+"\n")
                .setCancelable(false)
                .setPositiveButton("설정",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                showGpsOptions();
                            }
                        })
                .setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    // 위치 설정 (암시적 인텐트)
    private void showGpsOptions() {
        Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(gpsOptionsIntent);
    }


    // 위치 관리자로 부터 현재 위치를 전달받도록 한다.
    private void requestMyLocation() {
        LocationManager manager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try {
            long minTime = 10000;
            float minDistance = 0;
            manager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    minTime,
                    minDistance,
                    new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            showCurrentLocation(location);

                            //Places API Web Service
                            currentPosition = new LatLng( location.getLatitude(), location.getLongitude());
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    }
            );
            Location lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastLocation != null) {
                showCurrentLocation(lastLocation);
            }

            manager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    minTime,
                    minDistance,
                    new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            showCurrentLocation(location);
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    }
            );


        } catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    private void showCurrentLocation(Location location) {
        // 현재 위치를 이용해 LatLng 객체 생성
        LatLng curPoint = new LatLng(location.getLatitude(), location.getLongitude());

        // 현재 위치를 지도의 중심으로 표시
        // 축척 1~19 or 21
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 18));

        checkLocation();
    }


    // 맛집정보 중 주소(Json)  를 Geocoder api를 통해 위도,경도값으로 변환
    public void checkLocation() {
        StringRequest request = new StringRequest(
                Request.Method.POST, ip,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        List<Address> address = new ArrayList<>();
                        List<String> list =new ArrayList<String>();
                        try{
                            JSONArray jarray = new JSONArray(response);
                            for(int i =0; i<jarray.length();i++) {
                                JSONObject jObject = jarray.getJSONObject(i);
                                String storeaddress = jObject.getString("storeaddress");
                                try {
                                    list.add(storeaddress);
                                    Log.d("test",list.get(i));
                                    address = geocoder.getFromLocationName(list.get(i), 10);
                                                                                                            // 지역이름 , 읽을 개수
                                    showMarker(address.get(0).getLatitude(),address.get(0).getLongitude() ) ;    // location 객체 , 위도 ,경도*/
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                    Log.e("test","입출력 오류 - 서버에서 주소변환시 에러발생");
                                }
                            }

                        } catch (JSONException e) {
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
        );
        request.setShouldCache(false);
        Volley.newRequestQueue(this).add(request);
    }


    // DB에 저장된 맛집 주소를 받아와 marker 로 표시
    private void showMarker(Double latitude,Double longitude){
            marker = new MarkerOptions();
            marker.position(new LatLng(latitude,longitude));
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
            map.addMarker(marker);
    }

    // onPause(), onResume() 상태일때도 계속적으로 나의 위치 추적
    @Override
    protected void onPause() {
        super.onPause();
        if(map !=null){
            map.setMyLocationEnabled(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(map !=null){
            map.setMyLocationEnabled(true);
        }
    }

    // Back 키 기능으로 종료
    @Override
    public void onBackPressed() {
        backHelper.onBackPressed();
    }

    //Places API Web Service
    //PlacesListener 인터페이스 구현 메소드 4개
    @Override
    public void onPlacesFailure(PlacesException e) {}

    @Override
    public void onPlacesStart() {}

    @Override
    public void onPlacesSuccess(final List<Place> places) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (noman.googleplaces.Place place : places) {

                    LatLng latLng
                            = new LatLng(place.getLatitude()
                            , place.getLongitude());

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title(place.getName());
                    markerOptions.snippet(place.getVicinity());
                    Marker item = map.addMarker(markerOptions);
                    previous_marker.add(item);

                }

                //중복 마커 제거
                HashSet<Marker> hashSet = new HashSet<Marker>();
                hashSet.addAll(previous_marker);
                previous_marker.clear();
                previous_marker.addAll(hashSet);

            }
        });

    }

    @Override
    public void onPlacesFinished() {}



    public void showPlaceInformation(LatLng location)
    {

        //map.clear();
        // 지도 클리어

        if (previous_marker != null)
            previous_marker.clear();//지역정보 마커 클리어

        new NRPlaces.Builder()
                .listener(MainActivity.this)
                .key("AIzaSyBJnb7okH2xRIfR_2LseQjYJnMIw1c2sJ0")   //Places API Web Service 키
                .latlng(location.latitude, location.longitude)//현재 위치
                .radius(500) //500 미터 내에서 검색
                .type(PlaceType.RESTAURANT) //음식점
                .build()
                .execute();
    }


    // 디바이스 등록
    private void addDevice() {
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
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

    //디바이스 정보
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

    public String getMobile() {
        String mobile = null;
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if ( telephonyManager.getLine1Number() != null ) {
            mobile = telephonyManager.getLine1Number();
        }

        return mobile;
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



    //서버로 등록 ID 전송
    public void sendToMobileServer(final String regId) {
        StringRequest request = new StringRequest(Request.Method.POST, registerUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
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

}
