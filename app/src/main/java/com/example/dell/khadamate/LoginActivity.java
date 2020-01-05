package com.example.dell.khadamate;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dell.khadamate.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import static com.example.dell.khadamate.splashScreen.mAuth;
import static com.example.dell.khadamate.splashScreen.myRef;
import static com.example.dell.khadamate.splashScreen.user;


public class LoginActivity extends AppCompatActivity {
    private Button Register;
    private EditText mEmailEditText,mPasswordEditText;
    private pl.droidsonroids.gif.GifImageView mLoadingImage;

    private Button mLoginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ConnectivityManager cManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo Info = cManager.getActiveNetworkInfo();
        mLoadingImage = findViewById(R.id.LoadingImage);
        mEmailEditText = findViewById(R.id.EmailEditText);
        mPasswordEditText = findViewById(R.id.PasswordEditText);
        Register = findViewById(R.id.registerButton);
        mLoginButton = findViewById(R.id.loginButton);
        if(Info!=null && Info.isConnected()){
            mLoginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final String email = mEmailEditText.getText().toString();
                    String password = mPasswordEditText.getText().toString();
                    if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                        Toast.makeText(LoginActivity.this,"المرجو ملئ الخانات",Toast.LENGTH_LONG).show();
                    }else {
                        mLoadingImage.setVisibility(View.VISIBLE);
                        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    DatabaseReference usersRef = myRef.child("users").getRef();
                                    Query userRefQuery = (Query) usersRef.orderByChild("email").equalTo(email);
                                    userRefQuery.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for(DataSnapshot users: dataSnapshot.getChildren()) {
                                                user = users.getValue(User.class);
                                                if(user.getRole().equals("Worker")){
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
                                    mLoadingImage.setVisibility(View.GONE);
                                    Toast.makeText(LoginActivity.this,"المرجو التأكد من المعلومات ",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }
            });
            Register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(LoginActivity.this,Register.class));
                    finish();

                }
            });
        }else{
            Register.setEnabled(false);
            mLoginButton.setEnabled(false);
            findViewById(R.id.NetworkException).setVisibility(View.VISIBLE);
        }


}
}