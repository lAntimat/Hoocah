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

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ru.lantimat.hoocah.R;
import ru.lantimat.hoocah.Utils.SquareImageView;
import ru.lantimat.hoocah.models.GoodsModel;


public class GoodsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    View.OnClickListener mClickListener;

    private ArrayList<GoodsModel> mList;
    int type;
    int typePosition;

    public GoodsRecyclerAdapter(ArrayList<GoodsModel> itemList) {
        this.mList = itemList;
    }

    public void setClickListener(View.OnClickListener callback) {
        mClickListener = callback;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_goods, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {mClickListener.onClick(view);
                    }
                });
        return new GoodsViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            final Context context;
            ((GoodsViewHolder) holder).mTitle.setText(mList.get(position).getName());
            context = ((GoodsViewHolder) holder).mImg.getContext();
            Picasso.with(context)
                    .load(mList.get(position).getImgUrl())
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(((GoodsViewHolder) holder).mImg, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                // Try again online if cache failed
                Picasso.with(context)
                        .load(mList.get(position).getImgUrl())
                        .into(((GoodsViewHolder) holder).mImg);
            }
        });
        /*new Picasso.Builder(context)
                .downloader(new OkHttpDownloader(context, Integer.MAX_VALUE))
                .build()
                .load(mList.get(position).getImgUrl())
                .into(((GoodsViewHolder) holder).mImg);*/
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
    public static class GoodsViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitle;
        private SquareImageView mImg;
        public GoodsViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.tvName);
            mImg = (SquareImageView) itemView.findViewById(R.id.imageView);
        }
    }
}
