package ru.lantimat.hoocah.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by lantimat on 14.06.17.
 */
@IgnoreExtraProperties
public class ActiveItemModel extends Item {

    String taste;

    public ActiveItemModel(int id, String name, String description, String taste, int price) {
        this.id = id;
        this.name = name;
        this.desription = description;
        this.taste = taste;
        this.price = price;
    }

    public ActiveItemModel() {
    }

    public String getTaste() {
        return taste;
    }

    public void setTaste(String taste) {
        this.taste = taste;
    }
}
