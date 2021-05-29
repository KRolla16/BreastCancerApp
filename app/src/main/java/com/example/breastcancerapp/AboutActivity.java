package com.example.breastcancerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class AboutActivity extends AppCompatActivity {


    TextView textViewb, textViewA1, textViewA2, textViewA3;
    ImageView imageViewA1;
    Button buttonA1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        textViewb = (TextView) findViewById(R.id.bannerInfo);
        textViewA1 = (TextView) findViewById(R.id.txtAboutCancer);
        textViewA2 = (TextView) findViewById(R.id.txtAboutCancer2);
        textViewA3 = (TextView) findViewById(R.id.txtAboutCancer3);
        imageViewA1 = (ImageView) findViewById(R.id.imageViewAbout);
        buttonA1 = (Button) findViewById(R.id.buttoncheckyourbreasts);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.about);

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



        buttonA1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ytVideo = "https://www.youtube.com/watch?v=KVSbiwraxbw";
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, ytVideo);
                startActivity(intent);
            }
        });
    }
}