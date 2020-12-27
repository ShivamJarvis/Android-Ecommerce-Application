package com.codebrew.techdeal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements CartRecyclerAdapter.RemoveCartButtonIsClickedInterface, View.OnClickListener {
    private ArrayList<String> prodId;
    private RecyclerView mRecyclerView;
    private TextView totalMrp,totalDiscount,netAmount,totalMrpTxt,totalDiscTxt,netAmtTxt;
    private int totalAmount=0,totalDisc=0,totalNetAmount=0;
    private boolean isCartEmpty = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.logo);
        setTitle("");
        prodId = new ArrayList<>();
        totalMrp = findViewById(R.id.total_mrp);
        Button continueShoppingBtn = findViewById(R.id.continue_shopping_btn);
        Button proceedToCheckoutBtn = findViewById(R.id.proceed_checkout_btn);
        continueShoppingBtn.setOnClickListener(this);
        proceedToCheckoutBtn.setOnClickListener(this);
        totalDiscTxt = findViewById(R.id.tot_disc_txt);
        totalMrpTxt = findViewById(R.id.tot_amount_txt);
        netAmtTxt = findViewById(R.id.tot_net_amount_txt);
        totalDiscount = findViewById(R.id.total_discount);
        netAmount = findViewById(R.id.total_net_amount);

        mRecyclerView = findViewById(R.id.cart_recycler_view);

        updateCartItems();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(CartActivity.this));

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
                            Intent intent = new Intent(CartActivity.this,ChooseLoginSignupActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
                break;
            case R.id.my_profile_item:
                Intent profileIntent = new Intent(CartActivity.this,ProfileActivity.class);
                startActivity(profileIntent);
                break;
            case R.id.my_order_item:
                Intent orderIntent = new Intent(CartActivity.this,OrdersActivity.class);
                startActivity(orderIntent);
                break;
            case R.id.my_cart_item:
                break;
            case R.id.seller_login_register:
                Intent sellerLoginRegisterIntent = new Intent(CartActivity.this,SellerMainActivity.class);
                startActivity(sellerLoginRegisterIntent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void RemoveButtonClicked(String productId, int mPosition) {
        ProgressDialog removerCartProgressDialog = new ProgressDialog(CartActivity.this);
        removerCartProgressDialog.setMessage("Loading");
        removerCartProgressDialog.show();
        ParseQuery<ParseObject> itemQuery = ParseQuery.getQuery("Cart");
        itemQuery.whereEqualTo("product_id",productId);
        itemQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null && objects!=null){
                    for(ParseObject object : objects){
                        object.deleteInBackground(new DeleteCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(e==null) {
                                    updateCartItems();
                                    CartRecyclerAdapter cartRecyclerAdapter = (CartRecyclerAdapter) mRecyclerView.getAdapter();
                                    cartRecyclerAdapter.notifyDataSetChanged();
                                    removerCartProgressDialog.dismiss();
                                }
                                else{
                                    removerCartProgressDialog.dismiss();
                                    Toast.makeText(CartActivity.this,"Something Went Wrong",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    private void updateCartItems(){
        if(prodId.size()>0){
            prodId.clear();
        }


        if(totalAmount!=0){
            totalAmount = 0;
            totalDisc = 0;
            totalNetAmount = 0;
        }

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.show();

        ParseQuery<ParseObject> cartItemsQuery = ParseQuery.getQuery("Cart");
        cartItemsQuery.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
        cartItemsQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){
                    if(objects.size()>0){
                        for(ParseObject cartItem : objects){
                            ParseQuery<ParseObject> getProductQuery = ParseQuery.getQuery("Product");
                            getProductQuery.whereEqualTo("objectId",cartItem.getString("product_id"));
                            getProductQuery.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> pObjects, ParseException e) {
                                    if(e==null){
                                        for(ParseObject product : pObjects){

                                            totalAmount += product.getInt("mrp_price");
                                            totalDisc = (product.getInt("mrp_price") - Integer.parseInt(product.getString("our_price"))) + totalDisc;
                                            totalNetAmount += Integer.parseInt(product.getString("our_price"));
                                            prodId.add(product.getObjectId());
                                        }
                                        mRecyclerView.setAdapter(new CartRecyclerAdapter(CartActivity.this,prodId));
                                        totalDiscount.setText(totalDisc+"");
                                        totalMrp.setText(totalAmount+"");
                                        netAmount.setText(totalNetAmount+"");

                                    }
                                }
                            });

                        }
                    }
                    else{

                        Toast.makeText(CartActivity.this,"Cart is Empty",Toast.LENGTH_LONG).show();
                        totalMrp.setText("Cart is Empty");
                        isCartEmpty = true;
                        totalMrp.setTextSize(30.0f);
                        totalDiscount.setText("");
                        netAmount.setText("");
                        totalDiscTxt.setText("");
                        totalMrpTxt.setText("");
                        netAmtTxt.setText("");
                    }
                    progressDialog.dismiss();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.continue_shopping_btn:
                Intent backToHome = new Intent(CartActivity.this,HomePageActivity.class);
                startActivity(backToHome);
                finish();
                break;
            case R.id.proceed_checkout_btn:
                if(isCartEmpty){
                    Toast.makeText(CartActivity.this,"Sorry Cart is Empty",Toast.LENGTH_LONG).show();
                }else {
                    Intent intent = new Intent(CartActivity.this,ProceedToCheckoutActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }
}