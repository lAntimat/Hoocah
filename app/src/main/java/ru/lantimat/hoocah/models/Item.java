package ru.lantimat.hoocah.models;

/**
 * Created by lantimat on 14.06.17.
 */

public class Item {
    String name;
    String desription;
    float price;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesription() {
        return desription;
    }

    public void setDesription(String desription) {
        this.desription = desription;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
