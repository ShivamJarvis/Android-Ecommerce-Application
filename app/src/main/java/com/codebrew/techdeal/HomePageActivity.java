package com.codebrew.techdeal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import com.google.android.material.navigation.NavigationView;
import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;
import java.util.ArrayList;
import java.util.List;

public class HomePageActivity extends AppCompatActivity implements FeaturedProductsRecyclerAdapter.ProductIsClickedInterface, View.OnClickListener, PopularProductsRecyclerAdapter.PopularProductIsClickedInterface {
    private int[] banners = {R.drawable.banner1,R.drawable.banner2,R.drawable.banner3,R.drawable.banner4,R.drawable.banner5};
    private CarouselView carouselView;
    private RecyclerView featureRecyclerView,popularRecyclerView;
    ArrayList<Bitmap> productImages = new ArrayList<Bitmap>();
    ArrayList<String> productId = new ArrayList<>();
    ArrayList<String> popularProductId = new ArrayList<>();
    private Toolbar toolbar;
    private NavigationView nav;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawerLayout;

    ArrayList<String>  productNames = new ArrayList<String>();
    private ProgressDialog mProgressDialog;
    private TextView searchEditText;

    private ImageView motherboardCat,graphicCardCat,cpuCat,processorCat,ramCat,storageCat,usbCat,mointorCat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please Wait");
        mProgressDialog.show();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Window window = this.getWindow();
        window.setStatusBarColor(getColor(R.color.primaryDarkColor));

