package com.example.techytech;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class SellerMainActivity extends AppCompatActivity implements SellerPendingOrderRecyclerAdapter.SeeOrderButtonIsClickedInterface {
    private TextView sellerWelcomeText;
    private RecyclerView pendingOrdersRecyclerView;
    private ArrayList<String> objectsIds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_main);
        sellerWelcomeText = findViewById(R.id.seller_welcome_text);
        objectsIds = new ArrayList<>();
        ParseQuery<ParseUser> parseUserParseQuery = ParseUser.getQuery();
        parseUserParseQuery.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
        parseUserParseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e==null && objects!=null){
                    if(objects.size()>0){
                        sellerWelcomeText.setText(sellerWelcomeText.getText()+objects.get(0).getString("seller"));

                    }
                }
            }
        });

        pendingOrdersRecyclerView = findViewById(R.id.seller_pending_orders_recycler_view);
        getObjectsIds();
        pendingOrdersRecyclerView.setLayoutManager(new LinearLayoutManager(SellerMainActivity.this));

    }


    private void getObjectsIds(){
        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Orders");
        parseQuery.whereNotEqualTo("order_status","Order Delivered");
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
            }
        });
    }


    @Override
    public void SeeOrderButtonIsClicked(String orderNo) {
        Intent intent = new Intent(SellerMainActivity.this,SeeCompleteOrderActivity.class);
        intent.putExtra("order_id",orderNo);
        startActivity(intent);
    }
}