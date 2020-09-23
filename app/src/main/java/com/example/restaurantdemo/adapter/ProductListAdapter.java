package com.example.restaurantdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantdemo.R;
import com.example.restaurantdemo.config.config;
import com.example.restaurantdemo.ui.activity.ProductDetailActivity;
import com.example.restaurantdemo.utils.T;
import com.example.restaurantdemo.vo.ProductItem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductListItemHolder> {
    private Context context;
    private List<ProductItem> mProductItems;
    private LayoutInflater mInflater;

    public ProductListAdapter(Context context, List<ProductItem> mProductItems) {
        this.context = context;
        this.mProductItems = mProductItems;
        mInflater = LayoutInflater.from(context);
    }


    public interface onProductListner{
        void onProductAdd(ProductItem productItem);
        void onProductSub(ProductItem productItem);
    }

    private  onProductListner mOnProductListner;
    public void setOnProductListner(onProductListner onProductListner){
        mOnProductListner = onProductListner;
    }

    @NonNull
    @Override
    public ProductListItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.product_item_list, parent, false);
        return new ProductListItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListItemHolder holder, int position) {
        ProductItem productItem = mProductItems.get(position);
        Picasso.get()
                .load(config.baseURL+productItem.getIcon())
                .placeholder(R.drawable.pictures_no)
                .into(holder.imageView);
        holder.mTvName.setText(productItem.getName());
        holder.mTvCount.setText(productItem.count+"");
        holder.mTvLabel.setText(productItem.getLabel());
        holder.mTvPrice.setText(productItem.getPrice()+"元/份");

    }

    @Override
    public int getItemCount() {
        return mProductItems.size();
    }

    class ProductListItemHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public TextView mTvName;
        public TextView mTvLabel;
        public TextView mTvPrice;
        public ImageView imageAdd;
        public ImageView imageSub;
        public TextView mTvCount;

        public ProductListItemHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.id_iv_img);
            mTvName = itemView.findViewById(R.id.id_tv_name);
            mTvLabel = itemView.findViewById(R.id.id_tv_label);
            mTvPrice = itemView.findViewById(R.id.id_tv_price);
            imageAdd = itemView.findViewById(R.id.id_img_add);
            imageSub = itemView.findViewById(R.id.id_img_sub);
            mTvCount = itemView.findViewById(R.id.id_tv_count);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProductDetailActivity.launch(context,mProductItems.get(getLayoutPosition()));
                }
            });

            imageAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getLayoutPosition();
                    ProductItem productItem = mProductItems.get(pos);
                    productItem.count += 1;
                    mTvCount.setText(productItem.count+"");
                    //回调到Activity
                    if(mOnProductListner != null){
                        mOnProductListner.onProductAdd(productItem);
                    }

                }
            });
            imageSub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getLayoutPosition();
                    ProductItem productItem = mProductItems.get(pos);
                    if(productItem.count <= 0){
                        T.showToast("已经为0了！");
                        return;
                    }
                    productItem.count -= 1;
                    mTvCount.setText(productItem.count+"");
                    //回调到Activity
                    if(mOnProductListner != null){
                        mOnProductListner.onProductSub(productItem);
                    }

                }
            });
        }
    }
}
