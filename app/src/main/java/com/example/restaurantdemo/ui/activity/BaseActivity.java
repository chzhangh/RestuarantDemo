package com.example.restaurantdemo.ui.activity;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.restaurantdemo.R;

public class BaseActivity extends AppCompatActivity {
    private ProgressDialog mLog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setUpToolbar();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(0xff000000);
        }
        mLog = new ProgressDialog(this);
        mLog.setMessage("加载中");

    }
    protected void stopLodingProgress() {
        if(mLog != null && mLog.isShowing()){
            mLog.dismiss();
        }
    }

    protected void startLodingProgress() {
       mLog.show();
    }
    void setUpToolbar() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
            setActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }
    protected void ToLoginActivity() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLodingProgress();
        mLog = null;
    }
}
