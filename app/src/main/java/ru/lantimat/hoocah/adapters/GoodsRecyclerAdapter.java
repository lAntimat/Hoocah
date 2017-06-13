package ru.lantimat.hoocah.adapters;

/**
 * Created by Ильназ on 12.04.2017.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ru.lantimat.hoocah.R;
import ru.lantimat.hoocah.models.GoodsModel;


public class GoodsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int SWITCH_TYPE = 0;
    public static final int HISTORY_TYPE = 1;

    View.OnClickListener mClickListener;

    private  ArrayList<GoodsModel> mList;

    public GoodsRecyclerAdapter(ArrayList<GoodsModel> itemList) {
        this.mList = itemList;
    }

    public void setClickListener(View.OnClickListener callback) {
        mClickListener = callback;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);

        RecyclerView.ViewHolder holder = new SimpleViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onClick(view);
            }
        });
        return holder;

    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String itemText = mList.get(position).getName();
        String imgUrl = mList.get(position).getImgUrl();
        ((SimpleViewHolder) holder).mTitle.setText(itemText);
        Context context = ((SimpleViewHolder) holder).mImg.getContext();
        Picasso.with(context).load(imgUrl).into(((SimpleViewHolder) holder).mImg);
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
    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitle;
        private ImageView mImg;
        public SimpleViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.itemTextView);
            mImg = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }
}
