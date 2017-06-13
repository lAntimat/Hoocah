package ru.lantimat.hoocah.models;

import java.util.ArrayList;

/**
 * Created by Ильназ on 13.06.2017.
 */

public class HookahModel {

    int id;
    String name;
    double price;
    ArrayList<String> taste;

    public HookahModel(int id, String name, double price, ArrayList<String> taste) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.taste = taste;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setPrice(double price) {
        this.price = price;
    }

    public ArrayList<String> getTaste() {
        return taste;
    }

    public void setTaste(ArrayList<String> taste) {
        this.taste = taste;
    }
}
