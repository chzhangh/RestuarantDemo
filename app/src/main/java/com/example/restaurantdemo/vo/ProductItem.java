package com.example.restaurantdemo.vo;


import com.example.restaurantdemo.bean.Product;

/**
 * Created by zhanghongyang01 on 16/10/18.
 */

public class ProductItem extends Product {
    public int count;

    public ProductItem(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.label = product.getLabel();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.icon = product.getIcon();
        this.resturant = product.getResturant();
    }

}
