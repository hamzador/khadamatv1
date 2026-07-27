package com.example.dell.khadamate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dell.khadamate.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DatabaseReference;

import static com.example.dell.khadamate.splashScreen.mAuth;
import static com.example.dell.khadamate.splashScreen.myRef;
import static com.example.dell.khadamate.splashScreen.user;

public class NormalUserRegister extends AppCompatActivity {
    private Button mBackButton, mRegisterButton;
    private EditText mNUserFName, mNUserLName, mNUserEmail, mNUserPassword, mNUserRepeatePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_user_register);
        mNUserFName = findViewById(R.id.NUserFName);
        mNUserLName = findViewById(R.id.NUserLName);
        mNUserEmail = findViewById(R.id.NUserEmail);
        mNUserPassword = findViewById(R.id.NUserPassword);
        mNUserRepeatePassword = findViewById(R.id.NUserRepeatePassword);
        mBackButton = findViewById(R.id.backButton);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NormalUserRegister.this, Register.class));
                finish();
            }
        });
        mRegisterButton = findViewById(R.id.RegisterButton);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String firstName = mNUserFName.getText().toString().trim();
                final String lastName = mNUserLName.getText().toString().trim();
                final String email = mNUserEmail.getText().toString().trim();
                final String password = mNUserPassword.getText().toString().trim();
                final String repeatPassword = mNUserRepeatePassword.getText().toString().trim();

                if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName)
                        || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)
                        || TextUtils.isEmpty(repeatPassword)) {
                    Toast.makeText(NormalUserRegister.this, R.string.fill_all_fields, Toast.LENGTH_LONG).show();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(NormalUserRegister.this, R.string.invalid_email, Toast.LENGTH_LONG).show();
                    return;
                }
                if (password.length() < 6) {
                    Toast.makeText(NormalUserRegister.this, R.string.password_too_short, Toast.LENGTH_LONG).show();
                    return;
                }
                if (!password.equals(repeatPassword)) {
                    Toast.makeText(NormalUserRegister.this, R.string.password_mismatch, Toast.LENGTH_LONG).show();
                    return;
                }

                mRegisterButton.setEnabled(false);
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful() && task.getResult() != null
                                        && task.getResult().getUser() != null) {
                                    insertUserIntoDB(task.getResult().getUser().getUid(),
                                            firstName, lastName, email);
                                } else {
                                    mRegisterButton.setEnabled(true);
                                    String message = task.getException() != null
                                            ? task.getException().getMessage()
                                            : getString(R.string.registration_failed);
                                    Toast.makeText(NormalUserRegister.this, message, Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
    }

    public void insertUserIntoDB(String uid, String firstName, String lastName, String email) {
        try {
            User normalUser = new User(firstName, lastName, firstName + " " + lastName, email, "NormalUser");
            DatabaseReference usersRef = myRef.child("users");
            usersRef.child(uid).setValue(normalUser)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                user = normalUser;
                                Toast.makeText(NormalUserRegister.this, R.string.registration_success, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(NormalUserRegister.this, HomePage.class));
                                finish();
                            } else {
                                mRegisterButton.setEnabled(true);
                                Toast.makeText(NormalUserRegister.this, R.string.registration_failed, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        } catch (Exception e) {
            mRegisterButton.setEnabled(true);
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
