package com.example.dell.khadamate.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dell.khadamate.R;

import static com.example.dell.khadamate.splashScreen.user;


public class workerProfile extends Fragment {
    TextView mFullName,mFirstName,mLastName,mMail,mWorkerAddressProfile,mTelephoneWorker;
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("المعلومات الشخصية");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_worker_profile, container, false);

        mFullName = v.findViewById(R.id.FullName);
        mFirstName = v.findViewById(R.id.FirstName);
        mLastName = v.findViewById(R.id.LastName);
        mMail = v.findViewById(R.id.mail);
        mWorkerAddressProfile = v.findViewById(R.id.WorkerAddressProfile);
        mTelephoneWorker = v.findViewById(R.id.TelephoneWorker);

        mFullName.setText(user.getFullName());
        mFirstName.setText(user.getFName());
        mLastName.setText(user.getLName());
        mMail.setText(user.getEmail());
        mWorkerAddressProfile.setText(user.getAddress());
        mTelephoneWorker.setText(user.getPhone());
        return v;
    }

}
