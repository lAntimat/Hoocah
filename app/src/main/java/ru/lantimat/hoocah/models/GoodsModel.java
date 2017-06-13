package ru.lantimat.hoocah.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

/**
 * Created by Ильназ on 13.06.2017.
 */

@IgnoreExtraProperties
public class GoodsModel {
    String name;
    String imgUrl;
    ArrayList<ItemModel> itemModels;

    public GoodsModel(String name, String imgUrl, ArrayList<ItemModel> itemModels) {
        this.name = name;
        this.imgUrl = imgUrl;
        this.itemModels = itemModels;
    }

    public GoodsModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String ingUrl) {
        this.imgUrl = ingUrl;
    }

    public ArrayList<ItemModel> getItemModels() {
        return itemModels;
    }

    public void setItemModels(ArrayList<ItemModel> itemModels) {
        this.itemModels = itemModels;
    }
}
