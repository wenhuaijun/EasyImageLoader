package com.wenhuaijun.easyimageloader.imageLoader;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

/**图片内存缓存类
 * Created by wenhuaijun on 2016/4/23 0023.
 */
public class ImageLrucache extends LruCache<String,Bitmap>{
    public static final String TAG ="TAG";
    //获取分配给该应用的最大内存
    public static int maxMemory =(int) (Runtime.getRuntime().maxMemory() / 1024);
    //lruChache能获取的缓存大小为整个应用内存的八分之一
    public  static  int cacheSize =maxMemory/8;

    public ImageLrucache() {
        super(cacheSize);
    }
    @Override
    protected int sizeOf(String key, Bitmap value) {
        //return value.getRowBytes()*value.getHeight()/
        return value.getByteCount() / 1024;
    }

    //添加bitmap到内存缓存中
    public void addBitmapToMemoryCache(String key,Bitmap bitmap){

        if(getBitmapFromMemCache(key)==null){
            this.put(key, bitmap);
        }
    }

    //从内存缓存中获取bitmap
    private Bitmap getBitmapFromMemCache(String key){
        return this.get(key);
    }
    //通过url获取内存缓存
    public Bitmap loadBitmapFromMemCache(String url){
        final String key =MD5Utils.hashKeyFromUrl(url);
        Bitmap bitmap =getBitmapFromMemCache(key);
        return bitmap;
    }
}
