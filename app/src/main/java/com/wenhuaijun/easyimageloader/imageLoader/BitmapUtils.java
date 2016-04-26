package com.wenhuaijun.easyimageloader.imageLoader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Administrator on 2016/4/24 0024.
 */
public class BitmapUtils {
    //计算图片的缩放值
    public static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height/ (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    // 根据路径获得图片并压缩，返回bitmap用于显示
    public static Bitmap getSmallBitmap(String filePath,int reqWidth,int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }
    //根据输入流获取图片并压缩，返回bitmap用于显示
    public static Bitmap getSmallBitmap(InputStream inputStream,int reqWidth,int reqHeight){
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds =true;
        BitmapFactory.decodeStream(inputStream,null,options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(inputStream, null, options);
    }

    //把bitmap转换成String
    public static String bitmapToString(String filePath,int reqWidth,int reqHeight) {

        Bitmap bm = getSmallBitmap(filePath, reqWidth, reqHeight);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }
    //压缩图片质量，一般用于保存图片到本地或者上传图片到服务器时对图片质量进行压缩
    public static void saveBitmapToOutputStrea(OutputStream outputStream,Bitmap bitmap,int rate){
        //使用此流读取
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //第二个参数影响的是图片的质量，但是图片的宽度与高度是不会受影响滴
        bitmap.compress(Bitmap.CompressFormat.JPEG,rate,baos);
        //这个函数能够设定图片的宽度与高度
        //Bitmap map = Bitmap.createScaledBitmap(bitmap, 400, 400, true);
        //把数据转为为字节数组
        byte[] byteArray = baos.toByteArray();
        try {
            outputStream.write(byteArray);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(outputStream!=null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }
}
