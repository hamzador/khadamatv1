package com.example.dell.khadamate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dell.khadamate.Model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DatabaseReference;

import org.w3c.dom.Text;

import static com.example.dell.khadamate.splashScreen.mAuth;
import static com.example.dell.khadamate.splashScreen.myRef;
import static com.example.dell.khadamate.splashScreen.user;

public class NormalUserRegister extends AppCompatActivity {
    private Button mBackButton ,mRegisterButton;
    private EditText mNUserFName,mNUserLName ,mNUserEmail,mNUserPassword,mNUserRepeatePassword;
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

                if(!mNUserLName.getText().toString().trim().isEmpty()||
                        !mNUserFName.getText().toString().trim().isEmpty()||
                        !mNUserEmail.getText().toString().trim().isEmpty() ||
                        !mNUserPassword.getText().toString().trim().isEmpty()
                        || !mNUserRepeatePassword.getText().toString().trim().isEmpty()
                        && mNUserRepeatePassword.getText().toString().trim()
                        .equals(mNUserPassword.getText().toString().trim()))
                {
                    Toast.makeText(NormalUserRegister.this, "11111111111111", Toast.LENGTH_SHORT).show();
                    Toast.makeText(NormalUserRegister.this, ""+mAuth.toString(), Toast.LENGTH_LONG).show();
                    mAuth.createUserWithEmailAndPassword(mNUserEmail.getText().toString(),mNUserPassword.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Log.v("LoginActivity", " Registration good");
                            Toast.makeText(NormalUserRegister.this, "22222222222222", Toast.LENGTH_SHORT).show();
                            insertUserIntoDB();
                        }

                    });
                }else{
                    Toast.makeText(NormalUserRegister.this,"المرجو ملء الخانات",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void insertUserIntoDB(){
        try{
            Toast.makeText(NormalUserRegister.this, "3333333", Toast.LENGTH_SHORT).show();
           // Log.d("Insert Into database","Creating User.....");
            User NormalUser = new User(mNUserFName.getText().toString(),
                    mNUserLName.getText().toString(),mNUserFName.getText().toString()+" "+mNUserLName.getText().toString()
                    ,mNUserEmail.getText().toString(),"NormalUser");
            DatabaseReference usersRef = myRef.child("users");
           // Log.d("Insert Into database","Creating an ID.....");
            String key = usersRef.push().getKey();
          //  Log.d("Insert Into database","Creating an ID.....");
            System.out.println(usersRef);
           // Log.d("Insert Into database","Inserting User.....");
            usersRef.child(key).setValue(NormalUser);
            user = NormalUser;
            startActivity(new Intent(NormalUserRegister.this,HomePage.class));
            finish();

        }catch (Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
}
