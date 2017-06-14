package ru.lantimat.hoocah.models;

/**
 * Created by lantimat on 14.06.17.
 */

public class ActiveItemModel extends Item {

    int count;

    public ActiveItemModel(String name, String description, float price, int count) {
        this.name = name;
        this.desription = description;
        this.price = price;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
