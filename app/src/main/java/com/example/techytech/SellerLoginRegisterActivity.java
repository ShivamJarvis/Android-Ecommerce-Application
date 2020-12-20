package com.example.techytech;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class SellerLoginRegisterActivity extends AppCompatActivity {
    private EditText businessName,gstNo,aadharNo,panNo;
    private Button registerBusinessBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login_register);
        checkUserIsAlreadyASeller();
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



    private void checkUserIsAlreadyASeller(){
        ParseQuery<ParseUser> userParseQuery = ParseUser.getQuery();
        userParseQuery.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
        userParseQuery.whereExists("seller");
        userParseQuery.whereExists("gst_no");
        userParseQuery.whereExists("aadhar_no");
        userParseQuery.whereExists("pan_no");

        userParseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(objects!=null && e==null){
                    if(objects.size()>0){
                        Intent intentToSellerMainActivity = new Intent(SellerLoginRegisterActivity.this,
                                SellerMainActivity.class);
                        startActivity(intentToSellerMainActivity);
                        finish();
                    }
                }
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
        }

        ParseQuery<ParseUser> userParseQuery = ParseUser.getQuery();
        userParseQuery.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
        userParseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e==null && objects!=null){
                    if(objects.size()>0){
                        for(ParseUser myUser : objects){
                            myUser.put("seller",businessName.getText().toString());
                            myUser.put("gst_no",gstNo.getText().toString());
                            myUser.put("aadhar_no",aadharNo.getText().toString());
                            myUser.put("pan_no",panNo.getText().toString());
                            myUser.saveInBackground();
                            Intent intentToSellerMainActivity = new Intent(SellerLoginRegisterActivity.this,
                                    SellerMainActivity.class);
                            startActivity(intentToSellerMainActivity);
                            finish();
                        }
                    }
                }
            }
        });
    }

}