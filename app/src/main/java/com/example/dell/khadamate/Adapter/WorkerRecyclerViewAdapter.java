package com.example.dell.khadamate.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell.khadamate.Model.Worker;
import com.example.dell.khadamate.R;
import com.example.dell.khadamate.VProfileWorker;

import java.util.ArrayList;

public class WorkerRecyclerViewAdapter extends RecyclerView.Adapter<WorkerRecyclerViewAdapter.MyViewHolder> {

    ArrayList<Worker> myWorkersList;
    Context mContext;
    String Services;
    public WorkerRecyclerViewAdapter(ArrayList<Worker> myWorkersList,Context context,String Services) {
        Log.e("Worker","Checking adapter 3 ........");
        this.myWorkersList = myWorkersList;
        this.mContext = context;
        this.Services = Services;
    }

    public WorkerRecyclerViewAdapter(ArrayList<Worker> myWorkersList, Context mContext) {
        Log.e("Worker","Checking adapter over ........");
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
            Log.e("Worker","Checking adapter 4 ........");
            mWorkerImageProfile = itemView.findViewById(R.id.workerImageProfile);
            mWorkerFullName = itemView.findViewById(R.id.workerFullName);
            mLikes = itemView.findViewById(R.id.likes);
            mDislikes = itemView.findViewById(R.id.dislikes);
            mDistance = itemView.findViewById(R.id.Distance);
            mRecyclerViewItem = itemView.findViewById(R.id.RecyclerViewItem);
        }
        // each data item is just a string in this case

    }



    @NonNull
    @Override
    public WorkerRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.worker_recycler_item,viewGroup,false);
        Log.e("Worker","Checking adapter 5 ........");
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WorkerRecyclerViewAdapter.MyViewHolder myViewHolder, int i) {
        Log.e("Worker","Checking adapter 6 ........");
            final Worker worker = myWorkersList.get(i);
            myViewHolder.mWorkerFullName.setText(worker.getFirstname()+" "+worker.getLastname());
            myViewHolder.mLikes.setText(""+worker.getLikes());
            myViewHolder.mDislikes.setText(""+worker.getDislikes());
            myViewHolder.mDistance.setText(""+this.mileToKm(worker.getDistance()));
            myViewHolder.mWorkerImageProfile.setImageResource(R.drawable.ic_user_avatar);
            myViewHolder.mRecyclerViewItem.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {
                    Log.e("Worker","Checking adapter 7 ........ "+worker.getLastname());
                    if(!Services.isEmpty()){
                        mContext.startActivity(new Intent(mContext,VProfileWorker.class).putExtra("FirstName",worker.getFirstname())
                                .putExtra("LastName", worker.getLastname()).putExtra("Service" , Services));
                    }

                }
            });



    }

    @Override
    public int getItemCount() {
        return myWorkersList.size();
    }

    public String mileToKm(double distance){
        if((int)(distance*0.001) == 0){
            return String.format(" %.1f", distance)+" متر ";
        }else{
            return String.format(" %.2f", distance* 0.001)+" كلم ";
        }
    }


}
