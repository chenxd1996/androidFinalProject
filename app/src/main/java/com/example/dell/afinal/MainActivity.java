package com.example.dell.afinal;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button add_btn;
    private Button search_btn;
    private ListView list;
    private EditText search_et;
    // SimpleAdapter mSimpleAdapter;
    private MainAdapter adapter;
    private ArrayList<Diary> diaries;

    private String search_text = "";

    Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        addListener();
        verifyStoragePermissions(this);
    }

    private void init() {
        database = new Database(MainActivity.this);
        adapter = new MainAdapter(MainActivity.this, R.layout.list_item);
        updateDiaries("");

        add_btn= (Button) findViewById(R.id.add_btn);
        search_btn = (Button) findViewById(R.id.search_btn);
        search_et = (EditText) findViewById(R.id.search_et);

        list=(ListView) findViewById(R.id.list);
//        HashMap<String, Object> item = new HashMap<String, Object>();
//        item.put("ItemImage", R.mipmap.search);
//        item.put("ItemTitle", "Hello!");
        list.setAdapter(adapter);
    }

    private void addListener() {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("title", diaries.get(i).getTitle());
                MainActivity.this.startActivity(intent);
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("确认删除？")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                database.deleteDB(diaries.get(position).getTitle());
                                diaries.remove(position);
                                updateDiaries(search_text);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return false;
            }
        });

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, edit_note.class);
                startActivity(intent);
            }
        });



        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDiaries(search_text);
            }
        });

        search_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                search_text = search_et.getText().toString();
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

    @Override
    protected void onResume() {
        super.onResume();
        updateDiaries(search_text);
    }

    private void updateDiaries(String search_text) {
        diaries = database.myfind(search_text);
        adapter.setDiaries(diaries);
        adapter.notifyDataSetChanged();
    }
}
