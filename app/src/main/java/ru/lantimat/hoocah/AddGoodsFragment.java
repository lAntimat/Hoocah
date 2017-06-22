package ru.lantimat.hoocah;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import ru.lantimat.hoocah.models.ItemModel;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddGoodsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddGoodsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddGoodsFragment extends Fragment implements OnBackPressedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = "AddGoodsFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;

    private OnFragmentInteractionListener mListener;
    int level = 0;
    int goodsPosition = -1;
    int itemsPosition = -1;
    int tastePosition = -1;

    long goodsCount;
    int itemsCount;
    float activeItemPrice = 0;
    GoodsRecyclerAdapter goodsRecyclerAdapter;
    ItemsRecyclerAdapter itemsRecyclerAdapter;
    RecyclerView recyclerView;
    ArrayList<GoodsModel> arGoods;
    ArrayList<ItemModel> arItems = new ArrayList<>();
    String pushKey;

    ActiveOrder activeOrder; //Модель активного заказа
    ArrayList<ActiveItemModel> arActiveItem = new ArrayList<>();

    private DatabaseReference mDatabaseGoodsReference;
    private DatabaseReference mDatabaseActiveItemReference;
    private DatabaseReference mDatabaseTablesReference;
    private DatabaseReference mDatabaseReference;

    long unixTime = System.currentTimeMillis() / 1000L; //Время открытия счета
    long orderId = -1;

    public AddGoodsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AddGoodsFragment newInstance(String param1) {
        AddGoodsFragment fragment = new AddGoodsFragment();
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
        mDatabaseGoodsReference = FirebaseDatabase.getInstance().getReference();
        mDatabaseActiveItemReference = FirebaseDatabase.getInstance().getReference(Constants.ACTIVE_ITEM);
        mDatabaseTablesReference = FirebaseDatabase.getInstance().getReference(Constants.TABLES);

        ArrayList<ItemModel> arItems = new ArrayList<>();
        /*ArrayList<String> arTaste = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            arTaste.add("Вкус " + i);
        }*/

        /*arItems.add((new ItemModel(10001,"Адалия"," ","https://thumbs.dreamstime.com/z/hookah-flat-design-illustration-isolated-white-background-51687110.jpg" ,300f, arTaste)));
        arItems.add((new ItemModel(10002,"Альфакир"," ","https://thumbs.dreamstime.com/z/hookah-flat-design-illustration-isolated-white-background-51687110.jpg" ,350f, arTaste)));
        arItems.add((new ItemModel(10003,"Нахла"," ","https://thumbs.dreamstime.com/z/hookah-flat-design-illustration-isolated-white-background-51687110.jpg" ,400f, arTaste)));
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
        mDatabaseGoodsReference.child("3").setValue(goodsModel2);*/

        goodsListener();
        activeItemListener();
        closeOrdersListener();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_add_goods, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(goodsPosition==-1) showAddGoodsDialog();
                else if(goodsPosition!=-1) showAddItemsDialog();
            }
        });

        arGoods = new ArrayList<>();
        setupGoodsRecyclerView();

        return view;
    }

    public void showAddGoodsDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = this.getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_add_goods, null);
        dialogBuilder.setView(dialogView);

        final EditText edName = (EditText) dialogView.findViewById(R.id.edName);
        final EditText edUrl = (EditText) dialogView.findViewById(R.id.edUrl);

        dialogBuilder.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                GoodsModel goodsModel = new GoodsModel(edName.getText().toString(), edUrl.getText().toString(), null);
                mDatabaseGoodsReference.child(Constants.GOODS_MODEL).child(String.valueOf(goodsCount)).setValue(goodsModel);
            }
        });
        dialogBuilder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }
    public void showChangeGoodsDialog(final int position) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = this.getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_add_goods, null);
        dialogBuilder.setView(dialogView);

        final EditText edName = (EditText) dialogView.findViewById(R.id.edName);
        final EditText edUrl = (EditText) dialogView.findViewById(R.id.edUrl);

        edName.setText(arGoods.get(position).getName());
        edUrl.setText(arGoods.get(position).getImgUrl());

        dialogBuilder.setPositiveButton("Изменить", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                GoodsModel goodsModel = arGoods.get(position);
                goodsModel.setName(edName.getText().toString());
                goodsModel.setImgUrl(edUrl.getText().toString());
                mDatabaseGoodsReference.child(Constants.GOODS_MODEL).child(String.valueOf(position)).setValue(goodsModel);
            }
        });
        dialogBuilder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }
    public void showAddItemsDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = this.getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_add_items, null);
        dialogBuilder.setView(dialogView);

        final EditText edId = (EditText) dialogView.findViewById(R.id.edId);
        final EditText edName = (EditText) dialogView.findViewById(R.id.edName);
        final EditText edDescription = (EditText) dialogView.findViewById(R.id.edDescription);
        final EditText edUrl = (EditText) dialogView.findViewById(R.id.edUrl);
        final EditText edPrice = (EditText) dialogView.findViewById(R.id.edPrice);

        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                GoodsModel goodsModel = arGoods.get(goodsPosition);
                ItemModel itemModel = new ItemModel(Integer.parseInt(edId.getText().toString()), edName.getText().toString(),edDescription.getText().toString(), edUrl.getText().toString(), Integer.parseInt(edPrice.getText().toString()), null);
                /*ArrayList<ItemModel> arItems1 = new ArrayList<>();
                if(goodsModel.getItemModels()!=null) {
                    arItems1 = goodsModel.getItemModels();
                    arItems1.add(itemModel);
                } else arItems1.add(itemModel);*/
                arItems.add(itemModel);
                mDatabaseGoodsReference.child(Constants.GOODS_MODEL).child(String.valueOf(goodsPosition)).child(Constants.ITEMS_MODEL).setValue(arItems);

            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }
    public void showChangeItemsDialog(final int position) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = this.getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_add_items, null);
        dialogBuilder.setView(dialogView);

        final EditText edId = (EditText) dialogView.findViewById(R.id.edId);
        final EditText edName = (EditText) dialogView.findViewById(R.id.edName);
        final EditText edDescription = (EditText) dialogView.findViewById(R.id.edDescription);
        final EditText edUrl = (EditText) dialogView.findViewById(R.id.edUrl);
        final EditText edPrice = (EditText) dialogView.findViewById(R.id.edPrice);

        edId.setText(String.valueOf(arGoods.get(goodsPosition).getItemModels().get(position).getId()));
        edName.setText(arGoods.get(goodsPosition).getItemModels().get(position).getName());
        edDescription.setText(arGoods.get(goodsPosition).getItemModels().get(position).getDesription());
        edUrl.setText(arGoods.get(goodsPosition).getItemModels().get(position).getImgUrl());
        edPrice.setText(String.valueOf(arGoods.get(goodsPosition).getItemModels().get(position).getPrice()));


        dialogBuilder.setPositiveButton("Изменить", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                GoodsModel goodsModel = arGoods.get(goodsPosition);
                ItemModel itemModel = new ItemModel(Integer.parseInt(edId.getText().toString()), edName.getText().toString(),edDescription.getText().toString(), edUrl.getText().toString(), Integer.parseInt(edPrice.getText().toString()), null);
                /*ArrayList<ItemModel> arItems = new ArrayList<>();
                if(goodsModel.getItemModels()!=null) {
                    arItems = goodsModel.getItemModels();
                    arItems.set(position, itemModel);
                } else arItems.set(position, itemModel);*/
                arItems.set(position, itemModel);
                mDatabaseGoodsReference.child(Constants.GOODS_MODEL).child(String.valueOf(goodsPosition)).child(Constants.ITEMS_MODEL).setValue(arItems);
            }
        });
        dialogBuilder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }


    private void goodsListener() {
        mDatabaseGoodsReference = FirebaseDatabase.getInstance().getReference();
        mDatabaseGoodsReference.child(Constants.GOODS_MODEL).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                arGoods.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    goodsCount = dataSnapshot.getChildrenCount();
                    GoodsModel categoryModel = postSnapshot.getValue(GoodsModel.class);
                    //Toast.makeText(getContext(), categoryModel.getName(), Toast.LENGTH_SHORT).show();
                    arGoods.add(categoryModel);
                }
                if(goodsPosition!=-1) {
                    arItems.clear();
                    arItems.addAll(arGoods.get(goodsPosition).getItemModels());
                }

                if(goodsRecyclerAdapter!=null) goodsRecyclerAdapter.notifyDataSetChanged();
                if(itemsRecyclerAdapter!=null) itemsRecyclerAdapter.notifyDataSetChanged();
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
        mDatabaseReference= FirebaseDatabase.getInstance().getReference(Constants.CLOSE_ORDER);
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
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
        goodsRecyclerAdapter = new GoodsRecyclerAdapter(arGoods);
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

        ItemClickSupport.addTo(recyclerView).setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(RecyclerView recyclerView, final int position, View v) {
                new MaterialDialog.Builder(getContext())
                        .items(R.array.dialog_add_goods_items)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int position1, CharSequence text) {
                                switch (position1) {
                                    case 0:
                                        showChangeGoodsDialog(position);
                                        break;
                                    case 1:
                                        arGoods.remove(position);
                                        mDatabaseGoodsReference.child(Constants.GOODS_MODEL).setValue(arGoods);
                                        if(goodsRecyclerAdapter!=null) goodsRecyclerAdapter.notifyDataSetChanged();
                                        break;
                                }
                            }
                        })
                        .positiveText("Закрыть")
                        .show();
                return true;
            }
        });

    }

    private void setupItemsRecyclerView(final int position) {   //Этот метод отображает первый уровень товаров
        level = 1;
        arItems.clear();
        if(arGoods.get(position).getItemModels()!=null) arItems.addAll(arGoods.get(position).getItemModels());
        itemsRecyclerAdapter = new ItemsRecyclerAdapter(arItems);
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
                                                .content(arGoods.get(goodsPosition).getItemModels().get(position1).getDesription())
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
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(itemsRecyclerAdapter);


        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                //Toast.makeText(v.getContext(), "position = " + position, Toast.LENGTH_SHORT).show();
                itemsPosition = position;
                new MaterialDialog.Builder(getContext())
                        .items(R.array.dialog_add_goods_items)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int position1, CharSequence text) {
                                switch (position1) {
                                    case 0:
                                        showChangeItemsDialog(itemsPosition);
                                        break;
                                    case 1:
                                        arItems.remove(itemsPosition);
                                        mDatabaseGoodsReference.child(Constants.GOODS_MODEL).child(String.valueOf(goodsPosition)).child(Constants.ITEMS_MODEL).setValue(arItems);
                                        break;
                                }
                            }
                        })
                        .positiveText("Закрыть")
                        .show();
            }
        });

        ItemClickSupport.addTo(recyclerView).setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(RecyclerView recyclerView, final int position, View v) {

                return true;
            }
        });


    }

    private void setupTasteRecyclerView(int position) {
        level = 2;
        TasteRecyclerAdapter tasteRecyclerAdapter = new TasteRecyclerAdapter(arGoods.get(goodsPosition).getItemModels().get(itemsPosition).getTaste());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(tasteRecyclerAdapter);

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                tastePosition = position;

            }
        });
    }

    private void addActiveItemToFireBase() {
        int id = arGoods.get(goodsPosition).getItemModels().get(itemsPosition).getId();
        String name = arGoods.get(goodsPosition).getItemModels().get(itemsPosition).getName();
        String description = arGoods.get(goodsPosition).getItemModels().get(itemsPosition).getDesription();
        String taste = null;
        if(tastePosition!=-1) taste = arGoods.get(goodsPosition).getItemModels().get(itemsPosition).getTaste().get(tastePosition);
        int price = arGoods.get(goodsPosition).getItemModels().get(itemsPosition).getPrice();
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
                if (dataSnapshot.child(mParam1).exists()) {

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
                getActivity().finish();
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
