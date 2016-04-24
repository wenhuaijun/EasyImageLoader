package com.wenhuaijun.easyimageloader.demo;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.wenhuaijun.easyimageloader.R;

/**
 * Created by Administrator on 2016/4/24 0024.
 */
public class ImageRecyclerAdapter extends RecyclerView.Adapter<ImageItemViewHolder>{
    private NetImage[] data;
    private boolean isScrolling =false;
    public ImageRecyclerAdapter(NetImage[] data) {
        this.data = data;
    }

    @Override
    public ImageItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ImageItemViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(ImageItemViewHolder holder, int position) {
        holder.imageView.setImageResource(R.drawable.ic_loading);
        if(!isScrolling){
            holder.setData(data[position]);
        }
    }

    @Override
    public int getItemCount() {
        if(data==null){
            return 0;
        }
        return data.length;
    }

    public NetImage[] getData() {
        return data;
    }

    public void setData(NetImage[] data) {
        this.data = data;
    }

    public boolean isScrolling() {
        return isScrolling;
    }

    public void setIsScrolling(boolean isScrolling) {
        this.isScrolling = isScrolling;
    }
}
