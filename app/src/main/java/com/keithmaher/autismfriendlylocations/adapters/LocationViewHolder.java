package com.keithmaher.autismfriendlylocations.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.keithmaher.autismfriendlylocations.R;

public class LocationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView name;
    public TextView address;
    public ImageView ImageView;

    private ItemClickListener itemClickListener;

    public LocationViewHolder(View itemView){
        super(itemView);

        address = itemView.findViewById(R.id.newsUserDate);
        name = itemView.findViewById(R.id.newsUserName);
        ImageView = itemView.findViewById(R.id.newsUserImg);

        itemView.setOnClickListener(this);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }
}
