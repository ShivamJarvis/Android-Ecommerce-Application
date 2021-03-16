package com.codebrew.techdeal;

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

public class InventoryRecyclerAdapter extends RecyclerView.Adapter<InventoryViewHolder> {
    private ArrayList<String> objectIds;
    interface IsInventoryItemIsClicked{
        void InventoryItemIsClicked(String objectId);
    }

    public InventoryRecyclerAdapter(ArrayList<String> objectIds, IsInventoryItemIsClicked inventoryItemIsClicked) {
        this.objectIds = objectIds;
        this.inventoryItemIsClicked = inventoryItemIsClicked;
    }

    private IsInventoryItemIsClicked inventoryItemIsClicked;


    @NonNull
    @Override
    public InventoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.inventory_view_holder,parent,false);
        return new InventoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryViewHolder holder, int position) {
        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Product");
        parseQuery.whereEqualTo("objectId",objectIds.get(position));
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(objects != null && e==null)
                {
                    for(ParseObject object : objects)
                    {
                        holder.getProductId().setText("PROD"+object.getObjectId());
                        holder.getProductName().setText(object.getString("product_name"));
                        ParseFile parseFile = object.getParseFile("product_image");
                        parseFile.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if(data.length != 0 && e==null)
                                {
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                                    holder.getProductImage().setImageBitmap(bitmap);
                                }
                            }
                        });

                    }
                }
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inventoryItemIsClicked.InventoryItemIsClicked(objectIds.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return objectIds.size();
    }
}
