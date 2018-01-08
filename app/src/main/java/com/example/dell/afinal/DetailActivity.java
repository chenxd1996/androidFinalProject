package com.example.dell.afinal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
    private Database database;

    private Button back_btn;
    private TextView title_tv;
    private TextView content_tv;
    private TextView date_tv;
    private TextView emotion_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        init();
        addListener();
    }

    private void init() {
        database = new Database(DetailActivity.this);

        back_btn = findViewById(R.id.back_btn);
        title_tv = (TextView) findViewById(R.id.title);
        content_tv = (TextView) findViewById(R.id.content);
        date_tv = (TextView) findViewById(R.id.date);
        emotion_tv = (TextView) findViewById(R.id.emotion);

        Intent intent = getIntent();
        ArrayList<Diary> result = database.myfind(intent.getStringExtra("title"));
        Diary diary = result.get(0);
        title_tv.setText(diary.getTitle());
        content_tv.setText(diary.getContent());
        date_tv.setText(diary.getDate());
        emotion_tv.setText(diary.getEmotion());
    }

    private void addListener() {
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
