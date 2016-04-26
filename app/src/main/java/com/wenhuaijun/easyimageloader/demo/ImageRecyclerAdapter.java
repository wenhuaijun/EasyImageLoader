package com.wenhuaijun.easyimageloader.demo;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
        Log.i("heheda","onCreateViewHolder");
        return new ImageItemViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(ImageItemViewHolder holder, int position) {
        //第一次onBindViewHolder的时候，正常执行所有步骤，不管滑动
        Log.i("heheda", "onBindViewHolder----" + position);
     /*   holder.imageView.setImageResource(R.drawable.ic_loading);
        holder.setData(data[position]);*/
        holder.setLayoutParams(data[position]);
        if (isScrolling) {
            Log.i("heheda", "isScrolling");
            holder.imageView.setImageResource(R.drawable.ic_loading);
        }else{
            Log.i("heheda", "notScrolling");
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
