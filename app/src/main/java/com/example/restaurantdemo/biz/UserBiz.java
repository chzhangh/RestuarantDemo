package com.example.restaurantdemo.biz;

import com.example.restaurantdemo.bean.User;
import com.example.restaurantdemo.config.config;
import com.example.restaurantdemo.net.CommonCallBack;
import com.zhy.http.okhttp.OkHttpUtils;

public class UserBiz {
    /**
     * 登录
     */
    public void Login(String username, String password,
                       CommonCallBack<User> callBack){
        OkHttpUtils.post()
                .url(config.baseURL+"user_login")
                .tag(this) //用来取消我们的请求的
                .addParams("username",username)
                .addParams("password",password)
                .build()
                .execute(callBack);

    }


    /**
     * 注册
     */
    public void register(String username, String password,
                          CommonCallBack<User> callBack){
        OkHttpUtils.post()
                .url(config.baseURL+"user_register")
                .tag(this)
                .addParams("username",username)
                .addParams("password",password)
                .build()
                .execute(callBack);

    }
    public void onDestroy(){
        OkHttpUtils.getInstance().cancelTag(this);
    }
}
