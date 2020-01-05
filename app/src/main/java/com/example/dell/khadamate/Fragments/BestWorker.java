package com.example.dell.khadamate.Fragments;

import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import static com.example.dell.khadamate.splashScreen.user;
import com.example.dell.khadamate.Adapter.WorkerRecyclerViewAdapter;
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

import static com.example.dell.khadamate.MyLocationListener.Latitude;
import static com.example.dell.khadamate.MyLocationListener.Longitude;
import static com.example.dell.khadamate.splashScreen.myRef;
import static com.example.dell.khadamate.splashScreen.user;


public class BestWorker extends Fragment {
    public static ArrayList<Worker> myWorkerList;
    private RecyclerView mBestWorkerRecyclerView;
    DatabaseReference reactionRef ;

    bestWorkerAdapter myAdapter;



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("الحرفيين الممتازين");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_best_worker, container, false);
        mBestWorkerRecyclerView = v.findViewById(R.id.BestWorkerRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mBestWorkerRecyclerView.setHasFixedSize(true);
        mBestWorkerRecyclerView.setLayoutManager(linearLayoutManager);
        myWorkerList = new ArrayList<Worker>();
        reactionRef = myRef.child("reactions").getRef();
        Query reactionRefQuery = (Query) reactionRef.orderByChild("reactorFullName").equalTo(user.getFullName());
        reactionRefQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //if(!myWorkerList.isEmpty()) myWorkerList.clear();
                String Services[] = {"نجار","ميكانيكي","بلومبي","كهربائي"};
                    for(DataSnapshot reactions : dataSnapshot.getChildren() ){
                        Reaction reaction = reactions.getValue(Reaction.class);
                        Log.e("7rira : ","Hello world 2 "+reaction.getReaction());
                        if(reaction.getReaction().equals("Like")){
                            for(int i=0;i<Services.length;i++){
                                DatabaseReference workerRef = myRef.child("services").getRef().child(Services[i]).getRef();
                                Log.e("7rira : ","Hello world 1 "+myRef.child("services").getRef().child(Services[i]).getRef());
                                Log.e("7rira : ","Hello world "+reaction.getReactedFullName());
                                Query workerQuery = (Query) workerRef.orderByChild("FullName").equalTo(reaction.getReactedFullName()).getRef();
                                workerQuery.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for(DataSnapshot myWorker : dataSnapshot.getChildren()){
                                            if(myWorker.getValue(Worker.class).getFullName().equals((reaction.getReactedFullName()))){

                                                    Log.e("7rira : ","Hello world 3");
                                                    Worker worker = myWorker.getValue(Worker.class);
                                                    Location myLocation = new Location("myLocation");
                                                    Location WorkerLocation = new Location("WorkerLocation");
                                                    myLocation.setLatitude(Latitude);
                                                    myLocation.setLongitude(Longitude);
                                                    WorkerLocation.setLatitude(worker.getLatitude());
                                                    WorkerLocation.setLongitude(worker.getLongitude());
                                                    worker.setDistance(myLocation.distanceTo(WorkerLocation));
                                                    Log.e("7rira "," test  here 2 "+myWorkerList.add(worker));

                                            }
                                        }
                                        Log.e("7rira "," test  here 3 "+myWorkerList.size());
                                        myAdapter = new bestWorkerAdapter(myWorkerList,getContext());

                                        mBestWorkerRecyclerView.setAdapter(myAdapter);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                Log.e("7rira "," test  here "+myWorkerList.size());
                        }

                    }

                }
                Log.e("7rira "," test  here 4 "+myWorkerList.size());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Log.e("workers : ",reactionRef.toString());

        return v;
    }

}
