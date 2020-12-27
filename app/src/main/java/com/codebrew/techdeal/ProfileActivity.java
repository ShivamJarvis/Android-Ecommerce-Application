package com.codebrew.techdeal;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class ProfileActivity extends AppCompatActivity {
    private TextView myName,myUserName, myEmail, customerDate;
    private ImageView myNamePencil, myEmailPencil;
    private Button updateProfileButton, updatePasswordButton;
    enum State{
        DONE,DOING
    }
    private State state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.logo);
        setTitle("");

        myName = findViewById(R.id.my_name);
        myEmail = findViewById(R.id.my_email);
        myUserName = findViewById(R.id.my_username);
        customerDate = findViewById(R.id.customer_since);
        myNamePencil = findViewById(R.id.my_name_pencil);
        myEmailPencil = findViewById(R.id.my_email_pencil);
        updateProfileButton = findViewById(R.id.update_profile_button);
        updatePasswordButton = findViewById(R.id.update_password_button);
        state = State.DONE;
        if(state == State.DONE){
            myNamePencil.setVisibility(View.GONE);
            myEmailPencil.setVisibility(View.GONE);
        }
        myName.setText(ParseUser.getCurrentUser().getString("name"));
        myEmail.setText(ParseUser.getCurrentUser().getEmail());
        myUserName.setText(ParseUser.getCurrentUser().getUsername());
        customerDate.setText(ParseUser.getCurrentUser().getCreatedAt().toString().substring(0,11));

        updateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state == State.DONE){
                    myNamePencil.setVisibility(View.VISIBLE);
                    myEmailPencil.setVisibility(View.VISIBLE);
                    updateProfileButton.setText("Done");
                    state = State.DOING;

                }
                else if(state == State.DOING){
                    myNamePencil.setVisibility(View.GONE);
                    myEmailPencil.setVisibility(View.GONE);
                    updateProfileButton.setText("Update Profile");
                    state = State.DONE;
                }
            }
        });


        updatePasswordButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                EditText newPassword = new EditText(ProfileActivity.this);
                EditText reNewPassword = new EditText(ProfileActivity.this);
                LinearLayout myLinearLayout = new LinearLayout(ProfileActivity.this);
                myLinearLayout.setOrientation(LinearLayout.VERTICAL);


                newPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                reNewPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
               newPassword.setHint("Enter New Password");
               reNewPassword.setHint("Re-type New Password");
               myLinearLayout.addView(newPassword);
               myLinearLayout.addView(reNewPassword);
                builder.setTitle("Update Your Password")
                        .setView(myLinearLayout)
                        .setPositiveButton("Change Password", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String newPasswordTxt = newPassword.getText().toString();
                                String reNewPasswordTxt = reNewPassword.getText().toString();

                                if(newPasswordTxt.length()<6){
                                    Toast.makeText(ProfileActivity.this,"Password Length at least 6 characters",Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                if(newPasswordTxt.equals(reNewPasswordTxt)) {
                                    ParseQuery<ParseUser> query = ParseUser.getQuery();
                                    query.getInBackground(ParseUser.getCurrentUser().getObjectId(), new GetCallback<ParseUser>() {
                                        @Override
                                        public void done(ParseUser object, ParseException e) {
                                            if(e==null){
                                                object.setPassword(newPasswordTxt);
                                                object.saveInBackground();
                                                Toast.makeText(ProfileActivity.this,"Password Changed Successfully",Toast.LENGTH_SHORT).show();
                                                ParseUser.logOut();
                                                Intent intent = new Intent(ProfileActivity.this,ChooseLoginSignupActivity.class);
                                                startActivity(intent);
                                                finish();

                                            }
                                            else{
                                                Toast.makeText(ProfileActivity.this,"Something Went Wrong",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                                else{
                                    Toast.makeText(ProfileActivity.this,"New Password Must Be Same with Retype Password",Toast.LENGTH_SHORT).show();
                                }
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create().show();

            }
        });


        myNamePencil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                EditText nameEditText = new EditText(ProfileActivity.this);
                nameEditText.setHint("Enter Full Name");
                nameEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setTitle("Change Name")
                        .setView(nameEditText)
                        .setPositiveButton("Change", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ParseQuery<ParseUser> query= ParseUser.getQuery();
                                query.getInBackground(ParseUser.getCurrentUser().getObjectId(), new GetCallback<ParseUser>() {
                                    @Override
                                    public void done(ParseUser object, ParseException e) {
                                        if(e==null){
                                            object.put("name",nameEditText.getText().toString());
                                            object.saveInBackground();
                                            Toast.makeText(ProfileActivity.this,"Name Update Successfully",Toast.LENGTH_SHORT).show();
                                            myName.setText(nameEditText.getText().toString());
                                        }

                                    }
                                });
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();
            }
        });

        myEmailPencil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                EditText emailEditText = new EditText(ProfileActivity.this);
                emailEditText.setHint("Enter Email");
                emailEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setTitle("Change Email")
                        .setView(emailEditText)
                        .setPositiveButton("Change", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ParseQuery<ParseUser> query= ParseUser.getQuery();
                                query.getInBackground(ParseUser.getCurrentUser().getObjectId(), new GetCallback<ParseUser>() {
                                    @Override
                                    public void done(ParseUser object, ParseException e) {
                                        if(e==null){
                                            object.setEmail(emailEditText.getText().toString());
                                            object.saveInBackground();
                                            Toast.makeText(ProfileActivity.this,"Email Update Successfully",Toast.LENGTH_SHORT).show();
                                            myEmail.setText(emailEditText.getText().toString());
                                        }
                                    }
                                });
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();
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
                            Intent intent = new Intent(ProfileActivity.this,ChooseLoginSignupActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
                break;
            case R.id.my_profile_item:

                break;
            case R.id.my_order_item:
                Intent orderIntent = new Intent(ProfileActivity.this,OrdersActivity.class);
                startActivity(orderIntent);
                break;
            case R.id.my_cart_item:
                Intent cartIntent = new Intent(ProfileActivity.this, CartActivity.class);
                startActivity(cartIntent);
                break;
            case R.id.seller_login_register:

                Intent sellerLoginRegisterIntent = new Intent(ProfileActivity.this,SellerMainActivity.class);
                startActivity(sellerLoginRegisterIntent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}