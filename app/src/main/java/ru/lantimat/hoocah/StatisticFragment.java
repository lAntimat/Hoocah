package ru.lantimat.hoocah;

import android.content.Context;
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
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import ru.lantimat.hoocah.Utils.Constants;
import ru.lantimat.hoocah.Utils.ItemClickSupport;
import ru.lantimat.hoocah.adapters.GoodsRecyclerAdapter;
import ru.lantimat.hoocah.adapters.ItemsRecyclerAdapter;
import ru.lantimat.hoocah.adapters.TasteRecyclerAdapter;
import ru.lantimat.hoocah.models.ActiveItemModel;
import ru.lantimat.hoocah.models.ActiveOrder;
import ru.lantimat.hoocah.models.CloseOrder;
import ru.lantimat.hoocah.models.GoodsModel;
import ru.lantimat.hoocah.models.ItemModel;
import ru.lantimat.hoocah.models.TableModel;

import static android.content.ContentValues.TAG;


public class StatisticFragment extends Fragment implements OnBackPressedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String TAG = "StatisticFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;

    GoodsRecyclerAdapter goodsRecyclerAdapter;
    RecyclerView recyclerView;
    ArrayList<GoodsModel> arrayList;

    TextView tvDay, tvWeek;


    private DatabaseReference mDatabaseReference;

    float sum = 0;
    public StatisticFragment() {
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
    public static StatisticFragment newInstance(String param1) {
        StatisticFragment fragment = new StatisticFragment();
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
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        refListener();


        getProfit();
        getProfitWeek();

    }

    private void getProfit() {
        Query myTopPostsQuery = null;
        try {
            myTopPostsQuery = mDatabaseReference.child(Constants.CLOSE_ORDER)
                        .orderByChild("unixTimeClose").startAt(getStartOfDayInMillis("2017-06-23"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        sum = 0;
        myTopPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    sum += postSnapshot.getValue(CloseOrder.class).getTotalPrice();
                    Log.d(TAG, "хмхм" + sum);
                }
                Log.d(TAG, "Выручка" + sum);
                tvDay.setText("Выручка за день " + sum);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void getProfitWeek() {
        Query myTopPostsQuery = null;
        try {
            myTopPostsQuery = mDatabaseReference.child(Constants.CLOSE_ORDER)
                        .orderByChild("unixTimeClose").startAt(getStartOfDayInMillis("2017-06-16"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        sum = 0;
        myTopPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    sum += postSnapshot.getValue(CloseOrder.class).getTotalPrice();
                }
                tvWeek.setText("Выручка за неделю " + sum);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_statistic, container, false);
        tvDay = (TextView) view.findViewById(R.id.tvDay);
        tvWeek = (TextView) view.findViewById(R.id.tvWeek);


        return view;
    }


    /**
     * @param date the date in the format "yyyy-MM-dd"
     */
    public long getStartOfDayInMillis(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(format.parse(date));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Log.d(TAG, "Начало дня" + calendar.getTimeInMillis());
        return calendar.getTimeInMillis()/1000L;
    }

    /**
     * @param date the date in the format "yyyy-MM-dd"
     */
    public long getEndOfDayInMillis(String date) throws ParseException {
        // Add one day's time to the beginning of the day.
        // 24 hours * 60 minutes * 60 seconds * 1000 milliseconds = 1 day
        return getStartOfDayInMillis(date) + (24 * 60 * 60 * 1000);
    }


    private void refListener() {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mDatabaseReference.child("goodsModel").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                ArrayList<CloseOrder> ar = new ArrayList<CloseOrder>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    CloseOrder closeOrder = postSnapshot.getValue(CloseOrder.class);
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


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private void setupGoodsRecyclerView() {  //Этот метод отображает нулевой уровень товаров
        goodsRecyclerAdapter = new GoodsRecyclerAdapter(arrayList);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(goodsRecyclerAdapter);


        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                //Toast.makeText(v.getContext(), "position = " + position, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        //Toast.makeText(getContext(), "BackPressed", Toast.LENGTH_SHORT).show();
    }

}
