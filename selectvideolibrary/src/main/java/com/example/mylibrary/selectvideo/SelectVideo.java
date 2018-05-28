package com.example.mylibrary.selectvideo;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Administrator on 2018/5/28.
 */

public class SelectVideo {
    public static void openSelectVideo(Context context){
        context.startActivity(new Intent(context, SelectVideoActivity.class));
    }
}
