package com.example.dell.khadamate;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.dell.khadamate.Adapter.SliderAdapter;

public class WorkerRegister extends AppCompatActivity {
    private Button mBackButton;
    private ViewPager mViewPager;
    private Spinner spinner;
    private SliderAdapter mSliderAdapter;
    private LinearLayout Dots_Layout;
    private ImageView[] dots;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_register);

        mViewPager = findViewById(R.id.viewPager);
        mBackButton = findViewById(R.id.backButton);
        mSliderAdapter =new SliderAdapter(getSupportFragmentManager());
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WorkerRegister.this, Register.class));
                finish();
            }
        });
        mViewPager.setAdapter(mSliderAdapter);
        Dots_Layout = findViewById(R.id.dotsLayout);
        createDots(0);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                createDots(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });



    }

    private void createDots(int current_position){
        if(Dots_Layout!=null) Dots_Layout.removeAllViews();
        dots = new ImageView[3];

        for(int i=0;i<3;i++){
            dots[i] = new ImageView(this);
            if(i==current_position){
                dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.active_dot));
            }else{
                dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.inactive_dot));
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(4,0,4,0);
            Dots_Layout.addView(dots[i],params);
        }
    }

}