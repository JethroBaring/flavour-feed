package com.finalproject.flavourfeed;

import java.util.ArrayList;

public class OrderModel {
    final static int PENDING = 1;
    final static int CONFIRMED = 2;
    final static int IN_PROGRESS = 3;
    final static int ON_THE_WAY = 4;
    final static int DELIVERED = 5;
    public String orderId;
    public int status;

    public ArrayList<ProductModel> items;
}
