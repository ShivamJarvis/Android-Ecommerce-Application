package com.example.techytech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ProgressCallback;

import java.util.ArrayList;
import java.util.List;

public class OrdersActivity extends AppCompatActivity implements OrderRecyclerAdapter.OrderItemIsClickedInterface {
    private RecyclerView orderRecyclerView;
    private ArrayList<String> orderObjectId,orderStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.logo);
        setTitle("");
        orderObjectId = new ArrayList<>();

        orderRecyclerView = findViewById(R.id.order_recycler_view);
        orderStatus = new ArrayList<>();
        ProgressDialog progressDialog = new ProgressDialog(OrdersActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Orders");
        parseQuery.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){
                    if(objects.size()>0){
                        for(ParseObject object : objects){
                            orderObjectId.add(object.getObjectId());
                            orderStatus.add(object.getString("order_status"));
                        }
                        orderRecyclerView.setAdapter(new OrderRecyclerAdapter(orderObjectId,OrdersActivity.this,OrdersActivity.this,orderStatus));
                        progressDialog.dismiss();
                    }
                }
            }
        });




        orderRecyclerView.setLayoutManager(new LinearLayoutManager(OrdersActivity.this));



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_home_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout_item:
                ParseUser.logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null){
                            Intent intent = new Intent(OrdersActivity.this,ChooseLoginSignupActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
                break;
            case R.id.my_profile_item:
                Intent profileIntent = new Intent(OrdersActivity.this,ProfileActivity.class);
                startActivity(profileIntent);
                break;
            case R.id.my_order_item:
                break;
            case R.id.my_cart_item:
                Intent cartIntent = new Intent(OrdersActivity.this, CartActivity.class);
                startActivity(cartIntent);
                break;
            case R.id.seller_login_register:

                Intent sellerLoginRegisterIntent = new Intent(OrdersActivity.this,SellerMainActivity.class);
                startActivity(sellerLoginRegisterIntent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OrderItemIsClicked(String orderId,String orderStatus) {
        Intent intent = new Intent(OrdersActivity.this,OrderDetailsActivity.class);
        intent.putExtra("orderObjectId",orderId);
        intent.putExtra("orderStatus",orderStatus);
        startActivity(intent);
    }
}