package ru.lantimat.hoocah;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PayActivity extends AppCompatActivity implements NumKeyboardFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        Fragment fragment;
        //Fragment fragment2;
        fragment = GoodsFragment.newInstance("");
        //fragment2 = BillFragment.newInstance(id);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.keyboard_frame, fragment).commit();
        //fragmentManager.beginTransaction().replace(R.id.keyboard_frame, fragment2).commit();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
