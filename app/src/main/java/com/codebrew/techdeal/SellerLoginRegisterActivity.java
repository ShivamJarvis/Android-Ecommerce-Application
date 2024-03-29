package com.codebrew.techdeal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class SellerLoginRegisterActivity extends AppCompatActivity {
    private EditText businessName,gstNo,aadharNo,panNo;
    private Button registerBusinessBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        businessName = findViewById(R.id.registered_business_name);
        gstNo = findViewById(R.id.gst_no);
        aadharNo = findViewById(R.id.aadhar_no);
        panNo = findViewById(R.id.pan_no);
        registerBusinessBtn = findViewById(R.id.register_seller_btn);

        registerBusinessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewSellerAccount();
            }
        });

    }


    private void registerNewSellerAccount(){

        if(businessName.getText().toString().equals("") ||
        aadharNo.getText().toString().equals("")||
        gstNo.getText().toString().equals("")||
        panNo.getText().toString().equals("")){
            Toast.makeText(SellerLoginRegisterActivity.this,
                    "All Fields Are Necessary to Fill",
                    Toast.LENGTH_LONG).show();
            return;
        }

        ParseObject newSeller = new ParseObject("Seller");
        newSeller.put("seller",businessName.getText().toString());
        newSeller.put("gst_no",gstNo.getText().toString());
        newSeller.put("aadhar_no",aadharNo.getText().toString());
        newSeller.put("pan_no",panNo.getText().toString());
        newSeller.put("username",ParseUser.getCurrentUser().getUsername());
        newSeller.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    Intent intentToSellerMainActivity = new Intent(SellerLoginRegisterActivity.this,
                            SellerMainActivity.class);
                    intentToSellerMainActivity.putExtra("sellerName",businessName.getText().toString());

                    startActivity(intentToSellerMainActivity);
                    finish();
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