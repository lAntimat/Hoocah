package ru.lantimat.hoocah.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by lantimat on 14.06.17.
 */
@IgnoreExtraProperties
public class ActiveItemModel extends Item {

    int count;
    String taste;

    public ActiveItemModel(int id, String name, String description, String taste, float price, int count) {
        this.id = id;
        this.name = name;
        this.desription = description;
        this.taste = taste;
        this.price = price;
        this.count = count;
    }

    public ActiveItemModel() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void countPlusOne() {
        this.count++;
    }

    public String getTaste() {
        return taste;
    }

    public void setTaste(String taste) {
        this.taste = taste;
    }

    public float getTotalPrice() {
        return price*count;
    }
}
