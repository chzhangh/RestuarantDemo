package com.example.restaurantdemo.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.restaurantdemo.R;
import com.example.restaurantdemo.bean.Product;
import com.example.restaurantdemo.config.config;
import com.example.restaurantdemo.utils.T;
import com.squareup.picasso.Picasso;

import java.io.Serializable;

public class ProductDetailActivity extends BaseActivity {

    private Product product;
    private ImageView mIvImage;
    private TextView mTvTitle;
    private TextView mTvDesc;
    private TextView mTvPrice;
    private static final String KEY_PRODUCT = "key_product";
    public static void launch(Context context,Product product){
        Intent intent = new Intent(context,ProductDetailActivity.class);
        intent.putExtra(KEY_PRODUCT,product);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        setUpToolbar();
        setTitle("详情");
        Intent intent = getIntent();
        if(intent != null){
           product = (Product) intent.getSerializableExtra(KEY_PRODUCT);

        }
        if(product == null){
            T.showToast("参数传递错误");
        }

        initView();
    }

    private void initView() {
        mIvImage = findViewById(R.id.id_iv_img);
        mTvTitle = findViewById(R.id.id_tv_title);
        mTvDesc = findViewById(R.id.id_tv_content);
        mTvPrice = findViewById(R.id.id_tv_price);

        Picasso.get()
                .load(config.baseURL+product.getIcon())
                .placeholder(R.drawable.pictures_no)
                .into(mIvImage);
        mTvTitle.setText(product.getName());
        mTvDesc.setText(product.getDescription());
        mTvPrice.setText(product.getPrice()+"元/份");

    }
}