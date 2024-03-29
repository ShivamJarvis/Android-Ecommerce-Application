package com.codebrew.techdeal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class SellerPendingOrderRecyclerAdapter extends RecyclerView.Adapter<SellerPendingOrderViewHolder> {
    private ArrayList<String> orderNoList;
    interface SeeOrderButtonIsClickedInterface{
        void SeeOrderButtonIsClicked(String orderNo);
    }

    private SeeOrderButtonIsClickedInterface seeOrderButtonIsClickedInterface;

    public SellerPendingOrderRecyclerAdapter(ArrayList<String> orderNoList,SeeOrderButtonIsClickedInterface seeOrderButtonIsClickedInterface) {
        this.orderNoList = orderNoList;
        this.seeOrderButtonIsClickedInterface = seeOrderButtonIsClickedInterface;
    }

    @NonNull
    @Override
    public SellerPendingOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.seller_pending_order_view_holder,parent,false);
        return new SellerPendingOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SellerPendingOrderViewHolder holder, int position) {

        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Orders");
        parseQuery.whereEqualTo("objectId",orderNoList.get(position));

        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null && objects!=null){
                    if(objects.size()>0){

                            holder.getOrderNo().setText("#ORDER : "+(position+1));
                            holder.getOrderStatus().setText(objects.get(0).getString("order_status"));
                            holder.getPendingOrderBtn().setVisibility(View.VISIBLE);



                    }
                }
            }
        });
        holder.getPendingOrderBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seeOrderButtonIsClickedInterface.SeeOrderButtonIsClicked(orderNoList.get(position));
            }
        });



    }

    @Override
    public int getItemCount() {
        return orderNoList.size();
    }
}
