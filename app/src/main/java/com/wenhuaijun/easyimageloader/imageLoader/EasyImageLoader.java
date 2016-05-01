package com.wenhuaijun.easyimageloader.imageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import com.wenhuaijun.easyimageloader.R;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 供外层使用的图片加载类，通过它实现图片加载
 * Created by Wenhuaijun on 2016/4/22 0022.
 */
public class EasyImageLoader {
    private static EasyImageLoader instance=null;
    private Context mContext;
    private static ImageLrucache imageLrucache;
    private static ImageDiskLrucache imageDiskLrucache;
    //创建一个静态的线程池对象
    private static ThreadPoolExecutor THREAD_POOL_EXECUTOR = null;
    //创建一个更新ImageView的UI的Handler
    private static TaskHandler mMainHandler;
    public static EasyImageLoader getInstance(Context context){
        if(instance==null){
            synchronized (EasyImageLoader.class){
                if(instance == null){
                    instance = new EasyImageLoader(context);
                }
            }
        }
        return instance;
    }
    //私有的构造方法，防止在外部实例化该ImageLoader
    private EasyImageLoader(Context context){
        mContext =context.getApplicationContext();
        THREAD_POOL_EXECUTOR = ImageThreadPoolExecutor.getInstance();
        imageLrucache = new ImageLrucache();
        imageDiskLrucache = new ImageDiskLrucache(context);
        mMainHandler = new TaskHandler();
    }

    public void bindBitmap(final String url, final ImageView imageView){
        bindBitmap(url, imageView, 0, 0);
    }

    public void bindBitmap(final String uri,final ImageView imageView,final int reqWidth,final int reqHeight){
        //设置加载loadding图片
        imageView.setImageResource(R.drawable.ic_loading);
        //防止加载图片的时候数据错乱
       // imageView.setTag(TAG_KEY_URI, uri);
        imageView.setTag(uri);
        //从内存缓存中获取bitmap
        Bitmap bitmap = imageLrucache.loadBitmapFromMemCache(uri);
        if(bitmap!=null){
            JUtils.Log("从内存中获取到了bitmap_1");
            imageView.setImageBitmap(bitmap);
            return;
        }
        LoadBitmapTask loadBitmapTask =new LoadBitmapTask(mContext,mMainHandler,imageView,uri,reqWidth,reqHeight);
       //使用线程池去执行Runnable对象
        THREAD_POOL_EXECUTOR.execute(loadBitmapTask);

    }
    //返回内存缓存类
    public static ImageLrucache getImageLrucache(){
        if(imageLrucache==null){
            imageLrucache = new ImageLrucache();
        }
        return imageLrucache;
    }
    //返回本地缓存类
    public static ImageDiskLrucache getImageDiskLrucache(Context context){
        if(imageDiskLrucache==null){
            imageDiskLrucache = new ImageDiskLrucache(context);
        }
        return imageDiskLrucache;
    }
}
