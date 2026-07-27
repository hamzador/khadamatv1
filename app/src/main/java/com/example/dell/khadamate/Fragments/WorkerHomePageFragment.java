package com.example.dell.khadamate.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dell.khadamate.Model.Reaction;
import com.example.dell.khadamate.R;
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

import static com.example.dell.khadamate.splashScreen.myRef;
import static com.example.dell.khadamate.splashScreen.user;

public class WorkerHomePageFragment extends Fragment {
    private PieChart mPieChart;
    private TextView mWorkerMail;
    private DatabaseReference reactionRef;
    private Query reactionRefQuery;
    private ValueEventListener reactionListener;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() != null) {
            getActivity().setTitle(R.string.home_title);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_worker_home_page, container, false);
        mWorkerMail = v.findViewById(R.id.WorkerMail);
        if (user != null && user.getEmail() != null && mWorkerMail != null) {
            mWorkerMail.setText(user.getEmail());
        }

        reactionRef = myRef.child("reactions");
        mPieChart = v.findViewById(R.id.pieChart);
        mPieChart.setUsePercentValues(true);
        mPieChart.getDescription().setEnabled(false);
        mPieChart.setExtraOffsets(5, 10, 5, 5);
        mPieChart.setDragDecelerationFrictionCoef(0.95f);
        mPieChart.setDrawHoleEnabled(true);
        mPieChart.setHoleColor(Color.parseColor("#F0FDFA"));
        mPieChart.setTransparentCircleRadius(61.f);
        mPieChart.setNoDataText(getString(R.string.no_ratings_yet));

        if (user == null) {
            return v;
        }

        reactionRefQuery = reactionRef.orderByChild("reactedFullName")
                .equalTo(user.getFName() + " " + user.getLName());
        reactionListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int allReaction = 0;
                int likeReaction = 0;
                int dislikeReaction = 0;
                int unReaction = 0;

                for (DataSnapshot reactions : dataSnapshot.getChildren()) {
                    Reaction reaction = reactions.getValue(Reaction.class);
                    if (reaction == null || reaction.getReaction() == null) {
                        continue;
                    }
                    switch (reaction.getReaction()) {
                        case "Like":
                            likeReaction++;
                            break;
                        case "Dislike":
                            dislikeReaction++;
                            break;
                        case "Unlike":
                            unReaction++;
                            break;
                    }
                    allReaction++;
                }

                if (allReaction != 0) {
                    ArrayList<PieEntry> yValues = new ArrayList<>();
                    yValues.add(new PieEntry((likeReaction * 100f) / allReaction, getString(R.string.satisfied)));
                    yValues.add(new PieEntry((dislikeReaction * 100f) / allReaction, getString(R.string.unsatisfied)));
                    yValues.add(new PieEntry((unReaction * 100f) / allReaction, getString(R.string.neutral)));
                    PieDataSet dataSet = new PieDataSet(yValues, getString(R.string.clients));
                    dataSet.setSliceSpace(3f);
                    dataSet.setSelectionShift(5f);
                    dataSet.setColors(ColorTemplate.LIBERTY_COLORS);
                    PieData data = new PieData(dataSet);
                    data.setValueTextSize(10f);
                    data.setValueTextColor(Color.YELLOW);
                    mPieChart.clear();
                    mPieChart.setData(data);
                    mPieChart.invalidate();
                } else {
                    mPieChart.clear();
                    mPieChart.setNoDataText(getString(R.string.no_ratings_yet));
                    mPieChart.invalidate();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        reactionRefQuery.addValueEventListener(reactionListener);
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (reactionRefQuery != null && reactionListener != null) {
            reactionRefQuery.removeEventListener(reactionListener);
        }
    }
}
