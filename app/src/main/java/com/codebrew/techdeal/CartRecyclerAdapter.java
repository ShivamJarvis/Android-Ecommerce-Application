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


public class CartRecyclerAdapter extends RecyclerView.Adapter<CartViewHolder> {
    private ArrayList<Bitmap> images;
    private ArrayList<String> prodId;

    interface RemoveCartButtonIsClickedInterface{
        void RemoveButtonClicked(String productId,int position);
    }

    private RemoveCartButtonIsClickedInterface removeCartButtonIsClickedInterface;

    public CartRecyclerAdapter(RemoveCartButtonIsClickedInterface removeCartButtonIsClickedInterface,ArrayList<String> prodId) {
        this.images = images;
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
        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Product");
        parseQuery.whereEqualTo("objectId",prodId.get(position));
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null && objects!=null){
                    if(objects.size()>0){
                        for(ParseObject object : objects){
                            if(object.getString("product_name").length()>25){
                                holder.getCartTextName().setText(object.getString("product_name").substring(0,20)+" ...");

                            }else{
                                holder.getCartTextName().setText(object.getString("product_name"));
                            }


                            holder.getCartTextPrice().setText("Rs. "+object.getString("our_price"));
                            ParseFile parseFile = object.getParseFile("product_image");
                            parseFile.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {
                                    if(e==null && data!=null){
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                                        holder.getCartProdImage().setImageBitmap(bitmap);
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });


            holder.getRemoveItemFromCart().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeCartButtonIsClickedInterface.RemoveButtonClicked(prodId.get(position),position);
                }
            });
    }

    @Override
    public int getItemCount() {
        return prodId.size();
    }
}
