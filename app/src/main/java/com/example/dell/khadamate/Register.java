package com.example.dell.khadamate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class Register extends AppCompatActivity {
    private Button mBackButton;
    private CardView mNormalUserAccount;
    private CardView mWorkerAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findViewById(R.id.NormalUserAccount);

        mWorkerAccount = findViewById(R.id.WorkerAccount);
        mNormalUserAccount = findViewById(R.id.NormalUserAccount);
        mNormalUserAccount.startAnimation(new AnimationUtils().loadAnimation(getApplicationContext(),
                R.anim.slide_up));
        mWorkerAccount.startAnimation(new AnimationUtils().loadAnimation(getApplicationContext(),
                R.anim.slide_up));
        mBackButton = findViewById(R.id.backButton);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this,LoginActivity.class));
                finish();
            }
        });

        mWorkerAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this,WorkerRegister.class));
                finish();
            }
        });
        mNormalUserAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this,NormalUserRegister.class));
                finish();
            }
        });
    }
}
