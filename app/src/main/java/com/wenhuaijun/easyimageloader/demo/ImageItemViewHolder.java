package com.wenhuaijun.easyimageloader.demo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wenhuaijun.easyimageloader.R;
import com.wenhuaijun.easyimageloader.imageLoader.EasyImageLoader;
import com.wenhuaijun.easyimageloader.imageLoader.JUtils;


/**
 * Created by wenhuaijun on 2016/4/24 0024.
 */
public class ImageItemViewHolder extends RecyclerView.ViewHolder{
    public ImageView imageView;
    private float screenWidth;
    private int height;
    private float width;
    private ViewGroup.LayoutParams layoutParams;
    private Context context;
    public ImageItemViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_image,parent,false));
        imageView =(ImageView)itemView.findViewById(R.id.itemview_img);
        context =parent.getContext();
        screenWidth =JUtils.getScreenWidth();
        layoutParams =imageView.getLayoutParams();
    }
    //绑定数据
    public void setData(NetImage netImage,int layoutType){
        //垂直线性布局
        if(layoutType ==Constants.LinearLayoutStyle){
            //加载高清图片并按宽高压缩
            EasyImageLoader.getInstance(context)
                    .bindBitmap(netImage.getPic_url_noredirect(), imageView, (int) width, height);
        }//错位式布局模式
        else if(layoutType ==Constants.StagedGridLayoutStyle){
            //加载小图片
            EasyImageLoader.getInstance(context).bindBitmap(netImage.getThumbUrl(), imageView, (int) width, height);
        }

    }
    //这个会先执行
    public void setLayoutParams(NetImage netImage,int layoutType){
        //垂直线性布局
        if(layoutType == 1){
            width =screenWidth;
        }//错位式布局
        else if(layoutType == 2){
            width =screenWidth/2;
        }
        //图片根据频幕宽度等比缩放
        height = (int)(netImage.getThumb_height()*(width/netImage.getThumb_width()));
        layoutParams.width =(int)width;
        layoutParams.height =height;
        imageView.setLayoutParams(layoutParams);
    }
}
