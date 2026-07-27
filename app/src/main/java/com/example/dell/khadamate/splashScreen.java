package com.example.dell.khadamate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.example.dell.khadamate.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class splashScreen extends AppCompatActivity {
    private LinearLayout mSplashLayout;
    private pl.droidsonroids.gif.GifImageView mLoadingImage;
    public static FirebaseDatabase database;
    public static DatabaseReference myRef;
    public static FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    public static User user;
    private final Handler handler = new Handler();
    private boolean routed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        mAuth = FirebaseAuth.getInstance();
        mSplashLayout = findViewById(R.id.splashLayout);
        mLoadingImage = findViewById(R.id.LoadingImage);

        mSplashLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.mytransition));
        mLoadingImage.startAnimation(AnimationUtils.loadAnimation(this, R.anim.mytransition));

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (routed) {
                    return;
                }
                if (firebaseAuth.getCurrentUser() != null && firebaseAuth.getCurrentUser().getEmail() != null) {
                    DatabaseReference usersRef = myRef.child("users").getRef();
                    Query userRefQuery = usersRef.orderByChild("email")
                            .equalTo(firebaseAuth.getCurrentUser().getEmail());
                    userRefQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (routed) {
                                return;
                            }
                            for (DataSnapshot users : dataSnapshot.getChildren()) {
                                user = users.getValue(User.class);
                                if (user == null || user.getRole() == null) {
                                    continue;
                                }
                                routed = true;
                                if (user.getRole().equals("Worker")) {
                                    startActivity(new Intent(splashScreen.this, WorkerHomePage.class));
                                } else if (user.getRole().equals("NormalUser")) {
                                    startActivity(new Intent(splashScreen.this, HomePage.class));
                                }
                                finish();
                                return;
                            }
                            routed = true;
                            startActivity(new Intent(splashScreen.this, LoginActivity.class));
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            if (!routed) {
                                routed = true;
                                startActivity(new Intent(splashScreen.this, LoginActivity.class));
                                finish();
                            }
                        }
                    });
                } else {
                    routed = true;
                    startActivity(new Intent(splashScreen.this, LoginActivity.class));
                    finish();
                }
            }
        };

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAuth.addAuthStateListener(mAuthListener);
            }
        }, 1800);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        if (mAuth != null && mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
