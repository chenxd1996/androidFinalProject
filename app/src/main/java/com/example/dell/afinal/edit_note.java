package com.example.dell.afinal;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TimePicker;
import android.widget.Toast;

import com.baidu.aip.face.AipFace;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;


public class edit_note extends AppCompatActivity {
    private Button time_btn, date_btn, back_btn, save_btn;
    private ImageView photo_iv;
    private EditText title_et, content_et;
    private ProgressBar progressBar;

    public final static int SELECT_IMAGE_REQUEST_CODE = 100;
    public final static int REQUEST_CODE_CAMERA = 200;
    public final static int REQUEST_END = 300;

    private Uri uri;
    private String camera_photo_path;

    private String photo;
    private String title;
    private String content;
    private String emotion;
    private String date;
    private String time;

    public static final String APP_ID = "10611049";
    public static final String API_KEY = "TwjyorglYg1Yzm6UTCZ1DaHL";
    public static final String SECRET_KEY = "P9A3KH1bV2ZhVNwdwW8TNUHMdyxXTgYV";

    private Database database;

    public static Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        init();
        addListener();
    }

    private void init() {
        database = new Database(this);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
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

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case REQUEST_END:
                        progressBar.setVisibility(View.GONE);
                        break;
                }
            }
        };
    }

    private void addListener() {
        time_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog time = new TimePickerDialog(edit_note.this, new OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        edit_note.this.time = hour + "时" + minute + "分";
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
                        edit_note.this.date = year + "年" + month + "月" + day + "日";
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

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_note.this.title = title_et.getText().toString();
                edit_note.this.content = content_et.getText().toString();
                database.myinsert(edit_note.this.title, edit_note.this.content,
                        edit_note.this.photo, edit_note.this.emotion, edit_note.this.date + edit_note.this.time);
                finish();
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

                camera_photo_path = file.getPath();
                uri = FileProvider.getUriForFile(edit_note.this, getPackageName() + ".fileprovider",
                        file);
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
                if (data != null) {
                    uri = data.getData();
                    this.photo = uri.toString();
                    photo_iv.setImageURI(uri);
                    getEmotion(getRealPathFromURI(uri));
                    progressBar.setVisibility(View.VISIBLE);
                }
                break;
            }
            case REQUEST_CODE_CAMERA: {
                photo_iv.setImageURI(uri);
                getEmotion(camera_photo_path);
                this.photo = uri.toString();
                progressBar.setVisibility(View.VISIBLE);
                break;
            }
        }
    }

    private void getEmotion(final String path) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                HashMap<String, String> options = new HashMap<String, String>();
                options.put("face_fields", "beauty,expression");
                // 初始化一个AipFace
                AipFace client = new AipFace(APP_ID, API_KEY, SECRET_KEY);
                // 可选：设置网络连接参数
                client.setConnectionTimeoutInMillis(2000);
                client.setSocketTimeoutInMillis(60000);
                // 调用接口
                JSONObject res = client.detect(path, options);
                Log.e("res", res.toString());
                Message.obtain(handler, REQUEST_END).sendToTarget();
                JSONArray result;
                int emotion = -1;
                try {
                    result = res.getJSONArray("result");
                    emotion = result.getJSONObject(0).getInt("expression");
                    Log.e("emotion", emotion + "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                switch (emotion) {
                    case -1:
                        edit_note.this.emotion = "未知";
                        break;
                    case 0:
                        edit_note.this.emotion = "心情一般";
                        break;
                    case 1:
                        edit_note.this.emotion = "开心";
                        break;
                    case 2:
                        edit_note.this.emotion = "很开心";
                        break;
                }
            }
        };
        thread.start();
    }

    private String getRealPathFromURI(Uri contentUri)
    {
        String[] proj = { MediaStore.Audio.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        try {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return "";
    }
}
