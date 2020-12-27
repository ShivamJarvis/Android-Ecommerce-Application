package com.codebrew.techdeal;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderRecylerViewHolder extends RecyclerView.ViewHolder {
    private TextView orderId, orderProductName, orderStatus,numberOfProduct;
    private ImageView orderProductImage;

    public OrderRecylerViewHolder(@NonNull View itemView) {
        super(itemView);
        orderId = itemView.findViewById(R.id.order_id);
        orderProductName = itemView.findViewById(R.id.order_product_name);
        orderStatus = itemView.findViewById(R.id.order_status);
        orderProductImage = itemView.findViewById(R.id.order_product_image);
        numberOfProduct = itemView.findViewById(R.id.no_of_products);
    }

    public TextView getNumberOfProduct() {
        return numberOfProduct;
    }

    public TextView getOrderId() {
        return orderId;
    }

    public TextView getOrderProductName() {
        return orderProductName;
    }

    public TextView getOrderStatus() {
        return orderStatus;
    }

    public ImageView getOrderProductImage() {
        return orderProductImage;
    }
}
