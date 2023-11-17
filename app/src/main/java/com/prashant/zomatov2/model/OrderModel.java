package com.prashant.zomatov2.model;

import java.util.List;

public class OrderModel {
    private long uid;
    private long userId;
    private List<MenuItem> items;
    private double totalAmount;
    private String orderTime;
    private String status;
}
