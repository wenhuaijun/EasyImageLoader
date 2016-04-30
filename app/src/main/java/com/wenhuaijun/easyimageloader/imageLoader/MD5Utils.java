package com.wenhuaijun.easyimageloader.imageLoader;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密文件获取到唯一的String，作为该文件的标识符，一般用于给文件命名
 * Created by wenhuaijun on 2016/4/23 0023.
 */
public class MD5Utils {
    //将url通过MD5转化为唯一的字符串，用于标识
    public static String hashKeyFromUrl(String url){
        String  cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(url.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());

        } catch (NoSuchAlgorithmException e) {
            cacheKey =String.valueOf(url.hashCode());
        }
        return cacheKey;

    }
    private static String bytesToHexString(byte[] bytes){
        StringBuilder sb = new StringBuilder();
        for(int i =0;i<bytes.length;i++){
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if(hex.length() == 1){
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
