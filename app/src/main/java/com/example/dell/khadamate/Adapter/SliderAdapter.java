package com.example.dell.khadamate.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;

import com.example.dell.khadamate.Fragments.WorkerRegisterOne;
import com.example.dell.khadamate.Fragments.WorkerRegisterThree;
import com.example.dell.khadamate.Fragments.WorkerRegisterTwo;

public class SliderAdapter extends FragmentPagerAdapter {
    Context context;
    LayoutInflater layoutInflater;
    private int[] Layouts;

    public SliderAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0: return new WorkerRegisterOne();
            case 1: return new WorkerRegisterTwo();
            case 2: return new WorkerRegisterThree();
            default:break;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}