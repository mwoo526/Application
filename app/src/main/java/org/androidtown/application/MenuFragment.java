package org.androidtown.application;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.androidtown.application.adapter.MenuBaseAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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

        adapter.addItem(((DetailActivity)getActivity()).getStoremenu1(),((DetailActivity)getActivity()).getStoreprice1());
        adapter.addItem(((DetailActivity)getActivity()).getStoremenu2(),((DetailActivity)getActivity()).getStoreprice2());
        adapter.addItem(((DetailActivity)getActivity()).getStoremenu3(),((DetailActivity)getActivity()).getStoreprice3());

    }

}
