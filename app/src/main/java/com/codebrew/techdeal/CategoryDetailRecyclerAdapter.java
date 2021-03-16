package com.codebrew.techdeal;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class CategoryDetailRecyclerAdapter extends RecyclerView.Adapter<CategoryDetailViewHolder> {
    private ArrayList<Bitmap> prodImages;
    private ArrayList<String> prodNames;
    private ArrayList<String> prodPrices;
    private ArrayList<String> prodId;

    interface ProductItemIsClickedInterface{
        void ProductItemClicked(String productId);
    }

    private ProductItemIsClickedInterface productItemIsClickedInterface;

    public CategoryDetailRecyclerAdapter(ArrayList<Bitmap> prodImages, ArrayList<String> prodNames, ArrayList<String> prodPrices, ArrayList<String> prodId, ProductItemIsClickedInterface productItemIsClickedInterface) {
        this.prodImages = prodImages;
        this.prodNames = prodNames;
        this.prodPrices = prodPrices;
        this.prodId = prodId;
        this.productItemIsClickedInterface = productItemIsClickedInterface;
    }

    @NonNull
    @Override
    public CategoryDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.categoru_detail_view_holder,parent,false);
        return new CategoryDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryDetailViewHolder holder, int position) {

        holder.getProdImageView().setImageBitmap(prodImages.get(position));
        holder.getProdNameTextView().setText(prodNames.get(position));
        holder.getProdPriceTextView().setText("Rs. "+prodPrices.get(position));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productItemIsClickedInterface.ProductItemClicked(prodId.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return prodId.size();
    }

}
