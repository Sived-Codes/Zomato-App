package com.prashant.zomatov2.model;

import java.util.List;

public class RestaurantModel {
    private String uid;
    private String name;
    private String address;
    private String phoneNumber;
    private String image;
    private String ownerName;
    private String famousItem;
    private List<MenuItem> menu;

    public RestaurantModel() {
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getFamousItem() {
        return famousItem;
    }

    public void setFamousItem(String famousItem) {
        this.famousItem = famousItem;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<MenuItem> getMenu() {
        return menu;
    }

    public void setMenu(List<MenuItem> menu) {
        this.menu = menu;
    }
}
