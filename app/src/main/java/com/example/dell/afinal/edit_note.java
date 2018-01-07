package com.example.dell.afinal;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;


public class edit_note extends AppCompatActivity {
    private Button time_btn, date_btn, back_btn, save_btn;
    private ImageView photo_iv;
    private EditText title_et, content_et;

    public final static int SELECT_IMAGE_REQUEST_CODE = 100;
    public final static int REQUEST_CODE_CAMERA = 200;

    private Uri uri;

    private String photo;
    private String title;
    private String content;
    private String emotion;
    private String time;

    pruvate


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        init();
        addListener();
    }

    private void init() {
        photo_iv = (ImageView) findViewById(R.id.photo);
        time_btn = (Button) findViewById(R.id.time_btn);
        date_btn = (Button) findViewById(R.id.date_btn);
        back_btn = (Button) findViewById(R.id.back_btn);
        save_btn = (Button) findViewById(R.id.save_btn);
        title_et = (EditText) findViewById(R.id.title);
        content_et = (EditText) findViewById(R.id.content);
        content_et.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        content_et.setSingleLine(false);
        content_et.setHorizontallyScrolling(false);
    }

    private void addListener() {
        time_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog time = new TimePickerDialog(edit_note.this, new OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        Toast.makeText(edit_note.this, i+"hour "+i1+"minute", Toast.LENGTH_SHORT).show();
                    }
                },18,25,true);
                time.show();
            }
        });

        date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePicker = new DatePickerDialog(edit_note.this, new OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        Toast.makeText(edit_note.this, year+"year "+(month+1)+"month "+day+"day", Toast.LENGTH_SHORT).show();
                    }
                },2013,7,20);
                datePicker.show();
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        photo_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopWindow();
            }
        });
    }

    private void showPopWindow() {
        View popView = View.inflate(this, R.layout.pop_window, null);
        Button bt_album = (Button) popView.findViewById(R.id.btn_pop_album);
        Button bt_camera = (Button) popView.findViewById(R.id.btn_pop_camera);
        Button bt_cancle = (Button) popView.findViewById(R.id.btn_pop_cancel);
        //获取屏幕宽高
        int weight = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels*1/3;

        final PopupWindow popupWindow = new PopupWindow(popView,weight,height);
        //popupWindow.setAnimationStyle(R.style.a);
        popupWindow.setFocusable(true);
        //点击外部popueWindow消失
        popupWindow.setOutsideTouchable(true);

        bt_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, SELECT_IMAGE_REQUEST_CODE);
                popupWindow.dismiss();

            }
        });
        bt_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //实例化一个intent，并指定action
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = new File(Environment.getExternalStorageDirectory(),
                        Long.toString(System.currentTimeMillis()));
                uri = Uri.fromFile(file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                //启动activity
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
                popupWindow.dismiss();
            }
        });
        bt_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        //popupWindow消失屏幕变为不透明
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });
        //popupWindow出现屏幕变为半透明
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
        popupWindow.showAtLocation(popView, Gravity.BOTTOM,0,50);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SELECT_IMAGE_REQUEST_CODE: {
                uri = data.getData();
                this.photo = uri.toString();
                photo_iv.setImageURI(uri);
                break;
            }
            case REQUEST_CODE_CAMERA: {
                photo_iv.setImageURI(uri);
                break;
            }
        }
    }

    private getEmotion(Uri uri) {
        
    }
}
