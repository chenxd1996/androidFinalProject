package com.example.dell.afinal;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by roger on 2018/1/7.
 */

public class MainAdapter extends BaseAdapter {
    private int resourceId;
    private ArrayList<Diary> diaries;
    private Context context;
    private Database database;

    public MainAdapter(Context context, int textViewResourceId) {
        database = new Database(context);
        resourceId = textViewResourceId;
        this.context = context;
    }

    @Override
    public int getCount() {
        return diaries.size();
    }

    @Override
    public Object getItem(int index) {
        return diaries.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Diary contact = diaries.get(position);
        View view;
        if (convertView == null) {
            view= LayoutInflater.from(context).inflate(resourceId, null);
        } else {
            view = convertView;
        }
        TextView name = (TextView) view.findViewById(R.id.list_title);
        name.setText(contact.getTitle());
        ImageView img = (ImageView) view.findViewById(R.id.list_image);
        if (contact.getEmotion() != null) {
            switch (contact.getEmotion()) {
                case "心情一般":
                    img.setImageResource(R.mipmap.unhappy);
                    break;
                case "开心":
                    img.setImageResource(R.mipmap.smile);
                    break;
                case "很开心":
                    img.setImageResource(R.mipmap.laugh);
                    break;
            }
        } else {
            img.setImageURI(null);
        }
        return view;
    }

    public void setDiaries(ArrayList<Diary> diaries) {
        this.diaries = diaries;
    }
}
