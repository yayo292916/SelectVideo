package com.example.mylibrary.selectvideo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Administrator on 2018/5/28.
 */

public class SelectVideo {
    private Context context;
    private int titleColor;
    private int tag;
    private int recordTime;
    private Activity activity;

    public SelectVideo(Context context, int titleColor, int tag, int recordTime) {
        this.context = context;
        this.titleColor = titleColor;
        this.tag = tag;
        this.recordTime = recordTime;
        activity= (Activity) context;
    }

    private void openVideo() {
        Intent intent = new Intent(activity, SelectVideoActivity.class);
        intent.putExtra("titleColor", titleColor);
        intent.putExtra("tag", tag);
        intent.putExtra("recordTime", recordTime);
        activity.startActivityForResult(intent,tag);

    }

    public static final class Builder {
        private Context context;
        private int titleColor;
        private SelectVideo selectVideo;
        private int tag=1000;
        private int recordTime=60;

        public Builder setRecordTime(int recordTime) {
            this.recordTime = recordTime;
            return this;
        }

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder setTitleColor(int titleColor) {
            this.titleColor = titleColor;
            return this;
        }

        public Builder setTag(int tag) {
            this.tag = tag;
            return this;
        }

        public SelectVideo build() {
            selectVideo = new SelectVideo(context, titleColor,tag,recordTime);
            return selectVideo;
        }

        public void openSelectVideo() {
            if (context!=null)
            selectVideo.openVideo();
        }
        public int getTag(){
            return tag;
        }
    }
}
