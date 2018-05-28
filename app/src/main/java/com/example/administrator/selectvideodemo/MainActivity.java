package com.example.administrator.selectvideodemo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mylibrary.selectvideo.SelectVideo;

public class MainActivity extends AppCompatActivity {
    private Button bt;
    private ImageView img;
    private SelectVideo.Builder builder;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=this;
        bt = (Button) findViewById(R.id.bt);
        img = (ImageView) findViewById(R.id.img);
        initView();
        initListener();
    }

    private void initView() {
        builder = new SelectVideo.Builder();
        builder.setContext(context)
                .setTitleColor(0xffe91d01)
                .setRecordTime(30)
                .setTag(105)
                .build();
    }

    private void initListener() {
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.openSelectVideo();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null)
            return;
        if (requestCode == builder.getTag()) {
           String videoPath = data.getStringExtra("videoPath");
            Toast.makeText(context,videoPath,Toast.LENGTH_SHORT).show();
            Glide.with(context).load(videoPath).into(img);
        }
    }
}
