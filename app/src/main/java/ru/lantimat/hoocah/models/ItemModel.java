package ru.lantimat.hoocah.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

/**
 * Created by Ильназ on 13.06.2017.
 */
@IgnoreExtraProperties
public class ItemModel extends Item {


    String imgUrl;
    ArrayList<String> taste;

    public ItemModel( String name, String desription, String imgUrl, float price, ArrayList<String> taste) {
        this.name = name;
        this.desription = desription;
        this.imgUrl = imgUrl;
        this.price = price;
        this.taste = taste;
    }

    public ItemModel() {
    }



    public ArrayList<String> getTaste() {
        return taste;
    }

    public void setTaste(ArrayList<String> taste) {
        this.taste = taste;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
