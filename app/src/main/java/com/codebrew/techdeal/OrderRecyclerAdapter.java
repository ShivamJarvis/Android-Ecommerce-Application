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

public class OrderRecyclerAdapter extends RecyclerView.Adapter<OrderRecylerViewHolder> {
    private ArrayList<String> orderObjectId,orderStatus;
    private Context context;

    interface OrderItemIsClickedInterface{
        void OrderItemIsClicked(String orderId,String orderStatus);
    }
    private OrderItemIsClickedInterface orderItemIsClickedInterface;

    public OrderRecyclerAdapter(ArrayList<String> orderObjectId, OrderItemIsClickedInterface orderItemIsClickedInterface,Context context,ArrayList<String> orderStatus) {
        this.orderObjectId = orderObjectId;
        this.orderItemIsClickedInterface = orderItemIsClickedInterface;
        this.context = context;
        this.orderStatus = orderStatus;
    }

    @NonNull
    @Override
    public OrderRecylerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.order_view_holder,parent,false);
        return new OrderRecylerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderRecylerViewHolder holder, int position) {
        holder.getOrderId().setText("Order Id : ORDER"+orderObjectId.get(position));
        ParseQuery<ParseObject> orderQuery = ParseQuery.getQuery("Orders");
        orderQuery.whereEqualTo("objectId", orderObjectId.get(position));
        orderQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){
                    if(objects.size()>0){
                            ParseObject object = objects.get(0);
                            holder.getOrderStatus().setText(object.getString("order_status"));
                        String productId = object.getString("products");
                        ParseQuery<ParseObject> productQuery = ParseQuery.getQuery("Product");
                        productQuery.whereEqualTo("objectId",productId);
                        productQuery.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> pObjects, ParseException e1) {
                                if(e1==null && pObjects.size()>0){

                                    ParseObject pOobject = pObjects.get(0);
                                    if(pOobject.getString("product_name").length()>13){
                                        holder.getOrderProductName().setText(pOobject.getString("product_name").substring(0,13)+" ...");
                                    }
                                    else{
                                        holder.getOrderProductName().setText(pOobject.getString("product_name"));
                                    }
                                    ParseFile parseFile = pOobject.getParseFile("product_image");
                                    parseFile.getDataInBackground(new GetDataCallback() {
                                        @Override
                                        public void done(byte[] data, ParseException e) {
                                            if(e==null){
                                                Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                                                holder.getOrderProductImage().setImageBitmap(bitmap);
                                            }
                                        }
                                    });
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
                orderItemIsClickedInterface.OrderItemIsClicked(orderObjectId.get(position),orderStatus.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return orderObjectId.size();
    }
}
