package com.example.techytech;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class CartRecyclerAdapter extends RecyclerView.Adapter<CartViewHolder> {
    private ArrayList<Bitmap> images;
    private ArrayList<String> prodNames, prodPrices,prodId;

    interface RemoveCartButtonIsClickedInterface{
        void RemoveButtonClicked(String productId,int position);
    }

    private RemoveCartButtonIsClickedInterface removeCartButtonIsClickedInterface;

    public CartRecyclerAdapter(ArrayList<Bitmap> images, ArrayList<String> prodNames, ArrayList<String> prodPrices,RemoveCartButtonIsClickedInterface removeCartButtonIsClickedInterface,ArrayList<String> prodId) {
        this.images = images;
        this.prodNames = prodNames;
        this.prodPrices = prodPrices;
        this.removeCartButtonIsClickedInterface = removeCartButtonIsClickedInterface;
        this.prodId = prodId;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.cart_view_holder,parent,false);

        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
            holder.getCartProdImage().setImageBitmap(images.get(position));
            holder.getCartTextName().setText(prodNames.get(position));
            holder.getCartTextPrice().setText(prodPrices.get(position));
            holder.getRemoveItemFromCart().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeCartButtonIsClickedInterface.RemoveButtonClicked(prodId.get(position),position);
                }
            });
    }

    @Override
    public int getItemCount() {
        return prodNames.size();
    }
}
