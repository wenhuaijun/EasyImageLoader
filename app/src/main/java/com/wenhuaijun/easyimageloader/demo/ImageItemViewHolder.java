package com.wenhuaijun.easyimageloader.demo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wenhuaijun.easyimageloader.R;
import com.wenhuaijun.easyimageloader.imageLoader.ImageLoader;


/**
 * Created by Administrator on 2016/4/24 0024.
 */
public class ImageItemViewHolder extends RecyclerView.ViewHolder{
    public boolean isLoadding =true;
    public boolean isInit =true;
    public ImageView imageView;
    private ImageLoader imageLoader;
    public ImageItemViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_image,parent,false));
        imageView =(ImageView)itemView.findViewById(R.id.itemview_img);
        imageLoader = ImageLoader.build(parent.getContext());
    }
    public void setData(NetImage netImage){
        imageLoader.bindBitmap(netImage.getThumbUrl(),imageView);
    }


}
