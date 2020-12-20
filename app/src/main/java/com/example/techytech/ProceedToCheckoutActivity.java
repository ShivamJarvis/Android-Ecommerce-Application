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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class ProceedToCheckoutActivity extends AppCompatActivity implements AddressRecyclerAdapter.AddressIsClickedInterface {
    private ArrayList<String> adressesList,stateCityList,zipCodeList,addressId;
    private RecyclerView mRecyclerView;
    private TextView addNewAddressTextView,saveAddressesText;
    private Button addNewAddressButton;
    private EditText stateText,cityText,zipCodetext,addressLine1Text,addressLine2Text;
    private String recievedBuyNowProductId,recievedBuyNowSellerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_proceed_to_checkout);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.logo);
        setTitle("");
        try{
            recievedBuyNowProductId = getIntent().getStringExtra("buy_now_product");
            recievedBuyNowSellerName = getIntent().getStringExtra("seller_name");
        }catch (Exception e){

        }
        mRecyclerView = findViewById(R.id.address_recycler_view);
        adressesList = new ArrayList<>();
        stateText = findViewById(R.id.state);
        saveAddressesText = findViewById(R.id.saved_address_text);
        cityText = findViewById(R.id.city);
        zipCodetext = findViewById(R.id.zip_code);
        addressLine1Text = findViewById(R.id.address_line_1);
        addressLine2Text = findViewById(R.id.address_line_2);
        addNewAddressButton = findViewById(R.id.proceed_payment_btn);
        stateCityList = new ArrayList<>();
        zipCodeList = new ArrayList<>();
        addressId = new ArrayList<>();
        addNewAddressTextView = findViewById(R.id.to_be_change_add_address);

        fetchUserAllAddress();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(ProceedToCheckoutActivity.this));

        addNewAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(addressLine1Text.getText().toString().equals("")||
                stateText.getText().toString().equals("")||
                cityText.getText().toString().equals("")||
                zipCodetext.getText().toString().equals("")){

                    Toast.makeText(ProceedToCheckoutActivity.this,"Please specify complete details",Toast.LENGTH_LONG).show();
                    return;
                }


                ParseObject newAddress = new ParseObject("UserAddress");
                newAddress.put("username",ParseUser.getCurrentUser().getUsername());
                newAddress.put("address_line_1",addressLine1Text.getText().toString());
                newAddress.put("address_line_2",addressLine2Text.getText().toString());
                newAddress.put("state",stateText.getText().toString());
                newAddress.put("city",cityText.getText().toString());
                newAddress.put("zip_code",zipCodetext.getText().toString());

                newAddress.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null){
                            Toast.makeText(ProceedToCheckoutActivity.this,"Address is saved for future orders",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(ProceedToCheckoutActivity.this,ProceedToPaymentActivity.class);
                            intent.putExtra("addressId",newAddress.getObjectId());
                            startActivity(intent);
                        }
                    }
                });

            }
        });


    }


    private void fetchUserAllAddress(){
        ProgressDialog progressDialog = new ProgressDialog(ProceedToCheckoutActivity.this);
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        ParseQuery<ParseObject> userAddressQuery = ParseQuery.getQuery("UserAddress");
        userAddressQuery.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        userAddressQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){
                    if(objects.size()>0){
                        for(ParseObject object : objects){
                            adressesList.add(object.getString("address_line_1")+"\n"+object.getString("address_line_2"));
                            stateCityList.add(object.getString("city")+", "+object.getString("state"));
                            zipCodeList.add(object.getString("zip_code"));
                            addressId.add(object.getObjectId());
                        }mRecyclerView.setAdapter(new AddressRecyclerAdapter(adressesList,stateCityList,zipCodeList,addressId,ProceedToCheckoutActivity.this));

                    }else{
                        Toast.makeText(ProceedToCheckoutActivity.this,"Add New Address",Toast.LENGTH_LONG).show();
                        addNewAddressTextView.setText(addNewAddressTextView.getText().toString().substring(3));
                        saveAddressesText.setVisibility(View.INVISIBLE);
                    }progressDialog.dismiss();
                }
            }
        });



    }


    @Override
    public void AdressIsClicked(String addressId) {
        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("UserAddress");
        parseQuery.whereEqualTo("objectId",addressId);
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null && objects.size()>0){
                    if(recievedBuyNowProductId != null){
                    if(recievedBuyNowProductId.equals("") == false){
                        Intent intent1 = new Intent(ProceedToCheckoutActivity.this,ProceedToPaymentActivity.class);
                        intent1.putExtra("addressId",addressId);
                        intent1.putExtra("buy_now_product",recievedBuyNowProductId);
                        intent1.putExtra("seller_name",recievedBuyNowSellerName);
                        startActivity(intent1);
                    }
                    }
                    else{
                        Intent intent = new Intent(ProceedToCheckoutActivity.this,ProceedToPaymentActivity.class);
                        intent.putExtra("addressId",addressId);
                        startActivity(intent);
                    }

                }
            }
        });
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
                            Intent intent = new Intent(ProceedToCheckoutActivity.this,ChooseLoginSignupActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
                break;
            case R.id.my_profile_item:
                Intent profileIntent = new Intent(ProceedToCheckoutActivity.this,ProfileActivity.class);
                startActivity(profileIntent);
                break;
            case R.id.my_order_item:
                Intent orderIntent = new Intent(ProceedToCheckoutActivity.this,OrdersActivity.class);
                startActivity(orderIntent);
                break;
            case R.id.my_cart_item:
                Intent cartIntent = new Intent(ProceedToCheckoutActivity.this, CartActivity.class);
                startActivity(cartIntent);
                break;
            case R.id.seller_login_register:

                Intent sellerLoginRegisterIntent = new Intent(ProceedToCheckoutActivity.this,SellerMainActivity.class);
                startActivity(sellerLoginRegisterIntent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}