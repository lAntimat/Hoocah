package ru.lantimat.hoocah.adapters;

/**
 * Created by Ильназ on 12.04.2017.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.iconics.view.IconicsButton;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ru.lantimat.hoocah.R;
import ru.lantimat.hoocah.Utils.SquareImageView;
import ru.lantimat.hoocah.models.ItemModel;



public class ItemsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public MyAdapterListener onClickListener;
    public interface MyAdapterListener {

        void btnDotsOnClick(View v, int position);
    }

    View.OnClickListener mClickListener;

    private ArrayList<ItemModel> mList;
    int type;
    int typePosition;

    public ItemsRecyclerAdapter(ArrayList<ItemModel> itemList) {
        this.mList = itemList;

    }


    public void setButtonClickListener(MyAdapterListener listener) {
        this.onClickListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_item, parent, false);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mClickListener.onClick(view);
                    }
                });
                return new ItemViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final Context context;

        ((ItemViewHolder) holder).mTitle.setText(mList.get(position).getName());
        ((ItemViewHolder) holder).mPrice.setText(String.valueOf(mList.get(position).getPrice()));
        context = ((ItemViewHolder) holder).mImg.getContext();
            Picasso.with(context)
                    .load(mList.get(position).getImgUrl())
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(((ItemViewHolder) holder).mImg, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            // Try again online if cache failed
                            Picasso.with(context)
                                    .load(mList.get(position).getImgUrl())
                                    .into(((ItemViewHolder) holder).mImg);
                        }
                    });

        /*new Picasso.Builder(context)
                .downloader(new OkHttpDownloader(context, Integer.MAX_VALUE))
                .build()
                .load(mList.get(position).getImgUrl())
                .into(((ItemViewHolder) holder).mImg);*/

        ((ItemViewHolder) holder).btnDots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.btnDotsOnClick(v, position);
            }
        });
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

    public void remove(int position) {
        mList.remove(position);
        this.notifyDataSetChanged();
    }

    public void set(int position, ItemModel itemModel) {
        mList.set(position, itemModel);
        this.notifyDataSetChanged();
    }

    public void add(ItemModel itemModel) {
        mList.add(itemModel);

    }



    public static class ItemViewHolder extends RecyclerView.ViewHolder {



        private TextView mTitle;
        private TextView mPrice;
        private SquareImageView mImg;
        private IconicsButton btnDots;
        public ItemViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.tvName);
            mPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            mImg = (SquareImageView) itemView.findViewById(R.id.imageView);
            btnDots = (IconicsButton) itemView.findViewById(R.id.tvDots);

        }
    }
}
