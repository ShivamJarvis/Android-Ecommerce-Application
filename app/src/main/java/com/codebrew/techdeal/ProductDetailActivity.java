package com.codebrew.techdeal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private CarouselView carouselView;
    private String recievedProductId;
    public ArrayList<Bitmap> pImages;
    private TextView productNameTextView,productDetailName,productPrice,noReview;
    private TextView productDetailMrp,productDetailOurPrice,productSeller,productDesc;
    private Button buyBtn,addCartBtn;
    private SimpleRatingBar simpleRatingBar;
    private CarouselView carouseClicklView;
    private ListView reviewListView;
    private ArrayAdapter reviewArrayAdapter;
    private ArrayList<String> reviewArrayList;


    enum State{
        ADD,Go
    }
    private State state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recievedProductId = getIntent().getStringExtra("productId");
        setContentView(R.layout.activity_product_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        carouseClicklView = findViewById(R.id.product_image_carousel);
        reviewListView = findViewById(R.id.review_list_view);
        reviewArrayList = new ArrayList<>();
        reviewArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,reviewArrayList);
        noReview = findViewById(R.id.no_review);
        state = State.ADD;
        buyBtn = findViewById(R.id.buy_btn);
        addCartBtn = findViewById(R.id.add_cart);
        simpleRatingBar = findViewById(R.id.product_rating);
        buyBtn.setOnClickListener(this);
        addCartBtn.setOnClickListener(this);
        carouselView = findViewById(R.id.product_image_carousel);
        pImages = new ArrayList<>();
        productNameTextView = findViewById(R.id.product_name);
        productDetailName = findViewById(R.id.product_detail_product_name);
        productPrice = findViewById(R.id.product_price);
        productDetailMrp = findViewById(R.id.product_detail_product_mrp);
        productDetailOurPrice = findViewById(R.id.product_detail_product_our_price);
        productSeller = findViewById(R.id.product_detail_seller_name);
        productDesc = findViewById(R.id.product_detail_desc);


        checkProductIsAlreadyInCart();
        showProductDetails();


        ParseQuery<ParseObject> parseQueryForImages = ParseQuery.getQuery("ProductImages");
        parseQueryForImages.whereEqualTo("product_id", recievedProductId);
        parseQueryForImages.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> imageObjects, ParseException e) {
                if (e == null) {
                    if (imageObjects.size() > 0) {
                        carouselView.setImageListener(new ImageListener() {
                            @Override
                            public void setImageForPosition(int position, ImageView imageView) {
                                ParseFile parseFile = (ParseFile) imageObjects.get(position).getParseFile("images");
                                parseFile.getDataInBackground(new GetDataCallback() {
                                    @Override
                                    public void done(byte[] data, ParseException e) {
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                        imageView.setScaleType(ImageView.ScaleType.CENTER);
                                        imageView.setImageBitmap(bitmap);
                                        imageView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(ProductDetailActivity.this,ProductZoomImageActivity.class);
                                                intent.putExtra("productId",recievedProductId);
                                                startActivity(intent);
                                            }
                                        });

                                    }
                                });
                            }
                        });
                        carouselView.setPageCount(imageObjects.size());
                    }
                }
            }
        });

        carouseClicklView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailActivity.this,ProductZoomImageActivity.class);
                intent.putExtra("productId",recievedProductId);
                startActivity(intent);
            }
        });

        showCustomerReview();

    }


    private void checkProductIsAlreadyInCart(){
        ParseQuery<ParseObject> isInCartQuery = ParseQuery.getQuery("Cart");
        isInCartQuery.whereEqualTo("product_id",recievedProductId);
        isInCartQuery.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
        isInCartQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null && objects.size()>0){
                    state = State.Go;
                    addCartBtn.setText("Go To Cart");
                }
            }
        });

    }


    private void showProductDetails(){
        ParseQuery<ParseObject> productDetailQuery = ParseQuery.getQuery("Product");
        productDetailQuery.whereEqualTo("objectId",recievedProductId);

        productDetailQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null && objects.size() > 0){
                    for(ParseObject mObject:objects){
                        productNameTextView.setText(mObject.getString("product_name"));
                        productDetailName.setText(mObject.getString("product_name"));
                        productPrice.setText("Rs. "+ mObject.getString("our_price"));
                        productDetailMrp.setText("Rs. "+mObject.get("mrp_price"));
                        productDetailOurPrice.setText("Rs. "+mObject.get("our_price"));
                        productSeller.setText(mObject.getString("seller"));
                        productDesc.setText(mObject.getString("description"));
                        simpleRatingBar.setRating(Float.parseFloat(mObject.getString("rating")));
                    }
                }
            }
        });
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.add_cart:
                if(state == State.ADD){
                    state = State.Go;
                    addCartBtn.setText("Go To Cart");
                    ParseObject newCartItem = new ParseObject("Cart");
                    newCartItem.put("username", ParseUser.getCurrentUser().getUsername());
                    newCartItem.put("product_id",recievedProductId);

                    ParseQuery<ParseObject> productParseQuery = ParseQuery.getQuery("Product");
                    productParseQuery.whereEqualTo("objectId",recievedProductId);
                    productParseQuery.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if(e==null && objects.size()>0){
                                for(ParseObject object : objects){
                                    newCartItem.put("product_price",object.getString("our_price"));
                                }
                                newCartItem.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if(e==null){
                                            Toast.makeText(ProductDetailActivity.this,"Product added to the cart",Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            Toast.makeText(ProductDetailActivity.this,"Something Went Wrong",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    });



                }
                else if(state == State.Go){
                    Intent goToCartIntent = new Intent(ProductDetailActivity.this,CartActivity.class);
                    startActivity(goToCartIntent);
                }
                break;
            case R.id.buy_btn:
                ParseQuery<ParseObject> buyNowQuery = ParseQuery.getQuery("Product");
                buyNowQuery.whereEqualTo("objectId",recievedProductId);
                buyNowQuery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if(e==null && objects!=null){
                            if(objects.size()>0){
                                Intent buyNow = new Intent(ProductDetailActivity.this,ProceedToCheckoutActivity.class);
                                buyNow.putExtra("buy_now_product",recievedProductId);
                                buyNow.putExtra("seller_name",objects.get(0).getString("seller"));
                                startActivity(buyNow);
                            }
                        }
                    }
                });


                break;
        }





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
                            Intent intent = new Intent(ProductDetailActivity.this,ChooseLoginSignupActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
                break;
            case R.id.my_profile_item:
                Intent profileIntent = new Intent(ProductDetailActivity.this,ProfileActivity.class);
                startActivity(profileIntent);
                break;
            case R.id.my_order_item:
                Intent orderIntent = new Intent(ProductDetailActivity.this,OrdersActivity.class);
                startActivity(orderIntent);
                break;
            case R.id.my_cart_item:
                Intent cartIntent = new Intent(ProductDetailActivity.this,CartActivity.class);
                startActivity(cartIntent);
                break;
            case R.id.seller_login_register:

                Intent sellerLoginRegisterIntent = new Intent(ProductDetailActivity.this,SellerMainActivity.class);
                startActivity(sellerLoginRegisterIntent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    private void showCustomerReview(){
        ParseQuery<ParseObject> reviewQuery = ParseQuery.getQuery("Review");
        reviewQuery.whereEqualTo("product_id",recievedProductId);
        reviewQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){
                    if(objects.size()>0){
                        for(ParseObject parseObject : objects){
                            reviewArrayList.add(parseObject.getString("review"));
                        }
                        reviewListView.setAdapter(reviewArrayAdapter);
                    }
                    else{
                        reviewListView.setVisibility(View.GONE);
                        noReview.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}