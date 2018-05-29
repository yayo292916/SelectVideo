package com.example.mylibrary.selectvideo;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.mylibrary.R;
import com.example.mylibrary.selectvideo.adapter.SelectVideoActivity_BottomListDialogAdapter;
import com.example.mylibrary.selectvideo.base.DefaultBaseActivity;
import com.example.mylibrary.selectvideo.bean.Video;
import com.example.mylibrary.selectvideo.dialog.BottomListDialog;
import com.example.mylibrary.selectvideo.inteface.AbstructProvider;
import com.example.mylibrary.selectvideo.provider.VideoProvider;
import com.example.mylibrary.selectvideo.utils.AdapterUtils;
import com.example.mylibrary.selectvideo.utils.ScreenUtil;
import com.example.mylibrary.selectvideo.utils.StatusBarHeightUtil;
import com.gyf.barlibrary.ImmersionBar;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class SelectVideoActivity extends DefaultBaseActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private Map<String, List<Video>> AllList;
    private RelativeLayout actionbar;
    private ImageView img_album_arrow, recordVideoImg;
    private TextView select_video;
    protected int width,height;
    private int titleColor,tag,recordTime=60;
    private View barColor;

    @Override
    protected void initialize() {
        ImmersionBar.with(this).init();
        setContentView(R.layout.activity_select_video);
        titleColor=getIntent().getIntExtra("titleColor",0xff00000);
        tag=getIntent().getIntExtra("tag",1001);
        recordTime=getIntent().getIntExtra("recordTime",60);
    }

    @Override
    protected void initView() {
        width = ScreenUtil.getScreenWidth(context);
        height = ScreenUtil.getStatusHeight(context);
        barColor=findViewById(R.id.barColor);
        recordVideoImg = (ImageView) findViewById(R.id.recordVideoImg);
        actionbar = (RelativeLayout) findViewById(R.id.actionbar);
        actionbar.setBackgroundColor(titleColor);
        barColor.setBackgroundColor(titleColor);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.Gray, R.color.Gray, R.color.Gray, R.color.Gray);
        startRefreshing(swipeRefreshLayout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(context, 3));

        findViewById(R.id.title_back).setOnClickListener(this);
        findViewById(R.id.select_video).setOnClickListener(this);

        img_album_arrow = (ImageView) findViewById(R.id.img_album_arrow);
        select_video = (TextView) findViewById(R.id.select_video);
        recordVideoImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                //设置视频录制的最长时间
                intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, recordTime);
                //设置视频录制的画质
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                startActivityForResult(intent, 123);
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        new initVideosThread().start();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.title_back) {
            finish();

        } else if (i == R.id.select_video) {
            if (bottomListDialog != null) {
                bottomListDialog.show();
                img_album_arrow.setSelected(true);
            }

        }
    }

    private BottomListDialog bottomListDialog;
    private Adapter adapter;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case AppConstant.WHAT.SUCCESS:
                    stopRefreshing(swipeRefreshLayout);
                    adapter = new Adapter(R.layout.adapter_select_video_item2, (List<Video>) msg.obj);
                    mRecyclerView.setAdapter(adapter);
                    mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
                        @Override
                        public void SimpleOnItemClick(final BaseQuickAdapter adapter, View view, final int position) {
                            Intent i = new Intent();
                            i.putExtra("videoPath", ((List<Video>) adapter.getData()).get(position).getPath());
                            setResult(tag, i);
                            finish();
                        }
                    });

                    final SelectVideoActivity_BottomListDialogAdapter bottomListDialogAdapter = new SelectVideoActivity_BottomListDialogAdapter(activity, AllList);

                    bottomListDialog = new BottomListDialog.Builder(activity
                            , bottomListDialogAdapter,
                            height - actionbar.getHeight() - StatusBarHeightUtil.getStatusBarHeight(context)
                    ).setOnItemClickListener(new BottomListDialog.OnItemClickListener() {
                        @Override
                        public void onClick(Dialog dialog, int which) {
                            dialog.dismiss();
                            String album = (String) bottomListDialogAdapter.getAllList().keySet().toArray()[which];
                            adapter.setNewData(bottomListDialogAdapter.getAllList().get(album));
                            select_video.setText(album);
                            img_album_arrow.setSelected(false);
                        }
                    }).create();
                    break;

                case AppConstant.WHAT.FAILURE:
                    stopRefreshing(swipeRefreshLayout);
                    break;
            }
        }
    };


    class initVideosThread extends Thread {
        @Override
        public void run() {
            super.run();
            AbstructProvider provider = new VideoProvider(activity);
            List<Video> list = (List<Video>) provider.getList();

            List<Video> templist = new ArrayList<>();
            AllList = new HashMap<>();

            //我需要可以查看所有视频 所以加了这样一个文件夹名称
            AllList.put(" " + getResources().getString(R.string.all_video), list);

            //主要是读取文件夹的名称 做分文件夹的展示

            for (Video video : list) {
                String album = video.getAlbum();
                if (TextUtils.isEmpty(album)) {
                    album = "Camera";
                }

                if (AllList.containsKey(album)) {
                    AllList.get(album).add(video);
                } else {
                    templist = new ArrayList<>();
                    templist.add(video);
                    AllList.put(album, templist);
                }
            }

            //在子线程读取好数据后使用handler 更新
            if (list == null || list.size() == 0) {
                Message message = new Message();
                message.what = AppConstant.WHAT.FAILURE;
                mHandler.sendMessage(message);
            } else {
                Message message = new Message();
                message.what = AppConstant.WHAT.SUCCESS;
                message.obj = list;
                mHandler.sendMessage(message);
            }
        }
    }

    @Override
    public void onRefresh() {
        initData();
    }

    protected void startRefreshing(final SwipeRefreshLayout swipeRefreshLayout) {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
    }

    protected void stopRefreshing(final SwipeRefreshLayout swipeRefreshLayout) {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    class Adapter extends BaseQuickAdapter<Video> {


        public Adapter(int layoutResId, List<Video> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, Video item) {
            String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(item.getDuration()),
                    TimeUnit.MILLISECONDS.toMinutes(item.getDuration()) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(item.getDuration())),
                    TimeUnit.MILLISECONDS.toSeconds(item.getDuration()) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(item.getDuration())));

            helper.setText(R.id.text_duration, hms);
            float itemWidth = ScreenUtil.getScreenWidth(context);
            ImageView simpleDraweeView = AdapterUtils.getAdapterView(helper.getConvertView(), R.id.simpleDraweeView);
            int width = (int) ((itemWidth - 4) / 3);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, width);
            simpleDraweeView.setLayoutParams(layoutParams);

            Glide
                    .with(context)
                    .load(Uri.fromFile(new File(item.getPath())))
                    .asBitmap()
                    .into(simpleDraweeView);
        }
    }

    @Override
    protected void onDestroy() {
        ImmersionBar.with(this).destroy();
        super.onDestroy();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data==null)
            return;
        if (requestCode==123){
            super.initData();
            new initVideosThread().start();
        }
    }
}
