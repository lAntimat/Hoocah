package ru.lantimat.hoocah.models;

import java.util.ArrayList;

/**
 * Created by lantimat on 14.06.17.
 */

public class ActiveOrder {

    private int id;
    private int data;
    private String name;
    private boolean active;
    private float totalPrice;
    private ArrayList<ActiveItemModel> arActiveItemModel;

    public ActiveOrder(int id, int data, String name, boolean active, float totalPrice, ArrayList<ActiveItemModel> arActiveItemModel) {
        this.id = id;
        this.data = data;
        this.name = name;
        this.active = active;
        this.totalPrice = totalPrice;
        this.arActiveItemModel = arActiveItemModel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public ArrayList<ActiveItemModel> getArActiveItemModel() {
        return arActiveItemModel;
    }

    public void setArActiveItemModel(ArrayList<ActiveItemModel> arActiveItemModel) {
        this.arActiveItemModel = arActiveItemModel;
    }
}
