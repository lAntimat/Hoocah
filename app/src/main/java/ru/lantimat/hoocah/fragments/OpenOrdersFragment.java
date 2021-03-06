package ru.lantimat.hoocah.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ru.lantimat.hoocah.OrderActivity;
import ru.lantimat.hoocah.PayActivity;
import ru.lantimat.hoocah.R;
import ru.lantimat.hoocah.Utils.Constants;
import ru.lantimat.hoocah.Utils.ItemClickSupport;
import ru.lantimat.hoocah.adapters.OpenOrderRecyclerAdapter;
import ru.lantimat.hoocah.models.ActiveItemModel;
import ru.lantimat.hoocah.models.ActiveOrder;


public class OpenOrdersFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    private String mParam1;
    int pos;

    RecyclerView recyclerView;
    OpenOrderRecyclerAdapter openOrderRecyclerAdapter;
    ArrayList<ActiveItemModel> arActiveModel = new ArrayList<>();
    private DatabaseReference mDatabaseActiveItemReference;
    ActiveOrder activeOrder; //Активный счет
    ArrayList<ActiveOrder> arActiveOrder = new ArrayList<>();


    public static OpenOrdersFragment newInstance(String param1) {
        OpenOrdersFragment fragment = new OpenOrdersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    public OpenOrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }

        mDatabaseActiveItemReference = FirebaseDatabase.getInstance().getReference(Constants.ACTIVE_ITEM);
        activeItemListener();

        mDatabaseActiveItemReference.keepSynced(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_open_orders, container,false);

        setupRecyclerView(view);

        return view;
    }


    private void setupRecyclerView(View view) {
        //arActiveModel.add(new ActiveItemModel("Название", "", "", 300f, 1));
        openOrderRecyclerAdapter = new OpenOrderRecyclerAdapter(arActiveOrder);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));
        recyclerView.setAdapter(openOrderRecyclerAdapter);

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                pos = position;
                new MaterialDialog.Builder(getContext())
                        .items(R.array.dialog_open_orders_items)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                Intent intent;
                                switch (which) {
                                    case 0:
                                        intent = new Intent(getContext(), PayActivity.class);
                                        intent.putExtra("id", arActiveOrder.get(pos).getId());
                                        startActivity(intent);
                                        break;
                                    case 1:
                                        intent = new Intent(getContext(), OrderActivity.class);
                                        intent.putExtra("id", arActiveOrder.get(pos).getId());
                                        startActivity(intent);
                                        break;
                                    case 2:
                                        //Ставим статус заказу - Активный
                                        arActiveOrder.get(pos).setActive(true);
                                        mDatabaseActiveItemReference.child(arActiveOrder.get(pos).getId()).setValue(arActiveOrder.get(pos));
                                        break;
                                    case 3:
                                        //Ставим статус заказу - ожидает оплаты
                                        arActiveOrder.get(pos).setActive(false);
                                        mDatabaseActiveItemReference.child(arActiveOrder.get(pos).getId()).setValue(arActiveOrder.get(pos));
                                        break;
                                }
                            }
                        })
                        .show();
            }
        });

    }

    private void deleteItem(int position) {
        arActiveModel.remove(position);
        mDatabaseActiveItemReference.child("activeItem").setValue(arActiveModel);
    }


    private void activeItemListener() {
        mDatabaseActiveItemReference = FirebaseDatabase.getInstance().getReference(Constants.ACTIVE_ITEM);
        Query myOrdersTimeQuery = null;
        myOrdersTimeQuery = mDatabaseActiveItemReference.orderByChild("unixTime");
        myOrdersTimeQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.d("Ope", "Child count" + String.valueOf(dataSnapshot.getChildrenCount()));
                updateRecyclerView(dataSnapshot);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void updateRecyclerView(DataSnapshot dataSnapshot) {

        arActiveOrder.clear();
        for(DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
            activeOrder = postSnapshot.getValue(ActiveOrder.class);
            arActiveOrder.add(activeOrder);
        }

        openOrderRecyclerAdapter.notifyDataSetChanged();
    }

    private void closeBill() {

        DatabaseReference closeBillReference = FirebaseDatabase.getInstance().getReference("closeBills");
        long unixTime = System.currentTimeMillis() / 1000L;
        closeBillReference.child(String.valueOf(unixTime)).setValue(activeOrder);
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
