package com.example.techytech;

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

public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerViewHolder> {
    private ArrayList<String> productId;
    interface SearchItemClickListenerInterface{
        void SearchItemIsClicked(String productId);
    }
    private SearchItemClickListenerInterface searchItemClickListenerInterface;

    public SearchRecyclerAdapter(ArrayList<String> productId, SearchItemClickListenerInterface searchItemClickListenerInterface) {
        this.productId = productId;
        this.searchItemClickListenerInterface = searchItemClickListenerInterface;
    }

    @NonNull
    @Override
    public SearchRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.search_found_viewholder,parent,false);
        return new SearchRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchRecyclerViewHolder holder, int position) {
        ParseQuery<ParseObject> productsQuery = ParseQuery.getQuery("Product");
        productsQuery.whereEqualTo("objectId", productId.get(position));
        productsQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size() > 0) {
                    for (ParseObject parseObject : objects) {

                        if (parseObject.getString("product_name").length() > 20) {
                            holder.getProdNameTextView().setText(parseObject.getString("product_name").substring(0, 16) + " ...");
                        } else {
                            holder.getProdNameTextView().setText(parseObject.getString("product_name"));
                        }
                        holder.getProdPriceTextView().setText("Rs. "+parseObject.getString("our_price"));
                        ParseFile parseFile = parseObject.getParseFile("product_image");
                        parseFile.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                holder.getProdImageView().setImageBitmap(bitmap);

                            }
                        });

                    }
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchItemClickListenerInterface.SearchItemIsClicked(productId.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return productId.size();
    }
}
