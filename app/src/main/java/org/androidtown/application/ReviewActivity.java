package org.androidtown.application;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.androidtown.application.model.DeviceInfo;

import java.util.HashMap;
import java.util.Map;

public class ReviewActivity extends AppCompatActivity implements View.OnClickListener{

    EditText editText1,editText2,editText3;
    ImageButton imageButton;
    Button button;
    //String url="http://13.124.233.188/process/listreview";
    //String url="http://192.168.0.20:3000/process/listreview";
    String url="http://192.168.0.6:3000/process/listreview";

    String storename,reviewtitle,reviewcontent,reviewscore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        setTitle("평가 및 리뷰");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent =getIntent();
        storename=intent.getStringExtra("storename");

        editText1=(EditText)findViewById(R.id.editText1); // 제목
        editText2=(EditText)findViewById(R.id.editText2); // 평점
        editText3=(EditText)findViewById(R.id.editText3); // 내용
        imageButton=(ImageButton)findViewById(R.id.imageButton);
        button=(Button)findViewById(R.id.button);

        imageButton.setOnClickListener(this);
        button.setOnClickListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(this,DetailActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button:
                reviewtitle=editText1.getText().toString();
                reviewcontent=editText3.getText().toString();
                reviewscore=editText2.getText().toString();
                addReview();
                break;
            case R.id.imageButton:
                Intent intent=new Intent(this,CameraActivity.class);
                startActivity(intent);
                break;
        }

    }

    private void addReview() {

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
            protected Map<String, String> getParams()  throws com.android.volley.AuthFailureError {
                    Map<String, String>  params = new HashMap<>();
                Log.e("test",getStorename()+getReviewtitle()+getReviewcontent()+getReviewscore());
                params.put("reviewstore", getStorename());
                params.put("reviewtitle", getReviewtitle());
                params.put("reviewcontent", getReviewcontent());
                params.put("reviewscore", getReviewscore());

                return params;
            }
        };

        request.setShouldCache(false);
        Volley.newRequestQueue(this).add(request);
    }

    public String getStorename() {
        return storename;
    }

    public String getReviewtitle() {
        return reviewtitle;
    }

    public String getReviewcontent() {
        return reviewcontent;
    }

    public String getReviewscore() {
        return reviewscore;
    }
}
