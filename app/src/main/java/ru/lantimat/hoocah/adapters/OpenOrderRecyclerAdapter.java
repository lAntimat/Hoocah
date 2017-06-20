package ru.lantimat.hoocah.adapters;

/**
 * Created by Ильназ on 12.04.2017.
 */

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import ru.lantimat.hoocah.R;
import ru.lantimat.hoocah.models.ActiveOrder;


public class OpenOrderRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    View.OnClickListener mClickListener;

    private ArrayList<ActiveOrder> mList;

    public OpenOrderRecyclerAdapter(ArrayList<ActiveOrder> itemList) {
        this.mList = itemList;
    }


    public void setClickListener(View.OnClickListener callback) {
        mClickListener = callback;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_open_orders, parent, false);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mClickListener.onClick(view);
                    }
                });
                return new ItemViewHolder(view);
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String strName;
        strName = "";
        if(mList.get(position).isActive()) {
            strName += " (Активный)";
            ((ItemViewHolder) holder).mStatus.setTextColor(Color.BLUE);
        }
        else {
            strName += " (Ожидает оплаты)";
            ((ItemViewHolder) holder).mStatus.setTextColor(Color.BLACK);

        }

        ((ItemViewHolder) holder).mId.setText("Столик №" + mList.get(position).getId());
        ((ItemViewHolder) holder).mStatus.setText(strName);

        String items = "";
        if(mList!=null && mList.get(position).getArActiveItemModel()!=null) {
            for (int i = 0; i < mList.get(position).getArActiveItemModel().size(); i++)
                items += mList.get(position).getArActiveItemModel().get(i).getName() + " " + "\n";
            ((ItemViewHolder) holder).mItems.setText(items);
            long unixBillOpenTime = mList.get(position).getUnixTime();
            convertUnixTimeToDate(unixBillOpenTime);
            ((ItemViewHolder) holder).mDate.setText(convertUnixTimeToDate(unixBillOpenTime));
            ((ItemViewHolder) holder).mTotalPrice.setText(String.valueOf("Сумма к оплате: " + mList.get(position).getTotalPrice()));
            if(mList.get(position).getComment()!=null) {
                ((ItemViewHolder) holder).mComment.setVisibility(View.VISIBLE);
                ((ItemViewHolder) holder).mComment.setText(mList.get(position).getComment());
            } else  ((ItemViewHolder) holder).mComment.setVisibility(View.GONE);
        }

    }

    private String convertUnixTimeToDate(long unixTime) {
        Date date = new Date(unixTime*1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd\nHH:mm:ss"); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+3")); // give a timezone reference for formating (see comment at the bottom
        String formattedDate = sdf.format(date);
        return formattedDate;
    }

    @Override
    public int getItemCount() {
        if (mList == null)
            return 0;
        return mList.size();
    }
    @Override
    public int getItemViewType(int position) {
        if (mList != null) {
            //ControlItemsModel object = mList.get(position);
            //if (object != null) {
               // return object.getType();
          //  }
        }
        return 0;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView mId;
        private TextView mStatus;
        private TextView mItems;
        private TextView mDate;
        private TextView mTotalPrice;
        private TextView mComment;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mId = (TextView) itemView.findViewById(R.id.tvId);
            mStatus = (TextView) itemView.findViewById(R.id.tvStatus);
            mItems = (TextView) itemView.findViewById(R.id.tvItems);
            mDate = (TextView) itemView.findViewById(R.id.tvDate);
            mTotalPrice = (TextView) itemView.findViewById(R.id.tvTotalPrice);
            mComment = (TextView) itemView.findViewById(R.id.tvComment);
        }
    }
}
