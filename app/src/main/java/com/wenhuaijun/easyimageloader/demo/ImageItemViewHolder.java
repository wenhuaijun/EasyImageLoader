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
    private int height;
    private float width;
    private ViewGroup.LayoutParams layoutParams;
    public ImageItemViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_image,parent,false));
        imageView =(ImageView)itemView.findViewById(R.id.itemview_img);
        imageLoader = ImageLoader.build(parent.getContext());
        screenWidth =JUtils.getScreenWidth();
        layoutParams =imageView.getLayoutParams();
    }
    public void setData(NetImage netImage){
        //根据imageView所需实际像素的宽和高去
        imageLoader.bindBitmap(netImage.getThumbUrl(), imageView, (int)width, height);
    }
    public void setLayoutParams(NetImage netImage){
        //图片根据频幕宽度等比缩放
        width =screenWidth;
        height = (int)(netImage.getThumb_height()*(width/netImage.getThumb_width()));
        layoutParams.width =(int)width;
        layoutParams.height =height;
        imageView.setLayoutParams(layoutParams);
    }
}
