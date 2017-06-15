package ru.lantimat.hoocah.adapters;

/**
 * Created by Ильназ on 12.04.2017.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ru.lantimat.hoocah.R;
import ru.lantimat.hoocah.models.ActiveItemModel;


public class ActiveItemRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    View.OnClickListener mClickListener;

    private ArrayList<ActiveItemModel> mList;

    public ActiveItemRecyclerAdapter(ArrayList<ActiveItemModel> itemList) {
        this.mList = itemList;
    }


    public void setClickListener(View.OnClickListener callback) {
        mClickListener = callback;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_active_item, parent, false);
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

        ((ItemViewHolder) holder).mTitle.setText(mList.get(position).getName());
        ((ItemViewHolder) holder).mCount.setText(String.valueOf(mList.get(position).getCount()));
        ((ItemViewHolder) holder).mPrice.setText(String.valueOf(mList.get(position).getPrice()));
        ((ItemViewHolder) holder).mTotalPrice.setText(String.valueOf(mList.get(position).getTotalPrice()));

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
        private TextView mTitle;
        private TextView mCount;
        private TextView mPrice;
        private TextView mTotalPrice;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.tvName);
            mCount = (TextView) itemView.findViewById(R.id.tvCount);
            mPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            mTotalPrice = (TextView) itemView.findViewById(R.id.tvTotalPrice);
        }
    }
}
