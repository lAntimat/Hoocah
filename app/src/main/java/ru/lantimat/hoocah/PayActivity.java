package ru.lantimat.hoocah;

import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialize.color.Material;

import ru.lantimat.hoocah.fragments.EditPayFragment;
import ru.lantimat.hoocah.fragments.NumKeyboardFragment;

public class PayActivity extends AppCompatActivity implements NumKeyboardFragment.OnFragmentInteractionListener {

    NumKeyboardFragment fragmentA;
    EditPayFragment fragmentB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(new IconicsDrawable(this)
                .icon(GoogleMaterial.Icon.gmd_arrow_back)
                .color(Color.WHITE)
                .sizeDp(16));
        toolbar.setTitleTextColor(Material.White._1000.getAsColor());

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        String id = null;
        if(getIntent()!=null) {
            id = getIntent().getStringExtra("id");
        }

        Fragment fragment;
        Fragment fragment2;
        fragment = NumKeyboardFragment.newInstance("");
        fragment2 = EditPayFragment.newInstance(id);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.close_order_frame, fragment).commit();
        fragmentManager.beginTransaction().replace(R.id.pay_frame, fragment2).commit();



        fragmentA = (NumKeyboardFragment) fragment;
        fragmentB = (EditPayFragment) fragment2;


       fragmentA.setButtonClickListener(new NumKeyboardFragment.ButtonClickListener() {
           @Override
           public void onTextChange(CharSequence newText) {
               fragmentB.updateTextValue(newText);
           }
       });



    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
