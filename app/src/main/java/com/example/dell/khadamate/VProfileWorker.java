package com.example.dell.khadamate;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.khadamate.Model.Reaction;
import com.example.dell.khadamate.Model.User;
import com.example.dell.khadamate.Model.Worker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.example.dell.khadamate.splashScreen.myRef;
import static com.example.dell.khadamate.splashScreen.user;

public class VProfileWorker extends AppCompatActivity {
    String fname, lname;
    User worker;
    Worker w;
    @Nullable
    String service;
    private Button mLikeButton, mDislikeButton, mCallButton, mMapsButton;
    DatabaseReference reactionRef;
    private TextView mVWorkerFullName, mVWorkerDislikes, mVWorkerLikes, mVWorkerPhone, mVWorkerAddress;
    private Query reactionRefQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vprofile_worker);

        if (getIntent().getExtras() == null || user == null) {
            Toast.makeText(this, R.string.profile_unavailable, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        mLikeButton = findViewById(R.id.likeButton);
        mDislikeButton = findViewById(R.id.dislikeButton);
        mCallButton = findViewById(R.id.callButton);
        mMapsButton = findViewById(R.id.mapsButton);
        mVWorkerFullName = findViewById(R.id.VWorkerFullName);
        mVWorkerLikes = findViewById(R.id.VWorkerLikes);
        mVWorkerDislikes = findViewById(R.id.VWorkerDislikes);
        mVWorkerPhone = findViewById(R.id.VWorkerPhone);
        mVWorkerAddress = findViewById(R.id.VWorkerAddress);

        fname = getIntent().getExtras().getString("FirstName");
        lname = getIntent().getExtras().getString("LastName");
        service = getIntent().getExtras().getString("Service");

        if (TextUtils.isEmpty(fname) || TextUtils.isEmpty(lname) || TextUtils.isEmpty(service)) {
            Toast.makeText(this, R.string.profile_unavailable, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        mVWorkerFullName.setText(fname + " " + lname);
        mVWorkerPhone.setText(R.string.loading);
        mVWorkerAddress.setText(R.string.loading);

        DatabaseReference usersRef = myRef.child("users").getRef();
        Query userRefQuery = usersRef.orderByChild("fname").equalTo(fname);
        DatabaseReference workerRef = myRef.child("services").child(service).getRef();
        Query workerRefQuery = workerRef.orderByChild("firstname").equalTo(fname);

        workerRefQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot workers : dataSnapshot.getChildren()) {
                    w = workers.getValue(Worker.class);
                    if (w != null && lname.equals(w.getLastname())) {
                        mVWorkerLikes.setText(String.valueOf(w.getLikes()));
                        mVWorkerDislikes.setText(String.valueOf(w.getDislikes()));
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
                for (DataSnapshot users : dataSnapshot.getChildren()) {
                    worker = users.getValue(User.class);
                    if (worker != null && lname.equals(worker.getLName())) {
                        mVWorkerFullName.setText(worker.getFName() + " " + worker.getLName());
                        mVWorkerAddress.setText(worker.getAddress() != null ? worker.getAddress() : "—");
                        mVWorkerPhone.setText(worker.getPhone() != null ? worker.getPhone() : "—");
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        reactionRef = myRef.child("reactions");
        reactionRefQuery = reactionRef.orderByChild("reactedFullName").equalTo(fname + " " + lname);
        reactionRefQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                resetReactionButtons();
                for (DataSnapshot reactions : dataSnapshot.getChildren()) {
                    Reaction reaction = reactions.getValue(Reaction.class);
                    if (reaction == null) {
                        continue;
                    }
                    if (reaction.getReactorFullName().equals(user.getFName() + " " + user.getLName())) {
                        if ("Like".equals(reaction.getReaction())) {
                            mLikeButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_liked, 0, 0, 0);
                            mLikeButton.setBackgroundResource(R.drawable.button_style3);
                        } else if ("Dislike".equals(reaction.getReaction())) {
                            mDislikeButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_disliked, 0, 0, 0);
                            mDislikeButton.setBackgroundResource(R.drawable.button_style3);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        mLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleReaction("Like");
            }
        });

        mDislikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleReaction("Dislike");
            }
        });

        mCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (worker == null || TextUtils.isEmpty(worker.getPhone())) {
                    Toast.makeText(VProfileWorker.this, R.string.phone_unavailable, Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent dial = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + worker.getPhone()));
                startActivity(dial);
            }
        });

        mMapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (worker == null) {
                    Toast.makeText(VProfileWorker.this, R.string.location_unavailable, Toast.LENGTH_SHORT).show();
                    return;
                }
                Uri geoUri;
                if (worker.getLatitude() != 0 || worker.getLongitude() != 0) {
                    geoUri = Uri.parse(String.format(Locale.US, "geo:%f,%f?q=%f,%f(%s)",
                            worker.getLatitude(), worker.getLongitude(),
                            worker.getLatitude(), worker.getLongitude(),
                            Uri.encode(worker.getFullName() != null ? worker.getFullName() : "")));
                } else if (!TextUtils.isEmpty(worker.getAddress())) {
                    geoUri = Uri.parse("geo:0,0?q=" + Uri.encode(worker.getAddress()));
                } else {
                    Toast.makeText(VProfileWorker.this, R.string.location_unavailable, Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, geoUri);
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                } else {
                    Toast.makeText(VProfileWorker.this, R.string.maps_unavailable, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void resetReactionButtons() {
        mLikeButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like, 0, 0, 0);
        mLikeButton.setBackgroundResource(R.drawable.button_reaction);
        mDislikeButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_dislike, 0, 0, 0);
        mDislikeButton.setBackgroundResource(R.drawable.button_reaction);
    }

    private void handleReaction(final String targetReaction) {
        if (w == null || user == null) {
            Toast.makeText(this, R.string.profile_unavailable, Toast.LENGTH_SHORT).show();
            return;
        }
        final DatabaseReference reactionsRef = myRef.child("reactions");
        final String key = reactionsRef.push().getKey();
        reactionRefQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean updated = false;
                for (DataSnapshot reactions : dataSnapshot.getChildren()) {
                    Reaction reaction = reactions.getValue(Reaction.class);
                    if (reaction == null) {
                        continue;
                    }
                    if (reaction.getReactorFullName().equals(user.getFName() + " " + user.getLName())) {
                        String current = reaction.getReaction();
                        String next = targetReaction;
                        if (targetReaction.equals(current)) {
                            next = "Unlike";
                            if ("Like".equals(current)) {
                                removeLikes();
                            } else if ("Dislike".equals(current)) {
                                removeDislike();
                            }
                        } else {
                            if ("Like".equals(targetReaction)) {
                                addLikes();
                                if ("Dislike".equals(current)) {
                                    removeDislike();
                                }
                            } else {
                                addDislike();
                                if ("Like".equals(current)) {
                                    removeLikes();
                                }
                            }
                        }
                        Map<String, Object> mapData = new HashMap<>();
                        mapData.put("reaction", next);
                        reactions.getRef().updateChildren(mapData);
                        updated = true;
                        break;
                    }
                }

                if (!updated) {
                    Reaction reaction = new Reaction(user.getFName() + " " + user.getLName(),
                            w.getFirstname() + " " + w.getLastname(), targetReaction);
                    if (key != null) {
                        reactionsRef.child(key).setValue(reaction);
                    }
                    if ("Like".equals(targetReaction)) {
                        addLikes();
                    } else {
                        addDislike();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    protected void addLikes() {
        updateCounter("likes", 1);
    }

    protected void removeLikes() {
        updateCounter("likes", -1);
    }

    protected void addDislike() {
        updateCounter("dislikes", 1);
    }

    protected void removeDislike() {
        updateCounter("dislikes", -1);
    }

    private void updateCounter(final String field, final int delta) {
        DatabaseReference workerRef = myRef.child("services").child(service).getRef();
        Query workerRefQuery = workerRef.orderByChild("firstname").equalTo(fname);
        workerRefQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot workers : dataSnapshot.getChildren()) {
                    Worker w1 = workers.getValue(Worker.class);
                    if (w1 != null && lname.equals(w1.getLastname())) {
                        int current = "likes".equals(field) ? w1.getLikes() : w1.getDislikes();
                        int next = Math.max(0, current + delta);
                        Map<String, Object> mapData = new HashMap<>();
                        mapData.put(field, next);
                        workers.getRef().updateChildren(mapData);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
