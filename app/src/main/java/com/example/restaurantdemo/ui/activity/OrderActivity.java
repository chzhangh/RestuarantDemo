package com.example.restaurantdemo.ui.activity;


import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.restaurantdemo.R;
import com.example.restaurantdemo.UserInfoHolder;
import com.example.restaurantdemo.adapter.OrderAdapter;
import com.example.restaurantdemo.bean.Order;
import com.example.restaurantdemo.bean.User;
import com.example.restaurantdemo.biz.OrderBiz;
import com.example.restaurantdemo.net.CommonCallBack;
import com.example.restaurantdemo.ui.view.CircleTransform;
import com.example.restaurantdemo.ui.view.refresh.SwipeRefresh;
import com.example.restaurantdemo.ui.view.refresh.SwipeRefreshLayout;
import com.example.restaurantdemo.utils.T;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Picasso.Builder;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends BaseActivity {
    private  Button mBtnOder;
    private TextView mTvUserName;
    private RecyclerView mRecylerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ImageView mIvIcon;
    private OrderAdapter adapter;
    private List<Order> mDatas = new ArrayList<>();
    private OrderBiz orderBiz = new OrderBiz();
    private int mCurrentPage = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        initView();
        initEvent();

    }

    private void initEvent() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefresh.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadDatas();
            }
        });

        mSwipeRefreshLayout.setOnPullUpRefreshListener(new SwipeRefreshLayout.OnPullUpRefreshListener() {
            @Override
            public void onPullUpRefresh() {
                loadMore();
            }
        });
        mBtnOder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderActivity.this,ProductListActivity.class);
                startActivityForResult(intent,1001);
            }
        });
    }

    private void loadMore() {
        startLodingProgress();
        orderBiz.listByPage(++mCurrentPage, new CommonCallBack<List<Order>>() {
            @Override
            public void onError(Exception e) {
                stopLodingProgress();
                T.showToast(e.getMessage());
                mCurrentPage--;
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onSuccess(List<Order> e) {
                stopLodingProgress();
                if(e.size()==0){
                    T.showToast("木有订单了！" );
                    mSwipeRefreshLayout.setRefreshing(false);
                    return;
                }else {
                    T.showToast("订单加载成功！");
                    mDatas.addAll(e);
                    adapter.notifyDataSetChanged();
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });

    }

    private void loadDatas() {
        startLodingProgress();
        orderBiz.listByPage(0, new CommonCallBack<List<Order>>() {
            @Override
            public void onError(Exception e) {
                stopLodingProgress();
                T.showToast(e.getMessage());
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onSuccess(List<Order> e) {
               stopLodingProgress();
               mCurrentPage = 0;
               T.showToast("订单更新成功");
               mDatas.clear();
               mDatas.addAll(e);
               adapter.notifyDataSetChanged();
               if(mSwipeRefreshLayout.isRefreshing()){
                   mSwipeRefreshLayout.setRefreshing(false);
               }
            }
        });
    }

    private void initView() {
        mBtnOder = findViewById(R.id.id_btn_take_order);
        mTvUserName = findViewById(R.id.id_tv_name);
        mRecylerView = findViewById(R.id.id_recyclerview);
        mSwipeRefreshLayout = findViewById(R.id.id_refresh_layout);
        mIvIcon = findViewById(R.id.id_iv_icon);

        User user = UserInfoHolder.getInstance().getUser();
        if(user != null){
            mTvUserName.setText(user.getUsername());
        }else{
            ToLoginActivity();
            finish();
            return;
        }
        mSwipeRefreshLayout.setMode(SwipeRefresh.Mode.BOTH);//代表上拉，下拉都是支持的
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED,Color.BLACK,Color.GREEN,Color.YELLOW);
        //RecyclerView
        adapter = new OrderAdapter(mDatas,this);
        mRecylerView.setLayoutManager(new LinearLayoutManager(this));
        mRecylerView.setAdapter(adapter);
       //Picasso.get().load(Common.getImage(openWeatherMap.getWeatherList().get(0).getIcon()))
        //  .into(imageView);
       Picasso.get()
               .load(R.drawable.icon)
               .placeholder(R.drawable.pictures_no)
               .transform(new CircleTransform())
               .into(mIvIcon);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1001 && resultCode==RESULT_OK){
            loadDatas();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        try {
            if(keyCode == KeyEvent.KEYCODE_BACK) {
                Intent home = new Intent(Intent.ACTION_MAIN);
                home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                home.addCategory(Intent.CATEGORY_HOME);
                startActivity(home);
                return true;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return super.onKeyDown(keyCode, event);
    }
}