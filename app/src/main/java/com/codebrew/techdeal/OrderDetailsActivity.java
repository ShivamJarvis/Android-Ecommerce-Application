package com.codebrew.techdeal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kofigyan.stateprogressbar.StateProgressBar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;


public class OrderDetailsActivity extends AppCompatActivity {

    private CardView reviewCardView;
    private TextView paymentMode,deliveryMode,orderAmount,orderDate,deliveryCharges,netAmountPaid;
    private EditText customerReview;
    private Button customerReviewSubmitButton;

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

        String recievedOrderId = getIntent().getStringExtra("orderObjectId");
        reviewCardView = findViewById(R.id.review_card);
        customerReview = findViewById(R.id.customer_review);
        customerReviewSubmitButton = findViewById(R.id.customer_review_submit_button);


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
            reviewCardView.setVisibility(View.VISIBLE);
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

        checkUserAlreadyGiveReview();

        customerReviewSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customerNewReview();
                checkUserAlreadyGiveReview();
            }
        });

    }


    private void customerNewReview(){
        ParseObject newCustomerReview = new ParseObject("Review");
        ParseQuery<ParseObject> orderQuery = ParseQuery.getQuery("Orders");
        orderQuery.whereEqualTo("objectId",getIntent().getStringExtra("orderObjectId"));
        orderQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> orderObjects, ParseException orderException) {
                if(orderException==null && orderObjects!=null){
                    ParseObject object = orderObjects.get(0);
                    newCustomerReview.put("order_id",object.getObjectId());
                    newCustomerReview.put("username", ParseUser.getCurrentUser().getUsername());
                    ParseQuery<ParseObject> productQuery = ParseQuery.getQuery("Product");
                    productQuery.whereEqualTo("objectId",object.getString("products"));
                    productQuery.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> productObjects, ParseException productException) {
                            if(productException==null && productObjects!=null){
                                newCustomerReview.put("product_id",productObjects.get(0).getObjectId());
                                newCustomerReview.put("review",customerReview.getText().toString());
                                newCustomerReview.saveInBackground();
                                Toast.makeText(OrderDetailsActivity.this,"Thanks for giving us your valuable time",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }


    private void checkUserAlreadyGiveReview(){
        ParseQuery<ParseObject> orderQuery = ParseQuery.getQuery("Orders");
        orderQuery.whereEqualTo("objectId",getIntent().getStringExtra("orderObjectId"));
        orderQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){
                    if(objects.size()>0){
                        if(objects.get(0).getString("order_status").equals("Order Delivered")){
                            ParseQuery<ParseObject> reviewQuery = ParseQuery.getQuery("Review");
                            reviewQuery.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
                            reviewQuery.whereEqualTo("order_id",getIntent().getStringExtra("orderObjectId"));
                            reviewQuery.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> objects, ParseException e) {
                                    if(e==null && objects.size()>0){
                                        reviewCardView.setVisibility(View.GONE);
                                    }
                                    else {
                                        reviewCardView.setVisibility(View.VISIBLE);
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });



    }

}