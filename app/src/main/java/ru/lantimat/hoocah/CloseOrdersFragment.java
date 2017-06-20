package ru.lantimat.hoocah;

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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ru.lantimat.hoocah.Utils.Constants;
import ru.lantimat.hoocah.Utils.ItemClickSupport;
import ru.lantimat.hoocah.adapters.CloseOrderRecyclerAdapter;
import ru.lantimat.hoocah.models.ActiveItemModel;
import ru.lantimat.hoocah.models.CloseOrder;


public class CloseOrdersFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    private String mParam1;

    RecyclerView recyclerView;
    CloseOrderRecyclerAdapter closeOrderRecyclerAdapter;
    ArrayList<ActiveItemModel> arActiveModel = new ArrayList<>();
    private DatabaseReference mDatabaseCloseOrderReference;
    CloseOrder closeOrder; //Активный счет
    ArrayList<CloseOrder> arCloseOrder = new ArrayList<>();


    public static CloseOrdersFragment newInstance(String param1) {
        CloseOrdersFragment fragment = new CloseOrdersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    public CloseOrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }

        mDatabaseCloseOrderReference = FirebaseDatabase.getInstance().getReference(Constants.CLOSE_ORDER);
        closeOrderListener();
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
        closeOrderRecyclerAdapter = new CloseOrderRecyclerAdapter(arCloseOrder);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));
        recyclerView.setAdapter(closeOrderRecyclerAdapter);

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, final int position, View v) {
                new MaterialDialog.Builder(getContext())
                        .items(R.array.dialog_open_orders_items)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                Intent intent;
                                switch (which) {
                                    case 0:
                                        intent = new Intent(getContext(), PayActivity.class);
                                        intent.putExtra("id", arCloseOrder.get(position).getTableId());
                                        startActivity(intent);
                                        break;
                                    case 1:
                                        intent = new Intent(getContext(), OrderActivity.class);
                                        intent.putExtra("id", arCloseOrder.get(position).getTableId());
                                        startActivity(intent);
                                        break;
                                }
                            }
                        })
                        .show();
            }
        });

    }



    private void closeOrderListener() {
        mDatabaseCloseOrderReference = FirebaseDatabase.getInstance().getReference(Constants.CLOSE_ORDER);
        mDatabaseCloseOrderReference.addValueEventListener(new ValueEventListener() {
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

        arCloseOrder.clear();
        for(DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
            closeOrder = postSnapshot.getValue(CloseOrder.class);
            arCloseOrder.add(closeOrder);
        }

        closeOrderRecyclerAdapter.notifyDataSetChanged();
    }

    private void closeBill() {

        DatabaseReference closeBillReference = FirebaseDatabase.getInstance().getReference("closeBills");
        long unixTime = System.currentTimeMillis() / 1000L;
        closeBillReference.child(String.valueOf(unixTime)).setValue(closeOrder);
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
