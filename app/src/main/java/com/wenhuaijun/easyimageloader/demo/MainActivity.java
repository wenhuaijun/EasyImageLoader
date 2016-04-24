package com.wenhuaijun.easyimageloader.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.wenhuaijun.easyimageloader.R;
import com.wenhuaijun.easyimageloader.imageLoader.NetRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ImageRecyclerAdapter recyclerAdapter;
    private String searchWord="萌妹子";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView =(RecyclerView)findViewById(R.id.recyclerView);
       // recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch(newState){
                    //停止滑动时
                    case RecyclerView.SCROLL_STATE_IDLE:
                        recyclerAdapter.setIsScrolling(false);
                        recyclerAdapter.notifyDataSetChanged();
                        break;
                    default:
                        recyclerAdapter.setIsScrolling(true);
                        break;
                }
            }
        });
        try {
            searchWord = URLEncoder.encode(searchWord, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            searchWord ="girl";
        }
        //网络请求获取数据
        NetRequest.getRequest(this, Constants.searchPictureUrl+searchWord, NetImageResult.class, new NetRequest.BeanCallback<NetImageResult>() {
            @Override
            public void onSuccess(NetImageResult response) {
//                Log.i("TAG","size: +"+response.getItems().length);
                recyclerAdapter = new ImageRecyclerAdapter(response.getItems());
                recyclerView.setAdapter(recyclerAdapter);

            }

            @Override
            public void onError(Exception exception, String errorInfo) {
                Toast.makeText(MainActivity.this,"net error",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
