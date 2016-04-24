package com.wenhuaijun.easyimageloader.imageLoader;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/24 0024.
 */
public class NetRequest {

    public static void getRequest(final Activity activity, final String httpUrl, final CallBack callBack){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection httpUrlConnection=null;
                try {
                    URL url = new URL(httpUrl);
                    httpUrlConnection =(HttpURLConnection)url.openConnection();
                    httpUrlConnection.setRequestMethod("GET");
                    //设置是否允许输入，默认都是true
                    // httpUrlConnection.setDoInput(true);
                    //设置是否允许输出，默认是false,post的时候需要改为true
                    //httpUrlConnection.setDoOutput(true);
                    InputStream inputStream =httpUrlConnection.getInputStream();
                    final String response =getStringFromInputStream(inputStream);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onSuccess(response);
                        }
                    });
                } catch (final MalformedURLException e) {
                    e.printStackTrace();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onError(e, "error");
                        }
                    });

                } catch (final IOException e) {
                    e.printStackTrace();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onError(e, "error");
                        }
                    });

                }
                finally {
                    if(httpUrlConnection!=null)
                        httpUrlConnection.disconnect();
                }
            }
        }).start();

    }
    public static <T> void getRequest(final Activity activity, final String httpUrl, final Class<T> type,final BeanCallback<T> callBack){
        getRequest(activity, httpUrl, new CallBack() {
            @Override
            public void onSuccess(String response) {
                Log.i("TAG",response);
                callBack.onSuccess(new Gson().fromJson(response,type));
            }

            @Override
            public void onError(Exception exception, String errorInfo) {
                callBack.onError(exception,errorInfo);
            }
        });
    }
    public static <T> void postRequest(final Activity activity, final String httpUrl, final HashMap<String, String> params, final Class<T> type,final BeanCallback<T> callBack){
        postRequest(activity, httpUrl, params, new CallBack() {
            @Override
            public void onSuccess(String response) {
                callBack.onSuccess(new Gson().fromJson(response,type));
            }

            @Override
            public void onError(Exception exception, String errorInfo) {
                callBack.onError(exception,errorInfo);
            }
        });

    }
    public  static void postRequest(final Activity activity, final String httpUrl, final HashMap<String, String> params, final CallBack callBack){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection httpUrlConnection = null;
                PrintWriter printWriter;
                String stringParams=praseParams(params);

                try {
                    URL url = new URL(httpUrl);
                    httpUrlConnection = (HttpURLConnection)url.openConnection();
                    httpUrlConnection.setRequestMethod("POST");
                    httpUrlConnection.setDoOutput(true);
                    //获取UrlConnection对象对应的输入流
                    printWriter = new PrintWriter(httpUrlConnection.getOutputStream());
                    //发送请求参数到请求正文
                    printWriter.write(stringParams);
                    printWriter.flush();
                    //根据responseCode判断连接是否成功
                    if(httpUrlConnection.getResponseCode()!=200){
                        Log.i("response", "postRequest onError");
                    }else{
                        Log.i("response","postRequest success");
                    }
                    final String response=getStringFromInputStream(httpUrlConnection.getInputStream());
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onSuccess(response);
                        }
                    });



                } catch (final MalformedURLException e) {
                    e.printStackTrace();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onError(e, "error");
                        }
                    });

                } catch (final IOException e) {
                    e.printStackTrace();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onError(e, "error");
                        }
                    });

                }
                finally {
                    httpUrlConnection.disconnect();
                }
            }
        }).start();

    }

    //将输入字节流转化为String
    public static String getStringFromInputStream(InputStream inputStream){
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuffer buffer = new StringBuffer();
        String temp;
        try {
            while ((temp =reader.readLine())!=null){
                buffer.append(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {

            try {
                if(reader!=null)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return  buffer.toString();
    }
    public static String praseParams(Map<String,String> params){
        StringBuffer postParams=new StringBuffer();
        //组织请求参数
        Iterator iterable = params.entrySet().iterator();
        while(iterable.hasNext()){
            Map.Entry entry =(Map.Entry)iterable.next();
            postParams.append(entry.getKey());
            postParams.append("=");
            postParams.append(entry.getValue());
            postParams.append("&");

        }
        if(postParams.length()>0){
            postParams.deleteCharAt(postParams.length()-1);
            Log.i("response", postParams.toString());
        }
        return  postParams.toString();
    }
    public interface CallBack{
        public void onSuccess(String response);
        public void onError(Exception exception,String errorInfo);
    }
    public interface BeanCallback<T>{
        public void onSuccess(T response);
        public void onError(Exception exception,String errorInfo);
    }


    //通过url下载图片，未缩放图片宽高
    public static Bitmap downloadBitmapFromUrl(String  urlString){
        Bitmap bitmap =null;
        HttpURLConnection urlConnection =null;
        BufferedInputStream in =null;

        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(),8*1024);
            bitmap = BitmapFactory.decodeStream(in);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(in!=null){
                    in.close();
                }
                if(urlConnection!=null){
                    urlConnection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }
    //通过url下载图片获取字节流写入输出流中
    public static boolean downloadUrlToStream(String imageUrl,OutputStream outputStream){

        HttpURLConnection urlConnection =null;
        BufferedInputStream in =null;
        BufferedOutputStream out =null;
        try {
            final URL url = new URL(imageUrl);
            urlConnection =(HttpURLConnection) url.openConnection();
            in =new BufferedInputStream(urlConnection.getInputStream(),8*1024);
            out =new BufferedOutputStream(outputStream,8*1024);
            int b;
            while ((b =in.read()) !=-1){
                out.write(b);
            }
            //写入成功返回true
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(urlConnection!=null){
                urlConnection.disconnect();
            }
            try {
                if(in!=null){
                    in.close();
                }
                if(out!=null){
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return false;
    }
}
