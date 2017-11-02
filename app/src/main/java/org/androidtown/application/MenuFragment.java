package org.androidtown.application;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidtown.application.adapter.MenuBaseAdapter;
//support.v4 -> 예전 버전까지 지원

// ListFragment 클래스 상속
// Fragment와 ListView 가 제공하는 기능을 한번에 구현 , 구현과정을 간소화

public class MenuFragment extends ListFragment {

     MenuBaseAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        adapter=new MenuBaseAdapter();
        setListAdapter(adapter);
        // ListFragement에 Adapter 을 지정하는 setListAdapter() 함수
        addItem();
        return super.onCreateView(inflater,container,savedInstanceState);
    }

    public void addItem(){
        adapter.addItem("메뉴1","가격1");
        adapter.addItem("메뉴2","가격2");
        adapter.addItem("메뉴3","가격3");
        adapter.addItem("메뉴4","가격4");
        adapter.addItem("메뉴5","가격5");
    }

}