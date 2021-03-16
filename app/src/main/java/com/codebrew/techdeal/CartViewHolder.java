package com.codebrew.techdeal;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class CartViewHolder extends RecyclerView.ViewHolder {
    private ImageView cartProdImage;
    private TextView cartTextName;
    private TextView cartTextPrice;
    private ImageView removeItemFromCart;
    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        cartProdImage = itemView.findViewById(R.id.cart_prod_image);
        cartTextName = itemView.findViewById(R.id.cart_prod_name);
        cartTextPrice = itemView.findViewById(R.id.cart_prod_price);
        removeItemFromCart =  itemView.findViewById(R.id.remove_cart_btn);
    }

    public ImageView getRemoveItemFromCart() {
        return removeItemFromCart;
    }

    public ImageView getCartProdImage() {
        return cartProdImage;
    }

    public TextView getCartTextName() {
        return cartTextName;
    }

    public TextView getCartTextPrice() {
        return cartTextPrice;
    }
}
