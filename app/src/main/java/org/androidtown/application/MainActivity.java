package org.androidtown.application;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import noman.googleplaces.NRPlaces;
import noman.googleplaces.Place;
import noman.googleplaces.PlaceType;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener ,PlacesListener {

    ImageButton imageButton1,imageButton2,imageButton3,imageButton4;
    EditText editText;
    SupportMapFragment mapfragment;
    GoogleMap map;
    MarkerOptions marker;
    LocationManager locationManager;

    //
    LatLng currentPosition;
    List<Marker> previous_marker = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar= getSupportActionBar();
        actionBar.hide();

        imageButton1 = (ImageButton)findViewById(R.id.imageButton1);
        imageButton2 = (ImageButton)findViewById(R.id.imageButton2);
        imageButton3 = (ImageButton)findViewById(R.id.imageButton3);
        imageButton4 = (ImageButton)findViewById(R.id.imageButton4);
        EditText editText=(EditText) findViewById(R.id.editText);
        mapfragment=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.mapfragment);

        imageButton1.setOnClickListener(this);
        imageButton2.setOnClickListener(this);
        imageButton3.setOnClickListener(this);
        imageButton3.setOnClickListener(this);
        imageButton4.setOnClickListener(this);

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

        //Permission();

        //
        previous_marker = new ArrayList<Marker>();

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
                /*try {*/
                showPlaceInformation(currentPosition);
                /*}
                catch (Exception e){
                    Toast.makeText(getApplicationContext(),"위치설정을 한후 건물밖으로 나오세요.",Toast.LENGTH_SHORT).show();
                }*/
                break;

        }
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

    // 위치 설정
    private void showGpsOptions() {
        Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(gpsOptionsIntent);
    }


    // 위치 관리자로 부터 현재 위치를 전달받도록 합니다.
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

                            //
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

        //showMarker(location);
    }
    // 음식점 위치 marker 로 표시하기
    private void showMarker(Location location){
        if(marker ==null ) {
            marker = new MarkerOptions();
            marker.position(new LatLng(location.getLatitude(),location.getLongitude()));
            marker.title(" 내 위치 ");
            marker.snippet("GPS로 확인한 위치");
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
            map.addMarker(marker);
        }
        else{
            marker.position(new LatLng(location.getLatitude(),location.getLongitude()));
        }

    }

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

//////////////// placesListener 구현
    @Override
    public void onPlacesFailure(PlacesException e) {

    }

    @Override
    public void onPlacesStart() {

    }

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
    public void onPlacesFinished() {

    }

    public void showPlaceInformation(LatLng location)
    {
        map.clear();//지도 클리어

        if (previous_marker != null)
            previous_marker.clear();//지역정보 마커 클리어

        new NRPlaces.Builder()
                .listener(MainActivity.this)
                .key("AIzaSyBJnb7okH2xRIfR_2LseQjYJnMIw1c2sJ0")
                .latlng(location.latitude, location.longitude)//현재 위치
                .radius(500) //500 미터 내에서 검색
                .type(PlaceType.RESTAURANT) //음식점
                .build()
                .execute();
    }
}
