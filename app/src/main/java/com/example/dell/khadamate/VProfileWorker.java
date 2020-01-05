package com.example.dell.khadamate;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.dell.khadamate.Model.Reaction;
import com.example.dell.khadamate.Model.User;
import com.example.dell.khadamate.Model.Worker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import static com.example.dell.khadamate.splashScreen.myRef;
import static com.example.dell.khadamate.splashScreen.user;

public class VProfileWorker extends AppCompatActivity {
    String fname,lname;
    User worker;
    Worker w;
    @Nullable
    String service;
    private Button mLikeButton,mDislikeButoon;
    static public int count = 0;
    DatabaseReference reactionRef ;
    private TextView mVWorkerFullName,mVWorkerDislikes
            ,mVWorkerLikes,mVWorkerPhone,mVWorkerAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vprofile_worker);
        mLikeButton = findViewById(R.id.likeButton);
        mDislikeButoon = findViewById(R.id.dislikeButton);
        mVWorkerFullName = findViewById(R.id.VWorkerFullName);
        mVWorkerLikes = findViewById(R.id.VWorkerLikes);
        mVWorkerDislikes = findViewById(R.id.VWorkerDislikes);
        mVWorkerPhone = findViewById(R.id.VWorkerPhone);
        mVWorkerAddress = findViewById(R.id.VWorkerAddress);

        DatabaseReference usersRef = myRef.child("users").getRef();
        fname = getIntent().getExtras().getString("FirstName");
        lname = getIntent().getExtras().getString("LastName");
        service = getIntent().getExtras().getString("Service");
        Query userRefQuery = (Query) usersRef.orderByChild("fname").equalTo(fname);
        DatabaseReference workerRef = myRef.child("services").child(service).getRef();
        Query workerRefQuery = (Query) workerRef.orderByChild("firstname").equalTo(fname);
        workerRefQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot workers: dataSnapshot.getChildren()) {

                    w = workers.getValue(Worker.class);
                    if(w.getLastname().equals(lname)){
                        mVWorkerLikes.setText(""+w.getLikes());
                        mVWorkerDislikes.setText(""+w.getDislikes());
                        break;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        userRefQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot users: dataSnapshot.getChildren()) {
                    worker = users.getValue(User.class);
                    if(worker.getLName().equals(lname)){
                        System.out.println("role "+worker.toString());
                        mVWorkerFullName.setText(worker.getFName()+" "+worker.getLName());
                        mVWorkerAddress.setText(worker.getAddress());
                        mVWorkerPhone.setText(""+worker.getPhone());
                        break;
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Log.e(" fname",lname);
        reactionRef = myRef.child("reactions");
        Query reactionRefQuery = (Query) reactionRef.orderByChild("reactedFullName").equalTo(fname+" "+lname);
        reactionRefQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count = 0;
                for(DataSnapshot reactions : dataSnapshot.getChildren()){
                    count++;
                    Reaction reaction = reactions.getValue(Reaction.class);
                    if(reaction.getReactorFullName().equals(user.getFName()+" "+user.getLName())){
                        if(reaction.getReaction().equals("Like")){
                            mLikeButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_liked,0,0,0);
                            mLikeButton.setBackgroundResource(R.drawable.button_style3);
                        }else if(reaction.getReaction().equals("Dislike")){
                            mDislikeButoon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_disliked,0,0,0);
                            mDislikeButoon.setBackgroundResource(R.drawable.button_style3);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mLikeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DatabaseReference reactionRef = myRef.child("reactions");
                String key = reactionRef.push().getKey();
                reactionRefQuery.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int Liked = 0;
                        int i=0;
                        for(DataSnapshot reactions : dataSnapshot.getChildren()){
                            Reaction reaction = reactions.getValue(Reaction.class);
                            String react = "";
                            if(reaction.getReactorFullName().equals(user.getFName()+" "+user.getLName())){
                                if(reaction.getReaction().equals("Like")){
                                    mLikeButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like,0,0,0);
                                    mLikeButton.setBackgroundResource(R.drawable.button_reaction);
                                    react = "Unlike";
                                    removeLikes();
                                }else if(reaction.getReaction().equals("Unlike")){
                                    mLikeButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_liked,0,0,0);
                                    mLikeButton.setBackgroundResource(R.drawable.button_style3);
                                    reactions.child("reaction").getChildren();
                                    react = "Like";
                                    addLikes();
                                }else if(reaction.getReaction().equals("Dislike")){
                                    mLikeButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_liked,0,0,0);
                                    mLikeButton.setBackgroundResource(R.drawable.button_style3);
                                    mDislikeButoon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_dislike,0,0,0);
                                    mDislikeButoon.setBackgroundResource(R.drawable.button_reaction);
                                    reactions.child("reaction").getChildren();
                                    react = "Like";
                                    addLikes();
                                    removeDislike();
                                }
                                Map mapData = new HashMap();
                                mapData.put("reaction", react);
                                i++;
                                reactions.getRef().updateChildren(mapData);
                                reactionRefQuery.removeEventListener(this);
                                Liked = 1;
                                break;
                            }
                        }

                        if(Liked == 0){
                            Reaction reaction = new Reaction(user.getFName()+" "+user.getLName(),
                                    w.getFirstname()+" "+w.getLastname(),"Like");
                            reactionRef.child(key).setValue(reaction);
                            mLikeButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_liked,0,0,0);
                            mLikeButton.setBackgroundResource(R.drawable.button_style3);
                            reactionRefQuery.removeEventListener(this);
                            addLikes();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }


                });
            }
        });

        Log.e("count : ",""+count);
        mDislikeButoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reactionRef = myRef.child("reactions");
                String key = reactionRef.push().getKey();
                reactionRefQuery.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int Liked = 0;
                        int i=0;
                        for(DataSnapshot reactions : dataSnapshot.getChildren()){
                            Reaction reaction = reactions.getValue(Reaction.class);
                            String react = "";
                            if(reaction.getReactorFullName().equals(user.getFName()+" "+user.getLName())){
                                if(reaction.getReaction().equals("Dislike")){
                                    mDislikeButoon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_dislike,0,0,0);
                                    mDislikeButoon.setBackgroundResource(R.drawable.button_reaction);
                                    react = "Unlike";
                                    removeDislike();
                                }else if(reaction.getReaction().equals("Unlike")){
                                    mDislikeButoon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_disliked,0,0,0);
                                    mDislikeButoon.setBackgroundResource(R.drawable.button_style3);
                                    reactions.child("reaction").getChildren();
                                    react = "Dislike";
                                    addDislike();
                                }else if(reaction.getReaction().equals("Like")){
                                    mDislikeButoon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_disliked,0,0,0);
                                    mDislikeButoon.setBackgroundResource(R.drawable.button_style3);
                                    mLikeButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like,0,0,0);
                                    mLikeButton.setBackgroundResource(R.drawable.button_reaction);
                                    reactions.child("reaction").getChildren();
                                    react = "Dislike";
                                    addDislike();
                                    removeLikes();
                                }
                                Map mapData = new HashMap();
                                mapData.put("reaction", react);
                                i++;
                                reactions.getRef().updateChildren(mapData);
                                reactionRefQuery.removeEventListener(this);
                                Liked = 1;
                                break;
                            }
                        }

                        if(Liked == 0){
                            Reaction reaction = new Reaction(user.getFName()+" "+user.getLName(),
                                    w.getFirstname()+" "+w.getLastname(),"Dislike");
                            reactionRef.child(key).setValue(reaction);
                            mDislikeButoon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_disliked,0,0,0);
                            mDislikeButoon.setBackgroundResource(R.drawable.button_style3);
                            reactionRefQuery.removeEventListener(this);
                            addDislike();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }


                });
            }
        });

    }

    protected void addLikes(){
        DatabaseReference workerRef = myRef.child("services").child(service).getRef();
        Query workerRefQuery = (Query) workerRef.orderByChild("firstname").equalTo(fname);
        workerRefQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot workers : dataSnapshot.getChildren()){
                    Worker w1 =  workers.getValue(Worker.class);
                    if(w1.getLastname().equals(lname)){
                        Log.e("Likes","Add");
                        Map mapData = new HashMap();
                        mapData.put("likes", w1.getLikes()+1);
                        workers.getRef().updateChildren(mapData);
                        workerRefQuery.removeEventListener(this);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    protected void removeLikes(){
        DatabaseReference workerRef = myRef.child("services").child(service).getRef();
        Query workerRefQuery = (Query) workerRef.orderByChild("firstname").equalTo(fname);
        workerRefQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot workers : dataSnapshot.getChildren()){
                    Worker w1 =  workers.getValue(Worker.class);
                    if(w1.getLastname().equals(lname)){
                        Map mapData = new HashMap();
                        mapData.put("likes", w1.getLikes()-1);
                        workers.getRef().updateChildren(mapData);
                        workerRefQuery.removeEventListener(this);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    protected void addDislike(){
        DatabaseReference workerRef = myRef.child("services").child(service).getRef();
        Query workerRefQuery = (Query) workerRef.orderByChild("firstname").equalTo(fname);
        workerRefQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot workers : dataSnapshot.getChildren()){
                    Worker w1 =  workers.getValue(Worker.class);
                    if(w1.getLastname().equals(lname)){
                        Map mapData = new HashMap();
                        mapData.put("dislikes", w1.getDislikes()+1);
                        workers.getRef().updateChildren(mapData);
                        workerRefQuery.removeEventListener(this);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    protected void removeDislike(){
        DatabaseReference workerRef = myRef.child("services").child(service).getRef();
        Query workerRefQuery = (Query) workerRef.orderByChild("firstname").equalTo(fname);
        workerRefQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot workers : dataSnapshot.getChildren()){
                    Worker w1 =  workers.getValue(Worker.class);
                    if(w1.getLastname().equals(lname)){
                        Map mapData = new HashMap();
                        mapData.put("dislikes", w1.getDislikes()-1);
                        workers.getRef().updateChildren(mapData);
                        workerRefQuery.removeEventListener(this);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
