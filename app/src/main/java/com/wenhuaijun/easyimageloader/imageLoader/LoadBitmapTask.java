package com.wenhuaijun.easyimageloader.imageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
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
    private String uri;
    private int reqWidth;
    private int reqHeight;
    private Handler mMainHandler;
    private ImageView imageView;
    private EasyImageLoader.BitmapCallback callback;

    //用于Handler处理的构造函数
    public LoadBitmapTask(Context context,Handler handler,ImageView imageview,String uri,int reqWidth,int reqHeight) {
        this.mMainHandler =handler;
        this.uri=uri;
        this.reqHeight =reqHeight;
        this.reqWidth =reqWidth;
        this.imageView =imageview;
        mContext =context.getApplicationContext();
    }
    //用于回调bitmap的重载构造函数
    public LoadBitmapTask(Context context,EasyImageLoader.BitmapCallback callback,String uri,int reqWidth,int reqHeight) {
        this.callback =callback;
        this.uri=uri;
        this.reqHeight =reqHeight;
        this.reqWidth =reqWidth;
        mContext =context.getApplicationContext();
    }

    @Override
    public void run() {
        //从本地或者网络获取bitmap
        final Bitmap bitmap =loadBitmap(uri, reqWidth, reqHeight);
        if(mMainHandler!=null){
            TaskResult loaderResult = new TaskResult(imageView,uri,bitmap);
            mMainHandler.obtainMessage(MESSAGE_POST_RESULT,loaderResult).sendToTarget();
        }
        if(callback !=null){
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    callback.onResponse(bitmap);
                }
            });

        }

    }

    /**
     * 从本地或者网络去获取bitmap
     * @param uri url
     * @param reqWidth 需求宽度
     * @param reqHeight 需求高度
     * @return
     */
    private Bitmap loadBitmap(String uri,int reqWidth,int reqHeight){
        Bitmap bitmap = null;
        try {
            //从本地缓存中获取bitmap
            bitmap = EasyImageLoader.getImageDiskLrucache(mContext).loadBitmapFromDiskCache(uri, reqWidth, reqHeight);
            if(bitmap!=null){
                JUtils.Log("从本地缓存中获取到了bitmap");
                //添加到内存缓存中
                EasyImageLoader.getImageLrucache().addBitmapToMemoryCache(MD5Utils.hashKeyFromUrl(uri), bitmap);
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
        EasyImageLoader.getImageDiskLrucache(mContext).downloadImageToDiskCache(url);
        //从网络保存到本地缓存中后，直接从本地缓存中获取bitmap;
        return EasyImageLoader.getImageDiskLrucache(mContext).loadBitmapFromDiskCache(url, reqWidth, reqHeight);
    }


}
