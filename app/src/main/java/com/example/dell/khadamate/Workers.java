package com.example.dell.khadamate;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.khadamate.Adapter.WorkerRecyclerViewAdapter;
import com.example.dell.khadamate.Model.Worker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.example.dell.khadamate.MyLocationListener.Latitude;
import static com.example.dell.khadamate.MyLocationListener.Longitude;
import static com.example.dell.khadamate.splashScreen.myRef;

public class Workers extends AppCompatActivity {
    private Toolbar mToolbar;
    private TextView mToolbar_title;
    private TextView mEmptyState;
    private ArrayList<Worker> myWorkerList;
    private RecyclerView mWorkerRecyclerView;
    private WorkerRecyclerViewAdapter myAdapter;
    private ValueEventListener workersListener;
    private DatabaseReference workerRef;
    public String services;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workers);

        if (getIntent().getExtras() == null || getIntent().getExtras().getString("Service") == null) {
            Toast.makeText(this, R.string.service_missing, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        services = getIntent().getExtras().getString("Service");
        mToolbar = findViewById(R.id.workerToolBar);
        mToolbar_title = findViewById(R.id.toolbar_title);
        mEmptyState = findViewById(R.id.emptyWorkers);
        mWorkerRecyclerView = findViewById(R.id.WorkerRecyclerView);
        mWorkerRecyclerView.setHasFixedSize(true);
        mWorkerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mToolbar_title.setText(services);
        setSupportActionBar(mToolbar);
        myWorkerList = new ArrayList<>();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        myAdapter = new WorkerRecyclerViewAdapter(myWorkerList, this, services);
        mWorkerRecyclerView.setAdapter(myAdapter);

        workerRef = myRef.child("services").child(services);
        workersListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myWorkerList.clear();
                for (DataSnapshot myWorker : dataSnapshot.getChildren()) {
                    Worker worker = myWorker.getValue(Worker.class);
                    if (worker == null) {
                        continue;
                    }
                    worker.setService(services);
                    Location myLocation = new Location("myLocation");
                    Location workerLocation = new Location("WorkerLocation");
                    myLocation.setLatitude(Latitude);
                    myLocation.setLongitude(Longitude);
                    workerLocation.setLatitude(worker.getLatitude());
                    workerLocation.setLongitude(worker.getLongitude());
                    worker.setDistance(myLocation.distanceTo(workerLocation));
                    myWorkerList.add(worker);
                }

                Collections.sort(myWorkerList, new Comparator<Worker>() {
                    @Override
                    public int compare(Worker w1, Worker w2) {
                        return Double.compare(w1.getDistance(), w2.getDistance());
                    }
                });

                myAdapter.notifyDataSetChanged();
                boolean empty = myWorkerList.isEmpty();
                mEmptyState.setVisibility(empty ? View.VISIBLE : View.GONE);
                mWorkerRecyclerView.setVisibility(empty ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Workers.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        workerRef.addValueEventListener(workersListener);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (workerRef != null && workersListener != null) {
            workerRef.removeEventListener(workersListener);
        }
    }
}
