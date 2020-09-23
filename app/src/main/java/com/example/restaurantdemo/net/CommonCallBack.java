package com.example.restaurantdemo.net;



import com.example.restaurantdemo.utils.GsonUtil;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;

public abstract class CommonCallBack<T> extends StringCallback {
    Type mType;

    public CommonCallBack() {
        Class<? extends CommonCallBack> clazz = getClass();
        Type genericSuperclass = clazz.getGenericSuperclass();
        if(genericSuperclass instanceof Class){
            throw new RuntimeException("Miss type Parames");
        }
        ParameterizedType genericSuperclass1 =
                (ParameterizedType) genericSuperclass;
        Type[] types = genericSuperclass1.getActualTypeArguments();
        mType = types[0];


    }

    @Override
    public void onError(Call call, Exception e, int id) {
        onError(e);
    }
    @Override
    public void onResponse(String response, int id) {
        try {
            JSONObject resp = new JSONObject(response);
            int resultCode = resp.getInt("resultCode");
            if(resultCode == 1){
                String data = resp.getString("data");

                onSuccess((T) GsonUtil.getGson().fromJson(data,mType));

            }else{
                onError(new RuntimeException(resp.getString("resultMessage")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            onError(e);
        }
    }
    public abstract void onError(Exception e);

    public abstract void onSuccess(T e);
}
