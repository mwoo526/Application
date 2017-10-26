package org.androidtown.application;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity implements Animation.AnimationListener {

    TextView textView;
    ImageView imageView1,imageView2;
    Animation anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // 액션바 숨기기
        ActionBar actionBar= getSupportActionBar();
        actionBar.hide();

        textView=(TextView)findViewById(R.id.textView);
        imageView1=(ImageView)findViewById(R.id.imageView1);
        imageView2=(ImageView)findViewById(R.id.imageView2);
        AnimationAlpha();
        AnimationTranslate();
        anim.setAnimationListener(this);
    }

    // 투명도 설정
    void AnimationAlpha(){
         anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.alpha);
        textView.startAnimation(anim);
        imageView1.startAnimation(anim);
    }
    // 위치 설정
    void AnimationTranslate(){
        anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.translate);
        imageView2.startAnimation(anim);

    }

    //AnimationListener 구현 메소드
    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        Intent intent= new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
