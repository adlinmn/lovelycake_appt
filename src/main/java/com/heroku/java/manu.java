package com.heroku.java;


public class menu {
    public String product_name;
    public float product_price;
    public String description;
    public String product_image;
    public String product_type;


    public String getProduct_name() {
        return this.product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public float getProduct_price() {
        return this.product_price;
    }

    public void setProductar_price(float product_price) {
        this.product_price = product_price;
    }

    public String getDescription() {
        return this.description;
    }

    public String getProduct_image() {
        return this.product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getProduct_type() {
        return this.product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

}
