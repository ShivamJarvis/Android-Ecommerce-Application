package com.codebrew.techdeal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText usernameSignup,nameSignup,emailSignup,passSignup;
    private Button registerBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.logo);
        setTitle("");

        usernameSignup = findViewById(R.id.username_register);
        nameSignup = findViewById(R.id.name_register);
        emailSignup = findViewById(R.id.email_register);
        passSignup = findViewById(R.id.pass_register);

        registerBtn = findViewById(R.id.register_btn);
        registerBtn.setOnClickListener(SignupActivity.this);

    }

    @Override
    public void onClick(View v) {
        ParseUser newUser = new ParseUser();
        newUser.setUsername(usernameSignup.getText().toString());
        newUser.setEmail(emailSignup.getText().toString());
        newUser.setPassword(passSignup.getText().toString());

        newUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    newUser.put("name",nameSignup.getText().toString());
                    newUser.saveInBackground();
                    Toast.makeText(SignupActivity.this,"Welcome, "+nameSignup.getText().toString(),
                            Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(SignupActivity.this,HomePageActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}