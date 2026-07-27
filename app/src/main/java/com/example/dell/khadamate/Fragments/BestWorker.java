package com.example.dell.khadamate.Fragments;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dell.khadamate.Adapter.bestWorkerAdapter;
import com.example.dell.khadamate.Model.Reaction;
import com.example.dell.khadamate.Model.Worker;
import com.example.dell.khadamate.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static com.example.dell.khadamate.MyLocationListener.Latitude;
import static com.example.dell.khadamate.MyLocationListener.Longitude;
import static com.example.dell.khadamate.splashScreen.myRef;
import static com.example.dell.khadamate.splashScreen.user;

public class BestWorker extends Fragment {
    public static ArrayList<Worker> myWorkerList;
    private RecyclerView mBestWorkerRecyclerView;
    private TextView mEmptyState;
    private bestWorkerAdapter myAdapter;
    private ValueEventListener reactionsListener;
    private Query reactionRefQuery;
    private final String[] services = {"نجار", "ميكانيكي", "بلومبي", "كهربائي"};
    private int pendingLookups = 0;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() != null) {
            getActivity().setTitle(R.string.best_workers_title);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_best_worker, container, false);
        mBestWorkerRecyclerView = v.findViewById(R.id.BestWorkerRecyclerView);
        mEmptyState = v.findViewById(R.id.emptyBestWorkers);
        mBestWorkerRecyclerView.setHasFixedSize(true);
        mBestWorkerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        myWorkerList = new ArrayList<>();
        myAdapter = new bestWorkerAdapter(myWorkerList, getContext());
        mBestWorkerRecyclerView.setAdapter(myAdapter);

        if (user == null || user.getFullName() == null) {
            showEmpty(true);
            return v;
        }

        reactionRefQuery = myRef.child("reactions").orderByChild("reactorFullName").equalTo(user.getFullName());
        reactionsListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myWorkerList.clear();
                Set<String> likedNames = new HashSet<>();
                for (DataSnapshot reactions : dataSnapshot.getChildren()) {
                    Reaction reaction = reactions.getValue(Reaction.class);
                    if (reaction != null && "Like".equals(reaction.getReaction())
                            && reaction.getReactedFullName() != null) {
                        likedNames.add(reaction.getReactedFullName());
                    }
                }

                if (likedNames.isEmpty()) {
                    myAdapter.notifyDataSetChanged();
                    showEmpty(true);
                    return;
                }

                pendingLookups = likedNames.size() * services.length;
                final Set<String> addedKeys = new HashSet<>();
                for (final String likedName : likedNames) {
                    for (final String serviceName : services) {
                        DatabaseReference workerRef = myRef.child("services").child(serviceName);
                        Query workerQuery = workerRef.orderByChild("FullName").equalTo(likedName);
                        workerQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot myWorker : dataSnapshot.getChildren()) {
                                    Worker worker = myWorker.getValue(Worker.class);
                                    if (worker == null) {
                                        continue;
                                    }
                                    String key = serviceName + "|" + worker.getFullName();
                                    if (!addedKeys.add(key)) {
                                        continue;
                                    }
                                    worker.setService(serviceName);
                                    Location myLocation = new Location("myLocation");
                                    Location workerLocation = new Location("WorkerLocation");
                                    myLocation.setLatitude(Latitude);
                                    myLocation.setLongitude(Longitude);
                                    workerLocation.setLatitude(worker.getLatitude());
                                    workerLocation.setLongitude(worker.getLongitude());
                                    worker.setDistance(myLocation.distanceTo(workerLocation));
                                    myWorkerList.add(worker);
                                }
                                pendingLookups--;
                                if (pendingLookups <= 0 && getContext() != null) {
                                    myAdapter.notifyDataSetChanged();
                                    showEmpty(myWorkerList.isEmpty());
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                pendingLookups--;
                                if (pendingLookups <= 0 && getContext() != null) {
                                    myAdapter.notifyDataSetChanged();
                                    showEmpty(myWorkerList.isEmpty());
                                }
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showEmpty(true);
            }
        };
        reactionRefQuery.addValueEventListener(reactionsListener);
        return v;
    }

    private void showEmpty(boolean empty) {
        if (mEmptyState != null) {
            mEmptyState.setVisibility(empty ? View.VISIBLE : View.GONE);
        }
        if (mBestWorkerRecyclerView != null) {
            mBestWorkerRecyclerView.setVisibility(empty ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (reactionRefQuery != null && reactionsListener != null) {
            reactionRefQuery.removeEventListener(reactionsListener);
        }
    }
}
