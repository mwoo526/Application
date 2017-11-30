package org.androidtown.application;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Calendar;

public class ReserveActivity extends AppCompatActivity {

    TextView textView1,textView2,textView3,textView4,textView5,textView6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve);
        setTitle("맛집 예약");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        textView1=(TextView)findViewById(R.id.textView1);
        textView2=(TextView)findViewById(R.id.textView2);
        textView3=(TextView)findViewById(R.id.textView3);
        textView4=(TextView)findViewById(R.id.textView4);
        textView5=(TextView)findViewById(R.id.textView5);
        textView6=(TextView)findViewById(R.id.textView6);

        Intent intent = getIntent();
        if (intent != null) {
            processIntent(intent);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * 수신자로부터 전달받은 Intent 처리
     *
     * @param intent
     */

    @Override
    protected void onNewIntent(Intent intent) {

        if (intent != null) {
            processIntent(intent);
        }
        super.onNewIntent(intent);

    }
    private void processIntent(Intent intent) {
        Calendar c = Calendar.getInstance();
        int mm = c.get(Calendar.MONTH)+1;
        int dd = c.get(Calendar.DAY_OF_MONTH);

        String store = intent.getStringExtra("store");
       // String date = intent.getStringExtra("date");
        String hour = intent.getStringExtra("hour");
        String minute = intent.getStringExtra("minute");
        String memo = "예약을 하셨습니다.";


        if(store!=null&&hour!=null&&minute!=null) {
            textView1.setText(store);
            textView2.setText(mm + "월");
            textView3.setText(dd + "일");
            textView4.setText(hour + "시");
            textView5.setText(minute + "분");
            textView6.setText(memo);

        }
    }



}
