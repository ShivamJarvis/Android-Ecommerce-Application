package com.example.techytech;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.kofigyan.stateprogressbar.StateProgressBar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;


public class OrderDetailsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.logo);
        setTitle("");

        StateProgressBar stateProgressBar;
        RecyclerView orderDetailProductRecyclerView;
        TextView topOrderIdDisplayText;
        TextView paymentMode,deliveryMode,orderAmount,orderDate,deliveryCharges,netAmountPaid;
        String recievedOrderId = getIntent().getStringExtra("orderObjectId");
        topOrderIdDisplayText = findViewById(R.id.order_details_id);
        topOrderIdDisplayText.setText(topOrderIdDisplayText.getText().toString()+recievedOrderId);
        stateProgressBar = findViewById(R.id.order_status_progress_bar);
        stateProgressBar.setStateDescriptionData(new String[]{"Order\nPlaced","Order\nPacked","Order\nShipped","Out\nfor\nDelivery","Order\nDelivered"});
        paymentMode = findViewById(R.id.payment_mode);
        deliveryMode = findViewById(R.id.delivery_mode);
        orderAmount = findViewById(R.id.order_amount);
        orderDate = findViewById(R.id.order_date);
        deliveryCharges = findViewById(R.id.delivery_charges);
        netAmountPaid = findViewById(R.id.net_amount);

        orderDetailProductRecyclerView = findViewById(R.id.order_details_recyclerview);
        String state = getIntent().getStringExtra("orderStatus");

        if(state.equals("Order Placed")){
            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
        }
        else if(state.equals("Order Packed")){

            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
        }
        else if(state.equals("Order Shipped")){
            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR);
        }
        else if(state.equals("Out for Delivery")){
            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FIVE);
        }
        else if(state.equals("Order Delivered")){
            stateProgressBar.setAllStatesCompleted(true);
        }


        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Orders");
        parseQuery.whereEqualTo("objectId",recievedOrderId);
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null && objects.size()==1){
                    ParseObject object = objects.get(0);
                    if(object.getString("payment_mode").equals("online")){
                        paymentMode.setText("Paid Online");
                        orderAmount.setText("Rs. "+(object.getInt("total_amount")-100));
                    }
                    if(object.getString("payment_mode").equals("cash")){
                        paymentMode.setText("Cash On Delivery");
                        orderAmount.setText("Rs. "+(object.getInt("total_amount")));
                    }
                    if(object.getString("delivery_type").equals("express")){
                        deliveryMode.setText("Express Delivery");
                        deliveryCharges.setText("Rs. 500");
                    }
                    if(object.getString("delivery_type").equals("standard")){
                        deliveryMode.setText("Standard Delivery");
                        deliveryCharges.setText("Rs. 100");
                    }

                    if(object.getString("delivery_type").equals("express")){
                        if(object.getString("payment_mode").equals("online")){
                            netAmountPaid.setText("Rs. "+(object.getInt("total_amount")+400));
                        }
                        if(object.getString("payment_mode").equals("cash")){
                            netAmountPaid.setText("Rs. "+(object.getInt("total_amount")+500));
                        }
                    }
                    if(object.getString("delivery_type").equals("standard")){
                        if(object.getString("payment_mode").equals("online")){

                            netAmountPaid.setText("Rs. "+(object.getInt("total_amount")+0));


                        }
                        if(object.getString("payment_mode").equals("cash")){
                            netAmountPaid.setText("Rs. "+(object.getInt("total_amount")+100));

                        }
                    }


                    orderDate.setText(object.getCreatedAt().toString().substring(0,11));
                    orderDetailProductRecyclerView.setAdapter(new OrderDetailsRecyclerAdapter(object.getString("products")+""));

                }
            }
        });
        orderDetailProductRecyclerView.setLayoutManager(new LinearLayoutManager(OrderDetailsActivity.this));

    }

}