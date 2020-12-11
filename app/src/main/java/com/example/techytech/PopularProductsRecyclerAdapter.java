package com.example.techytech;

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

public class PopularProductsRecyclerAdapter extends RecyclerView.Adapter<PopularProductsViewHolder> {
    private ArrayList<String> productId;
    private Context context;
    interface PopularProductIsClickedInterface {
        void PopularProductIsClicked(String productName);
    }
    private PopularProductIsClickedInterface popularProductIsClickedInterface;

    public PopularProductsRecyclerAdapter(ArrayList<String> productId, Context context, PopularProductIsClickedInterface popularProductIsClickedInterface) {
        this.productId = productId;
        this.context = context;
        this.popularProductIsClickedInterface = popularProductIsClickedInterface;
    }

    @NonNull
    @Override
    public PopularProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.featured_product_view_holder, parent, false);
        return new PopularProductsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularProductsViewHolder holder, int position) {
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
                popularProductIsClickedInterface.PopularProductIsClicked(productId.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return productId.size();
    }
}
