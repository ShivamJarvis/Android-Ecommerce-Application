package com.codebrew.techdeal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class SearchFoundActivity extends AppCompatActivity implements SearchRecyclerAdapter.SearchItemClickListenerInterface, View.OnClickListener {
    private String recievedSearchKey;
    private ArrayList<String> productNames;

    private RecyclerView mRecyclerView;
    private TextView noResultFound;
    private ImageView noResultFoundImage;
    private FloatingActionButton sortBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_found);
        recievedSearchKey = getIntent().getStringExtra("searchKey");
        productNames = new ArrayList<>();
        mRecyclerView = findViewById(R.id.search_recycler_view);
        noResultFound = findViewById(R.id.no_result_found);
        noResultFoundImage = findViewById(R.id.no_result_found_image);
        sortBtn = findViewById(R.id.sort_by_btn);

        sortBtn.setOnClickListener(this);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.show();

        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Product");

        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(objects!=null){
                    if(e==null && objects.size()>0){
                        for(ParseObject object : objects){
                            boolean isAlreadyAdded = false;
                            if(object.getString("product_name").toLowerCase().contains(recievedSearchKey) && isAlreadyAdded == false){
                                productNames.add(object.getObjectId());
                                isAlreadyAdded = true;
                            }
                            if(object.getString("category").toLowerCase().contains(recievedSearchKey) && isAlreadyAdded == false){
                                productNames.add(object.getObjectId());
                                isAlreadyAdded = true;
                            }
                        }
                        mRecyclerView.setAdapter(new SearchRecyclerAdapter(productNames,SearchFoundActivity.this));
                        progressDialog.dismiss();
                        if(productNames.size()==0){
                            noResultFound.setVisibility(View.VISIBLE);
                            noResultFoundImage.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void SearchItemIsClicked(String productId) {
        Intent intent = new Intent(SearchFoundActivity.this,ProductDetailActivity.class);
        intent.putExtra("productId",productId);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sort_by_btn:
                AlertDialog.Builder builder = new AlertDialog.Builder(SearchFoundActivity.this);
                LayoutInflater layoutInflater = SearchFoundActivity.this.getLayoutInflater();
                View dialogView = layoutInflater.inflate(R.layout.alert_dialog_box,null);
                RadioButton priceByDescending = dialogView.findViewById(R.id.priceByDescending);
                RadioButton priceByAscending = dialogView.findViewById(R.id.priceByAscending);
                builder.setView(dialogView);
                builder.setPositiveButton("Sort", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Product");
                                if(priceByAscending.isChecked()){
                                    parseQuery.addAscendingOrder("our_price");
                                }
                                if(priceByDescending.isChecked()){
                                    parseQuery.addDescendingOrder("our_price");
                                }
                                ProgressDialog progressDialog = new ProgressDialog(SearchFoundActivity.this);
                                progressDialog.setMessage("Loading");
                                progressDialog.show();

                                if(productNames.size()>0){
                                    productNames.clear();
                                }

                                parseQuery.findInBackground(new FindCallback<ParseObject>() {
                                    @Override
                                    public void done(List<ParseObject> objects, ParseException e) {
                                        if(objects!=null){
                                            if(e==null && objects.size()>0){
                                                for(ParseObject object : objects){
                                                    boolean isAlreadyAdded = false;
                                                    if(object.getString("product_name").toLowerCase().contains(recievedSearchKey) && isAlreadyAdded == false){
                                                        productNames.add(object.getObjectId());
                                                        isAlreadyAdded = true;
                                                    }
                                                    if(object.getString("category").toLowerCase().contains(recievedSearchKey) && isAlreadyAdded == false){
                                                        productNames.add(object.getObjectId());
                                                        isAlreadyAdded = true;
                                                    }
                                                }
                                                mRecyclerView.setAdapter(new SearchRecyclerAdapter(productNames,SearchFoundActivity.this));
                                                progressDialog.dismiss();
                                                if(productNames.size()==0){
                                                    noResultFound.setVisibility(View.VISIBLE);
                                                    noResultFoundImage.setVisibility(View.VISIBLE);
                                                }
                                            }
                                        }
                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}