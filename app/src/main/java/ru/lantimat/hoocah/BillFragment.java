package ru.lantimat.hoocah;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ru.lantimat.hoocah.adapters.ActiveItemRecyclerAdapter;
import ru.lantimat.hoocah.models.ActiveItemModel;
import ru.lantimat.hoocah.models.ActiveOrder;
import ru.lantimat.hoocah.models.GoodsModel;

import static android.content.ContentValues.TAG;


public class BillFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    private String mParam1;

    TextView tvTotalPrice;
    RecyclerView recyclerView;
    ActiveItemRecyclerAdapter activeItemRecyclerAdapter;
    ArrayList<ActiveItemModel> arActiveModel = new ArrayList<>();
    private DatabaseReference mDatabaseActiveItemReference;

    public static BillFragment newInstance(String param1) {
        BillFragment fragment = new BillFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    public BillFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }

        mDatabaseActiveItemReference = FirebaseDatabase.getInstance().getReference("activeItem");
        activeItemListener();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bill, container,false);
        tvTotalPrice = (TextView) view.findViewById(R.id.textView);
        setupRecyclerView(view);

        return view;
    }

    private void setupRecyclerView(View view) {
        //arActiveModel.add(new ActiveItemModel("Название", "", "", 300f, 1));
        activeItemRecyclerAdapter = new ActiveItemRecyclerAdapter(arActiveModel);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(activeItemRecyclerAdapter);

    }

    private void activeItemListener() {
        mDatabaseActiveItemReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(mParam1).exists()) {
                    ActiveOrder activeOrder = dataSnapshot.child(mParam1).getValue(ActiveOrder.class);
                    //arActiveModel = activeOrder.getArActiveItemModel();
                    if (activeOrder != null) {
                        arActiveModel.clear();
                        for (ActiveItemModel item: activeOrder.getArActiveItemModel()) {
                            arActiveModel.add(item);
                            activeItemRecyclerAdapter.notifyDataSetChanged();
                        }
                    }

                    tvTotalPrice.setText("Общая стоимость " + String.valueOf(activeOrder.getTotalPrice()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
