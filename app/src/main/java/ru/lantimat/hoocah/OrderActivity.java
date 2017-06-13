package ru.lantimat.hoocah;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class OrderActivity extends AppCompatActivity implements BillFragment.OnFragmentInteractionListener, GoodsFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);


        Fragment fragment;
        Fragment fragment2;
        fragment = new GoodsFragment();
        fragment2 = new BillFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.gods_frame, fragment).commit();
        fragmentManager.beginTransaction().replace(R.id.bill_frame, fragment2).commit();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
