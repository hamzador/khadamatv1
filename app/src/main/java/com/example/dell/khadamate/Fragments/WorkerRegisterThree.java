package com.example.dell.khadamate.Fragments;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dell.khadamate.Model.User;
import com.example.dell.khadamate.Model.Worker;
import com.example.dell.khadamate.R;
import com.example.dell.khadamate.WorkerHomePage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DatabaseReference;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.dell.khadamate.Fragments.WorkerRegisterOne.WFirstName;
import static com.example.dell.khadamate.Fragments.WorkerRegisterOne.WLastName;
import static com.example.dell.khadamate.Fragments.WorkerRegisterOne.WService;
import static com.example.dell.khadamate.MyLocationListener.Latitude;
import static com.example.dell.khadamate.MyLocationListener.Longitude;
import static com.example.dell.khadamate.splashScreen.mAuth;
import static com.example.dell.khadamate.splashScreen.myRef;
import static com.example.dell.khadamate.splashScreen.user;

public class WorkerRegisterThree extends Fragment {
    private EditText mWorkerTelephone, mWorkerAddress;
    public static String WTelephone, WAddress;
    private EditText mWorkerEmail, mWorkerPassword, mWorkerRepeatePassword;
    public static String WEmail, WPassword, WRepeatPassword;
    private pl.droidsonroids.gif.GifImageView mLoadingImage;
    private Button Save;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final ExecutorService geocodeExecutor = Executors.newSingleThreadExecutor();

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

        if (TextUtils.isEmpty(mWorkerAddress.getText().toString().trim())) {
            resolveAddressFromLocation(v);
        }

        Save = v.findViewById(R.id.save);
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mWorkerEmail == null || mWorkerPassword == null || mWorkerRepeatePassword == null) {
                    Toast.makeText(getContext(), R.string.fill_all_fields, Toast.LENGTH_LONG).show();
                    return;
                }

                String telephone = mWorkerTelephone.getText().toString().trim();
                String address = mWorkerAddress.getText().toString().trim();
                String email = mWorkerEmail.getText().toString().trim();
                String password = mWorkerPassword.getText().toString().trim();
                String repeatPassword = mWorkerRepeatePassword.getText().toString().trim();

                if (TextUtils.isEmpty(telephone) || TextUtils.isEmpty(address)
                        || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)
                        || TextUtils.isEmpty(repeatPassword)) {
                    Toast.makeText(getContext(), R.string.fill_all_fields, Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(WFirstName) || TextUtils.isEmpty(WLastName) || TextUtils.isEmpty(WService)) {
                    Toast.makeText(getContext(), R.string.complete_previous_steps, Toast.LENGTH_LONG).show();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(getContext(), R.string.invalid_email, Toast.LENGTH_LONG).show();
                    return;
                }
                if (password.length() < 6) {
                    Toast.makeText(getContext(), R.string.password_too_short, Toast.LENGTH_LONG).show();
                    return;
                }
                if (!password.equals(repeatPassword)) {
                    Toast.makeText(getContext(), R.string.password_mismatch, Toast.LENGTH_LONG).show();
                    return;
                }
                Registering(telephone, address, email, password);
            }
        });

        return v;
    }

    private void resolveAddressFromLocation(final View v) {
        mLoadingImage.setVisibility(View.VISIBLE);
        geocodeExecutor.execute(new Runnable() {
            @Override
            public void run() {
                String address = "";
                try {
                    Geocoder geocoder = new Geocoder(v.getContext(), new Locale("ar", "MA"));
                    List<Address> addresses = geocoder.getFromLocation(Latitude, Longitude, 1);
                    if (addresses != null && !addresses.isEmpty()) {
                        address = addresses.get(0).getAddressLine(0);
                    }
                } catch (IOException e) {
                    Log.e("WorkerRegisterThree", "Geocode failed: " + e.getMessage());
                }
                final String resolved = address;
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!isAdded()) {
                            return;
                        }
                        if (!TextUtils.isEmpty(resolved)) {
                            WAddress = resolved;
                            mWorkerAddress.setText(resolved);
                        }
                        mLoadingImage.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });
    }

    private void Registering(String telephone, String address, String email, String password) {
        WTelephone = telephone;
        WAddress = address;
        WEmail = email;
        WPassword = password;
        WRepeatPassword = password;
        Save.setEnabled(false);
        mAuth.createUserWithEmailAndPassword(WEmail, WPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful() && task.getResult() != null
                                && task.getResult().getUser() != null) {
                            insertUserIntoDB(task.getResult().getUser().getUid());
                        } else {
                            Save.setEnabled(true);
                            String message = task.getException() != null
                                    ? task.getException().getMessage()
                                    : getString(R.string.registration_failed);
                            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void insertUserIntoDB(String uid) {
        try {
            Worker workerListing = new Worker(WFirstName, WLastName, WFirstName + " " + WLastName,
                    0, 0, Latitude, Longitude, WService);
            User worker = new User(WFirstName, WLastName,
                    WFirstName + " " + WLastName, WEmail, WTelephone, "Worker", WAddress,
                    Latitude, Longitude, WService);
            DatabaseReference usersRef = myRef.child("users");
            DatabaseReference workerRef = myRef.child("services").child(WService);
            user = worker;
            usersRef.child(uid).setValue(worker);
            workerRef.child(uid).setValue(workerListing)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), R.string.registration_success, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getContext(), WorkerHomePage.class));
                                if (getActivity() != null) {
                                    getActivity().finish();
                                }
                            } else {
                                Save.setEnabled(true);
                                Toast.makeText(getContext(), R.string.registration_failed, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        } catch (Exception e) {
            Save.setEnabled(true);
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        geocodeExecutor.shutdownNow();
    }
}
