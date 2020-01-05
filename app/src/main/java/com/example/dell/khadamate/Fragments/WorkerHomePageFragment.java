package com.example.dell.khadamate.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.khadamate.Model.Reaction;
import com.example.dell.khadamate.MyLocationListener;
import com.example.dell.khadamate.R;
import com.example.dell.khadamate.Workers;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.Context.LOCATION_SERVICE;
import static android.widget.Toast.LENGTH_SHORT;
import static com.example.dell.khadamate.splashScreen.myRef;
import static com.example.dell.khadamate.splashScreen.user;

public class WorkerHomePageFragment extends Fragment {
    private PieChart mPieChart;
    TextView mWorkerMail;
    int AllReaction = 0 , LikeReaction=0, DislikeReaction=0, UnReaction=0;
    DatabaseReference reactionRef ;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("الصفحة الرئيسية");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_worker_home_page, container, false);
        mWorkerMail = v.findViewById(R.id.WorkerMail);
        //mWorkerMail.setText(user.getEmail());
        reactionRef = myRef.child("reactions");



        mPieChart = (PieChart) v.findViewById(R.id.pieChart);
        mPieChart.setUsePercentValues(true);
        mPieChart.getDescription().setEnabled(false);
        mPieChart.setExtraOffsets(5,10,5,5);
        mPieChart.setDragDecelerationFrictionCoef(0.95f);

        mPieChart.setDrawHoleEnabled(true);
        mPieChart.setHoleColor(R.color.profileBackgroundColor);
        mPieChart.setTransparentCircleRadius(61.f);
        ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();
        Query reactionRefQuery = (Query) reactionRef.orderByChild("reactedFullName").equalTo(user.getFName()+" "+user.getLName());
        reactionRefQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot reactions : dataSnapshot.getChildren()){
                    Reaction reaction = reactions.getValue(Reaction.class);
                    switch (reaction.getReaction()){
                        case "Like": LikeReaction++;break;
                        case "Dislike":DislikeReaction++;break;
                        case "Unlike":UnReaction++;break;
                    }
                    AllReaction++;
                }
                Log.e("Poucentage : ",""+LikeReaction);
                Log.e("Poucentage : ",""+DislikeReaction);
                if(AllReaction != 0){
                    yValues.add(new PieEntry((LikeReaction*100)/AllReaction,"الراضين"));
                    yValues.add(new PieEntry((DislikeReaction*100)/AllReaction,"المستائين"));
                    yValues.add(new PieEntry((UnReaction*100)/AllReaction,"المحايديين"));
                    PieDataSet dataSet = new PieDataSet(yValues,"الزبناء");
                    dataSet.setSliceSpace(3f);
                    dataSet.setSelectionShift(5f);
                    dataSet.setColors(ColorTemplate.LIBERTY_COLORS);

                    PieData data = new PieData(dataSet);
                    data.setValueTextSize(10f);
                    data.setValueTextColor(Color.YELLOW);

                    mPieChart.setData(data);
                }else{
                    mPieChart.setNoDataText("المعلومات غير متاحة أو أن حسابكم لم يتم تقييمه بعد");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return v;
    }
}
