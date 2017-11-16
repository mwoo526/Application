package org.androidtown.application.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidtown.application.R;
import org.androidtown.application.model.Menu;

import java.util.ArrayList;

/**
 * Created by MinWoo on 2017-11-01.
 */

public class MenuBaseAdapter extends BaseAdapter {

    private ArrayList<Menu> menuList = new ArrayList<Menu>();

    public MenuBaseAdapter(){

    }
    @Override
    public int getCount() {
        return menuList.size();
    }

    @Override
    public Object getItem(int i) {
        return menuList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final int pos =i;
        final Context context = viewGroup.getContext();

        // 'list_item' layout 을 inflate 하여 view 참조 획득
        if(view == null){
            LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item,viewGroup,false);
        }

        TextView textView1 =(TextView)view.findViewById(R.id.textView1);
        TextView textView2 =(TextView)view.findViewById(R.id.textView2);

        Menu menu = menuList.get(i);

        textView1.setText(menu.getMenu());
        textView2.setText(menu.getPrice());

        return view;
    }
    public void addItem(String menu,String price){
        Menu item = new Menu();

        item.setMenu(menu);
        item.setPrice(price);

        menuList.add(item);
    }



}
