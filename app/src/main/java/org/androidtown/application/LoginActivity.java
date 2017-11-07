package org.androidtown.application;

import android.content.DialogInterface;
import android.content.Intent;
import android.preference.DialogPreference;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.androidtown.application.helper.BackHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity  implements View.OnClickListener{

    private BackHelper backHelper;

    final String ip="http://13.124.233.188/process/listuser";

    Button button;
    EditText editText1,editText2;
    String checkId,checkPw;
    String id,pw,name;
    JSONObject jObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar= getSupportActionBar();
        actionBar.hide();

        button=(Button)findViewById(R.id.button);
        editText1=(EditText)findViewById(R.id.editText1);
        editText2=(EditText)findViewById(R.id.editText2);
        button.setOnClickListener(this);

        backHelper=new BackHelper(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:

               checkRequest();
        }
    }
    // 서버에 데이터를 요청한 후 문자열 타입으로 된 응답을 받기위해 StringRequest 객체생성 후
    //  Volley 라이브러리 의 RequestQueue에 넣어준다.
    // parm1 = POST, GET 방식
    // parm2 = ip 주소
    // parm3 = 웹서버로부터 응답을 받은 문자열 데이터를 확인

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
        checkId=editText1.getText().toString();
        checkPw=editText2.getText().toString();
        try{
            JSONArray jarray = new JSONArray(data);
            for(int i =0; i<jarray.length();i++) {
                jObject = jarray.getJSONObject(i);
                id = jObject.getString("id");
                pw = jObject.getString("password");
                name = jObject.getString("name");
                checkData(id,pw);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void checkData(String id, String pw) {
        String checkId=editText1.getText().toString();
        String checkPw=editText2.getText().toString();

        if (id.equals(checkId) && pw.equals(checkPw)){
            Intent intent = new Intent(this,MainActivity.class);
            intent.putExtra("Json",jObject.toString());
            intent.putExtra("name",name);
            startActivity(intent);
        }

    }
    public void showAlertDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("아이디 또는 비밀번호가 맞지 않습니다.");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        } );
        builder.create();
        builder.show();
    }

    @Override
    public void onBackPressed() {
        backHelper.onBackPressed();
    }
}
