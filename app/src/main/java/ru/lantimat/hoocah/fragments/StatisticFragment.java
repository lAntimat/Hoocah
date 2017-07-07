package ru.lantimat.hoocah.fragments;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import ru.lantimat.hoocah.OnBackPressedListener;
import ru.lantimat.hoocah.R;
import ru.lantimat.hoocah.Utils.Constants;
import ru.lantimat.hoocah.Utils.DayAxisValueFormatter;
import ru.lantimat.hoocah.Utils.ItemClickSupport;
import ru.lantimat.hoocah.Utils.MyAxisValueFormatter;
import ru.lantimat.hoocah.Utils.XYMarkerView;
import ru.lantimat.hoocah.adapters.GoodsRecyclerAdapter;
import ru.lantimat.hoocah.models.CloseOrder;
import ru.lantimat.hoocah.models.GoodsModel;


public class StatisticFragment extends Fragment implements OnBackPressedListener, OnChartValueSelectedListener, DatePickerDialog.OnDateSetListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String TAG = "StatisticFragment";
    private static int HOURS_BEFORE_ZERO = 7*60*60; //00:00 - 7:00 = 17:00
    private static int HOURS_AFTER_ZERO = 5*60*60; //00:00 + 5:00 = 05:00

    // TODO: Rename and change types of parameters
    private String mParam1;
    Context context;
    FragmentActivity fragmentActivity;
    GoodsRecyclerAdapter goodsRecyclerAdapter;
    RecyclerView recyclerView;
    ArrayList<GoodsModel> arrayList;

    TextView tvDay, tvWeek;

    private BarChart mChart;

    private DatabaseReference mDatabaseReference;

    float profitDay = 0;
    float profitWeek = 0;
    float sumForDay = 0;
    long timeToCompare;
    long lastTimeToCompare;
    final long oneDayUnixTime = 86400;

    ArrayList<Long> arCloseTime = new ArrayList<>();
    ArrayList<Float> arTotalPrices = new ArrayList<>();
    ArrayList<Float> arProfit = new ArrayList<>();
    ArrayList<CloseOrder> arCloseOrder = new ArrayList<>();

    Calendar dateAndTime=Calendar.getInstance();
    DateTime now = DateTime.now();

    long workStartTime;

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
        DateTime now = DateTime.now();
        getProfitWeek(now.getDayOfWeek(), "Выручка за неделю");
        setHasOptionsMenu(true);
        Log.d(TAG, "День недели " + now.getDayOfWeek());

    }

    private void getProfit() {

        DateTime now = DateTime.now();
        DateTime lastWeek = new DateTime();
        DateTime dateTime = new DateTime();
        String date;
        date = lastWeek.getYear() + "-" + lastWeek.getMonthOfYear() + "-" + lastWeek.getDayOfMonth();
        Log.d(TAG, "date" + date);


        Query myTopPostsQuery = null;
        try {
            if(dateTime.getHourOfDay()>17) {
                myTopPostsQuery = mDatabaseReference.child(Constants.CLOSE_ORDER)
                        .orderByChild("unixTimeClose").startAt(getStartOfDayInMillis(date) - HOURS_BEFORE_ZERO);
            } else
            myTopPostsQuery = mDatabaseReference.child(Constants.CLOSE_ORDER)
                        .orderByChild("unixTimeClose").startAt(getStartOfDayInMillis(date) - HOURS_AFTER_ZERO);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        profitDay = 0;
        myTopPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    profitDay += postSnapshot.getValue(CloseOrder.class).getTotalPrice();
                    Log.d(TAG, "хмхм" + profitDay);
                }
                Log.d(TAG, "Выручка" + profitDay);
                tvDay.setText("Выручка за смену " + profitDay);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void getProfitWeek(final int dayCount, final String str) {

        arCloseOrder.clear();
        arProfit.clear();
        now = DateTime.now();
        DateTime dateTime = new DateTime();

        DateTime lastWeek = new DateTime().minusDays(dayCount);
        String date;
        date = lastWeek.getYear() + "-" + lastWeek.getMonthOfYear() + "-" + lastWeek.getDayOfMonth();
        Log.d(TAG, "date" + date);
        try {
            workStartTime = getStartOfDayInMillis(date) + HOURS_AFTER_ZERO;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Query myTopPostsQuery = null;
                myTopPostsQuery = mDatabaseReference.child(Constants.CLOSE_ORDER)
                        .orderByChild("unixTimeClose").startAt(workStartTime);

        profitWeek = 0;
        sumForDay = 0;
            timeToCompare = workStartTime;

        myTopPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    profitWeek+= postSnapshot.getValue(CloseOrder.class).getTotalPrice();
                    arCloseOrder.add(postSnapshot.getValue(CloseOrder.class));
                    //Log.d(TAG, "Время закрытия" + closeTime);
                }
                tvWeek.setText(str + " " + profitWeek);

                for(int i = 0; i <= dayCount; i++) {
                    arProfit.add((float) getPriceForDayFromArray(arCloseOrder, timeToCompare, timeToCompare+oneDayUnixTime));
                    timeToCompare += oneDayUnixTime;
                }
                setData(arProfit, now.getDayOfYear()-dayCount);
                //Log.d(TAG, "День от начала года " + (now.getDayOfYear()-dayCount));
                mChart.invalidate();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private int getPriceForDayFromArray(ArrayList<CloseOrder> ar, long startTime, long endTime) {
        int sum = 0;
        for (int i = 0; i < ar.size(); i++) {
            if(ar.get(i).getUnixTimeClose() > startTime && ar.get(i).getUnixTimeClose() < endTime){
                sum += ar.get(i).getTotalPrice();
            }
        }
        return sum;
    }

    private void getProfitForPeriod(final DateTime firstDayDate, final DateTime lastDayDate) {


        arCloseOrder.clear();
        arProfit.clear();
        final int dayCount = lastDayDate.getDayOfYear() - firstDayDate.getDayOfYear();


        String date;
        date = firstDayDate.getYear() + "-" + firstDayDate.getMonthOfYear() + "-" + firstDayDate.getDayOfMonth();

        try {
            workStartTime = getStartOfDayInMillis(date) + HOURS_AFTER_ZERO;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Query myTopPostsQuery = null;
        myTopPostsQuery = mDatabaseReference.child(Constants.CLOSE_ORDER)
                .orderByChild("unixTimeClose").startAt(workStartTime).endAt(workStartTime + oneDayUnixTime*dayCount);

        profitWeek = 0;
        sumForDay = 0;
        timeToCompare = workStartTime;

        myTopPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    profitWeek+= postSnapshot.getValue(CloseOrder.class).getTotalPrice();
                    arCloseOrder.add(postSnapshot.getValue(CloseOrder.class));
                    //Log.d(TAG, "Время закрытия" + closeTime);
                }
                tvWeek.setText("Выручка за период с " + firstDayDate.toString(DateTimeFormat.longDate())
                        + " по " + lastDayDate.toString(DateTimeFormat.longDate()) + "\n" + profitWeek + " рублей");

                for(int i = 0; i <= dayCount; i++) {
                    arProfit.add((float) getPriceForDayFromArray(arCloseOrder, timeToCompare, timeToCompare+oneDayUnixTime));
                    timeToCompare += oneDayUnixTime;
                }
                setData(arProfit, firstDayDate.getDayOfYear()-1);
                //Log.d(TAG, "День от начала года " + (now.getDayOfYear()-dayCount));
                mChart.invalidate();
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

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        tvDay = (TextView) view.findViewById(R.id.tvDay);
        tvWeek = (TextView) view.findViewById(R.id.tvWeek);

        mChart = (BarChart) view.findViewById(R.id.chart1);
        mChart.setOnChartValueSelectedListener(this);

        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);

        mChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);

        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(mChart);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setTypeface(mTfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);

        IAxisValueFormatter custom = new MyAxisValueFormatter();

        YAxis leftAxis = mChart.getAxisLeft();
        //leftAxis.setTypeface(mTfLight);
        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        //rightAxis.setTypeface(mTfLight);
        rightAxis.setLabelCount(8, false);
        rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        // l.setExtra(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });
        // l.setCustom(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });

        XYMarkerView mv = new XYMarkerView(getContext(), xAxisFormatter);
        mv.setChartView(mChart); // For bounds control
        mChart.setMarker(mv); // Set the marker to the chart

        return view;
    }


    private void setData(ArrayList<Float> ar, int startDay) {

        Calendar calendar = Calendar.getInstance();
        float start = startDay+2;

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = (int) start, j = 0; i < ar.size()+start; i++, j++) {
            yVals1.add(new BarEntry(i, ar.get(j)));
        }

        BarDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "The year 2017");

            set1.setDrawIcons(false);

            set1.setColors(ColorTemplate.MATERIAL_COLORS);

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            //data.setValueTypeface(mTfLight);
            data.setBarWidth(0.9f);

            mChart.setData(data);
        }
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
        //Log.d(TAG, "Начало дня" + calendar.getTimeInMillis());
        return calendar.getTimeInMillis()/1000L;
    }

    /**
     * @param date the date in the format "yyyy-MM-dd"
     */
    public long getEndOfDayInMillis(String date) throws ParseException {
        // Add one day's time to the beginning of the day.
        // 24 hours * 60 minutes * 60 seconds * 1000 milliseconds = 1 day
        return getStartOfDayInMillis(date) + (24 * 60 * 60);
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
        fragmentActivity = (FragmentActivity) context;
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

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    public void initDatePicker() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //getActivity().getMenuInflater().inflate(R.menu.menu_statistik, menu);
        //super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Log.d(TAG, "Menu_click");
        //noinspection SimplifiableIfStatement
        if (id == R.id.statistik_for_week) {
            DateTime now = DateTime.now();
            getProfitWeek(now.getDayOfWeek(), "Выручка за неделю");
            return true;
        }
        if (id == R.id.statistik_for_month) {
            DateTime now = DateTime.now();
            getProfitWeek(now.getDayOfMonth(), "Выручка за месяц");
            return true;
        }
        if (id == R.id.statistik_for_period) {
            initDatePicker();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
        DateTime date = DateTime.now().withDate(year, monthOfYear+1, dayOfMonth);
        DateTime dateEnd = DateTime.now().withDate(yearEnd, monthOfYearEnd+1, dayOfMonthEnd);
        getProfitForPeriod(date, dateEnd);

    }
}
