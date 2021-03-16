package com.codebrew.techdeal;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryDetailViewHolder extends RecyclerView.ViewHolder {
    private ImageView prodImageView;
    private TextView prodPriceTextView;
    private TextView prodNameTextView;
    public CategoryDetailViewHolder(@NonNull View itemView) {
        super(itemView);
        prodImageView = itemView.findViewById(R.id.cat_prod_image);
        prodNameTextView = itemView.findViewById(R.id.cat_prod_name_view_holder);
        prodPriceTextView = itemView.findViewById(R.id.cat_prod_price_view_holder);
    }

    public ImageView getProdImageView() {
        return prodImageView;
    }

    public TextView getProdPriceTextView() {
        return prodPriceTextView;
    }

    public TextView getProdNameTextView() {
        return prodNameTextView;
    }
}
