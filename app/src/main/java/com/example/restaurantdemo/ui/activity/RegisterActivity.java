package com.example.restaurantdemo.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.restaurantdemo.R;
import com.example.restaurantdemo.UserInfoHolder;
import com.example.restaurantdemo.bean.User;
import com.example.restaurantdemo.biz.UserBiz;
import com.example.restaurantdemo.net.CommonCallBack;
import com.example.restaurantdemo.ui.activity.BaseActivity;
import com.example.restaurantdemo.utils.T;

public class RegisterActivity extends BaseActivity {
    private EditText eEditUserName;
    private EditText eEditpassword;
    private EditText eEditRepassword;
    private Button mBtn;
    private UserBiz mUserBiz = new UserBiz();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setUpToolbar();
        initView();
        initEvent();
        setTitle("注册");
    }




    private void initEvent() {
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = eEditUserName.getText().toString();
                final String password = eEditpassword.getText().toString();
                String repassword = eEditRepassword.getText().toString();

                if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
                    T.showToast("账号或者密码为空");
                    return;
                }

                if(!password.equals(repassword)){
                    T.showToast("两次输入的密码不一致");
                    return;
                }
                startLodingProgress();
                mUserBiz.register(username, password, new CommonCallBack<User>() {
                    @Override
                    public void onError(Exception e) {
                        stopLodingProgress();
                        T.showToast(e.getMessage());
                    }

                    @Override
                    public void onSuccess(User e) {
                        startLodingProgress();
                        T.showToast("注册成功"+e.getUsername());
                        LoginActivity.launch(RegisterActivity.this,e.getUsername(),e.getPassword());
                        finish();
                    }
                });
            }
        });
    }

    private void initView() {
        eEditUserName = findViewById(R.id.id_edit_username);
        eEditpassword = findViewById(R.id.id_edit_password);
        eEditRepassword = findViewById(R.id.id_edit_repassword);
        mBtn = findViewById(R.id.id_btn_register);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUserBiz.onDestroy();
    }
}