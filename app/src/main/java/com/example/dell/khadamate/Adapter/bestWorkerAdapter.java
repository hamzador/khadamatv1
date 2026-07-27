package com.example.dell.khadamate.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dell.khadamate.Model.Worker;
import com.example.dell.khadamate.R;
import com.example.dell.khadamate.VProfileWorker;

import java.util.ArrayList;

public class bestWorkerAdapter extends RecyclerView.Adapter<WorkerRecyclerViewAdapter.MyViewHolder> {

    private ArrayList<Worker> myWorkersList;
    private Context mContext;

    public bestWorkerAdapter(ArrayList<Worker> myWorkersList, Context mContext) {
        this.myWorkersList = myWorkersList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public WorkerRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.worker_recycler_item, viewGroup, false);
        return new WorkerRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkerRecyclerViewAdapter.MyViewHolder myViewHolder, int i) {
        final Worker worker = myWorkersList.get(i);
        myViewHolder.mWorkerFullName.setText(worker.getFirstname() + " " + worker.getLastname());
        myViewHolder.mLikes.setText(String.valueOf(worker.getLikes()));
        myViewHolder.mDislikes.setText(String.valueOf(worker.getDislikes()));
        myViewHolder.mDistance.setText(WorkerRecyclerViewAdapter.formatDistance(worker.getDistance()));
        myViewHolder.mWorkerImageProfile.setImageResource(R.drawable.ic_user_avatar);
        myViewHolder.mRecyclerViewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(worker.getService())) {
                    return;
                }
                Intent intent = new Intent(mContext, VProfileWorker.class)
                        .putExtra("FirstName", worker.getFirstname())
                        .putExtra("LastName", worker.getLastname())
                        .putExtra("Service", worker.getService());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myWorkersList == null ? 0 : myWorkersList.size();
    }
}
