package com.example.restaurantdemo.biz;

import com.example.restaurantdemo.bean.Product;
import com.example.restaurantdemo.config.config;
import com.example.restaurantdemo.net.CommonCallBack;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.List;

public class ProductBiz {
    public void ListByPage(int currentPage, CommonCallBack<List<Product>> commonCallBack){
        OkHttpUtils.post()
                .url(config.baseURL+"product_find")
                .addParams("currentPage",currentPage+"")
                .tag(this)
                .build()
                .execute(commonCallBack);
    }
    public void onDestroy(){
        OkHttpUtils.getInstance().cancelTag(this);
    }
}
