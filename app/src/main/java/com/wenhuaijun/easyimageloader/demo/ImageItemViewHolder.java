package com.wenhuaijun.easyimageloader.demo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wenhuaijun.easyimageloader.R;
import com.wenhuaijun.easyimageloader.imageLoader.ImageLoader;
import com.wenhuaijun.easyimageloader.imageLoader.JUtils;


/**
 * Created by Administrator on 2016/4/24 0024.
 */
public class ImageItemViewHolder extends RecyclerView.ViewHolder{
    public boolean isLoadding =true;
    public boolean isInit =true;
    public ImageView imageView;
    private ImageLoader imageLoader;
    private float screenWidth;
    public ImageItemViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_image,parent,false));
        imageView =(ImageView)itemView.findViewById(R.id.itemview_img);
        imageLoader = ImageLoader.build(parent.getContext());
        screenWidth =JUtils.getScreenWidth();
    }
    public void setData(NetImage netImage){
        //设置宽为120px像素，高为134像素去压缩图片分辨率
        imageLoader.bindBitmap(netImage.getPic_url_noredirect(), imageView, 120, 134);
    }
    public void setLayoutParams(NetImage netImage){
        //图片根据频幕宽度等比缩放
        int height = (int)(netImage.getThumb_height()*(screenWidth/netImage.getThumb_width()));
        imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,height));
    }
}
