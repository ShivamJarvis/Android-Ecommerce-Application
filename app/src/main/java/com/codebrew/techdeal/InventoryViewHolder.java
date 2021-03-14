package com.codebrew.techdeal;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



public class InventoryViewHolder extends RecyclerView.ViewHolder {
    private TextView productId,productName;
    private ImageView productImage;
    public InventoryViewHolder(@NonNull View itemView) {
        super(itemView);
        productId = itemView.findViewById(R.id.inventory_product_id);
        productName = itemView.findViewById(R.id.inventory_product_name);
        productImage = itemView.findViewById(R.id.inventory_product_image);
    }

    public TextView getProductId() {
        return productId;
    }

    public TextView getProductName() {
        return productName;
    }

    public ImageView getProductImage() {
        return productImage;
    }
}
