package com.example.administrator.selectvideodemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.mylibrary.selectvideo.SelectVideoActivity;

public class MainActivity extends AppCompatActivity {
    private Button bt;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt= (Button) findViewById(R.id.bt);
        img= (ImageView) findViewById(R.id.img);
        initListener();
    }

    private void initListener() {
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SelectVideoActivity.class));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data==null)
            return;
        if (requestCode==102){

        }
    }
}
