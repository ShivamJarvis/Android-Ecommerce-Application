package com.example.techytech;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PopularProductsViewHolder extends RecyclerView.ViewHolder {
    private ImageView featureProductImage;
    private TextView featureProductName;
    public PopularProductsViewHolder(@NonNull View itemView) {
        super(itemView);
        featureProductImage = itemView.findViewById(R.id.featured_product_image);
        featureProductName = itemView.findViewById(R.id.featured_product_name);
    }

    public ImageView getFeatureProductImage() {
        return featureProductImage;
    }

    public TextView getFeatureProductName() {
        return featureProductName;
    }
}
