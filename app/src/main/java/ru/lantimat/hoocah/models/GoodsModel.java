package ru.lantimat.hoocah.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Ильназ on 13.06.2017.
 */

@IgnoreExtraProperties
public class GoodsModel {
    int id;
    String name;
    String imgUrl;

    public GoodsModel(int id, String name, String ingUrl) {
        this.id = id;
        this.name = name;
        this.imgUrl = ingUrl;
    }

    public GoodsModel() {
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String ingUrl) {
        this.imgUrl = ingUrl;
    }
}
