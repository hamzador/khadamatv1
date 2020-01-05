package com.example.dell.khadamate;

import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import com.example.dell.khadamate.Adapter.WorkerRecyclerViewAdapter;
import com.example.dell.khadamate.Model.Worker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import static android.widget.Toast.LENGTH_SHORT;
import static com.example.dell.khadamate.HomePage.MyLoc;
import static com.example.dell.khadamate.HomePage.mLocationManager;
import static com.example.dell.khadamate.MyLocationListener.Latitude;
import static com.example.dell.khadamate.MyLocationListener.Longitude;
import static com.example.dell.khadamate.splashScreen.myRef;

public class Workers extends AppCompatActivity {
    private Toolbar mToolbar;
    private TextView mToolbar_title;
    private ArrayList<Worker> myWorkerList;
    private RecyclerView mWorkerRecyclerView;
    WorkerRecyclerViewAdapter myAdapter;
    public String services;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workers);
        services = getIntent().getExtras().getString("Service");
        mToolbar = (Toolbar) findViewById(R.id.workerToolBar);
        mToolbar_title = findViewById(R.id.toolbar_title);
        mWorkerRecyclerView = findViewById(R.id.WorkerRecyclerView);
        mWorkerRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mWorkerRecyclerView.setLayoutManager(linearLayoutManager);
        mToolbar_title.setText(services);
        Log.e("Services",services);
        setSupportActionBar(mToolbar);
        myWorkerList = new ArrayList<Worker>();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        DatabaseReference workerRef = myRef.child("services").child(services).getRef();
        Query workerRefQuery = (Query) workerRef;
        workerRefQuery.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!myWorkerList.isEmpty()) myWorkerList.clear();
                for(DataSnapshot myWorker : dataSnapshot.getChildren()){
                    Worker worker = myWorker.getValue(Worker.class);
                    Location myLocation = new Location("myLocation");
                    Location WorkerLocation = new Location("WorkerLocation");
                    myLocation.setLatitude(Latitude);
                    myLocation.setLongitude(Longitude);
                    WorkerLocation.setLatitude(worker.getLatitude());
                    WorkerLocation.setLongitude(worker.getLongitude());
                    worker.setDistance(myLocation.distanceTo(WorkerLocation));
                    Log.e("Worker",worker.toString());
                    myWorkerList.add(worker);
                }
                Comparator<Worker> employeeNameComparator
                        = Comparator.comparing(
                        Worker::getDistance, (s1, s2) -> {
                            return s2.compareTo(s1);
                        });

                Collections.sort(myWorkerList,employeeNameComparator);
                Log.e("Worker","Checking adapter........");
                myAdapter = new WorkerRecyclerViewAdapter(myWorkerList,getApplicationContext(),services);
                Log.e("Worker","Checking adapter 2 ........");
                mWorkerRecyclerView.setAdapter(myAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
