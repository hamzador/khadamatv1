package com.example.dell.khadamate.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell.khadamate.Model.Worker;
import com.example.dell.khadamate.R;
import com.example.dell.khadamate.VProfileWorker;

import java.util.ArrayList;
import java.util.Locale;

public class WorkerRecyclerViewAdapter extends RecyclerView.Adapter<WorkerRecyclerViewAdapter.MyViewHolder> {

    private ArrayList<Worker> myWorkersList;
    private Context mContext;
    private String Services;

    public WorkerRecyclerViewAdapter(ArrayList<Worker> myWorkersList, Context context, String Services) {
        this.myWorkersList = myWorkersList;
        this.mContext = context;
        this.Services = Services;
    }

    public WorkerRecyclerViewAdapter(ArrayList<Worker> myWorkersList, Context mContext) {
        this.myWorkersList = myWorkersList;
        this.mContext = mContext;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView mWorkerImageProfile;
        TextView mWorkerFullName;
        TextView mLikes;
        TextView mDistance;
        TextView mDislikes;
        ConstraintLayout mRecyclerViewItem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mWorkerImageProfile = itemView.findViewById(R.id.workerImageProfile);
            mWorkerFullName = itemView.findViewById(R.id.workerFullName);
            mLikes = itemView.findViewById(R.id.likes);
            mDislikes = itemView.findViewById(R.id.dislikes);
            mDistance = itemView.findViewById(R.id.Distance);
            mRecyclerViewItem = itemView.findViewById(R.id.RecyclerViewItem);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.worker_recycler_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        final Worker worker = myWorkersList.get(i);
        myViewHolder.mWorkerFullName.setText(worker.getFirstname() + " " + worker.getLastname());
        myViewHolder.mLikes.setText(String.valueOf(worker.getLikes()));
        myViewHolder.mDislikes.setText(String.valueOf(worker.getDislikes()));
        myViewHolder.mDistance.setText(formatDistance(worker.getDistance()));
        myViewHolder.mWorkerImageProfile.setImageResource(R.drawable.ic_user_avatar);
        myViewHolder.mRecyclerViewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String service = !TextUtils.isEmpty(Services) ? Services : worker.getService();
                if (TextUtils.isEmpty(service)) {
                    return;
                }
                Intent intent = new Intent(mContext, VProfileWorker.class)
                        .putExtra("FirstName", worker.getFirstname())
                        .putExtra("LastName", worker.getLastname())
                        .putExtra("Service", service);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myWorkersList == null ? 0 : myWorkersList.size();
    }

    public static String formatDistance(double distanceMeters) {
        if ((int) (distanceMeters * 0.001) == 0) {
            return String.format(Locale.getDefault(), " %.0f م ", distanceMeters);
        }
        return String.format(Locale.getDefault(), " %.2f كم ", distanceMeters * 0.001);
    }
}
