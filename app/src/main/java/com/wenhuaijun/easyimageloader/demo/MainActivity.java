package com.wenhuaijun.easyimageloader.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.wenhuaijun.easyimageloader.R;
import com.wenhuaijun.easyimageloader.imageLoader.JUtils;
import com.wenhuaijun.easyimageloader.imageLoader.NetRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private SearchView searchView;
    private ImageRecyclerAdapter recyclerAdapter;
    private String searchWord="美女";
    private int layoutSysle =Constants.StagedGridLayoutStyle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar =(Toolbar)findViewById(R.id.toolbar);
        searchView =(SearchView)findViewById(R.id.searchView);
        setSupportActionBar(toolbar);
        recyclerView =(RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
       // addScrollListener();
        searchPicture(searchWord);
        initSearchView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_search:
                searchView.setVisibility(View.VISIBLE);
                searchView.onActionViewExpanded();
                searchView.requestFocus();
                break;
            case R.id.action_linearLayout:
                layoutSysle =Constants.LinearLayoutStyle;
                recyclerAdapter.setLayoutManagerType(layoutSysle);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                break;
            case R.id.action_stageGridLayout:
                layoutSysle =Constants.StagedGridLayoutStyle;
                recyclerAdapter.setLayoutManagerType(layoutSysle);
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return  true;
    }

    private void initSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                recyclerAdapter.clear();
                searchPicture(query);
                searchView.setVisibility(View.GONE);
                recyclerView.requestFocus();
                toolbar.setTitle(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    /**
     * 搜索图片请求
     * @param searchWord 搜索关键字
     */
    private void searchPicture(String searchWord) {
        try {
            searchWord = URLEncoder.encode(searchWord, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            searchWord ="girl";
        }
        //GET网络请求获取数据
        NetRequest.getRequest(Constants.searchPictureUrl+searchWord, NetImageResult.class, new NetRequest.BeanCallback<NetImageResult>() {
            @Override
            public void onSuccess(NetImageResult response) {
//                Log.i("TAG","size: +"+response.getItems().length);
                recyclerAdapter = new ImageRecyclerAdapter(response.getItems(),layoutSysle);
                recyclerView.setAdapter(recyclerAdapter);

            }

            @Override
            public void onError(Exception exception, String errorInfo) {
                Toast.makeText(MainActivity.this,"net error",Toast.LENGTH_SHORT).show();
                recyclerAdapter = new ImageRecyclerAdapter(null,layoutSysle);
            }
        });
    }

    /**
     * 设置滑动监听
     */
    private void addScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
    }
}
