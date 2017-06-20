package ru.lantimat.hoocah.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

/**
 * Created by lantimat on 14.06.17.
 */
@IgnoreExtraProperties
public class ActiveOrder {

    private String id;
    private String comment;
    private long unixTime;
    private boolean active;
    private float totalPrice;
    private ArrayList<ActiveItemModel> arActiveItemModel;

    public ActiveOrder(String id, long unixTime, boolean active, float totalPrice, ArrayList<ActiveItemModel> arrayList) {
        this.id = id;
        this.unixTime = unixTime;
        this.active = active;
        this.totalPrice = totalPrice;
        this.arActiveItemModel = arrayList;
    }

    public ActiveOrder() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getUnixTime() {
        return unixTime;
    }

    public void setUnixTime(long unixTime) {
        this.unixTime = unixTime;
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
