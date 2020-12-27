package com.codebrew.techdeal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PaymentStatusActivtity extends AppCompatActivity {
    private TextView paymentStatus, messageTxt;
    private ImageView paymentStatusImage1,paymentStatusImage2;
    private Button button;
    private boolean status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_status_activtity);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.logo);
        setTitle("");
        paymentStatus = findViewById(R.id.payment_status);
        paymentStatusImage1 = findViewById(R.id.payment_status_image);
        paymentStatusImage2 = findViewById(R.id.second_image_decision);
        messageTxt = findViewById(R.id.payment_note);
        button = findViewById(R.id.back_to_home);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentStatusActivtity.this,HomePageActivity.class);
                startActivity(intent);
                finish();
            }
        });
        status = getIntent().getBooleanExtra("status",true);
        if(status == true){
            paymentStatus.setText("Payment Successfull");
            paymentStatusImage1.setImageResource(R.drawable.correct);
            paymentStatusImage2.setImageResource(R.drawable.success);
            messageTxt.setText("Now you can track your order in My Order Section");

        }

        if(status == false){
            paymentStatus.setText("Payment Failed");
            paymentStatusImage1.setImageResource(R.drawable.wrong);
            paymentStatusImage2.setImageResource(R.drawable.failure);
            messageTxt.setText("Sorry your order is failed you can retry.");






        }


    }
}