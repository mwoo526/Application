package org.androidtown.application;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.zip.Inflater;
//support.v4 -> 예전 버전까지 지원



public class DetailFragment extends Fragment {

    TextView textView1,textView2,textView3;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup=(ViewGroup) inflater.inflate(R.layout.fragment_detail,container,false);
        // param1 : xml 레이아웃 파일
        // param2 : xml 레이아웃이 설정될 뷰그룹 객체
        textView1=(TextView)viewGroup.findViewById(R.id.textView1);
        textView2=(TextView)viewGroup.findViewById(R.id.textView2);
        Linkify.addLinks(textView1,Linkify.ALL);
        Linkify.addLinks(textView2,Linkify.PHONE_NUMBERS);


        return  viewGroup;
    }
}
