package com.example.dell.khadamate.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.dell.khadamate.R;

public class WorkerRegisterTwo extends Fragment {
    private EditText mWorkerEmail, mWorkerPassword , mWorkerRepeatePassword;
    public static String WEmail,WPassword,WRepeatPassword;
    public static boolean NoEmptyField_2;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragement_worker_register_two,container,false);
        mWorkerEmail = v.findViewById(R.id.WorkerEmail);
        mWorkerPassword = v.findViewById(R.id.WorkerPassword);
        mWorkerRepeatePassword = v.findViewById(R.id.RepeatePassword);
        return v;
    }

}
