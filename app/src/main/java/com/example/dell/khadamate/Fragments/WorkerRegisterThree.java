package com.example.dell.khadamate.Fragments;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dell.khadamate.Model.User;
import com.example.dell.khadamate.Model.Worker;
import com.example.dell.khadamate.MyLocationListener;
import com.example.dell.khadamate.R;
import com.example.dell.khadamate.WorkerHomePage;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DatabaseReference;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;
import static android.widget.Toast.LENGTH_SHORT;
import static com.example.dell.khadamate.MyLocationListener.Latitude;
import static com.example.dell.khadamate.MyLocationListener.Longitude;

import static com.example.dell.khadamate.Fragments.WorkerRegisterOne.WFirstName;
import static com.example.dell.khadamate.Fragments.WorkerRegisterOne.WLastName;
import static com.example.dell.khadamate.Fragments.WorkerRegisterOne.WService;
import static com.example.dell.khadamate.splashScreen.mAuth;
import static com.example.dell.khadamate.splashScreen.myRef;
import static com.example.dell.khadamate.splashScreen.user;

public class WorkerRegisterThree extends Fragment {
    private DatabaseReference mDatabase;
    private EditText mWorkerTelephone, mWorkerAddress;
    public static String WTelephone, WAddress;
    private EditText mWorkerEmail, mWorkerPassword , mWorkerRepeatePassword;
    public static String WEmail,WPassword,WRepeatPassword;
    private pl.droidsonroids.gif.GifImageView mLoadingImage;
    Geocoder geocoder;
    List<Address> addresses;
    private Button Save;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragement_worker_register_three, container, false);
        mLoadingImage = v.findViewById(R.id.Loading);
        mWorkerTelephone = v.findViewById(R.id.WorkerTelephone);
        mWorkerAddress = v.findViewById(R.id.WorkerAddress);
        mWorkerEmail = this.getActivity().findViewById(R.id.WorkerEmail);
        mWorkerPassword = this.getActivity().findViewById(R.id.WorkerPassword);
        mWorkerRepeatePassword = this.getActivity().findViewById(R.id.RepeatePassword);

        //System.out.println("Email "+mWorkerEmail.getText());
        if(mWorkerAddress.getText().toString().trim().isEmpty()){
            Log.v("addresshamza",mWorkerAddress.getText().toString());
                mLoadingImage.setVisibility(View.VISIBLE);
                Thread timer = new Thread(){
                    public void run(){
                        try {
                            sleep(10000);
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                        finally {
                            Log.d("Address 1","Error");
                            geocoder = new Geocoder(v.getContext(),Locale.FRANCE);
                                String address = "" ;
                                 try {
                                     Log.d("Address 1","Error");
                                   addresses = geocoder.getFromLocation(Latitude, Longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                    Log.d("Address",""+addresses.toString());
                                    address = addresses.get(0).getAddressLine(0);
                                    WAddress = " "+address;
                                    Log.e("Mapping ",""+Latitude);
                               } catch (IOException e) {
                                     Log.e("Mapping ",""+e.getMessage());
                                    //Toast.makeText(v.getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                                }
                                mWorkerAddress.setText(WAddress);
                                mLoadingImage.setVisibility(View.INVISIBLE);
                        }
                    }
                };
                timer.start();

        }

        Save = v.findViewById(R.id.save);
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mWorkerTelephone.getText().toString().trim().isEmpty() || !mWorkerAddress.getText().toString().trim().isEmpty() ){
                 if(!mWorkerEmail.getText().toString().trim().isEmpty() || !mWorkerPassword.getText().toString().trim().isEmpty()
                            || !mWorkerRepeatePassword.getText().toString().trim().isEmpty() || mWorkerPassword.equals(mWorkerRepeatePassword))
                      Registering();
                }
            }
        });

        return v;
    }

    private void Registering(){
        WTelephone = mWorkerTelephone.getText().toString();
        WAddress = mWorkerAddress.getText().toString();
        WEmail = mWorkerEmail.getText().toString();
        WPassword = mWorkerPassword.getText().toString();
        WRepeatPassword = mWorkerRepeatePassword.getText().toString();
        mAuth.createUserWithEmailAndPassword(WEmail,WPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
          @Override
          public void onSuccess(AuthResult authResult) {
              Log.e("LoginActivity", " Registration good");
              insertUserIntoDB();
          }

    });
    }

    public void insertUserIntoDB(){
        try{
            Log.d("Insert Into database","Creating User.....0");
            Worker worker1 = new Worker(WFirstName,WLastName,WFirstName+" "+WLastName,0,0,Latitude,Longitude);
            User worker = new User(WFirstName,WLastName,
                    WFirstName+" "+WLastName,WEmail,WTelephone,"Worker",WAddress,
                    Latitude,Longitude,WService);
            DatabaseReference usersRef = myRef.child("users");
            DatabaseReference workerRef = myRef.child("services").child(WService);
            Log.d("Insert Into database","Creating an ID.....1");
            String key = usersRef.push().getKey();
            Log.d("Insert Into database","Creating an ID.....2");
            System.out.println(usersRef);
            Log.d("Insert Into database","Inserting User.....3");
            user = worker;
            usersRef.child(key).setValue(worker);
            workerRef.child(key).setValue(worker1);
            startActivity(new Intent(getContext(),WorkerHomePage.class));
            getActivity().finish();


        }catch (Exception e){
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
}
