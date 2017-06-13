package ru.lantimat.hoocah.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

/**
 * Created by Ильназ on 13.06.2017.
 */
@IgnoreExtraProperties
public class ItemModel {

    String name;
    String desription;
    float price;
    ArrayList<String> taste;

    public ItemModel( String name, String desription, float price, ArrayList<String> taste) {
        this.name = name;
        this.desription = desription;
        this.price = price;
        this.taste = taste;
    }

    public ItemModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public ArrayList<String> getTaste() {
        return taste;
    }

    public void setTaste(ArrayList<String> taste) {
        this.taste = taste;
    }

    public String getDesription() {
        return desription;
    }

    public void setDesription(String desription) {
        this.desription = desription;
    }
}
