package com.codebrew.techdeal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;

import java.util.List;

public class SellerMainActivity extends AppCompatActivity implements SellerPendingOrderRecyclerAdapter.SeeOrderButtonIsClickedInterface, InventoryRecyclerAdapter.IsInventoryItemIsClicked {
    private TextView sellerWelcomeText,sellerIncome,sellerCharges,sellerNetIncome;
    private RecyclerView pendingOrdersRecyclerView,inventoryRecyclerView;
    private Button refreshButtonList;
    private ArrayList<String> objectsIds, inventoryObjectIds;
    private String sellerName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_main);

        sellerWelcomeText = findViewById(R.id.seller_welcome_text);
        sellerIncome = findViewById(R.id.seller_income);
        sellerCharges = findViewById(R.id.seller_charges);
        sellerNetIncome = findViewById(R.id.seller_net_income);
        refreshButtonList = findViewById(R.id.refresh_button_list);
        inventoryRecyclerView = findViewById(R.id.inventory_recycler_view);
        inventoryObjectIds = new ArrayList<>();
        calculateSellerMonthlyIncome();
        objectsIds = new ArrayList<>();
        ParseQuery<ParseObject> parseSellerParseQuery = ParseQuery.getQuery("Seller");
        parseSellerParseQuery.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
        parseSellerParseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null && objects!=null){
                    if(objects.size()>0){
                        sellerName = objects.get(0).getString("seller");
                        sellerWelcomeText.setText(sellerWelcomeText.getText()+objects.get(0).getString("seller"));

                    }
                }
            }
        });
        sellerName = "";
        getInventoryObjectIds();
        inventoryRecyclerView.setLayoutManager(new LinearLayoutManager(SellerMainActivity.this));
        pendingOrdersRecyclerView = findViewById(R.id.seller_pending_orders_recycler_view);
        getObjectsIds();
        pendingOrdersRecyclerView.setLayoutManager(new LinearLayoutManager(SellerMainActivity.this));
        refreshButtonList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SellerPendingOrderRecyclerAdapter adapter = (SellerPendingOrderRecyclerAdapter) pendingOrdersRecyclerView.getAdapter();
                if(adapter!=null){
                    getObjectsIds();
                    adapter.notifyDataSetChanged();
                }
                else{
                    getObjectsIds();
                }
            }
        });


    }

    private void getInventoryObjectIds() {

        ParseQuery<ParseObject> sellerQuery = ParseQuery.getQuery("Seller");
        sellerQuery.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
        sellerQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null)
                {
                    ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Product");
                    parseQuery.whereEqualTo("seller",objects.get(0).getString("seller"));
                    parseQuery.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if(objects!=null && e == null)
                            {
                                for(ParseObject object : objects)
                                {
                                    inventoryObjectIds.add(object.getObjectId());
                                }
                                inventoryRecyclerView.setAdapter(new InventoryRecyclerAdapter(inventoryObjectIds,SellerMainActivity.this));

                            }
                        }
                    });
                }
            }
        });




    }


    private void getObjectsIds(){
        ProgressDialog progressDialog = new ProgressDialog(SellerMainActivity.this);
        progressDialog.setMessage("Please Wait");
        progressDialog.show();
        if(objectsIds.size()>0){
            objectsIds.clear();
        }
        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Orders");
        parseQuery.whereNotEqualTo("order_status","Order Delivered");
        parseQuery.whereEqualTo("product_seller",sellerName);
        Toast.makeText(SellerMainActivity.this,sellerName,Toast.LENGTH_SHORT).show();
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null && objects!=null){
                    if(objects.size()>0){
                        for(ParseObject parseObject : objects){
                            objectsIds.add(parseObject.getObjectId());
                        }
                        pendingOrdersRecyclerView.setAdapter(new SellerPendingOrderRecyclerAdapter(objectsIds,SellerMainActivity.this));

                    }

                }
                progressDialog.dismiss();
            }
        });
    }


    @Override
    public void SeeOrderButtonIsClicked(String orderNo) {
        Intent intent = new Intent(SellerMainActivity.this,SeeCompleteOrderActivity.class);
        intent.putExtra("order_id",orderNo);
        startActivity(intent);
        finish();
    }


    private void calculateSellerMonthlyIncome(){
        ParseQuery<ParseObject> sellerParseQuery = ParseQuery.getQuery("Seller");
        sellerParseQuery.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
        sellerParseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> userObjects, ParseException userE) {
                if(userE==null && userObjects!=null){
                    if(userObjects.size()>0){
                        String userSellerName = userObjects.get(0).getString("seller");
                        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Orders");
                        parseQuery.whereEqualTo("product_seller",userSellerName);
                        parseQuery.whereEqualTo("order_status","Order Delivered");
                        parseQuery.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> objects, ParseException e) {
                                if(e==null && objects!=null){
                                    if(objects.size()>0){
                                        int totalAmount = 0;
                                        int totalCharges = 0;
                                        for(ParseObject object : objects){
                                            totalAmount += (Integer) object.getNumber("total_amount");
                                            totalCharges += 200;
                                        }
                                        sellerIncome.setText("Rs. "+totalAmount);
                                        sellerCharges.setText("Rs. "+totalCharges);
                                        sellerNetIncome.setText("Rs. "+(totalAmount-totalCharges));
                                    }

                                }

                            }
                        });
                    }

                }

            }
        });
    }



    @Override
    public void InventoryItemIsClicked(String objectId) {
        Intent intent = new Intent(SellerMainActivity.this,ManageInventory.class);
        intent.putExtra("productId",objectId);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}