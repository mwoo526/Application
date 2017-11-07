package org.androidtown.application;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView imageView1;
    TextView textView1,textView2;
    Button button1,button2,button3,button4;
    Fragment fr;
    private boolean isFragment=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ActionBar actionBar= getSupportActionBar();
        actionBar.hide();

        imageView1=(ImageView)findViewById(R.id.imageButton1);

        textView1=(TextView)findViewById(R.id.textView1);
        textView2=(TextView)findViewById(R.id.textView2);

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



}
