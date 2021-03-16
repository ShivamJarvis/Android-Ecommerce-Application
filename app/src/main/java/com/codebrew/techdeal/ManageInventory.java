package com.codebrew.techdeal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class ManageInventory extends AppCompatActivity implements View.OnClickListener {
    private String productId;
    private ImageView productImage;
    private TextView productName;
    private EditText availableQty, mrpPrice, sellingPrice;
    private Button updateInventoryButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_inventory);
        productId = getIntent().getStringExtra("productId");
        productImage = findViewById(R.id.see_product_image);
        productName = findViewById(R.id.see_product_name);
        availableQty = findViewById(R.id.available_qty);
        mrpPrice = findViewById(R.id.mrp_price);
        sellingPrice = findViewById(R.id.selling_price);
        updateInventoryButton = findViewById(R.id.update_inventory_button);
        getProductDetails();
        updateInventoryButton.setOnClickListener(ManageInventory.this);

    }

    private void getProductDetails() {
        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Product");
        parseQuery.whereEqualTo("objectId",productId);
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(objects!=null && e==null)
                {
                    productName.setText(objects.get(0).getString("product_name"));
                    availableQty.setText(objects.get(0).getNumber("stock").toString());
                    mrpPrice.setText(objects.get(0).getNumber("mrp_price").toString());
                    sellingPrice.setText(objects.get(0).getString("our_price"));
                    ParseFile parseFile = objects.get(0).getParseFile("product_image");
                    parseFile.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, ParseException e) {
                            if(data!=null && e==null)
                            {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                                productImage.setImageBitmap(bitmap);
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Product");
        parseQuery.whereEqualTo("objectId",productId);
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(objects!=null && e==null)
                {
                   objects.get(0).put("mrp_price",Integer.parseInt(mrpPrice.getText().toString()));
                   objects.get(0).put("our_price",sellingPrice.getText().toString());
                   objects.get(0).put("stock",Integer.parseInt(availableQty.getText().toString()));
                   objects.get(0).saveInBackground();
                    Snackbar.make(v,"Product Updated successfully!",Snackbar.LENGTH_LONG).show();
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

