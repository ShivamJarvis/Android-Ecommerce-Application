package com.example.techytech;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class SeeCompleteOrderActivity extends AppCompatActivity {

    private ImageView productImage;
    private TextView productText,productPrice;
    private Button updateOrderStatusBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_complete_order);
        productImage = findViewById(R.id.see_order_image);
        productText = findViewById(R.id.see_order_name);
        updateOrderStatusBtn = findViewById(R.id.update_order_status_btn);
        productPrice = findViewById(R.id.see_order_price);

        updateButtonText();
        updateOrderStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Orders");
                parseQuery.whereEqualTo("objectId",getIntent().getStringExtra("order_id"));
                parseQuery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if(e==null && objects!=null){
                            if(objects.size()>0){
                                for(ParseObject object : objects){
                                    if(updateOrderStatusBtn.getText().toString().equals("Order Packed")){
                                        object.put("order_status","Order Packed");
                                        updateOrderStatusBtn.setText("Order Shipped");
                                    }
                                    else if(updateOrderStatusBtn.getText().toString().equals("Order Shipped")){
                                        object.put("order_status","Order Shipped");
                                        updateOrderStatusBtn.setText("Order Out For Delivery");
                                    }
                                    else if(updateOrderStatusBtn.getText().toString().equals("Order Out For Delivery")){
                                        object.put("order_status","Out for Delivery");
                                        updateOrderStatusBtn.setText("Order Delivered");
                                    }
                                    else if(updateOrderStatusBtn.getText().toString().equals("Order Delivered")){
                                        object.put("order_status","Order Delivered");
                                        updateOrderStatusBtn.setVisibility(View.GONE);
                                    }
                                    object.saveInBackground();


                                }
                            }
                        }
                    }
                });
            }
        });
    }

    private void updateButtonText(){

        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Orders");
        parseQuery.whereEqualTo("objectId",getIntent().getStringExtra("order_id"));
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null && objects!=null){
                    if(objects.size()>0){
                        for(ParseObject object : objects){
                            productPrice.setText(productPrice.getText()+object.getNumber("total_amount").toString());
                            if(object.getString("order_status").equals("Order Placed")){
                                updateOrderStatusBtn.setText("Order Packed");
                            }
                            else if(object.getString("order_status").equals("Order Packed")){
                                updateOrderStatusBtn.setText("Order Shipped");

                            }
                            else if(object.getString("order_status").equals("Order Shipped")){
                                updateOrderStatusBtn.setText("Order Out For Delivery");
                            }
                            else if(object.getString("order_status").equals("Out for Delivery")){
                                updateOrderStatusBtn.setText("Order Delivered");
                            }

                            ParseQuery<ParseObject> productQuery = ParseQuery.getQuery("Product");
                            productQuery.whereEqualTo("objectId",object.getString("products"));
                            productQuery.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> parseObjects, ParseException e1) {
                                    if(e1==null && parseObjects!=null){
                                        if(parseObjects.size()==0){
                                            Toast.makeText(SeeCompleteOrderActivity.this,"Nothing in Product in",Toast.LENGTH_LONG).show();
                                        }
                                        productText.setText(parseObjects.get(0).getString("product_name"));
                                        ParseFile parseFile = parseObjects.get(0).getParseFile("product_image");
                                        parseFile.getDataInBackground(new GetDataCallback() {
                                            @Override
                                            public void done(byte[] data, ParseException e2) {
                                                if(e2==null && data!=null){
                                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                                                    productImage.setImageBitmap(bitmap);
                                                }
                                            }
                                        });
                                    }
                                    if(objects.size()==0){
                                        Toast.makeText(SeeCompleteOrderActivity.this,"Nothing in Product out",Toast.LENGTH_LONG).show();
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