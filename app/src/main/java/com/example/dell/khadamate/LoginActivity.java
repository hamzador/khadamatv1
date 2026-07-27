package com.example.dell.khadamate;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
    private EditText mEmailEditText, mPasswordEditText;
    private pl.droidsonroids.gif.GifImageView mLoadingImage;
    private Button mLoginButton;
    private TextView mForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ConnectivityManager cManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = cManager != null ? cManager.getActiveNetworkInfo() : null;
        mLoadingImage = findViewById(R.id.LoadingImage);
        mEmailEditText = findViewById(R.id.EmailEditText);
        mPasswordEditText = findViewById(R.id.PasswordEditText);
        Register = findViewById(R.id.registerButton);
        mLoginButton = findViewById(R.id.loginButton);
        mForgotPassword = findViewById(R.id.forgotPassword);

        if (info != null && info.isConnected()) {
            mLoginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String email = mEmailEditText.getText().toString().trim();
                    String password = mPasswordEditText.getText().toString().trim();
                    if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                        Toast.makeText(LoginActivity.this, R.string.fill_all_fields, Toast.LENGTH_LONG).show();
                    } else {
                        setLoading(true);
                        mAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            loadUserProfile(email);
                                        } else {
                                            setLoading(false);
                                            Toast.makeText(LoginActivity.this, R.string.login_invalid, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    }
                }
            });
            Register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(LoginActivity.this, Register.class));
                    finish();
                }
            });
            mForgotPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = mEmailEditText.getText().toString().trim();
                    if (TextUtils.isEmpty(email)) {
                        Toast.makeText(LoginActivity.this, R.string.enter_email_for_reset, Toast.LENGTH_LONG).show();
                        return;
                    }
                    mAuth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, R.string.reset_email_sent, Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(LoginActivity.this, R.string.reset_email_failed, Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            });
        } else {
            Register.setEnabled(false);
            mLoginButton.setEnabled(false);
            mForgotPassword.setEnabled(false);
            findViewById(R.id.NetworkException).setVisibility(View.VISIBLE);
        }
    }

    private void loadUserProfile(String email) {
        DatabaseReference usersRef = myRef.child("users").getRef();
        Query userRefQuery = usersRef.orderByChild("email").equalTo(email);
        userRefQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean found = false;
                for (DataSnapshot users : dataSnapshot.getChildren()) {
                    user = users.getValue(User.class);
                    if (user == null || user.getRole() == null) {
                        continue;
                    }
                    found = true;
                    if (user.getRole().equals("Worker")) {
                        startActivity(new Intent(LoginActivity.this, WorkerHomePage.class));
                        finish();
                    } else if (user.getRole().equals("NormalUser")) {
                        startActivity(new Intent(LoginActivity.this, HomePage.class));
                        finish();
                    }
                    break;
                }
                if (!found) {
                    setLoading(false);
                    Toast.makeText(LoginActivity.this, R.string.user_profile_missing, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                setLoading(false);
                Toast.makeText(LoginActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setLoading(boolean loading) {
        mLoadingImage.setVisibility(loading ? View.VISIBLE : View.GONE);
        mLoginButton.setEnabled(!loading);
        Register.setEnabled(!loading);
    }
}
