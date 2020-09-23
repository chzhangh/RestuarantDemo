package com.example.restaurantdemo.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.restaurantdemo.R;
import com.example.restaurantdemo.adapter.ProductListAdapter;
import com.example.restaurantdemo.bean.Order;
import com.example.restaurantdemo.bean.Product;
import com.example.restaurantdemo.biz.OrderBiz;
import com.example.restaurantdemo.biz.ProductBiz;
import com.example.restaurantdemo.net.CommonCallBack;
import com.example.restaurantdemo.ui.view.refresh.SwipeRefresh;
import com.example.restaurantdemo.ui.view.refresh.SwipeRefreshLayout;
import com.example.restaurantdemo.utils.T;
import com.example.restaurantdemo.vo.ProductItem;

import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefreshLayout;
    private TextView mTvCount;
    private Button mBtnPay;
    private ProductListAdapter mAdapter;
    private List<ProductItem> mDatas = new ArrayList<>();

    private ProductBiz mProductBiz = new ProductBiz();
    private OrderBiz mOrderBiz = new OrderBiz();
    private int currentPage;
    private float  mTotalPrice;
    private int mTotalcount;
    private Order mOrder = new Order();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        setUpToolbar();
        setTitle("订餐");

        initView();
        initEvent();
        loadData();
    }

    private void initEvent() {
        mRefreshLayout.setOnPullUpRefreshListener(new SwipeRefreshLayout.OnPullUpRefreshListener() {
            @Override
            public void onPullUpRefresh() {
                loadMore();
            }
        });

        mRefreshLayout.setOnRefreshListener(new SwipeRefresh.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

        mBtnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mTotalcount <= 0){
                    T.showToast("你还没有选择菜品。。。");
                    return;
                }
                mOrder.setCount(mTotalcount);
                mOrder.setPrice(mTotalPrice);
                mOrder.setId(mDatas.get(0).getResturant().getId());
                startLodingProgress();
              mOrderBiz.add(mOrder, new CommonCallBack<String>() {
                  @Override
                  public void onError(Exception e) {
                      stopLodingProgress();
                      T.showToast(e.getMessage());
                  }

                  @Override
                  public void onSuccess(String e) {
                      stopLodingProgress();
                      T.showToast("订单支付成功!");
                      setResult(RESULT_OK);
                      finish();
                  }
              });
            }
        });

        mAdapter.setOnProductListner(new ProductListAdapter.onProductListner() {
            @Override
            public void onProductAdd(ProductItem productItem) {
                mTotalcount++;
                mTvCount.setText("数量："+mTotalcount);
                mTotalPrice += productItem.getPrice();
                mBtnPay.setText(mTotalPrice+"元 立即支付");
                mOrder.addProduct(productItem);

            }

            @Override
            public void onProductSub(ProductItem productItem) {
                mTotalcount--;
                mTvCount.setText("数量："+mTotalcount);
                mTotalPrice -= productItem.getPrice();

                if(mTotalcount == 0){
                    mTotalPrice = 0;
                }
                mBtnPay.setText(mTotalPrice+"元 立即支付");
                mOrder.removeProduct(productItem);
            }
        });
    }

    private void loadData() {
        startLodingProgress();
        mProductBiz.ListByPage(0, new CommonCallBack<List<Product>>() {
            @Override
            public void onError(Exception e) {
                stopLodingProgress();
                T.showToast(e.getMessage());
                mRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onSuccess(List<Product> e) {
                stopLodingProgress();
                mRefreshLayout.setRefreshing(false);
                currentPage = 0;
                mDatas.clear();
                for (Product p:e) {
                    mDatas.add(new ProductItem(p));
                }
                mAdapter.notifyDataSetChanged();
                //刷新数据，清空选择的数量价格
                mTotalPrice = 0;
                mTotalcount = 0;

                mTvCount.setText("数量："+mTotalcount);
                mBtnPay.setText(mTotalPrice+"元 立即支付");
            }
        });

    }

    private void loadMore() {
        startLodingProgress();
        mProductBiz.ListByPage(++currentPage, new CommonCallBack<List<Product>>() {
            @Override
            public void onError(Exception e) {
                stopLodingProgress();
                T.showToast(e.getMessage());
                currentPage--;
                mRefreshLayout.setPullUpRefreshing(false);
            }

            @Override
            public void onSuccess(List<Product> e) {
                stopLodingProgress();
                mRefreshLayout.setPullUpRefreshing(false);
                if(e.size()<=0){
                    T.showToast("没有了！");
                    return;
                }
                T.showToast("找到了"+e.size()+"道菜");
                for (Product p:e) {
                    mDatas.add(new ProductItem(p));
                }
                mAdapter.notifyDataSetChanged();
            }
        });


    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.id_recycler);
        mTvCount = (TextView) findViewById(R.id.id_count);
        mBtnPay = (Button) findViewById(R.id.id_btn_pay);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.id_swiperefresh);

        mRefreshLayout.setMode(SwipeRefresh.Mode.BOTH);//代表上拉，下拉都是支持的
        mRefreshLayout.setColorSchemeColors(Color.RED,Color.BLACK,Color.GREEN,Color.YELLOW);
        mAdapter = new ProductListAdapter(this,mDatas);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mProductBiz.onDestroy();
    }
}