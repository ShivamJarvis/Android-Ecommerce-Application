package com.codebrew.techdeal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView username, fullName, orderCount,onProcessOrders;
    private TextInputEditText editName, editUsername, editEmail;
    private Button updateProfileBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        username = findViewById(R.id.username);
        fullName = findViewById(R.id.my_name);
        editName = findViewById(R.id.edit_name);
        editUsername = findViewById(R.id.edit_username);
        editEmail = findViewById(R.id.edit_email);
        orderCount = findViewById(R.id.order_count);
        onProcessOrders = findViewById(R.id.on_procress_orders);
        updateProfileBtn = findViewById(R.id.update_profile_btn);
        ParseUser parseUser = ParseUser.getCurrentUser();
        fullName.setText(parseUser.getString("name"));
        username.setText(parseUser.getUsername());
        editName.setText(parseUser.getString("name"));
        editUsername.setText(parseUser.getUsername());
        editEmail.setText(parseUser.getEmail());
        setDeliveredOrderCount();
        setOnProcessOrderCount();
        updateProfileBtn.setOnClickListener(ProfileActivity.this);
    }

    private void setDeliveredOrderCount()
    {
        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Orders");
        parseQuery.whereEqualTo("order_status","Order Delivered");
        parseQuery.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null)
                {
                    orderCount.setText(Integer.toString(objects.size()));
                }
            }
        });
    }

    private void setOnProcessOrderCount()
    {
        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Orders");
        parseQuery.whereNotEqualTo("order_status","Order Delivered");
        parseQuery.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null)
                {
                    onProcessOrders.setText(Integer.toString(objects.size()));
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.update_profile_btn:
                String name = editName.getText().toString();
                String email = editEmail.getText().toString();
                ParseUser parseUser = ParseUser.getCurrentUser();
                parseUser.setEmail(email);
                parseUser.put("name",name);
                parseUser.saveInBackground();

                Snackbar.make(v,"Profile Updated Successfully",Snackbar.LENGTH_LONG).show();
                break;
        }
    }
}