package ru.lantimat.hoocah;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ru.lantimat.hoocah.Utils.ItemClickSupport;
import ru.lantimat.hoocah.adapters.ActiveItemRecyclerAdapter;
import ru.lantimat.hoocah.models.ActiveItemModel;
import ru.lantimat.hoocah.models.ActiveOrder;


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
    ActiveOrder activeOrder; //Активный счет

    Button btnCloseBill;

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
        tvTotalPrice = (TextView) view.findViewById(R.id.tvTable);
        btnCloseBill = (Button) view.findViewById(R.id.btnCloseBill);

        btnCloseClickListener();
        setupRecyclerView(view);
        return view;
    }

    private void btnCloseClickListener() {
        btnCloseBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    private void setupRecyclerView(View view) {
        //arActiveModel.add(new ActiveItemModel("Название", "", "", 300f, 1));
        activeItemRecyclerAdapter = new ActiveItemRecyclerAdapter(arActiveModel);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(activeItemRecyclerAdapter);

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                deleteItem(position);
            }
        });

    }

    private void deleteItem(int position) {
        arActiveModel.remove(position);
        mDatabaseActiveItemReference.child(mParam1).child("arActiveItemModel").setValue(arActiveModel);
    }

    private void activeItemListener() {
        mDatabaseActiveItemReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(mParam1).exists()) {  //Если заказ для столика существует
                    activeOrder = dataSnapshot.child(mParam1).getValue(ActiveOrder.class);
                }
                updateRecyclerView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void updateRecyclerView() {
        float totalPrice = 0;
        arActiveModel.clear();
            if (activeOrder != null) {
                if(activeOrder.getArActiveItemModel()!=null) {
                    for (ActiveItemModel item : activeOrder.getArActiveItemModel()) {
                        arActiveModel.add(item);
                        totalPrice += item.getPrice(); //Считаем общую сумму заказа
                    }
                    mDatabaseActiveItemReference.child(mParam1).child("totalPrice").setValue(totalPrice);
                }
            } else totalPrice = 0;
        tvTotalPrice.setText("Общая стоимость " + totalPrice);

        activeItemRecyclerAdapter.notifyDataSetChanged();
    }

    private void closeBill() {

        DatabaseReference closeBillReference = FirebaseDatabase.getInstance().getReference("closeBills");
        long unixTime = System.currentTimeMillis() / 1000L;
        closeBillReference.child(String.valueOf(unixTime)).setValue(activeOrder);
    }

    private void addToActiveOrders() {
        mDatabaseActiveItemReference.child(mParam1).child("active").setValue(true);
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
