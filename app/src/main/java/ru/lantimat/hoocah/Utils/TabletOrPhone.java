package ru.lantimat.hoocah.Utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by lantimat on 20.06.17.
 */

public class TabletOrPhone {
    Activity activity;
    boolean isPhone;
    boolean isTablet;

    public TabletOrPhone(Activity activity) {

        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        float yInches= metrics.heightPixels/metrics.ydpi;
        float xInches= metrics.widthPixels/metrics.xdpi;
        double diagonalInches = Math.sqrt(xInches*xInches + yInches*yInches);
        if (diagonalInches>=6.5){
            // 6.5inch device or bigger
            isTablet=true;
            isPhone = false;
        }else{
            // smaller device
            isPhone=true;
            isTablet=false;
        }
    }

    public boolean isPhone() {
        return isPhone;
    }

    public boolean isTablet() {
        return isTablet;
    }
}
