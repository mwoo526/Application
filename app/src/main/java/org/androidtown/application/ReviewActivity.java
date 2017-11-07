package org.androidtown.application;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class ReviewActivity extends AppCompatActivity implements View.OnClickListener{

    EditText editText1,editText2,editText3;
    ImageButton imageButton;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        setTitle("평가 및 리뷰");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editText1=(EditText)findViewById(R.id.editText1);
        editText2=(EditText)findViewById(R.id.editText2);
        editText3=(EditText)findViewById(R.id.editText3);
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
                break;
            case R.id.imageButton:
                Intent intent=new Intent(this,CameraActivity.class);
                startActivity(intent);
                break;
        }

    }
}
