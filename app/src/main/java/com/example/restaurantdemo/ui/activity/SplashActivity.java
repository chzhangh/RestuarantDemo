package com.example.restaurantdemo.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.example.restaurantdemo.R;
import com.example.restaurantdemo.ui.activity.LoginActivity;

public class SplashActivity extends AppCompatActivity {
    private Button skipButton;
    private Handler handler = new Handler();
    private Runnable mRunnableToLogin = new Runnable() {
        @Override
        public void run() {
            toLoginActivity();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
        initEvent();
        handler.postDelayed(mRunnableToLogin,3000);
    }

    private void initEvent() {
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacks(mRunnableToLogin); //会打开两次activity
                toLoginActivity();
            }
        });
    }

    private void initView() {
        skipButton = findViewById(R.id.btn_skip);
    }


    private void toLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();//由于是闪屏页面，不需要backed是，所以需要finished
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(mRunnableToLogin); //避免造成内存泄漏
    }
}