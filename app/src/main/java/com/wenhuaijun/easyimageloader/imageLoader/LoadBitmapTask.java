package com.wenhuaijun.easyimageloader.imageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;

/**
 * Created by wenhuaijun on 2016/4/24 0024.
 * 从网络和本地去加载bitmap的Runnable
 */
public class LoadBitmapTask implements Runnable{
    public static final String TAG ="TAG";
    public static final int MESSAGE_POST_RESULT = 1;
    private boolean mIsDiskLruCacheCreated = false;
    private Context mContext;
   // private ImageLrucache imageLrucache;
    private ImageDiskLrucache imageDiskLrucache;
    private String uri;
    private int reqWidth;
    private int reqHeight;
    private Handler mMainHandler;
    private ImageView imageView;

    public LoadBitmapTask(Context context,Handler handler,ImageView imageview,String uri,int reqWidth,int reqHeight) {
        this.mMainHandler =handler;
        this.uri=uri;
        this.reqHeight =reqHeight;
        this.reqWidth =reqWidth;
        this.imageView =imageview;
        mContext =context.getApplicationContext();
     //   imageLrucache = new ImageLrucache();
        imageDiskLrucache =new ImageDiskLrucache(mContext);
    }

    @Override
    public void run() {
        //从本地或者网络获取bitmap
        Bitmap bitmap =loadBitmap(uri, reqWidth, reqHeight);
        if(bitmap!=null){
            TaskResult loaderResult = new TaskResult(imageView,uri,bitmap);
            mMainHandler.obtainMessage(MESSAGE_POST_RESULT,loaderResult).sendToTarget();
        }
    }

    private Bitmap loadBitmap(String uri,int reqWidth,int reqHeight){
        //从内存中获取bitmap缓存
        Bitmap bitmap = ImageLoader.imageLrucache.loadBitmapFromMemCache(uri);
        if(bitmap!=null){
            JUtils.Log("从内存中获取到了bitmap_2");
            return bitmap;
        }
        try {
            //从本地缓存中获取bitmap
            bitmap = imageDiskLrucache.loadBitmapFromDiskCache(uri,reqWidth,reqHeight);
            if(bitmap!=null){
                JUtils.Log("从本地缓存中获取到了bitmap");
                //添加到内存缓存中
                ImageLoader.imageLrucache.addBitmapToMemoryCache(MD5Utils.hashKeyFromUrl(uri), bitmap);
                return  bitmap;
            }else{
                //从网络下载bitmap到本地缓存，并从本地缓存中获取bitmap
                bitmap =loadBitmapFromHttp(uri,reqWidth,reqHeight);
                if(bitmap!=null){
                    JUtils.Log("从网络下载并保存到本地并从中读取bitmap成功");
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        if(bitmap==null&&!mIsDiskLruCacheCreated){
            //如果sd卡已满，无法使用本地缓存，则通过网络下载bitmap，一般不会调用这一步
            bitmap =NetRequest.downloadBitmapFromUrl(uri);
            Log.i(TAG, "sd卡满了，直接从网络获取");
        }
        return bitmap;
    }

    //从网络获取bitmap，并放入本地缓存中
    private Bitmap loadBitmapFromHttp(String url,int reqWidth,int reqHeight) throws IOException {
        //通过url从网络获取图片的字节流保存到本地缓存
        imageDiskLrucache.addBitmapToDiskCache(url,reqWidth,reqHeight);
        //从网络保存到本地缓存中后，直接从本地缓存中获取bitmap;
        return imageDiskLrucache.loadBitmapFromDiskCache(url,reqWidth,reqHeight);
    }


}
