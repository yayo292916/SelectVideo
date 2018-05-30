# SelectVideo
<br>单纯的视频选择库</br>
```java
##导入方法
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
  dependencies {
	        compile 'com.github.yayo292916:SelectVideo:1.0.1'
	}

##使用方法
SelectVideo.Builder builder = new SelectVideo.Builder();
        builder.setContext(context)
                .setRecordTime(30)  //录制最长时间
                .setTag(105)    //返回的 requestCode 值
                .build();
		
		
##构建好以后，一行代码打开	

    builder.openSelectVideo();


##获取选择后的数据
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

我的博客地址：https://blog.csdn.net/yy292916/article/details/80510041
