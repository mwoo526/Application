package org.androidtown.application;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidtown.application.helper.PhotoHelper;
import org.androidtown.application.helper.UploadFileHelper;

import java.io.File;

public class CameraActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView imageView;
    Button button1;
    String photoPath = null; // 촬영 결과물이 저장될 경로
    Bitmap bmp = null;  // 메모리로 로드한 이미지가 저장될 객체
    final int REQUEST_READ_FROM_EXTERNAL_STORAGE = 1;
    File dir;
    TextView messageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        setTitle("촬영 및 저장");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageView=(ImageView)findViewById(R.id.imageView);
        button1=(Button)findViewById(R.id.button1);
        button1.setOnClickListener(this);
        capture();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(this,ReviewActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void capture(){
        // 저장될 사진 파일 경로
        // -> ./mnt/sdcard/DCIM/p2017-11-04 17-45-00.jpg
        photoPath= PhotoHelper.getInstance().getNewPhotoPath();
        //카메라 APP 호출을 위한 암묵적 Intent
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //저장될 경로를 파라미터로 설정 --> URI
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.parse("file://" + photoPath));
        intent.putExtra(AUDIO_SERVICE,false);

        //카메라 호출 --> 저장 결과를 받기 위한 설정 필요함
        startActivityForResult(intent,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==100){
            if(resultCode== Activity.RESULT_OK){
                Intent photoIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.parse("file://" + photoPath));

                sendBroadcast(photoIntent);
                //기존에 표시되던 이미지 제거
                imageView.setImageBitmap(null);

                //그림이 저장된 객체가 존재한다면 ,메모리 해제
                //bmp는 gc의 수집 대상이 아니므로,
                //반드시 메모리 해제를 위해서 recycle()을 호출해야 한다.
                if(bmp!=null){
                    bmp.recycle();
                    bmp=null;
                }
                //썸네일 이미지 얻기
                bmp = PhotoHelper.getInstance().getThumb(this,photoPath);
                //출력
                imageView.setImageBitmap(bmp);
                uploadFile();
            }
        }
    }

    /*
    * bmp 객체는 App이 종료되어도 메모리에 여전히 남아 있기 때문에
    * App 종료시에 강제로 메모리에서 해제시켜야 한다.
    * */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(bmp!=null){
            bmp.recycle();
            bmp=null;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1:
                capture();

                break;
            case R.id.button2:
                break;

        }

    }

    private void uploadFile() {
        String sourceFileUri = photoPath;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {

            Log.e("uploadFile", "Source File not exist :"
                    + photoPath);

        } else {
            new UploadFileHelper(CameraActivity.this).execute(photoPath);
        }
    }



}
