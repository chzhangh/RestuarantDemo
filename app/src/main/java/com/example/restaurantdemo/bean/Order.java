package com.example.restaurantdemo.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Order implements Serializable {
    public static class ProductVo implements Serializable {
       public Product product;
        public int count;
    }

    public Map<Product,Integer> productIntegerMap = new HashMap<>();//用来存储点菜
    private int id;
    private Date date;
    private Resturant resturant;
    private int count; //消费了几个东西
    private float price;
    private List<ProductVo> ps;

    public void addProduct(Product product){
        Integer count = productIntegerMap.get(product);
        if(count == null || count == 0){
            count=1;
        }else {
            count++;
        }

        productIntegerMap.put(product,count);


    }
    public void removeProduct(Product product){
        Integer count = productIntegerMap.get(product);
        if (count == null || count <=0){
            return;
        }else {
            count--;
        }

        productIntegerMap.put(product,count);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Resturant getResturant() {
        return resturant;
    }

    public void setResturant(Resturant resturant) {
        this.resturant = resturant;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public List<ProductVo> getPs() {
        return ps;
    }

    public void setPs(List<ProductVo> ps) {
        this.ps = ps;
    }
}
