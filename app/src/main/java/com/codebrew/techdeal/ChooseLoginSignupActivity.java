package com.codebrew.techdeal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.parse.ParseUser;

public class ChooseLoginSignupActivity extends AppCompatActivity implements View.OnClickListener {
    private Button goToLoginButton, goToSignupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_login_signup);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.logo);
        setTitle("");
        goToLoginButton = findViewById(R.id.go_login_btn);
        goToSignupButton = findViewById(R.id.go_signup_btn);
        goToLoginButton.setOnClickListener(ChooseLoginSignupActivity.this);
        goToSignupButton.setOnClickListener(ChooseLoginSignupActivity.this);

        if(ParseUser.getCurrentUser()!=null){
            Intent intent = new Intent(ChooseLoginSignupActivity.this,HomePageActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.go_login_btn:
                Intent intentLogin = new Intent(ChooseLoginSignupActivity.this,LoginActivity.class);
                startActivity(intentLogin);

                break;

            case R.id.go_signup_btn:
                Intent intentSignUp = new Intent(ChooseLoginSignupActivity.this,SignupActivity.class);
                startActivity(intentSignUp);

                break;

        }

    }
}