package com.codebrew.techdeal;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderDetailsViewHolder extends RecyclerView.ViewHolder {
    private TextView productName;
    private ImageView productImage;
    public OrderDetailsViewHolder(@NonNull View itemView) {
        super(itemView);
        productImage = itemView.findViewById(R.id.order_details_product_image_viewholder);
        productName = itemView.findViewById(R.id.order_details_product_viewholder);
    }

    public TextView getProductName() {
        return productName;
    }

    public ImageView getProductImage() {
        return productImage;
    }
}
