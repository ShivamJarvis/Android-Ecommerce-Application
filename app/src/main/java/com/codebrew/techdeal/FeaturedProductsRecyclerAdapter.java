package com.codebrew.techdeal;

import android.content.Context;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class FeaturedProductsRecyclerAdapter extends RecyclerView.Adapter<FeaturedProductsViewHolder> {


    private ArrayList<String> productId;
    private Context context;

    interface ProductIsClickedInterface {
        void ProductIsClicked(String productName);
    }

    private ProductIsClickedInterface productIsClickedInterface;

    public FeaturedProductsRecyclerAdapter(Context context,  ArrayList<String> productId, ProductIsClickedInterface productIsClickedInterface) {
        this.context = context;
        this.productIsClickedInterface = productIsClickedInterface;
        this.productId = productId;
    }

    @NonNull
    @Override
    public FeaturedProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.featured_product_view_holder, parent, false);

        return new FeaturedProductsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedProductsViewHolder holder, int position) {

        ParseQuery<ParseObject> productsQuery = ParseQuery.getQuery("Product");
        productsQuery.whereEqualTo("objectId", productId.get(position));
        productsQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size() > 0) {
                    for (ParseObject parseObject : objects) {

                        if (parseObject.getString("product_name").length() > 20) {
                            holder.getFeatureProductName().setText(parseObject.getString("product_name").substring(0, 16) + " ...");
                        } else {
                            holder.getFeatureProductName().setText(parseObject.getString("product_name"));
                        }
                        ParseFile parseFile = parseObject.getParseFile("product_image");
                        parseFile.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                holder.getFeatureProductImage().setImageBitmap(bitmap);

                            }
                        });

                    }
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productIsClickedInterface.ProductIsClicked(productId.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return productId.size();
    }
}