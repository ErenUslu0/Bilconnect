package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class EvaluateParticipantActivity extends AppCompatActivity {
    private RatingBar eval;
    private Button evaluateUser;
    private FloatingActionButton backButton;
    private ListView listView;
    private float rateValue;
    private ArrayList<Event> events = new ArrayList<Event>();
    private ArrayList<String> users = new ArrayList<String>();
    private FirebaseDatabase db;
    private DatabaseReference mRef;
    private Event eventt;
    private String eventId;
    private String usersId;
    private Event event;
    private User userToEvaluate;
    private ArrayAdapter<String> arrAdapter;
    private ArrayList<User> userArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate_participant);
        userArray = new ArrayList<User>();

        eval = findViewById(R.id.ratingBarStars);
        evaluateUser = findViewById(R.id.evaluateButton);
        listView = findViewById(R.id.listViewParticipants);
        backButton= findViewById(R.id.floatingActionButtonclose);
        arrAdapter  = new ArrayAdapter<String>(EvaluateParticipantActivity.this,android.R.layout.simple_list_item_1,users);
        listView.setAdapter(arrAdapter);
        eventId = getIntent().getStringExtra("eventId");

        db = FirebaseDatabase.getInstance("https://bilconnect-96cde-default-rtdb.europe-west1.firebasedatabase.app/");
        mRef = db.getReference();

        mRef.child("events").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    eventt = snapshot.getValue(Event.class);
                    usersId = eventt.getUsersIdList();
                    if(eventt.getEventId() != null && !eventt.getEventId().isEmpty())
                    {

                        if (eventt.getEventId().equals(eventId)) {
                                mRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot snapshot:dataSnapshot.getChildren())
                                        {
                                            String userId = snapshot.getKey();

                                            if(eventt.getUsersIdList().contains(userId) && !userId.equals(FirebaseAuth.getInstance().getUid()))
                                            {
                                                User usr = (User) snapshot.getValue(User.class);
                                                String userName = snapshot.child("name").getValue(String.class);
                                                users.add(userName);
                                                userArray.add(usr);
                                                System.out.println(6);
                                                arrAdapter.notifyDataSetChanged();
                                            }


                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        // Error occurred while retrieving user data
                                        Log.e("firebase", "Error getting user data", databaseError.toException());
                                    }
                                });
                            break;
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Error occurred while retrieving event data
                Log.e("firebase", "Error getting data", databaseError.toException());
            }
        });


        arrAdapter.notifyDataSetChanged();
        evaluateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                evaluate();
                startActivity(new Intent(EvaluateParticipantActivity.this,MainActivity.class));
            }
        });

        eval.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rateValue = ratingBar.getRating();
                //  User ratingi artıcak firebaseden current userı alıp ona göre userdaki metodu kullanacaz
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                userToEvaluate = userArray.get(position);
            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EvaluateParticipantActivity.this,AttendedEventsActivity.class));
            }
        });
    }


    public void evaluate()
    {
        try {
            Boolean done = userToEvaluate.evalRating(rateValue, (eventId + (FirebaseAuth.getInstance().getUid())));
            if(done) {
                mRef.child("users").child(userToEvaluate.getId()).setValue(userToEvaluate);
            }
            else {
                Toast.makeText(EvaluateParticipantActivity.this, "You have already rated this user in this event",Toast.LENGTH_LONG).show();
            }

        }
        catch (Exception e) {
            Toast.makeText(EvaluateParticipantActivity.this, e.getMessage(),Toast.LENGTH_SHORT).show();
        }

        // this method will evaluate the current users rating.

    }
}