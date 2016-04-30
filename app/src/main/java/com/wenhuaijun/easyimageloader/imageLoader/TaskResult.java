package com.wenhuaijun.easyimageloader.imageLoader;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * 启动的LoadBitmapTask后封装的结果。用Message传输
 * Created by wenhuaijun on 2016/4/24 0024.
 */
public class TaskResult {
        public ImageView imageView;
        public  String uri;
        public Bitmap bitmap;

        public TaskResult(ImageView imageView, String uri ,Bitmap bitmap){
            this.imageView =imageView;
            this.uri = uri;
            this.bitmap =bitmap;

        }
}
