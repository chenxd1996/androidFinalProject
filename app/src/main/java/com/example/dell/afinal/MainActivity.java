package com.example.dell.afinal;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    Button add_btn;
    Button search_btn;
    ListView list;
    SimpleAdapter mSimpleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        addListener();
        verifyStoragePermissions(this);
    }

    private void init() {
        add_btn= (Button) findViewById(R.id.add_btn);
        search_btn = (Button) findViewById(R.id.search_btn);
        list=(ListView) findViewById(R.id.list);
        ArrayList<HashMap<String, Object>> listItem=new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> temp=new HashMap<String, Object>();
        temp.put("ItemImage", R.mipmap.search);
        temp.put("ItemTitle", "Hello!");
        listItem.add(temp);
        mSimpleAdapter = new SimpleAdapter(this, listItem,
                R.layout.list_item,
                new String[]{"ItemImage", "ItemTitle"},
                new int[]{R.id.list_image, R.id.list_title});
        list.setAdapter(mSimpleAdapter);
    }

    private void addListener() {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, edit_note.class);
                MainActivity.this.startActivity(intent);
            }
        });

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, edit_note.class);
                startActivity(intent);
            }
        });
    }

    private void verifyStoragePermissions(Activity activity) {
        try {
            int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            }
            permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }
            permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, 0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
