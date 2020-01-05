package com.example.dell.khadamate;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        mAuth = FirebaseAuth.getInstance();
        mSplashLayout = findViewById(R.id.splashLayout);
        mLoadingImage = findViewById(R.id.LoadingImage);

        mSplashLayout.startAnimation(new AnimationUtils().loadAnimation(this, R.anim.mytransition));
        mLoadingImage.startAnimation(new AnimationUtils().loadAnimation(this, R.anim.mytransition));
        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null){
                    System.out.println("Step 1 : "+firebaseAuth.getCurrentUser().getEmail().toString());
                    DatabaseReference usersRef = myRef.child("users").getRef();
                   Query userRefQuery = (Query) usersRef.orderByChild("email").equalTo(firebaseAuth.getCurrentUser().getEmail());
                   userRefQuery.addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                           for(DataSnapshot users: dataSnapshot.getChildren()) {
                                user = users.getValue(User.class);
                                System.out.println("role "+user.toString());
                            if(user.getRole().equals("Worker")){
                                System.out.println("I'm here");
                                startActivity(new Intent(getApplicationContext(),WorkerHomePage.class));
                                finish();
                            }else if(user.getRole().equals("NormalUser")){
                              startActivity(new Intent(getApplicationContext(),HomePage.class));
                               finish();
                               }

                               break;
                           }
                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError databaseError) {

                       }
                   });
                 }else{
                      System.out.println("Step 2");
                      intent =  new Intent(splashScreen.this,LoginActivity.class);
                      startActivity(intent);
                      finish();
                  }
            }

        };
        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                   mAuth.addAuthStateListener(mAuthListener);
                }
            }
        };
        timer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}


