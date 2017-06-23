package ru.lantimat.hoocah;

import com.google.firebase.database.FirebaseDatabase;

import net.danlew.android.joda.JodaTimeAndroid;

/**
 * Created by lantimat on 20.06.17.
 */

public class FireBaseApp extends android.app.Application {

        @Override
        public void onCreate() {
            super.onCreate();
    /* Enable disk persistence  */
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            JodaTimeAndroid.init(this);
        }
}
