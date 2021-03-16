package com.codebrew.techdeal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText usernameLogin,passLogin;
    private Button loginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        loginBtn = findViewById(R.id.login_btn);
        usernameLogin = findViewById(R.id.username_login);
        passLogin = findViewById(R.id.pass_login);
        loginBtn.setOnClickListener(LoginActivity.this);
    }

    @Override
    public void onClick(View v) {
        ParseUser.logInInBackground(usernameLogin.getText().toString(), passLogin.getText().toString(),
                new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if(user!=null && e==null){
                            Toast.makeText(LoginActivity.this,"Welcome, "+user.get("name"),Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LoginActivity.this,HomePageActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Snackbar.make(v,"Invalid Credentials",Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
    }
}