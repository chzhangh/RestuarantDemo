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
import com.example.restaurantdemo.bean.Order;
import com.example.restaurantdemo.config.config;
import com.example.restaurantdemo.ui.activity.ProductDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderItemViewHolder> {
    private List<Order> mDatas;
    private Context mContext;
    private LayoutInflater mInflater;


    public OrderAdapter(List<Order> mDatas, Context mContext) {
        this.mDatas = mDatas;
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.order_item_list, parent, false);
        return new OrderItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
       Order order = mDatas.get(position);
       Picasso.get()
               .load(config.baseURL+order.getResturant().getIcon())
               .placeholder(R.drawable.pictures_no)
               .into(holder.imageView);
       if(order.getPs().size() > 0){
           holder.mTvPrice.setText(order.getPs().get(0)
                   .product.getName()+"等"+order.getCount()+"件商品");
       }else{
           holder.mTvLabel.setText("无消费");
       }
        holder.mTvName.setText(order.getResturant().getName());
        holder.mTvPrice.setText("共消费 "+order.getPrice()+"元");

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class OrderItemViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        public TextView mTvName;
        public TextView mTvLabel;
        public TextView mTvPrice;

        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //详情页面

                }
            });
            imageView = itemView.findViewById(R.id.id_iv_img);
            mTvName = itemView.findViewById(R.id.id_tv_name);
            mTvLabel = itemView.findViewById(R.id.id_tv_label);
            mTvPrice = itemView.findViewById(R.id.id_tv_price);
        }
    }

}
