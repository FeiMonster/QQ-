package com.example.asus.qqcehua;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private ListView menuView;
    private ListView mainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainView = (ListView) findViewById(R.id.main_listview);
        mainView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,Constant.NAMES));
        menuView = (ListView) findViewById(R.id.menu_listview);
        menuView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,Constant.sCheeseStrings){
            @NonNull
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setTextColor(Color.WHITE);
                return textView;
            }
        });

    }
}
