package com.heroku.java.bean;

public class Menu {
    private String menu_id;
    private String menu_name;
    private String menu_desc;
    private float price;

    public Menu(String menu_id, String menu_name, String menu_desc, float price) {
        this.menu_id = menu_id;
        this.menu_name = menu_name;
        this.menu_desc = menu_desc;
        this.price = price;
    }
    
    public Menu(){

    }
    
    public String getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(String menu_id) {
        this.menu_id = menu_id;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    public String getMenu_desc() {
        return menu_desc;
    }

    public void setMenu_desc(String menu_desc) {
        this.menu_desc = menu_desc;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
