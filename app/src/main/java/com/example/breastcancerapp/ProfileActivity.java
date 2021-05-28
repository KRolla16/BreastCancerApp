package com.example.breastcancerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private Button logout,nextAct;
    private FirebaseUser user;
    private DatabaseReference reference;
    ImageView profileImageView;

    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileImageView = (ImageView) findViewById(R.id.profile_image2);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.profile);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.photo:
                        startActivity(new Intent(getApplicationContext(), PredictActivity.class ));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.about:
                        startActivity(new Intent(getApplicationContext(), AboutActivity.class ));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class ));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(),ProfileActivity.class ));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.logout:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        return true;


                }
                return false;
            }
        });



        nextAct = (Button) findViewById(R.id.buttonNxtAct);

        nextAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
            }
        });



        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        final TextView greetingTextView = (TextView) findViewById(R.id.greeting);
        final TextView fullNameTextView = (TextView) findViewById(R.id.fullName);
        final TextView emailTextView = (TextView) findViewById(R.id.emailAdress);
        final TextView ageTextView = (TextView) findViewById(R.id.age);


        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               User userProfile = snapshot.getValue(User.class);
               if (userProfile != null){
                   String fullName = userProfile.fullName;
                   String email = userProfile.email;
                   String age = userProfile.age;

                   greetingTextView.setText("Welcome, \n" + fullName);
                   //fullNameTextView.setText(fullName);
                   emailTextView.setText(email);
                   ageTextView.setText(age);

                   if (user.getPhotoUrl() != null){
                       Glide.with(ProfileActivity.this)
                               .load(user.getPhotoUrl())
                               .into(profileImageView);
                   }
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Something Wrong happened", Toast.LENGTH_LONG).show();
            }
        });
    }
}