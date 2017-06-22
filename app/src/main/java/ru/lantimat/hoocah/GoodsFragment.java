package ru.lantimat.hoocah;

import android.content.Context;
import android.content.Intent;
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

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ru.lantimat.hoocah.Utils.Constants;
import ru.lantimat.hoocah.Utils.ItemClickSupport;
import ru.lantimat.hoocah.Utils.TabletOrPhone;
import ru.lantimat.hoocah.adapters.GoodsRecyclerAdapter;
import ru.lantimat.hoocah.adapters.ItemsRecyclerAdapter;
import ru.lantimat.hoocah.adapters.TasteRecyclerAdapter;
import ru.lantimat.hoocah.models.ActiveItemModel;
import ru.lantimat.hoocah.models.ActiveOrder;
import ru.lantimat.hoocah.models.GoodsModel;
import ru.lantimat.hoocah.models.Item;
import ru.lantimat.hoocah.models.ItemModel;
import ru.lantimat.hoocah.models.TableModel;

import static android.content.ContentValues.TAG;



public class GoodsFragment extends Fragment implements OnBackPressedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;

    int level = 0;
    int goodsPosition = -1;
    int itemsPosition = -1;
    int tastePosition = -1;
    float activeItemPrice = 0;
    GoodsRecyclerAdapter goodsRecyclerAdapter;
    ItemsRecyclerAdapter itemsRecyclerAdapter;
    RecyclerView recyclerView;
    ArrayList<GoodsModel> arrayList;
    String pushKey;

    ActiveOrder activeOrder; //Модель активного заказа
    ArrayList<ActiveItemModel> arActiveItem = new ArrayList<>();

    private DatabaseReference mDatabaseGoodsReference;
    private DatabaseReference mDatabaseActiveItemReference;
    private DatabaseReference mDatabaseTablesReference;
    private DatabaseReference mDatabaseReference;

    long unixTime = System.currentTimeMillis() / 1000L; //Время открытия счета
    long orderId = -1;
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
        mDatabaseGoodsReference = FirebaseDatabase.getInstance().getReference(Constants.GOODS_MODEL);
        mDatabaseActiveItemReference = FirebaseDatabase.getInstance().getReference(Constants.ACTIVE_ITEM);
        mDatabaseTablesReference = FirebaseDatabase.getInstance().getReference(Constants.TABLES);



        ArrayList<ItemModel> arItems = new ArrayList<>();
        /*ArrayList<String> arTaste = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            arTaste.add("Вкус " + i);
        }*/

        /*arItems.add((new ItemModel(10001,"Адалия"," ","https://thumbs.dreamstime.com/z/hookah-flat-design-illustration-isolated-white-background-51687110.jpg" ,300f, null)));
        arItems.add((new ItemModel(10002,"Альфакир"," ","https://thumbs.dreamstime.com/z/hookah-flat-design-illustration-isolated-white-background-51687110.jpg" ,350f, null)));
        arItems.add((new ItemModel(10003,"Нахла"," ","https://thumbs.dreamstime.com/z/hookah-flat-design-illustration-isolated-white-background-51687110.jpg" ,400f, null)));
        GoodsModel goodsModel = new GoodsModel("Кальяны", "https://thumbs.dreamstime.com/z/hookah-flat-design-illustration-isolated-white-background-51687110.jpg", arItems);
        mDatabaseGoodsReference.child("0").setValue(goodsModel);

        arItems = new ArrayList<>();
        arItems.add((new ItemModel(20001,"Чай малиновый"," ","http://rustovperm.ru/upload_modules/goods/dir/full/541de1ac61bbc422a301b4883096e78a.jpg" ,100f, null)));
        arItems.add((new ItemModel(20002,"Чай черный"," ","http://rustovperm.ru/upload_modules/goods/dir/full/541de1ac61bbc422a301b4883096e78a.jpg" ,100f, null)));
        arItems.add((new ItemModel(20003,"Coca-cola 0.5"," ","http://www.coca-cola.co.uk/content/dam/journey/gb/en/hidden/Products/lead-brand-image/Journey-brands-Product-Coca-Cola-Classic.jpg" ,50f, null)));
        GoodsModel goodsModel1 = new GoodsModel("Напитки", "http://icon-icons.com/icons2/588/PNG/512/bottle_wine_alcohol_drink_empty_icon-icons.com_55349.png",arItems);
        mDatabaseGoodsReference.child("1").setValue(goodsModel1);

        arItems = new ArrayList<>();
        arItems.add((new ItemModel(20001,"Пицца цезарь", "450 г \n Начинка (куриное филе жареное (филе грудки цыпленка","https://www.cafemumu.ru/upload/iblock/093/0937070a565ad7a051fa943e3ca3fc8a.png" ,210f, null)));
        arItems.add((new ItemModel(20002,"Пицца Пепперони","500г","https://www.cafemumu.ru/upload/iblock/f0a/f0ac5dc84655ee7757ec5a3b0a50973e.png" ,250f, null)));
        arItems.add((new ItemModel(20003,"Пицца Мясная","650г","https://www.cafemumu.ru/upload/iblock/049/0498a8e75c122e39bd1dd03782df603a.png" ,200f, null)));
        GoodsModel goodsModel2 = new GoodsModel("Пицца", "https://thumbs.dreamstime.com/z/pizza-flat-design-sign-icon-long-shadow-vector-70753649.jpg",arItems);
        mDatabaseGoodsReference.child("2").setValue(goodsModel2);*/

        goodsListener();
        activeItemListener();
        closeOrdersListener();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_goods, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        arrayList = new ArrayList<>();
        setupGoodsRecyclerView();

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

    private void closeOrdersListener() {
        mDatabaseReference= FirebaseDatabase.getInstance().getReference();
        mDatabaseReference.child(Constants.CLOSE_ORDER).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orderId = dataSnapshot.getChildrenCount()+1;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
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
                //Toast.makeText(v.getContext(), "position = " + position, Toast.LENGTH_SHORT).show();
                goodsPosition = position;
                setupItemsRecyclerView(position);
            }
        });

    }

    private void setupItemsRecyclerView(final int position) {   //Этот метод отображает первый уровень товаров
        level = 1;
        itemsRecyclerAdapter = new ItemsRecyclerAdapter(arrayList.get(position).getItemModels());
        itemsRecyclerAdapter.setButtonClickListener(new ItemsRecyclerAdapter.MyAdapterListener() {
            @Override
            public void btnDotsOnClick(View v, final int position1) {
                new MaterialDialog.Builder(getContext())
                        .items(R.array.dialog_goods_items)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                switch (which) {
                                    case 0:
                                        new MaterialDialog.Builder(getContext())
                                                .title("Описание")
                                                .content(arrayList.get(goodsPosition).getItemModels().get(position1).getDesription())
                                                .positiveText("Закрыть")
                                                .show();
                                        break;
                                }
                            }
                        })
                        .show();
            }
        });
        //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        /*TabletOrPhone tabletOrPhone = new TabletOrPhone(getActivity()); //Узнаем телефон это или планшет
        if(tabletOrPhone.isPhone()) recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));
        else if(tabletOrPhone.isTablet()) recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));*/
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(itemsRecyclerAdapter);


        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                //Toast.makeText(v.getContext(), "position = " + position, Toast.LENGTH_SHORT).show();
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
        int id = arrayList.get(goodsPosition).getItemModels().get(itemsPosition).getId();
        String name = arrayList.get(goodsPosition).getItemModels().get(itemsPosition).getName();
        String description = arrayList.get(goodsPosition).getItemModels().get(itemsPosition).getDesription();
        String taste = null;
        if(tastePosition!=-1) taste = arrayList.get(goodsPosition).getItemModels().get(itemsPosition).getTaste().get(tastePosition);
        int price = arrayList.get(goodsPosition).getItemModels().get(itemsPosition).getPrice();
        ActiveItemModel activeItemModel = new ActiveItemModel(id, name, description, taste, price);
        arActiveItem.add(activeItemModel);
        activeItemPrice += activeItemModel.getPrice();
        //closeOrder = new ActiveOrder(mParam1, unixTime, true, activeItemPrice, arActiveItem);
        mDatabaseActiveItemReference.child(mParam1).child("arActiveItemModel").setValue(arActiveItem);
        itemsPosition = -1;
        tastePosition =-1;
        setupGoodsRecyclerView();
    }

    private void activeItemListener() {
        mDatabaseActiveItemReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(mParam1).exists()) {
                    ActiveOrder activeOrder = dataSnapshot.child(mParam1).getValue(ActiveOrder.class);
                    if (activeOrder != null) {
                        if(activeOrder.getId()==null) {
                            activeOrder = new ActiveOrder(mParam1, unixTime, true, activeItemPrice, arActiveItem); //Заказ открыт
                            mDatabaseActiveItemReference.child(mParam1).setValue(activeOrder);
                            mDatabaseTablesReference.child(mParam1).setValue(new TableModel(Integer.parseInt(mParam1), Integer.parseInt(activeOrder.getId()), false));
                        }
                        if (activeOrder.getArActiveItemModel() != null)
                            arActiveItem = activeOrder.getArActiveItemModel();
                        else arActiveItem.clear();
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
                setupItemsRecyclerView(goodsPosition);
                break;
        }
    }

}
