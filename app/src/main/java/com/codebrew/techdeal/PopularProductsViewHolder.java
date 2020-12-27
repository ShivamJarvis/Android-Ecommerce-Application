package com.codebrew.techdeal;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PopularProductsViewHolder extends RecyclerView.ViewHolder {
    private ImageView popularProductImage;
    private TextView popularProductName;
    public PopularProductsViewHolder(@NonNull View itemView) {
        super(itemView);
        popularProductImage = itemView.findViewById(R.id.popular_product_image);
        popularProductName = itemView.findViewById(R.id.popular_product_name);
    }

    public ImageView getPopularProductImage() {
        return popularProductImage;
    }

    public TextView getPopularProductName() {
        return popularProductName;
    }
}