        nav = findViewById(R.id.nav_menu);
        drawerLayout = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        searchEditText = findViewById(R.id.search_edit_text);
        searchEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToSearchArea = new Intent(HomePageActivity.this,SearchTypeActivity.class);
                startActivity(goToSearchArea);
            }
        });
        if(ParseUser.getCurrentUser()==null){
            Intent goToSignIn = new Intent(HomePageActivity.this,ChooseLoginSignupActivity.class);
            startActivity(goToSignIn);
            finish();
        }

        popularRecyclerView = findViewById(R.id.popular_products_recycler_view);
        motherboardCat = findViewById(R.id.motherboard_cat);
        graphicCardCat = findViewById(R.id.graphic_card_cat);
        cpuCat = findViewById(R.id.cpu_cabinets_cat);
        processorCat =findViewById(R.id.processor_cat);
        ramCat = findViewById(R.id.ram_cat);
        storageCat = findViewById(R.id.storage_cat);
        usbCat = findViewById(R.id.usb_cat);
        mointorCat = findViewById(R.id.monitor_cat);

        motherboardCat.setOnClickListener(this);
        graphicCardCat.setOnClickListener(this);
        cpuCat.setOnClickListener(this);
        processorCat.setOnClickListener(this);
        ramCat.setOnClickListener(this);
        storageCat.setOnClickListener(this);
        usbCat.setOnClickListener(this);
        mointorCat.setOnClickListener(this);

        carouselView = findViewById(R.id.banners_carousel_view);
        carouselView.setPageCount(banners.length);
        carouselView.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageResource(banners[position]);
            }
        });


        featureRecyclerView = findViewById(R.id.featured_products_recycler_view);
        LinearLayoutManager featureLinearLayoutManager = new LinearLayoutManager(HomePageActivity.this);
        featureLinearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        updateFeatureProductList();
        featureRecyclerView.setLayoutManager(featureLinearLayoutManager);

        LinearLayoutManager popularLinearLayoutManager = new LinearLayoutManager(HomePageActivity.this);
        popularLinearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        updatePopularProductList();
        popularRecyclerView.setLayoutManager(popularLinearLayoutManager);

        nav.bringToFront();
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.menu_logout_item:
                        ParseUser.logOutInBackground(new LogOutCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(e==null){
                                    Intent intent = new Intent(HomePageActivity.this,ChooseLoginSignupActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.menu_profile_item:
                        Intent profileIntent = new Intent(HomePageActivity.this,ProfileActivity.class);
                        startActivity(profileIntent);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.menu_order_item:
                        Intent orderIntent = new Intent(HomePageActivity.this,OrdersActivity.class);
                        startActivity(orderIntent);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.menu_seller_login_register:
                        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Seller");
                        parseQuery.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
                        parseQuery.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> objects, ParseException e) {
                                if(e==null)
                                {
                                    if(objects!=null)
                                    {
                                        Intent sellerLoginRegisterIntent = new Intent(HomePageActivity.this,SellerMainActivity.class);
                                        sellerLoginRegisterIntent.putExtra("sellerName",objects.get(0).getString("seller"));
                                        startActivity(sellerLoginRegisterIntent);
                                    }
                                    else{
                                        Intent sellerLoginRegisterIntent = new Intent(HomePageActivity.this,SellerLoginRegisterActivity.class);
                                        startActivity(sellerLoginRegisterIntent);
                                    }
                                }
                            }
                        });

                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                }
                return false;
            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();
        mProgressDialog.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_page_cart_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.my_cart_item:
                Intent cartIntent = new Intent(HomePageActivity.this,CartActivity.class);
                startActivity(cartIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateFeatureProductList(){
        ProgressDialog progressDialog = new ProgressDialog(HomePageActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        ParseQuery<ParseObject> featuredProductsQuery = new ParseQuery<ParseObject>("Product");
        featuredProductsQuery.whereEqualTo("isFeatured",true);
        if(productId.size()>0){
            productId.clear();
        }

        featuredProductsQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> allProducts, ParseException e) {
                if(allProducts!=null) {
                    if (allProducts.size() > 0 && e == null) {
                        for (ParseObject product : allProducts) {

                            productId.add(product.getObjectId());

                        }
                        featureRecyclerView.setAdapter(new FeaturedProductsRecyclerAdapter(HomePageActivity.this, productId, HomePageActivity.this));
                        progressDialog.dismiss();
                    }
                }

                  else{
                    progressDialog.dismiss();
                }
                }


        });
    }

    @Override
    public void ProductIsClicked(String mProductId) {
        Intent intent = new Intent(HomePageActivity.this,ProductDetailActivity.class);
        intent.putExtra("productId",mProductId);
        startActivity(intent);
    }


    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.motherboard_cat:
                Intent intent1 = new Intent(HomePageActivity.this,CategoryDetailActivity.class);
                intent1.putExtra("categoryName","Motherboard");
                startActivity(intent1);
                break;

                case R.id.graphic_card_cat:
                Intent intent2 = new Intent(HomePageActivity.this,CategoryDetailActivity.class);
                intent2.putExtra("categoryName","Graphic Cards");
                startActivity(intent2);
                break;
                case R.id.cpu_cabinets_cat:
                Intent intent3 = new Intent(HomePageActivity.this,CategoryDetailActivity.class);
                intent3.putExtra("categoryName","Cabinets");
                startActivity(intent3);
                break;
                case R.id.processor_cat:
                Intent intent4 = new Intent(HomePageActivity.this,CategoryDetailActivity.class);
                intent4.putExtra("categoryName","Processor");
                startActivity(intent4);
                break;

                case R.id.ram_cat:
                Intent intent5 = new Intent(HomePageActivity.this,CategoryDetailActivity.class);
                intent5.putExtra("categoryName","RAM");
                startActivity(intent5);
                break;

                case R.id.storage_cat:
                Intent intent6 = new Intent(HomePageActivity.this,CategoryDetailActivity.class);
                intent6.putExtra("categoryName","Storage");
                startActivity(intent6);
                break;
                case R.id.usb_cat:
                Intent intent7 = new Intent(HomePageActivity.this,CategoryDetailActivity.class);
                intent7.putExtra("categoryName","USB");
                startActivity(intent7);
                break;
            case R.id.monitor_cat:
                Intent intent8 = new Intent(HomePageActivity.this,CategoryDetailActivity.class);
                intent8.putExtra("categoryName","Monitors");
                startActivity(intent8);
                break;


        }

    }


    private void updatePopularProductList(){
        ParseQuery<ParseObject> featuredProductsQuery = new ParseQuery<ParseObject>("Product");
        featuredProductsQuery.whereEqualTo("isPopular",true);

        if(productId.size()>0){
            productId.clear();
        }


        featuredProductsQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> allProducts, ParseException e) {
                if(allProducts!=null){
                    if(allProducts.size()>0 && e==null){
                        for(ParseObject product:allProducts){

                            popularProductId.add(product.getObjectId());

                        }
                        popularRecyclerView.setAdapter(new PopularProductsRecyclerAdapter(popularProductId,HomePageActivity.this,HomePageActivity.this));
                    }
                }

            }
        });
    }


    @Override
    public void PopularProductIsClicked(String pProductId) {
        Intent intent = new Intent(HomePageActivity.this,ProductDetailActivity.class);
        intent.putExtra("productId",pProductId);
        startActivity(intent);
    }
}