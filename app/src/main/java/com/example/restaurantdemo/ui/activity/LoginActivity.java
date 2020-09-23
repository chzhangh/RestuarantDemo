package com.example.restaurantdemo.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.restaurantdemo.R;
import com.example.restaurantdemo.UserInfoHolder;
import com.example.restaurantdemo.bean.User;
import com.example.restaurantdemo.biz.UserBiz;
import com.example.restaurantdemo.net.CommonCallBack;
import com.example.restaurantdemo.utils.T;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;

public class LoginActivity extends BaseActivity {
    private UserBiz mUserBiz = new UserBiz();
    private EditText mEditUserName;
    private EditText mEditPwd;
    private Button mBtnLogin;
    private TextView mTvRegister;
    private static final String KEY_USERNAME = "key_username";
    private static final String KEY_PWD = "key_pwd";

    public static void launch(Context context, String username, String password) {
        Intent intent = new Intent(context,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(KEY_USERNAME,username);
        intent.putExtra(KEY_PWD,password);
        context.startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initIntent(intent);
    }

    private void initIntent(Intent intent) {
        if(intent == null){
            return;
        }
        String username = intent.getStringExtra(KEY_USERNAME);
        final String password = intent.getStringExtra(KEY_PWD);
        mEditUserName.setText(username);
        mEditPwd.setText(password);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CookieJarImpl cookieJar = (CookieJarImpl) OkHttpUtils.getInstance().getOkHttpClient().cookieJar();
        cookieJar.getCookieStore().removeAll();
    }

    private void initView() {
        mEditUserName = findViewById(R.id.edit_account);
        mEditPwd = findViewById(R.id.edit_pwd);
        mBtnLogin = findViewById(R.id.btn_login);
        mTvRegister = findViewById(R.id.id_tv_register);
    }

    private void initEvent() {
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = mEditUserName.getText().toString();
                String password = mEditPwd.getText().toString();
                if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
                    T.showToast("账号或者密码为空");
                    return;
                }
               // startLodingProgress();
                mUserBiz.Login(username, password, new CommonCallBack<User>() {
                    @Override
                    public void onError(Exception e) {
                        stopLodingProgress();
                        T.showToast(e.getMessage());
                    }

                    @Override
                    public void onSuccess(User e) {
                        startLodingProgress();
                        T.showToast("登录成功");
                        //检查是否登陆成功,保存用户的登录信息
                        UserInfoHolder.getInstance().setUser(e);
                    toOrderActivity();
                    }
                });

            }
        });
        mTvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toRegisterActivity();
            }
        });
    }



    private void toRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void toOrderActivity() {
        Intent intent = new Intent(this, OrderActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUserBiz.onDestroy();
    }
}