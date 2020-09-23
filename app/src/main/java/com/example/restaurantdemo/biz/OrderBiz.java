package com.example.restaurantdemo.biz;

import com.example.restaurantdemo.bean.Order;
import com.example.restaurantdemo.bean.Product;
import com.example.restaurantdemo.config.config;
import com.example.restaurantdemo.net.CommonCallBack;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;

public class OrderBiz {
    public void listByPage(int currentPage, CommonCallBack<List<Order>> commonCallBack){
        OkHttpUtils.post()
                .url(config.baseURL+"order_find")
                .tag(this)
                .addParams("currentpage",currentPage+"")
                .build()
                .execute(commonCallBack);
    }


    public void add(Order order,CommonCallBack<String> commonCallBack){
        StringBuilder stringBuilder = new StringBuilder();
        Map<Product, Integer> map = order.productIntegerMap;
        for (Product p : map.keySet()) {
            stringBuilder.append(p.getId()+"_"+map.get(p));
            stringBuilder.append("|");
        }
        stringBuilder.deleteCharAt(stringBuilder.length()-1);
        OkHttpUtils.post()
                .url(config.baseURL+"order_add")
                .tag(this)
                .addParams("res_id",order.getResturant().getId()+"")
                .addParams("product_str", stringBuilder.toString())
                .addParams("count",order.getCount()+"")
                .addParams("price",order.getPrice()+"")
                .build()
                .execute(commonCallBack);

    }
    public void onDestroy(){
        OkHttpUtils.getInstance().cancelTag(this);
    }
}
