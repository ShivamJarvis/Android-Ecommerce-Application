package com.codebrew.techdeal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProceedToPaymentActivity extends AppCompatActivity implements PaymentResultListener {
    private String recievedAddressId,recieveBuyBowProduct,recievedSellerName;
    private TextView addressLinesTextView,stateCityTextView,zipCodeTextView;
    private RadioButton standardDeliveryRB,expressDeliveryRB,cashRB,onlineRB;
    private int totalPrice=0,mTotalPrice=0;
    private Button placeOrderButton;
    private String paymentMode;
    private String deliveryMethod;
    private static final String TAG = ProceedToPaymentActivity.class.getSimpleName();
    Checkout checkout = new Checkout();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proceed_to_payment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Checkout.preload(getApplicationContext());
        try{
            recieveBuyBowProduct = getIntent().getStringExtra("buy_now_product");
            recievedSellerName = getIntent().getStringExtra("seller_name");

        }catch (Exception e){
            e.printStackTrace();
        }

        recievedAddressId = getIntent().getStringExtra("addressId");
        addressLinesTextView = findViewById(R.id.address_lines);
        stateCityTextView = findViewById(R.id.state_city);
        zipCodeTextView = findViewById(R.id.zip_code_place);
        standardDeliveryRB = findViewById(R.id.standard_radio_btn);
        expressDeliveryRB = findViewById(R.id.express_radio_btn);
        cashRB = findViewById(R.id.cash_on_delivery_radio_btn);
        onlineRB = findViewById(R.id.pay_online_radio_btn);
        placeOrderButton = findViewById(R.id.place_order_btn);
        placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewOrder();
            }
        });
        fetchUserAddress();
    }

    private void fetchUserAddress(){
        ParseQuery<ParseObject> userAddress = ParseQuery.getQuery("UserAddress");
        userAddress.whereEqualTo("objectId",recievedAddressId);
        userAddress.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null && objects.size()>0){
                    ParseObject object = objects.get(0);
                    addressLinesTextView.setText(object.getString("address_line_1")+"\n"+object.getString("address_line_2"));
                    stateCityTextView.setText(object.getString("city")+", "+object.getString("state"));
                    zipCodeTextView.setText("ZIP Code : "+object.getString("zip_code"));
                }
            }
        });
    }

    public void createNewOrder(){

        if(cashRB.isChecked()==false && onlineRB.isChecked() == false){
            Toast.makeText(ProceedToPaymentActivity.this,"Please Choose Payment Mode",Toast.LENGTH_SHORT).show();
            return;
        }
        if(standardDeliveryRB.isChecked() == false && expressDeliveryRB.isChecked() == false){
            Toast.makeText(ProceedToPaymentActivity.this,"Please Choose Delivery Mode",Toast.LENGTH_SHORT).show();
            return;
        }
        if(recieveBuyBowProduct != null){


        if(recieveBuyBowProduct.equals("")==false){
            totalPrice = 0;
            ParseQuery<ParseObject> productQuery = ParseQuery.getQuery("Product");
            productQuery.whereEqualTo("objectId",recieveBuyBowProduct);
            productQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> pObjects, ParseException e) {
                    if(e==null && pObjects.size()>0){
                        for(ParseObject pPObject : pObjects){
                            totalPrice += Integer.parseInt(pPObject.getString("our_price"));
                        }
                        if(onlineRB.isChecked()) {
                            if(standardDeliveryRB.isChecked()){
                                totalPrice+=0;
                            }
                            else if(expressDeliveryRB.isChecked()){
                                totalPrice+=400;
                            }

                            Checkout checkout = new Checkout();
                            checkout.setKeyID("rzp_test_raczRd7xDgFF9t");
                            try {
                                JSONObject options = new JSONObject();
                                options.put("name", "TechyTech");
                                options.put("theme.color", "#3399cc");
                                options.put("currency", "INR");
                                options.put("amount", Integer.toString(totalPrice * 100));//pass amount in currency subunits
                                checkout.open(ProceedToPaymentActivity.this, options);
                            } catch (Exception h) {
                                Log.i(TAG, "Error in starting Razorpay Checkout");
                            }
                        }
                        if(cashRB.isChecked()){
                            int cashPrice = 300;
                            if(standardDeliveryRB.isChecked()){
                                cashPrice+=100;
                            }
                            else if(expressDeliveryRB.isChecked()){
                                cashPrice+=500;
                            }
                            Checkout checkout = new Checkout();
                            checkout.setKeyID("rzp_test_raczRd7xDgFF9t");
                            try {
                                JSONObject options = new JSONObject();
                                options.put("name", "TechyTech");
                                options.put("theme.color", "#3399cc");
                                options.put("currency", "INR");
                                options.put("amount", Integer.toString(cashPrice * 100));//pass amount in currency subunits
                                checkout.open(ProceedToPaymentActivity.this, options);
                            } catch (Exception h) {
                                Log.i(TAG, "Error in starting Razorpay Checkout");
                            }
                        }
                    }
                }
            });


        }
        }
        else{


        ArrayList<String> productsInCart = new ArrayList<>();
        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Cart");
        parseQuery.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null && objects.size()>0){
                    for(ParseObject object : objects){
                        productsInCart.add(object.getString("product_id"));
                        totalPrice += Integer.parseInt(object.getString("product_price"));

                    }
                    if(onlineRB.isChecked()) {
                        if(standardDeliveryRB.isChecked()){
                            totalPrice+=0;
                        }
                        else if(expressDeliveryRB.isChecked()){
                            totalPrice+=400;
                        }

                        Checkout checkout = new Checkout();
                        checkout.setKeyID("rzp_test_raczRd7xDgFF9t");
                        try {
                            JSONObject options = new JSONObject();
                            options.put("name", "TechyTech");
                            options.put("theme.color", "#3399cc");
                            options.put("currency", "INR");
                            options.put("amount", Integer.toString(totalPrice * 100));//pass amount in currency subunits
                            checkout.open(ProceedToPaymentActivity.this, options);
                        } catch (Exception h) {
                            Log.i(TAG, "Error in starting Razorpay Checkout");
                        }
                    }
                    if(cashRB.isChecked()){
                        int cashPrice = 300;
                        if(standardDeliveryRB.isChecked()){
                            cashPrice+=100;
                        }
                        else if(expressDeliveryRB.isChecked()){
                            cashPrice+=500;
                        }
                        Checkout checkout = new Checkout();
                        checkout.setKeyID("rzp_test_raczRd7xDgFF9t");
                        try {
                            JSONObject options = new JSONObject();
                            options.put("name", "TechyTech");
                            options.put("theme.color", "#3399cc");
                            options.put("currency", "INR");
                            options.put("amount", Integer.toString(cashPrice * 100));//pass amount in currency subunits
                            checkout.open(ProceedToPaymentActivity.this, options);
                        } catch (Exception h) {
                            Log.i(TAG, "Error in starting Razorpay Checkout");
                        }
                    }

                }
            }
        });
        }

    }


    @Override
    public void onPaymentSuccess(String s) {

        ProgressDialog progressDialog = new ProgressDialog(ProceedToPaymentActivity.this);
        progressDialog.setMessage("Please Wait");
        progressDialog.show();

        if(recieveBuyBowProduct != null) {
            if (recieveBuyBowProduct.equals("") == false) {
                ParseQuery<ParseObject> buyNowProductQuery = ParseQuery.getQuery("Product");
                buyNowProductQuery.whereEqualTo("objectId", recieveBuyBowProduct);
                buyNowProductQuery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e == null && objects.size() > 0) {
                            for (ParseObject object : objects) {
                                object.put("stock",(object.getNumber("stock").intValue()-1));
                                object.saveInBackground();
                                totalPrice = Integer.parseInt(object.getString("our_price"));
                            }
                            ParseObject newOrder = new ParseObject("Orders");
                            newOrder.put("address_id",recievedAddressId);
                            newOrder.put("username", ParseUser.getCurrentUser().getUsername());
                            if(expressDeliveryRB.isChecked()){
                                newOrder.put("delivery_type","express");
                                deliveryMethod = "express";
                            }
                            else if(standardDeliveryRB.isChecked()){
                                newOrder.put("delivery_type","standard");
                                deliveryMethod = "standard";
                            }

                            if(cashRB.isChecked()){
                                newOrder.put("payment_mode","cash");
                                paymentMode = "cash";
                            }
                            else if(onlineRB.isChecked()){
                                newOrder.put("payment_mode","online");
                                paymentMode = "online";
                            }
                            newOrder.put("total_amount", totalPrice);
                            newOrder.put("products", recieveBuyBowProduct);
                            newOrder.put("order_status","Order Placed");
                            newOrder.put("product_seller", recievedSellerName);
                            newOrder.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        Intent intent1 = new Intent(ProceedToPaymentActivity.this, PaymentStatusActivtity.class);
                                        intent1.putExtra("status", true);
                                        progressDialog.dismiss();
                                        startActivity(intent1);
                                        finish();
                                    }
                                    else{
                                        progressDialog.dismiss();
                                        Toast.makeText(ProceedToPaymentActivity.this,e.toString(),Toast.LENGTH_LONG).show();
                                        Log.i("MYMSG",e.toString());
                                    }
                                }
                            });

                        }
                    }
                });
            }
        }
        else {


            ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Cart");
            parseQuery.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
            parseQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {

                    if (e == null && objects.size() > 0) {
                        for (ParseObject object : objects) {
                            ParseQuery<ParseObject> productQuery = ParseQuery.getQuery("Product");
                            productQuery.whereEqualTo("objectId",object.getString("product_id"));
                            productQuery.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> pObjects, ParseException e) {
                                    if(e==null && pObjects.size()>0){
                                        totalPrice = Integer.parseInt(pObjects.get(0).getString("our_price"));
                                        pObjects.get(0).put("stock",(pObjects.get(0).getNumber("stock").intValue()-1));
                                        pObjects.get(0).saveInBackground();
                                        ParseObject newOrder = new ParseObject("Orders");
                                        newOrder.put("address_id",recievedAddressId);
                                        newOrder.put("username", ParseUser.getCurrentUser().getUsername());
                                        if(expressDeliveryRB.isChecked()){
                                            newOrder.put("delivery_type","express");
                                            deliveryMethod = "express";
                                        }
                                        else if(standardDeliveryRB.isChecked()){
                                            newOrder.put("delivery_type","standard");
                                            deliveryMethod = "standard";
                                        }

                                        if(cashRB.isChecked()){
                                            newOrder.put("payment_mode","cash");
                                            paymentMode = "cash";
                                        }
                                        else if(onlineRB.isChecked()){
                                            newOrder.put("payment_mode","online");
                                            paymentMode = "online";
                                        }
                                        newOrder.put("product_seller",pObjects.get(0).getString("seller"));
                                        newOrder.put("total_amount", totalPrice);
                                        newOrder.put("order_status","Order Placed");
                                        newOrder.put("products", pObjects.get(0).getObjectId());
                                        newOrder.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e == null) {
                                                    ParseQuery<ParseObject> cartQuery = ParseQuery.getQuery("Cart");
                                                    cartQuery.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
                                                    cartQuery.findInBackground(new FindCallback<ParseObject>() {
                                                        @Override
                                                        public void done(List<ParseObject> objects, ParseException e) {
                                                            if (e == null && objects.size() > 0) {
                                                                for (ParseObject object : objects) {
                                                                    object.deleteInBackground();
                                                                }
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    }

                                }

                            });
                        }
                        Intent intent1 = new Intent(ProceedToPaymentActivity.this, PaymentStatusActivtity.class);
                        intent1.putExtra("status", true);
                        startActivity(intent1);
                        finish();
                    }
                }
            });
        }


    }

    @Override
    public void onPaymentError(int i, String s) {
        Intent intent2 = new Intent(ProceedToPaymentActivity.this,PaymentStatusActivtity.class);
        intent2.putExtra("status",false);
        startActivity(intent2);
        finish();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}