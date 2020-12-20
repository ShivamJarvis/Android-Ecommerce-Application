package com.example.techytech;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class SellerPendingOrderViewHolder extends RecyclerView.ViewHolder {
    private TextView orderNo,orderStatus,pendingOrderBtn;
    public SellerPendingOrderViewHolder(@NonNull View itemView) {
        super(itemView);
        orderNo = itemView.findViewById(R.id.order_no);
        orderStatus = itemView.findViewById(R.id.order_status_seller);
        pendingOrderBtn = itemView.findViewById(R.id.see_order_btn);
    }

    public TextView getOrderNo() {
        return orderNo;
    }

    public TextView getOrderStatus() {
        return orderStatus;
    }

    public TextView getPendingOrderBtn() {
        return pendingOrderBtn;
    }
}
