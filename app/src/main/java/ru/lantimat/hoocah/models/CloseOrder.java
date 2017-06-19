package ru.lantimat.hoocah.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

/**
 * Created by lantimat on 14.06.17.
 */
@IgnoreExtraProperties
public class CloseOrder {

    private String id;
    private long unixTimeOpen;
    private long unixTimeClose;
    private float totalPrice;
    private ArrayList<ActiveItemModel> arActiveItemModel;

    public CloseOrder(String id, long unixTimeOpen, long unixTimeClose, float totalPrice, ArrayList<ActiveItemModel> arActiveItemModel) {
        this.id = id;
        this.unixTimeOpen = unixTimeOpen;
        this.unixTimeClose = unixTimeClose;
        this.totalPrice = totalPrice;
        this.arActiveItemModel = arActiveItemModel;
    }

    public CloseOrder() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getUnixTimeOpen() {
        return unixTimeOpen;
    }

    public void setUnixTimeOpen(long unixTimeOpen) {
        this.unixTimeOpen = unixTimeOpen;
    }

    public long getUnixTimeClose() {
        return unixTimeClose;
    }

    public void setUnixTimeClose(long unixTimeClose) {
        this.unixTimeClose = unixTimeClose;
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
