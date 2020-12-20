package com.example.techytech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class CategoryDetailActivity extends AppCompatActivity implements CategoryDetailRecyclerAdapter.ProductItemIsClickedInterface {
    private String categoryName;
    private ArrayList<Bitmap> prodImages;
    private ArrayList<String> prodNames;
    private ArrayList<String> prodPrices;
    private ArrayList<String> prodId;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.logo);
        setTitle("");

        categoryName = getIntent().getStringExtra("categoryName");
        prodImages = new ArrayList<>();
//        Toast.makeText(this, categoryName, Toast.LENGTH_SHORT).show();
        prodNames = new ArrayList<>();
        prodPrices = new ArrayList<>();
        prodId = new ArrayList<>();
        mRecyclerView = findViewById(R.id.category_detail_recycler_view);
        updateProductList();


        mRecyclerView.setLayoutManager(new LinearLayoutManager(CategoryDetailActivity.this));

    }



    private void updateProductList(){
        ParseQuery<ParseObject> findProductQuery = ParseQuery.getQuery("Product");
        findProductQuery.whereEqualTo("category",categoryName);
        findProductQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null){
                    if (objects.size()>0){
                        for(ParseObject product : objects){
                            prodNames.add(product.getString("product_name"));
                            prodId.add(product.getObjectId());
                            prodPrices.add(product.getString("our_price"));
                            ParseFile image = product.getParseFile("product_image");
                            image.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {
                                    if(e==null){
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                                        prodImages.add(bitmap);
                                        mRecyclerView.setAdapter(new CategoryDetailRecyclerAdapter(prodImages,prodNames,prodPrices,prodId,CategoryDetailActivity.this));

                                    }


                                }

                            });

                        }
                    }
                    else{
//                        Toast.makeText(CategoryDetailActivity.this,"No Object Found",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
//                    Toast.makeText(CategoryDetailActivity.this,"Something Went Wrong",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void ProductItemClicked(String productId) {
        Intent intent = new Intent(CategoryDetailActivity.this,ProductDetailActivity.class);
        intent.putExtra("productId",productId);
        startActivity(intent);
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
                            Intent intent = new Intent(CategoryDetailActivity.this,ChooseLoginSignupActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
                break;
            case R.id.my_profile_item:
                Intent profileIntent = new Intent(CategoryDetailActivity.this,ProfileActivity.class);
                startActivity(profileIntent);
                break;
            case R.id.my_order_item:
                Intent orderIntent = new Intent(CategoryDetailActivity.this,OrdersActivity.class);
                startActivity(orderIntent);
                break;
            case R.id.my_cart_item:
                Intent cartIntent = new Intent(CategoryDetailActivity.this,CartActivity.class);
                startActivity(cartIntent);
                break;
            case R.id.seller_login_register:

                Intent sellerLoginRegisterIntent = new Intent(CategoryDetailActivity.this,SellerMainActivity.class);
                startActivity(sellerLoginRegisterIntent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }




}