package ru.lantimat.hoocah;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ru.lantimat.hoocah.Utils.ItemClickSupport;
import ru.lantimat.hoocah.adapters.GoodsRecyclerAdapter;
import ru.lantimat.hoocah.adapters.ItemsRecyclerAdapter;
import ru.lantimat.hoocah.adapters.TasteRecyclerAdapter;
import ru.lantimat.hoocah.models.ActiveItemModel;
import ru.lantimat.hoocah.models.ActiveOrder;
import ru.lantimat.hoocah.models.GoodsModel;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GoodsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GoodsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GoodsFragment extends Fragment implements OnBackPressedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;

    private OnFragmentInteractionListener mListener;
    int level = 0;
    int goodsPosition = -1;
    int itemsPosition = -1;
    int tastePosition = -1;
    float activeItemPrice = 0;
    GoodsRecyclerAdapter goodsRecyclerAdapter;
    ItemsRecyclerAdapter itemsRecyclerAdapter;
    RecyclerView recyclerView;
    ArrayList<GoodsModel> arrayList;

    ActiveOrder activeOrder; //Модель активного заказа
    ArrayList<ActiveItemModel> activeItemModelArrayList = new ArrayList<>();

    private DatabaseReference mDatabaseGoodsReference;
    private DatabaseReference mDatabaseActiveItemReference;

    long unixTime = System.currentTimeMillis() / 1000L; //Время открытия счета
    public GoodsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment GoodsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GoodsFragment newInstance(String param1) {
        GoodsFragment fragment = new GoodsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
        // Write a message to the database
        mDatabaseGoodsReference = FirebaseDatabase.getInstance().getReference("goodsModel");
        mDatabaseActiveItemReference = FirebaseDatabase.getInstance().getReference("activeItem");
        activeItemListener();


        /*ArrayList<ItemModel> arItems = new ArrayList<>();
        ArrayList<String> arTaste = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            arTaste.add("Вкус " + i);
        }
        arItems.add((new ItemModel("Адалия","Табак из Турции","https://thumbs.dreamstime.com/z/hookah-flat-design-illustration-isolated-white-background-51687110.jpg" ,300f, arTaste)));
        arItems.add((new ItemModel("Альфакир","Табак из Турции","https://thumbs.dreamstime.com/z/hookah-flat-design-illustration-isolated-white-background-51687110.jpg" ,350f, arTaste)));
        arItems.add((new ItemModel("Нахла","Табак из Турции","https://thumbs.dreamstime.com/z/hookah-flat-design-illustration-isolated-white-background-51687110.jpg" ,400f, arTaste)));
        GoodsModel goodsModel = new GoodsModel("Кальяны", "https://thumbs.dreamstime.com/z/hookah-flat-design-illustration-isolated-white-background-51687110.jpg", arItems);
        mDatabaseGoodsReference.child("1").setValue(goodsModel);
        GoodsModel goodsModel1 = new GoodsModel("Напитки", "http://icon-icons.com/icons2/588/PNG/512/bottle_wine_alcohol_drink_empty_icon-icons.com_55349.png",arItems);
        mDatabaseGoodsReference.child("2").setValue(goodsModel1);
        GoodsModel goodsModel2 = new GoodsModel("Пицца", "https://thumbs.dreamstime.com/z/pizza-flat-design-sign-icon-long-shadow-vector-70753649.jpg",arItems);
        mDatabaseGoodsReference.child("3").setValue(goodsModel2);*/


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_goods, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        arrayList = new ArrayList<>();
        setupGoodsRecyclerView();

        goodsListener();


        return view;
    }

    private void goodsListener() {
        mDatabaseGoodsReference = FirebaseDatabase.getInstance().getReference();
        mDatabaseGoodsReference.child("goodsModel").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    GoodsModel categoryModel = postSnapshot.getValue(GoodsModel.class);
                    //Toast.makeText(getContext(), categoryModel.getName(), Toast.LENGTH_SHORT).show();
                    arrayList.add(categoryModel);
                    goodsRecyclerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    private void setupGoodsRecyclerView() {  //Этот метод отображает нулевой уровень товаров
        level = 0;
        goodsRecyclerAdapter = new GoodsRecyclerAdapter(arrayList);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(goodsRecyclerAdapter);


        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Toast.makeText(v.getContext(), "position = " + position, Toast.LENGTH_SHORT).show();
                goodsPosition = position;
                setupItemsRecyclerView(position);
            }
        });

    }

    private void setupItemsRecyclerView(final int position) {   //Этот метод отображает первый уровень товаров
        level = 1;
        itemsRecyclerAdapter = new ItemsRecyclerAdapter(arrayList.get(position).getItemModels());
        //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(itemsRecyclerAdapter);

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Toast.makeText(v.getContext(), "position = " + position, Toast.LENGTH_SHORT).show();
                itemsPosition = position;
                if(arrayList.get(goodsPosition).getItemModels().get(itemsPosition).getTaste()!=null) setupTasteRecyclerView(itemsPosition);
                else addActiveItemToFireBase();
            }
        });
    }

    private void setupTasteRecyclerView(int position) {
        level = 2;
        TasteRecyclerAdapter tasteRecyclerAdapter = new TasteRecyclerAdapter(arrayList.get(goodsPosition).getItemModels().get(itemsPosition).getTaste());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(tasteRecyclerAdapter);

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                tastePosition = position;
                addActiveItemToFireBase();

            }
        });
    }

    private void addActiveItemToFireBase() {
        String name = arrayList.get(goodsPosition).getItemModels().get(itemsPosition).getName();
        String description = arrayList.get(goodsPosition).getItemModels().get(itemsPosition).getDesription();
        String taste = null;
        if(tastePosition!=-1) taste = arrayList.get(goodsPosition).getItemModels().get(itemsPosition).getTaste().get(tastePosition);
        float price = arrayList.get(goodsPosition).getItemModels().get(itemsPosition).getPrice();
        ActiveItemModel activeItemModel = new ActiveItemModel(name, description, taste, price, 1);
        activeItemModelArrayList.add(activeItemModel);
        activeItemPrice += activeItemModel.getPrice();
        activeOrder = new ActiveOrder(mParam1, unixTime, true, activeItemPrice, activeItemModelArrayList);
        mDatabaseActiveItemReference.child(mParam1).setValue(activeOrder);
    }

    private void activeItemListener() {
        mDatabaseActiveItemReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(mParam1).exists()) {
                    ActiveOrder activeOrder = dataSnapshot.child(mParam1).getValue(ActiveOrder.class);
                    if (activeOrder != null) {
                        activeItemPrice = activeOrder.getTotalPrice();
                        unixTime = activeOrder.getUnixTime();
                        activeItemModelArrayList = activeOrder.getArActiveItemModel();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        //Toast.makeText(getContext(), "BackPressed", Toast.LENGTH_SHORT).show();
        switch (level) {
            case 0:

                break;
            case 1:
                itemsPosition = -1;
                setupGoodsRecyclerView();
                break;
            case 2:
                tastePosition =-1;
                setupItemsRecyclerView(itemsPosition);
                break;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
